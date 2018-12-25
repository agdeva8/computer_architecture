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
//		System.out.println("EQ: event added");
//		System.out.println("EQ: queue size is "+queue.size());
//		System.out.println("EQ: event added is of type " + event.getEventType());
//		System.out.println("EQ: event added is from " + event.getRequestingElement());
//		System.out.println("EQ: event added is to " + event.getProcessingElement());
		queue.add(event);
	}

	public void processEvents()
	{
		while(queue.isEmpty() == false && queue.peek().getEventTime() <= Clock.getCurrentTime())
		{
			Event event = queue.poll();
//			System.out.println("EQ: deleting event of type "+event.getEventType());
			event.getProcessingElement().handleEvent(event);
//			System.out.println("EQ: queue size is " + queue.size());
		}
	}

	public void deleteEvent(Processor containingProcessor)
	{
//		System.out.println("deleting event");
//		System.out.println("queue size is " + queue.size());
		PriorityQueue<Event> queue1 = new PriorityQueue<>(queue);
		queue =  new PriorityQueue<Event>(new EventComparator());
		while(!queue1.isEmpty())
		{
			Event event = queue1.poll();
//			System.out.println("requesting element is " + event.getRequestingElement());
			
			if(event.getEventType() == EventType.MemoryRead)
			{
				MemoryReadEvent e = (MemoryReadEvent) event;
				if(e.getPipelineElement() == containingProcessor.getIFUnit())
					continue;
			}
			
			if(event.getRequestingElement() == containingProcessor.getIFUnit() ||
				event.getProcessingElement() == containingProcessor.getIFUnit()){
//				System.out.println("not processing element");
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
