package generic;

public class CacheReadEvent extends Event{
	int address; 
	Element pipelineElement;
	
	public CacheReadEvent(long eventTime,Element pipelineElement, Element requestingElement, Element processingElement, int address)
	{
		super(eventTime, EventType.CacheRead, requestingElement, processingElement);
		this.address = address;
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
	
	public Element getPipelineElement()
	{
		return pipelineElement;
	}
}

