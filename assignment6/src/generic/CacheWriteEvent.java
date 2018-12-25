package generic;

public class CacheWriteEvent extends Event{
	int address;
	int value;
	Element pipelineElement;
	
	public CacheWriteEvent(long eventTime, Element pipelineElement, Element requestingElement, Element processingElement, int address, int value) {
		super(eventTime, EventType.CacheWrite, requestingElement, processingElement);
		this.address = address;
		this.value = value;
		this.pipelineElement = pipelineElement;
	}
	
	public void setAddress(int address)
	{
		this.address = address;
	}
	
	public int getAddress()
	{
		return address;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public void setValue(int value)
	{
		this.value = value;
	}
	
	public Element getPipelineElement()
	{
		return pipelineElement;
	}
}

