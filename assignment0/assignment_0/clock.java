package assignment_0;

public class clock {

	private int time = 0;
	public static void main(String[] args) {
		clock tryclock = new clock();
		System.out.println(tryclock.getTime());
		tryclock.updateTime10();
		System.out.println(tryclock.getTime());

	}
	public void resetTime()
	{
		time = 0;
	}
	
	public int getTime()
	{
		return time;
		
	}
	
	public void updateTime10()
	{
		time += 10;
	}
	
	public void updateTime()
	{
		time += 1;
	}

}
