package assignment_0;
import java.util.Random;

//this method is build to define the properties of border
public class border {
	// defining the initial width of the border
	public int width = 8;
	// setting the length of the border which does not changes with time 
	private int length = 1000;
	private sensor sensorGrid[][] = new sensor[width][length];
	public double pval_on = 0.2;
	public int time_taken = 0;
	public infiltrator firstInfiltrator = new infiltrator();
	
	public static void main(String[] args) {
		border testBorder = new border();
		testBorder.initialiseGrid();
		testBorder.changePvalGrid();
		testBorder.updateGridState();
		testBorder.printAttributes();
		testBorder.getTimeCross();
		//testBorder.updateGridState();
		testBorder.printAttributes();
		//System.out.println("");
		//testBorder.printAttributes();
		
		// now going into the infiltrator part
	}
	
	private void initialiseGrid()
	{
		for(int wi=0; wi<width; wi++)
			for(int li=0; li<length; li++)
				sensorGrid[wi][li] = new sensor();
	}
	
	private void changePvalGrid()
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
	
	private void getTimeCrossing()
	{
		System.out.println("in main algo");
		
		int pos_i = 0;
		Random rand = new Random();
		
		int pos_j = rand.nextInt(length);
		
		System.out.println("before first whie");
		while(sensorGrid[0][pos_j].sensorState == 1)
		{
			pos_j = rand.nextInt(length);
			System.out.println(pos_j);
		}
		System.out.println("after first while");
		
		time_taken = time_taken + 10;
		
		System.out.println("before second while loop");
		while(pos_i < width-1)
		{
			System.out.println(pos_i);
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
			
			time_taken += 10;
		}
		System.out.println("after second while loop");
			
	}
	//private void updateGridConfig()
	//{
		
	//}
	
	private void printAttributes()
	{
		//sensor trySensor = new sensor();
		//System.out.println(trySensor.getState());
		//trySensor.changePvalue(0.5);
		//System.out.println(trySensor.pval_on);
		//trySensor.updateState();
		System.out.print("time taken is ");
		System.out.println(time_taken);
		//System.out.println(sensorGrid[3][200].sensorState);
		System.out.println(sensorGrid[3][200].pval_on);
		//System.out.println(trySensor.getState());
		System.out.print("length of the border is ");
		System.out.println(length);
		System.out.print("Width of the border is ");
		System.out.println(width);
	}
	
}