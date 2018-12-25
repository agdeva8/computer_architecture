package processor.pipeline;

import generic.Statistics;
import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	
	int instruction, immx, branchTarget, op1, op2, currentPC;
	String opcode, rs1,rs2;
	boolean of_conflict = false, isBranchTaken = false;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	private boolean checkSelectedConfict(int instructionB)
	{
		if (instructionB == 0) return false;
		String instructB_bits = ControlUnit.instruction2Bits(instructionB);
		
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
		
		// checking if we need to check the conflict for op2;
		boolean isImm = ControlUnit.isImmediate(opcodeA);
		
		// checking for source 2 ;
		// store needs all the parameters (rs1 (rs2) and rd(rs1));
		if ((!isImm || ControlUnit.isSt(opcodeA)) && rs2.equals(rd_bin)) return true;
		
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
	
	private void performNoConflict()
	{
		IF_OF_Latch.setOFConflict(false);
		int rs1_val = Integer.parseInt(rs1, 2);  //rs1 and rs2 are not 2's compliment: 
		int rs2_val = Integer.parseInt(rs2, 2);
		
		if(rs1_val<0) rs1_val = 0;
		if(rs2_val<0) rs2_val = 0;
		
		op1 = containingProcessor.getRegisterFile().getValue(rs1_val);
		op2 = containingProcessor.getRegisterFile().getValue(rs2_val);

		
		IF_OF_Latch.donePendingTask();
		OF_EX_Latch.setOpcode(opcode);
		OF_EX_Latch.setImmx(immx);
		OF_EX_Latch.setBranchTarget(branchTarget);
		OF_EX_Latch.setOp1(op1);
		OF_EX_Latch.setOp2(op2);
		OF_EX_Latch.setInstruction(instruction);
		OF_EX_Latch.setPC(currentPC);
		OF_EX_Latch.setEX_enable(true);
		
		if(OF_EX_Latch.getIsBranchTaken())
			isBranchTaken = true;
		else
			isBranchTaken = false;
		return;
	}
	
	public void performOF()
	{
		// reseting the fetch becuase branch is taken;
		if(isBranchTaken)
		{
			instruction = 0;
			currentPC = 0;
			isBranchTaken = false;
			
			IF_OF_Latch.nullPendingTask();
			IF_OF_Latch.setOFConflict(false);
			IF_OF_Latch.setOFBusy(false);
			OF_EX_Latch.setEX_enable(false);
			return;
		}
		
		if(OF_EX_Latch.isEXBusy()) {
			IF_OF_Latch.setOFBusy(true);
			return;
		}
		else {
			IF_OF_Latch.setOFBusy(false);		
			// for the case when OF gets disabled and because of conflict it wasn't able to give values;
//			if(IF_OF_Latch.isOFConflict())
//			{
//				if (!isConfict()) {	// retrying to pass operands to execute stage;
//					performNoConflict();
//					return;
//				}
//			}
		}
		
		if(IF_OF_Latch.isOF_enable() || IF_OF_Latch.isPendingTask())
		{
			// getting instruction;
			instruction = IF_OF_Latch.getInstruction();
			currentPC = IF_OF_Latch.getPC();
			String instructionBits = ControlUnit.instruction2Bits(instruction);
			
			opcode = instructionBits.substring(0,5);
			String imm = instructionBits.substring(15,32);
			immx = ControlUnit.BinaryToInt(imm);
			
			String destImm = instructionBits.substring(10,32);
			if (opcode.equals("11000")) {
				branchTarget = ControlUnit.BinaryToInt(destImm) + currentPC;
			}
			else
				branchTarget = immx + currentPC;
			
			// rs1 is the register used in both R3, R2I type and unused in RI type:
			if(ControlUnit.isSt(opcode)) {
				rs1 = instructionBits.substring(10,15);   //[rd+imm] = rs1;	 // rs1 = rd
		  		rs2 = instructionBits.substring(5,10);	  // rs2 = rs1
			}
			else {
				rs1 = instructionBits.substring(5,10);
				rs2 = instructionBits.substring(10,15);
			}
			
			if (!isConfict())
				performNoConflict();
			else {
				Statistics.updateOFStall();
				IF_OF_Latch.setOFConflict(true);
				OF_EX_Latch.setEX_enable(false);
			}
		}
		else
			OF_EX_Latch.setEX_enable(false);
			
			if(OF_EX_Latch.getIsBranchTaken())
				isBranchTaken = true;
			else
				isBranchTaken = false;
			
	}

}
