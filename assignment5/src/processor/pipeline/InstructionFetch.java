package processor.pipeline;

import generic.Statistics;
import processor.Clock;
import processor.Processor;
import generic.Element;
import generic.Event;
import generic.Simulator;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
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
			wrongPCL1 = EX_IF_Latch.getWrongPCL1();	wrongPCL2 = EX_IF_Latch.getWrongPCL2();
			
			branchPC = EX_IF_Latch.getBranchPC();
			isBranchTaken = true;
			containingProcessor.getRegisterFile().setProgramCounter(branchPC);
			EX_IF_Latch.setIsBRanchTaken(false);
			IF_EnableLatch.setIF_enable(true);
			IF_EnableLatch.setIFBusy(false);
			Statistics.updateBranchStall();
		}
		else
			isBranchTaken = false;
	}
	
	private void resumePipeline(int pc, int newInstruction)
	{
		IF_OF_Latch.setInstruction(newInstruction);
		
		if(ControlUnit.isEndInstruction(newInstruction))
			IF_EnableLatch.setIF_enable(false);
		
		IF_OF_Latch.updatePendingTask();
		IF_OF_Latch.setOF_enable(true);
		IF_EnableLatch.setIFBusy(false);
		IF_OF_Latch.setPC(pc);
	}
	
	public void handleEvent(Event e)
	{
		if(IF_OF_Latch.isOFBusy() || IF_OF_Latch.isOFConflict()) {
			
			e.setEventTime(Clock.getCurrentTime()+1);
			Simulator.getEventQueue().addEvent(e);
		}
		else {
			MemoryResponseEvent event = (MemoryResponseEvent) e;
			
			if(wrongPCL1 < wrongPCL2 && (event.getPC()>wrongPCL1 && event.getPC()<wrongPCL2) ||
			   wrongPCL1 > wrongPCL2 && event.getPC()>wrongPCL1) {
				wrongPCL1 = 0; wrongPCL2 =0;
				return;
			}
			int newInstruction = event.getValue();
			IF_OF_Latch.setInstruction(newInstruction);
			
			if (newInstruction == -402653184)
				IF_EnableLatch.setIF_enable(false);
			resumePipeline(event.getPC(), event.getValue());
		}
	}
	
	public void performIF()
	{
		if(IF_EnableLatch.isIFBusy()) {
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
				
			currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			
			
			// adding this as memory read event in the simulator.
				Simulator.getEventQueue().addEvent(
						new MemoryReadEvent(
								Clock.getCurrentTime() + Configuration.mainMemoryLatency,
								this,
								containingProcessor.getMainMemory(),
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
