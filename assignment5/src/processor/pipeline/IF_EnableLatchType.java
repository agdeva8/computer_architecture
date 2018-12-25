package processor.pipeline;

public class IF_EnableLatchType {
	
	boolean IF_enable;
	boolean if_busy;
	public IF_EnableLatchType()
	{
		IF_enable = true;
		if_busy = false;
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}
	
	public boolean isIFBusy()
	{
		return if_busy;
	}
	
	public void setIFBusy(boolean if_busy)
	{
		this.if_busy = if_busy;
	}
	
}
