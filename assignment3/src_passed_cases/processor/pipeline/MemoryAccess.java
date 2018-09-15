package processor.pipeline;

import processor.Processor;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	int op2;
	int aluResult;
	int instruction;
	String opcode;
	int ldResult = 0; 
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	private boolean isLd()
	{
		return opcode.equals("10110");
	}
	
	private boolean isSt()
	{
		return opcode.equals("10111");
	}
	
	public void performMA()
	{
		if (EX_MA_Latch.isMA_enable())
		{
			//System.out.println("memory address ");
			// loading from latch;
			opcode = EX_MA_Latch.getOpcode();
			op2 = EX_MA_Latch.getOp2();
			aluResult = EX_MA_Latch.getAluResult();
			instruction = EX_MA_Latch.getInstruction();
			
			if (isLd())
				ldResult = containingProcessor.getMainMemory().getWord(aluResult);
					
			if (isSt())
			{
				containingProcessor.getMainMemory().setWord(aluResult,op2);  //address,value
				//System.out.println("address = " +aluResult);
				//System.out.println("value ="+op2);
			}
			EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);
			MA_RW_Latch.setInstruction(instruction);
			MA_RW_Latch.setOpcode(opcode);
			MA_RW_Latch.setAluResult(aluResult);
			MA_RW_Latch.setLdResult(ldResult);
			
		}
		
	}

}
