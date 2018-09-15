package processor.pipeline;

import generic.Statistics;
import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	int instruction,immx,branchTarget,op1,op2;
	String opcode;
	String rs1,rs2;
	boolean dont_updatePC = false;
	
	public boolean shouldUpdatePC()
	{
		return !dont_updatePC;
	}
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	private String instruction2Bits(int instruction)
	{
		String instructionBits = Integer.toBinaryString(instruction);
		while(instructionBits.length() < 32)
			instructionBits = "0" + instructionBits;
		return instructionBits;
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
	private boolean checkSelectedConfict(int instructionB)
	{
		if (instructionB == 0) return false;
		int instructionA = instruction;
		String instructA_bits = instruction2Bits(instructionA);
		String instructB_bits = instruction2Bits(instructionB);
		
		String opcodeA = opcode;
		String opcodeB = instructB_bits.substring(0, 5);

		// for A is jmp or end : do nothing
		if (opcodeA.equals("11000") || opcodeA.equals("11101"))
			return false;
		
		// for B is store, jmp, branches or end : do nothing
		if (opcodeB.equals("10111") || 
			opcodeB.equals("11000") ||
			opcodeB.equals("11001") ||
			opcodeB.equals("11010") ||
			opcodeB.equals("11011") ||
			opcodeB.equals("11100") ||
			opcodeB.equals("11101"))
			return false;
		
		String rd_bin;
		if (opcodeB.charAt(4) == '1' || opcodeB.equals("10110"))
			rd_bin = instructB_bits.substring(10,15);
		else
			rd_bin = instructB_bits.substring(15,20);
		
		// checking for source 1;
		if (rs1.equals(rd_bin)) return true;
		
		// checking if we need to check the conflict for 
		boolean isImm = false;
		// general checking for arithmatic types:
		isImm = opcodeA.charAt(4) == '1'; 
		
		// checking for load: store is covered in arithmatic type:
		if (opcodeA.equals("10110"))  isImm = true;
		
		// checking for conditional statement
		// setting flase for beq, blt and store as these are wrongly flagged as true;
		if (opcodeA.equals("11001") || opcodeA.equals("11011") || opcodeA.equals("10111")) isImm = false;
		
		// checking for source 2 ;
		if (!isImm && rs2.equals(rd_bin)) return true;
		
		return false;
		
	}
	
	private boolean isConfict()
	{
		int ex_instruction = containingProcessor.getEXUnit().getInstrucion();
		int ma_instruction = containingProcessor.getMAUnit().getInstrucion();
		int rw_instruction = containingProcessor.getRWUnit().getInstrucion();

		return
		(checkSelectedConfict(ex_instruction) ||
		 checkSelectedConfict(ma_instruction) ||
		 checkSelectedConfict(rw_instruction));
		
	}
	
	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{
			// getting instrucion
			instruction = IF_OF_Latch.getInstruction();
			String instructionBits = instruction2Bits(instruction);
			
			
			opcode = instructionBits.substring(0,5);
			String imm = instructionBits.substring(15,32);
			immx = BinaryToInt(imm);
			
			
			String destImm = instructionBits.substring(10,32);
			int pc = containingProcessor.getRegisterFile().getProgramCounter();
			if (opcode.equals("11000")) {
				branchTarget = BinaryToInt(destImm) + pc;
			}
			else
				branchTarget = immx + pc;
			
			// rs1 is the register used in both R3, R2I type and unused in RI type:
			if(isSt(opcode)) {
				rs1 = instructionBits.substring(10,15);   //[rd+imm] = rs1;	 // rs1 = rd
		  		rs2 = instructionBits.substring(5,10);	  // rs2 = rs1
			}
			else {
				rs1 = instructionBits.substring(5,10);
				rs2 = instructionBits.substring(10,15);
			}
			
			if (!isConfict()) {
				dont_updatePC = false;
				int rs1_val = Integer.parseInt(rs1, 2);  //rs1 and rs2 are not 2's compliment: 
				int rs2_val = Integer.parseInt(rs2, 2);
				
				
				if(rs1_val<0) rs1_val = 0;
				if(rs2_val<0) rs2_val = 0;
				
				op1 = containingProcessor.getRegisterFile().getValue(rs1_val);
				op2 = containingProcessor.getRegisterFile().getValue(rs2_val);
	
				OF_EX_Latch.setOpcode(opcode);
				OF_EX_Latch.setImmx(immx);
				OF_EX_Latch.setBranchTarget(branchTarget);
				OF_EX_Latch.setOp1(op1);
				OF_EX_Latch.setOp2(op2);
				OF_EX_Latch.setInstruction(instruction);
				OF_EX_Latch.setEX_enable(true);
			}
			else {
				Statistics.updateOFStall();
				dont_updatePC = true;
				OF_EX_Latch.setEX_enable(false);
			}
			
			if (containingProcessor.getEXUnit().getIsBranchTaken()){
				OF_EX_Latch.setEX_enable(false);
			}
		}
		else {
			instruction = 0;
			OF_EX_Latch.setEX_enable(false);
		}
			
	}

}
