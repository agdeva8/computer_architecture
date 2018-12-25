package assignment_0;

//this method is build to define the properties of border
public class border {
	// defining the initial width of the border
	private int width = 8;
	// setting the length of the border which does not changes with time 
	private int length = 1000;
	public double pval_on = 0.2;
	private clock time_taken = new clock();
	private infiltrator firstInfiltrator = new infiltrator();
	public sensor sensorGrid[][];

	
	border(int width, double pval_on)
	{
		this.width = width;
		this.pval_on = pval_on;
		sensorGrid = new sensor[width][length];
	}
	
	public static void main(String[] args) {
		border testBorder = new border(10,0.2);
		testBorder.initialiseGrid();
		testBorder.changePvalGrid();
		//testBorder.updateGridState();
		//testBorder.printAttributes();
		System.out.println(testBorder.getTimeCross());
		//testBorder.updateGridState();
//		testBorder.printAttributes();
		//System.out.println("");
		//testBorder.printAttributes();
		
	}
	

	
	public void initialiseGrid()
	{
		for(int wi=0; wi<width; wi++)
			for(int li=0; li<length; li++)
				sensorGrid[wi][li] = new sensor();
	}
	
	public void changePvalGrid()
	{
		for(int wi=0; wi<width; wi++)
			for(int li=0; li<length; li++)
				sensorGrid[wi][li].pval_on = pval_on;
	}
	
	private void updateGridState()
	{
		for(int wi=0; wi<width; wi++)
			for(int li=0; li<length;li++)
				sensorGrid[wi][li].updateState();
	}

	
	private int getInfiltratorDesicion(int pos_i,int pos_j)
	{
		// will return -1 if he decided not to move
		// else 0:front cell; 1:frontLeft_cell; 2:frontRight_cell;
		
		// -1 is used to show that the cell doesnt exist
		firstInfiltrator.own_cell = sensorGrid[pos_i][pos_j].sensorState;
		if(pos_i+1 < width)
			firstInfiltrator.front_cell = sensorGrid[pos_i+1][pos_j].sensorState;
		else
			firstInfiltrator.front_cell = -1;
		
		if(pos_i+1 < width && pos_j-1>=0)
			firstInfiltrator.frontLeft_cell = sensorGrid[pos_i+1][pos_j-1].sensorState;
		else
			firstInfiltrator.frontLeft_cell = -1;
		
		if(pos_i+1 < width && pos_j+1<length)
			firstInfiltrator.frontRight_cell = sensorGrid[pos_i+1][pos_j+1].sensorState;
		else
			firstInfiltrator.frontRight_cell = -1;
		
		return(firstInfiltrator.finalDecision());
	}
	
	public int getTimeCross()
	{

		// first updating the grid according to the probablity of sensors
		
		int pos_i = 0;
		int pos_j = 0;
		
		time_taken.updateTime10();
		updateGridState();
		while(sensorGrid[0][pos_j].sensorState == 1)
		{
			pos_j += 1;
			if(pos_j >= length)
			{
				pos_j = 0;
				updateGridState();
				time_taken.updateTime10();
			}
		}
		while(pos_i < width-1)
		{
			updateGridState();
			int decision = getInfiltratorDesicion(pos_i,pos_j);
			
			if(decision == 0)
				pos_i += 1;
			if(decision == 1)
			{
				pos_i += 1;
				pos_j -= 1;
			}
			if(decision == 2)
			{
				pos_i += 1;
				pos_j += 1;
			}			
			time_taken.updateTime10();
		}
		while(sensorGrid[pos_i][pos_j].sensorState == 1)
		{
			time_taken.updateTime10();
			updateGridState();
		}
		time_taken.updateTime10();
		return time_taken.getTime();
	}

	public void resetTime()
	{
		time_taken.resetTime();
	}
	private void printAttributes()
	{
		System.out.print("time taken is ");
		System.out.println(time_taken.getTime());
		System.out.println(sensorGrid[3][200].pval_on);
		System.out.print("length of the border is ");
		System.out.println(length);
		System.out.print("Width of the border is ");
		System.out.println(width);
	}
	
}