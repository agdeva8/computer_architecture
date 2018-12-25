package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	boolean ex_busy;
	int instruction,immx,branchTarget,op1,op2;
	String opcode;
	boolean isBranchTaken=false;
	int pc = 0;
	
	public OF_EX_LatchType()
	{
		EX_enable = false;
		ex_busy = false;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}

	public String getOpcode() {
		return opcode;
	}

	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}

	public int getImmx() {
		return immx;
	}

	public void setImmx(int immx) {
		this.immx = immx;
	}

	public int getBranchTarget() {
		return branchTarget;
	}

	public void setBranchTarget(int branchTarget) {
		this.branchTarget = branchTarget;
	}
	
	public int getOp1() {
		return op1;
	}
	
	public void setOp1(int op1) {
		this.op1 = op1;
	}

	public int getOp2() {
		return op2;
	}

	public void setOp2(int op2) {
		this.op2= op2;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}
	
	public boolean isEXBusy()
	{
		return ex_busy;
	}
	
	public void setEXBusy(boolean ex_busy)
	{
		this.ex_busy = ex_busy;
	}
	
	public boolean getIsBranchTaken()
	{
		return isBranchTaken;
	}
	
	public void setIsBranchTaken(boolean isBranchTaken)
	{
		this.isBranchTaken = isBranchTaken;
	}
	
	public int getPC()
	{
		return pc;
	}
	
	public void setPC(int pc)
	{
		this.pc = pc;
	}
}
