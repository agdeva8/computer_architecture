package processor.memorysystem;

public class CacheLine
{
	int associativity = 1;
	int tag[];
	int data[];
	boolean lineUsed[];
	
	public CacheLine(int associativity)
	{
		this.associativity = associativity;
		tag = new int[associativity];
		data = new int[associativity];
		lineUsed = new boolean[associativity];
	}
	
	public boolean inCacheLine(int tag_add)
	{
		for(int i=0; i<associativity; i++)
			if(lineUsed[i] && tag[i] == tag_add) return true;
		return false;
	}
	
	public int getCacheLine(int tag_add)
	{
		for(int i=0; i<associativity; i++)
			if(lineUsed[i] && tag[i] == tag_add) return data[i];
		return 0;
	}
	
	public void setCacheLine(int tag_add, int data_val)
	{
		// now implementing burte force scheme any scheme as the associativity is one;
		if(inCacheLine(tag_add)) {
			for(int i=0; i<associativity; i++)
				if(lineUsed[i] && tag[i] == tag_add)
					data[i] = data_val;
		}
		else
		{
			boolean setIt = false;
			for(int i=0; !setIt && i<associativity; i++)
			{
				if(!lineUsed[i])
				{
					setIt = true;
					lineUsed[i] = true;
					tag[i] = tag_add;
					data[i] = data_val;
				}
			}
			if(!setIt)
			{
				tag[0] = tag_add;	data[0] = data_val;
			}
		}
	}
	
}
