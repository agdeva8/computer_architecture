package processor.memorysystem;

import generic.*;
import generic.Event.EventType;
import processor.Clock;

public class MainMemory implements Element{
	int[] memory;
	
	public MainMemory()
	{
		memory = new int[65536];
	}
	
	public void handleEvent(Event e)
	{
		// now working for read type request;
		if(e.getEventType() == EventType.MemoryRead)
		{
			MemoryReadEvent event = (MemoryReadEvent) e;
			Simulator.getEventQueue().addEvent(
					new MemoryResponseEvent(
							Clock.getCurrentTime(),
							this,
							event.getRequestingElement(),
							getWord(event.getAddressToReadFrom()),
							event.getAddressToReadFrom()
							));
		}
		
		// working on write type request;
		if(e.getEventType() == EventType.MemoryWrite)
		{
			MemoryWriteEvent event = (MemoryWriteEvent) e;
			Simulator.getEventQueue().addEvent(
					new MemoryResponseEvent(
							Clock.getCurrentTime(),
							this,
							event.getRequestingElement(),
							0,
							event.getAddressToWriteTo()));
			setWord(event.getAddressToWriteTo(), event.getValue());
		}
	}
	
	public int getWord(int address)
	{
		return memory[address];
	}
	
	public void setWord(int address, int value)
	{
		memory[address] = value;
	}
	
	public String getContentsAsString(int startingAddress, int endingAddress)
	{
		if(startingAddress == endingAddress)
			return "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("\nMain Memory Contents:\n\n");
		for(int i = startingAddress; i <= endingAddress; i++)
		{
			sb.append(i + "\t\t: " + memory[i] + "\n");
		}
		sb.append("\n");
		return sb.toString();
	}
}
