package processor.pipeline;

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
			System.out.println("instruction fetch");
			
			// chaning program counter:
			if(EX_IF_Latch.isIF_enable()) {
				if(EX_IF_Latch.getIsBranchTaken()) {
					System.out.println("is branch taken");
					System.out.println(EX_IF_Latch.getBranchPC());
					containingProcessor.getRegisterFile().setProgramCounter(EX_IF_Latch.getBranchPC());
				}
				else
					containingProcessor.getRegisterFile().setProgramCounter(currentPC+1);
			}
			EX_IF_Latch.setIsBRanchTaken(false);
			
			currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			
			System.out.println("Program Counter is: "+ currentPC);
			//System.out.println("before new instruction");
			int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			//System.out.println(newInstruction);
			IF_OF_Latch.setInstruction(newInstruction);
			
			IF_EnableLatch.setIF_enable(false);
			IF_OF_Latch.setOF_enable(true);
			EX_IF_Latch.setIF_enable(false);
		}
	}

}
