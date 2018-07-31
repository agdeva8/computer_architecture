package assignment_0;

//this method is build to define the properties of border
public class border {
	// defining the initial width of the border
	int width = 8;
	// setting the length of the border which does not changes with time 
	private int length = 1000;
	private sensor sensorGrid[][] = new sensor[width][length];
	public double pval_on = 0.2;

	public static void main(String[] args) {
		border testBorder = new border();
		testBorder.initialiseGrid();
		testBorder.changePvalGrid();
		testBorder.updateGridState();
		testBorder.printAttributes();
		//System.out.println("");
		//testBorder.printAttributes();
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
		System.out.println(sensorGrid[3][200].sensorState);
		System.out.println(sensorGrid[3][200].pval_on);
		//System.out.println(trySensor.getState());
		System.out.print("length of the border is ");
		System.out.println(length);
		System.out.print("Width of the border is ");
		System.out.println(width);
	}
	
}