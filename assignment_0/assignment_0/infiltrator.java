package assignment_0;
import java.util.Arrays;

public class infiltrator {

	// Assuming we have the knowledge of all the 8 cells including our own.
	// Still we are interested in 4 cells Own,Front,FrontLeft,FrontRight
	
	// getting the on status of the main cells
	// 0 indicates that the sensor is off in that cell 
	// 1 indicates that the sensor is on in that cell 
	int own_cell = 1;
	int front_cell = 1;
	int frontLeft_cell = 1;
	int frontRight_cell = 1;
	
	public static void main(String args[])
	{
		infiltrator testInfiltrator = new infiltrator();
		int[] possible_cells = testInfiltrator.findPossibleCells();
		System.out.println(Arrays.toString(possible_cells));
		System.out.println(testInfiltrator.finalDecision());
		//System.out.println("hello");
	}
	
	private int[] findPossibleCells()
	{
		int[] possible_cells = new int[3];
		//0:front cell; 1:frontLeft_cell; 2:frontRight_cell;
		
		if(own_cell == 0)
		{
			if(front_cell == 0)
				possible_cells[0] = 1;
			if(frontLeft_cell == 0)
				possible_cells[1] = 1;
			if(frontRight_cell == 0)
				possible_cells[2] = 1;
		}
		return possible_cells;
	}
	
	public int finalDecision()
	{
		// will return -1 if he decide not to move else 0,1,2 according to possible cell
		int[] possible_cells = findPossibleCells();
		for(int i=0; i<3; i++)
		{
			if(possible_cells[i] == 1)
				return i;
		}
		return -1;
	}
}
