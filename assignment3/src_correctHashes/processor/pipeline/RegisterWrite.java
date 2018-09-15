package processor.pipeline;

import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	int instruction,ldResult,aluResult;
	String opcode;
	int rd;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	private boolean isWriteNeeded()
	{
		if (opcode.equals("11000") ||
			opcode.equals("11001") || 
			opcode.equals("11010") ||
			opcode.equals("11011") ||
			opcode.equals("11100") ||
			opcode.equals("10111")) return false;
		return true;
	}
	
	private boolean isLd()
	{
		return opcode.equals("10110");
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable())
		{
			// checking if it is needed to write ot not:
			// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
			System.out.println("RW");
			opcode = MA_RW_Latch.getOpcode();
			instruction = MA_RW_Latch.getInstruction();
			aluResult = MA_RW_Latch.getAluResult();
			ldResult = MA_RW_Latch.getLdResult();
			
			//System.out.println(opcode);
			//if(opcode.equals("11101")) System.out.println("working");
			//else 				  System.out.println("Not working");
			
			if(isWriteNeeded())
			{
				System.out.println("writing back");

				String instructionBits = Integer.toBinaryString(instruction);
				while(instructionBits.length() < 32)
					instructionBits = "0" + instructionBits;
				
				if (opcode.charAt(4) == '1' || opcode.equals("10110"))
				{
					System.out.println("rw1");
					String rd_bin = instructionBits.substring(10,15);
					rd = Integer.parseInt(rd_bin,2); 						// rd cant be -ve
					System.out.println("rd is: " +rd);
					//System.out.println(aluResult);
					if (isLd())
					{
						System.out.println("ldresult is: " + ldResult);
						//System.out.println("setting value ");
						containingProcessor.getRegisterFile().setValue(rd, ldResult);
					}
					else {
						System.out.println("alu result is: " + aluResult);
						//System.out.println("setting value ");
						containingProcessor.getRegisterFile().setValue(rd, aluResult);
					}
				}
				else
				{
					System.out.println("rw2");
					String rd_bin = instructionBits.substring(15,20);
					rd = Integer.parseInt(rd_bin,2);
					//System.out.println("rd bin: "+rd_bin);
					System.out.print("rd = ");
					System.out.println(rd);
					System.out.print("alu result is ");
					System.out.println(aluResult);
					containingProcessor.getRegisterFile().setValue(rd, aluResult);
				}
			}
			// checking for the end instruction:
			System.out.println(" " );
			if(opcode.equals("11101"))
			{
				System.out.println("founded end");
				Simulator.setSimulationComplete(true);
				// just to match the output given by Sir
				int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
				containingProcessor.getRegisterFile().setProgramCounter(currentPC+1);
			}
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
	}

}
