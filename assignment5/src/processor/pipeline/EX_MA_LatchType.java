package processor.pipeline;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	boolean ma_busy;
	int instruction,aluResult,op2;
	String opcode;
	
	public EX_MA_LatchType()
	{
		MA_enable = false;
		ma_busy = false;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	public int getAluResult() {
		return aluResult;
	}

	public void setAluResult(int aluResult) {
		this.aluResult= aluResult;
	}
	
	public int getOp2() {
		return op2;
	}

	public void setOp2(int op2) {
		this.op2= op2;
	}
	
	public String getOpcode() {
		return opcode;
	}

	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}
	
	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}
	
	public boolean isMABusy()
	{
		return ma_busy;
	}
	
	public void setMABusy(boolean ma_busy)
	{
		this.ma_busy = ma_busy;
	}
}
