package processor.pipeline;

import configuration.Configuration;
import generic.*;
import processor.Clock;
import processor.Processor;

public class MemoryAccess implements Element{
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	int op2, aluResult, instruction = 0, ldResult = 0; 
	String opcode;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	private void resumePipeline()
	{
		EX_MA_Latch.setMABusy(false);
		MA_RW_Latch.setRW_enable(true);
		
		MA_RW_Latch.setInstruction(instruction);
		MA_RW_Latch.setOpcode(opcode);
		MA_RW_Latch.setAluResult(aluResult);
		MA_RW_Latch.setLdResult(ldResult);
	}
	
	public void handleEvent(Event e)
	{
		MemoryResponseEvent event = (MemoryResponseEvent) e;
		ldResult = event.getValue();
		resumePipeline();
	}
	
	public int getInstrucion()
	{
		return instruction;
	}
		
	public void performMA()
	{
		if(EX_MA_Latch.isMABusy()) {
			// MA is busy in its own work;	// nothing to give to the RW stage;
			MA_RW_Latch.setRW_enable(false);
			return;
		}
		
		if (EX_MA_Latch.isMA_enable())
		{
			// MA stage got some work to do;
			
			// loading from latch;
			opcode = EX_MA_Latch.getOpcode();
			op2 = EX_MA_Latch.getOp2();
			aluResult = EX_MA_Latch.getAluResult();
			instruction = EX_MA_Latch.getInstruction();
			
			if (ControlUnit.isLd(opcode)) {
				Simulator.getEventQueue().addEvent(
						new MemoryReadEvent(
								Clock.getCurrentTime() + Configuration.mainMemoryLatency,
								this,
								containingProcessor.getMainMemory(),
								aluResult));
				EX_MA_Latch.setMABusy(true);
				return;
			}
			
			if (ControlUnit.isSt(opcode)) {
				Simulator.getEventQueue().addEvent(
						new MemoryWriteEvent(
								Clock.getCurrentTime() + Configuration.mainMemoryLatency,
								this,
								containingProcessor.getMainMemory(),
								aluResult,	//address, value
								op2));
				EX_MA_Latch.setMABusy(true);
				return;
			}
			resumePipeline();
		}
		else {
			// MA stage has done no work;
			instruction = 0;
			MA_RW_Latch.setRW_enable(false);
		}
		
	}
}
