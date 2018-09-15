package processor.pipeline;

import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	int instruction,immx,branchTarget,op1,op2;
	String opcode;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	
	private static boolean isSt(String opcode)
	{
		return opcode.equals("10111");
	}
	
	
	private static int BinaryToInt(String binaryInt) {
	    if (binaryInt.charAt(0) == '1') {
	        String invertedInt = invertDigits(binaryInt);
	        int decimalValue = Integer.parseInt(invertedInt, 2);
	        decimalValue = (decimalValue + 1) * -1;
	        return decimalValue;
	    } else {
	        return Integer.parseInt(binaryInt, 2);
	    }
	}
	private static String invertDigits(String binaryInt) {
	    String result = binaryInt;
	    result = result.replace("0", " ");
	    result = result.replace("1", "0");
	    result = result.replace(" ", "1");
	    return result;
	}
	
	
	public void performOF()
	{
		//System.out.println("in perform OF");
		if(IF_OF_Latch.isOF_enable())
		{
			//System.out.println("operand fetch");
			// calculating immediate. 
			instruction = IF_OF_Latch.getInstruction();
			String instructionBits = Integer.toBinaryString(instruction);
			while(instructionBits.length() < 32)
				instructionBits = "0" + instructionBits;
			//System.out.println(instruction);
			//System.out.println(instructionBits);
			
			//System.out.println(instructionBits);
			
			opcode = instructionBits.substring(0,5);
			String imm = instructionBits.substring(15,32);
			immx = BinaryToInt(imm);
			
			System.out.println("opcode is: " + opcode);
			
			String destImm = instructionBits.substring(10,32);
			int pc = containingProcessor.getRegisterFile().getProgramCounter();
			if (opcode.equals("11000")) {
				//System.out.println("jmp statement");
				//System.out.println((BinaryToInt(destImm)*4+ pc);
				branchTarget = BinaryToInt(destImm) + pc;
			}
			else
				branchTarget = immx + pc;
			
			//System.out.println(pc);
			//System.out.println(BinaryToInt(destImm));
			//System.out.println(branchTarget);
			//System.out.println(immx);
			
			
			// rs1 is the register used in both R3, R2I type and unused in RI type:
			String rs1,rs2;
			if(isSt(opcode)) {
				rs1 = instructionBits.substring(10,15);   //[rd+imm] = rs1;	 // rs1 = rd
				rs2 = instructionBits.substring(5,10);	  // rs2 = rs1
			}
			else {
				rs1 = instructionBits.substring(5,10);
				rs2 = instructionBits.substring(10,15);
			}
			
			
			
			int rs1_val = Integer.parseInt(rs1, 2);  //rs1 and rs2 are not 2's compliment: 
			int rs2_val = Integer.parseInt(rs2, 2);
			
			//System.out.println("rs1 binary: " + rs1);
			System.out.println("rs1 values: " + rs1_val);
			//System.out.println("rs2 binary: " + rs2);
			System.out.println("rs2 values: " + rs2_val);
			
			
			if(rs1_val<0) rs1_val = 0;
			if(rs2_val<0) rs2_val = 0;
			
			//System.out.println(rs1_val);
			//System.out.println(rs2_val);
			
			op1 = containingProcessor.getRegisterFile().getValue(rs1_val);
			op2 = containingProcessor.getRegisterFile().getValue(rs2_val);
			//System.out.println("ops are ");
			//System.out.println(op1);
			//System.out.println(op2);
			
			//System.out.println(rs1);
			
			
			//if (isSt(opcode)) System.out.println("is store");
			//else 	  System.out.println("not store");
			//imm = instructionBits[]
			
			OF_EX_Latch.setOpcode(opcode);
			OF_EX_Latch.setImmx(immx);
			OF_EX_Latch.setBranchTarget(branchTarget);
			OF_EX_Latch.setOp1(op1);
			OF_EX_Latch.setOp2(op2);
			OF_EX_Latch.setInstruction(instruction);
			
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
			
		}
	}

}
