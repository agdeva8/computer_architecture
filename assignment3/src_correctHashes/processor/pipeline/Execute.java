package processor.pipeline;

import processor.Processor;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	static int immx,branchTarget,op1,op2,aluResult;
	static String opcode;
	static boolean isBranchTaken = false;
	int instruction;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	private static boolean isImmediate()
	{
		boolean isImm = false;
		// general checking for arithmatic types:
		isImm = opcode.charAt(4) == '1'; 
		
		// checking for load: store is covered in arithmatic type:
		if (opcode.equals("10110"))  isImm = true;
		
		// checking for conditional statement
		// setting flase for beq and blt as these are wrongly flagged as true;
		if (opcode.equals("11001") || opcode.equals("11011")) isImm = false;
		
		return isImm;
	}
	
	private int implementAlu(int operand1, int operand2)
	{
		int aluResult = 0;
		int remainder = 0;
		switch (opcode) {
		case "00000":
			aluResult = operand1 + operand2;
			break;
		case "00001":
			aluResult = operand1 + operand2;
			//System.out.println("addi");
			//System.out.println(operand1);
			//System.out.println(operand2);
			//System.out.println(aluResult);
			break;
		case "00010":
		case "00011":
			aluResult = operand1 - operand2;
			break;
		case "00100":
		case "00101":
			aluResult = operand1 * operand2;
			break;
		case "00110":
		case "00111":
			aluResult = operand1 / operand2;
			remainder = operand1 % operand2;
			containingProcessor.getRegisterFile().setValue(31,remainder);
			break;
		case "01000":
		case "01001":
			aluResult = operand1 & operand2;
			break;
		case "01010":
		case "01011":
			aluResult = operand1 | operand2;
			break;
		case "01100":
		case "01101":
			aluResult = operand1 ^ operand2;
			break;
		case "10000":
		case "10001":
			aluResult = operand1 << operand2;
			break;
		case "10010":
		case "10011":
			aluResult = operand1 >>> operand2;
			break;
		case "10100":
		case "10101":
			aluResult = operand1 >> operand2;
			break;
		case "10110":
		case "10111":
			aluResult = operand1 + operand2;
			break;
		
		}
		if (opcode.equals("01110") || opcode.equals("01111"))
			if(operand1 < operand2) aluResult = 1;
			else 					aluResult = 0;
	return aluResult;
	}
	
	private static void checkBranch(int operand1,int operand2)
	{
		if ((opcode.equals("11001") && operand1 == operand2) ||
			(opcode.equals("11010") && operand1 != operand2) ||
			(opcode.equals("11011") && operand1 < operand2)  ||
			(opcode.equals("11100") && operand1 > operand2)  ||
			(opcode.equals("11000")))
			{
				//System.out.println(opcode);
				isBranchTaken = true;
				System.out.println("is branch taken: " + isBranchTaken);
			}
		else
		{
			//System.out.println(opcode);
			System.out.println("is branch taken: " + isBranchTaken);
		}
	}
	
	
	public void performEX()
	{
		// getting the value of immx and others.
		if (OF_EX_Latch.isEX_enable())
		{
			isBranchTaken = false;
			//System.out.println("execute");
			immx = OF_EX_Latch.getImmx();
			branchTarget = OF_EX_Latch.getBranchTarget();
			op1 = OF_EX_Latch.getOp1();
			op2 = OF_EX_Latch.getOp2();
			opcode = OF_EX_Latch.getOpcode();
			instruction = OF_EX_Latch.getInstruction();
			
			// performing the alu result:
			int operand2 = op2;
			if (isImmediate()) {
			//	System.out.println("is immediate");
			//	System.out.println(immx);
				operand2 = immx;
			}
			
			System.out.println("implementing alu");
			System.out.println("instruction is: " + instruction);
			System.out.println("op1 is: " + op1);
			System.out.println("op2 is: " + operand2);
			aluResult = implementAlu(op1, operand2);
			System.out.println("aluresult is: "+aluResult);
			checkBranch(op1,operand2);
			
			//System.out.println("in performEX");
			//System.out.println(op1);
			//System.out.println(operand2);
			//System.out.println(opcode);
			
			//System.out.println(aluResult);
		
			// changing the latch values: 
			OF_EX_Latch.setEX_enable(false);
			EX_MA_Latch.setMA_enable(true);
			EX_IF_Latch.setIF_enable(true);
			
			EX_IF_Latch.setBranchPC(branchTarget);
			EX_IF_Latch.setIsBRanchTaken(isBranchTaken);
			
			EX_MA_Latch.setOp2(op2);
			EX_MA_Latch.setAluResult(aluResult);
			EX_MA_Latch.setOpcode(opcode);
			EX_MA_Latch.setInstruction(instruction);
		}
	}

}
