package generic;

public class CacheResponseEvent extends Event{
	int value,pc;
	
	public CacheResponseEvent(long eventTime, Element requestingElement, Element processingElement, int value, int pc) {
		super(eventTime, EventType.CacheResponse, requestingElement, processingElement);
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
