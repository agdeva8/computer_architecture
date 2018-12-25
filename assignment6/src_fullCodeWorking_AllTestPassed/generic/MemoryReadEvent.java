package generic;

public class MemoryReadEvent extends Event {

	int addressToReadFrom;
	Element pipelineElement;
	
	public MemoryReadEvent(long eventTime, Element pipelineElement, Element requestingElement, Element processingElement, int address) {
		super(eventTime, EventType.MemoryRead, requestingElement, processingElement);
		this.addressToReadFrom = address;
		this.pipelineElement = pipelineElement;
	}

	public int getAddressToReadFrom() {
		return addressToReadFrom;
	}

	public void setAddressToReadFrom(int addressToReadFrom) {
		this.addressToReadFrom = addressToReadFrom;
	}
	
	public Element getPipelineElement()
	{
		return pipelineElement;
	}
}
