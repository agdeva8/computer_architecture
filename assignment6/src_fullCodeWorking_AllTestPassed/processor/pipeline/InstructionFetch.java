package processor.pipeline;

import processor.Clock;
import processor.Processor;
import generic.*;
import generic.Event.EventType;
import configuration.Configuration;

public class InstructionFetch implements Element{
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	static int currentPC, branchPC, wrongPCL1 = 0, wrongPCL2 = 0;
	static boolean isBranchTaken;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	private void checkBranchNextCycle()
	{
		if(EX_IF_Latch.getIsBranchTaken()) {
			//Simulator.getEventQueue().deleteEvent(containingProcessor);
			wrongPCL1 = EX_IF_Latch.getWrongPCL1();
			wrongPCL2 = EX_IF_Latch.getWrongPCL2();
			
			branchPC = EX_IF_Latch.getBranchPC();
			isBranchTaken = true;
			containingProcessor.getRegisterFile().setProgramCounter(branchPC);
			EX_IF_Latch.setIsBRanchTaken(false);
			IF_EnableLatch.setIF_enable(true);
			IF_EnableLatch.setIFBusy(false);
		}
		else
			isBranchTaken = false;
	}
	
	private void resumePipeline(int pc, int newInstruction)
	{
		
		IF_OF_Latch.setInstruction(newInstruction);
		
		if(ControlUnit.isEndInstruction(newInstruction))
			IF_EnableLatch.setIF_enable(false);
		
//		System.out.println("pending task updated");
		
		IF_OF_Latch.updatePendingTask();
		IF_OF_Latch.setOF_enable(true);
		IF_EnableLatch.setIFBusy(false);
		IF_OF_Latch.setPC(pc);
	}
	
	public void handleEvent(Event e)
	{
		if(IF_OF_Latch.isOFBusy() || IF_OF_Latch.isOFConflict()) {
			
//			System.out.println("IF: OF busy or in conflict");
			e.setEventTime(Clock.getCurrentTime()+1);
			Simulator.getEventQueue().addEvent(e);
		}
		else {
			int pc=0, value=0;
			
			if(e.getEventType() == EventType.CacheResponse) {
				CacheResponseEvent event = (CacheResponseEvent) e;
				pc = event.getPC();
				value = event.getValue();
			}
			
			if(e.getEventType() == EventType.MemoryResponse){
				MemoryResponseEvent event = (MemoryResponseEvent) e;
				pc = event.getPC();
				value = event.getValue();
			}
//			System.out.println("IF: sending pc "+pc+"\n");

//			if(wrongPCL1 < wrongPCL2 && (pc>wrongPCL1 && pc<wrongPCL2) ||
//			   wrongPCL1 > wrongPCL2 && pc>wrongPCL1) {
//				wrongPCL1 = 0; wrongPCL2 =0;
//				return;
//			}
			resumePipeline(pc, value);
		}
	}
	
	public void performIF()
	{
		if(IF_EnableLatch.isIFBusy()) {
//			System.out.println("IF: busy");
			checkBranchNextCycle();
			IF_OF_Latch.setOF_enable(false);
			return;
		}
		
		if(IF_EnableLatch.isIF_enable())
		{	
			currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			
			// changing program counter if required;
			if(!isBranchTaken)
				containingProcessor.getRegisterFile().setProgramCounter(currentPC+1);
			else
				Simulator.getEventQueue().deleteEvent(containingProcessor);
						
			currentPC = containingProcessor.getRegisterFile().getProgramCounter();
//			System.out.println("IF: Asking pc "+currentPC);
			
			// adding this as memory read event in the simulator.
			Simulator.getEventQueue().addEvent(
					new CacheReadEvent(
							Clock.getCurrentTime() + Configuration.L1i_latency,
							this,
							this,
							containingProcessor.getL1iCache(),
							currentPC));
			
			IF_EnableLatch.setIFBusy(true);
			isBranchTaken = false;
		}
		else
			IF_OF_Latch.setOF_enable(false);
		
		// now checking if the branch is taken or not ;
		checkBranchNextCycle();
	}
}
