package processor.memorysystem;

import configuration.Configuration;
import generic.*;
import generic.Event.EventType;
import processor.*;

public class Cache implements Element{
	Processor containingProcessor;
	int cache_size;
	int associativity;
	int line_size;
	int total_sets;
	CacheLine myCache[];
	
	public Cache(Processor containingProcessor, int cache_size, int associativity, int line_size)
	{
		this.containingProcessor = containingProcessor;
		this.cache_size = cache_size;
		this.associativity = associativity;
		this.line_size = line_size;
		
		total_sets = cache_size/(line_size*associativity);
		myCache = new CacheLine[total_sets];
		for(int i=0; i<total_sets; i++)	myCache[i] = new CacheLine(associativity);
	}
		
	public void handleEvent(Event e)
	{
		if(e.getEventType() == EventType.CacheRead)
		{
			CacheReadEvent event = (CacheReadEvent) e;
			int address = event.getAddress();
			
			// can't use tag as described in book because of the address type
			// it is using in this simulation;
			// hence using simple indexing;
			int setNumber = address % total_sets;
			if(myCache[setNumber].inCacheLine(address)) {
				int data = myCache[setNumber].getCacheLine(address);
				
				Simulator.getEventQueue().addEvent(
						new CacheResponseEvent(
								Clock.getCurrentTime(),
								this,
								event.getRequestingElement(),
								data,
								event.getAddress()
								));
			}
			else
			{
				Simulator.getEventQueue().addEvent(
						new MemoryReadEvent(
								Clock.getCurrentTime() + Configuration.mainMemoryLatency,
								event.getPipelineElement(),
								this,
								containingProcessor.getMainMemory(),
								event.getAddress()));
			}
			
		}
		
		if(e.getEventType() == EventType.MemoryResponse)
		{
			MemoryResponseEvent event = (MemoryResponseEvent) e;
			int address = event.getPC();
			int value = event.getValue();
			int setNumber = address % total_sets;
			myCache[setNumber].setCacheLine(address, value);
		}
		
		if(e.getEventType() == EventType.CacheWrite)
		{
			CacheWriteEvent event = (CacheWriteEvent) e;
			int address = event.getAddress();
			
			// can't use tag as described in book because of the address type
			// it is using in this simulation;
			// hence using simple indexing;
			int value = event.getValue();
			int setNumber = address % total_sets;

			myCache[setNumber].setCacheLine(address, value);
			Simulator.getEventQueue().addEvent(
					new CacheResponseEvent(
							Clock.getCurrentTime(),
							this,
							event.getRequestingElement(),
							0,
							event.getAddress()
							));

			Simulator.getEventQueue().addEvent(
					new MemoryWriteThroughEvent(
							Clock.getCurrentTime() + Configuration.mainMemoryLatency,
							event.getPipelineElement(),
							this,
							containingProcessor.getMainMemory(),
							event.getAddress(),
							event.getValue()));
						
		}
	}	
}
