package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	int instruction;
	boolean of_busy;
	boolean of_conflict;
	int pc;
	int pendingTask = 0;
	
	public IF_OF_LatchType()
	{
		OF_enable = false;
		of_busy = false;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}
	
	public boolean isOFBusy()
	{
		return of_busy;
	}
	
	public void setOFBusy(boolean of_busy)
	{
		this.of_busy = of_busy;
	}
	
	public boolean isOFConflict()
	{
		return of_conflict;
	}
	
	public void setOFConflict(boolean of_conflict)
	{
		this.of_conflict = of_conflict;
	}
	
	public int getPC()
	{
		return pc;
	}
	
	public void setPC(int pc)
	{
		this.pc = pc;
	}
	
	public boolean isPendingTask()
	{
		return pendingTask > 0;
	}
	
	public void updatePendingTask()
	{
		this.pendingTask += 1;
	}
	
	public void donePendingTask()
	{
		this.pendingTask -= 1;
	}
	
	public void nullPendingTask()
	{
		this.pendingTask = 0;
	}
}
