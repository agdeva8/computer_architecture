package generic;

import java.util.Comparator;
import java.util.PriorityQueue;

import generic.Event.EventType;
import processor.*;

public class EventQueue {
	
	PriorityQueue<Event> queue;
	
	public EventQueue()
	{
		queue = new PriorityQueue<Event>(new EventComparator());
	}
	
	public void addEvent(Event event)
	{
		queue.add(event);
	}

	public void processEvents()
	{
		while(queue.isEmpty() == false && queue.peek().getEventTime() <= Clock.getCurrentTime())
		{
			Event event = queue.poll();
			event.getProcessingElement().handleEvent(event);
		}
	}

	public void deleteEvent(Processor containingProcessor)
	{
		PriorityQueue<Event> queue1 = new PriorityQueue<>(queue);
		queue =  new PriorityQueue<Event>(new EventComparator());
		while(!queue1.isEmpty())
		{
			Event event = queue1.poll();
			
			if(event.getEventType() == EventType.MemoryRead)
			{
				MemoryReadEvent e = (MemoryReadEvent) event;
				if(e.getPipelineElement() == containingProcessor.getIFUnit())
					continue;
			}
			
			if(event.getRequestingElement() == containingProcessor.getIFUnit() ||
				event.getProcessingElement() == containingProcessor.getIFUnit()){
				continue;
			}
			queue.add(event);
		}
	}
}

class EventComparator implements Comparator<Event>
{
	@Override
    public int compare(Event x, Event y)
    {
		if(x.getEventTime() < y.getEventTime())
		{
			return -1;
		}
		else if(x.getEventTime() > y.getEventTime())
		{
			return 1;
		}
		else
		{
			return 0;
		}
    }
}
