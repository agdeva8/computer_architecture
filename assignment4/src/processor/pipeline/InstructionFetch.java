package processor.pipeline;

import generic.Statistics;
import processor.Processor;

public class InstructionFetch {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{
		if(IF_EnableLatch.isIF_enable())
		{
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			
			// changing program counter:
			if(EX_IF_Latch.isIF_enable() && containingProcessor.getOFUnit().shouldUpdatePC()) {
				if(!EX_IF_Latch.getIsBranchTaken())
					containingProcessor.getRegisterFile().setProgramCounter(currentPC+1);
			}
			EX_IF_Latch.setIsBRanchTaken(false);
			
			currentPC = containingProcessor.getRegisterFile().getProgramCounter();

			int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			IF_OF_Latch.setInstruction(newInstruction);
			
			if (newInstruction == -402653184)
				IF_EnableLatch.setIF_enable(false);
			
			IF_OF_Latch.setOF_enable(true);
		}
		else
			IF_OF_Latch.setOF_enable(false);
		
		EX_IF_Latch.setBranchPC(containingProcessor.getEXUnit().getBranchTarget());
		EX_IF_Latch.setIsBRanchTaken(containingProcessor.getEXUnit().getIsBranchTaken());
		
		if(EX_IF_Latch.getIsBranchTaken()) {
			containingProcessor.getRegisterFile().setProgramCounter(EX_IF_Latch.getBranchPC());
			IF_EnableLatch.setIF_enable(true);
			IF_OF_Latch.setOF_enable(false);
			Statistics.updateBranchStall();
		}
	}
}
