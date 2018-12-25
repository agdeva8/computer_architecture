package processor.pipeline;

public class EX_IF_LatchType {
	
	boolean IF_enable;
	int branchPC;
	boolean isBranchTaken;
	int wrongPCL1 = 0, wrongPCL2 = 0;
	
	public EX_IF_LatchType()
	{
		IF_enable = true;
		isBranchTaken = false;
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

	public int getBranchPC() {
		return branchPC;
	}

	public void setBranchPC(int branchPC) {
		this.branchPC= branchPC;
	}
	
	public boolean getIsBranchTaken() {
		return isBranchTaken;
	}

	public void setIsBRanchTaken(boolean isBranchTaken) {
		this.isBranchTaken = isBranchTaken;
	}
	
	public void setWrongPCL1(int wrongPCL1)
	{
		this.wrongPCL1 = wrongPCL1;
	}
	
	public void setWrongPCL2(int wrongPCL2)
	{
		this.wrongPCL2 = wrongPCL2;
	}

	public int getWrongPCL1()
	{
		return wrongPCL1;
	}
	
	public int getWrongPCL2()
	{
		return wrongPCL2;
	}
}
