package generic;

public class MemoryResponseEvent extends Event {

	int value,pc;
	
	public MemoryResponseEvent(long eventTime, Element requestingElement, Element processingElement, int value, int pc) {
		super(eventTime, EventType.MemoryResponse, requestingElement, processingElement);
		this.value = value;
		this.pc = pc;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public int getPC()
	{
		return pc;
	}
	
}

