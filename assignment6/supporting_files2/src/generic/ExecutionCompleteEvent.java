package generic;

public class ExecutionCompleteEvent extends Event {
	
	int aluResult, remainder;
	
	public ExecutionCompleteEvent(long eventTime, Element requestingElement, Element processingElement, int aluResult, int remainder)
	{
		super(eventTime, EventType.ExecutionComplete, requestingElement, processingElement);
		this.aluResult = aluResult;
		this.remainder = remainder;
	}
	
	public int getAluResult()
	{
		return aluResult;
	}
	
	public int getRemainder()
	{
		return remainder;
	}

}
