package assignment_0;
import java.util.Random;

public class sensor {
	// Sensor class in our problem set.
	
	// defining some parameters to define the sensor
	public int sensorState = 1; // should be private and type boolean in future
	// 0 is OFF and 1 if ON
	
	public int infiltratorState= 0; // should be private and type boolean in future
	// 0 if not standing 1 if standing 
	
	public double pval_on = 0.7;
	
	public static void main(String args[])
	{
		// This method is now used to check the class
		sensor trySensor = new sensor();
		trySensor.updateState();
		trySensor.changePvalue(0.4);
		trySensor.printAttributes();
		if(trySensor.detectMotion(0))
			System.out.println("got caught");
		else
			System.out.println("mission ongoing");
		trySensor.printAttributes();
	}
	
	public void updateState()
	{
		Random rand = new Random();
		int n = rand.nextInt(100)+1;
		sensorState = 0;
		if(n <= (int)(pval_on*100))
			sensorState = 1;
	}
	
	
	public boolean detectMotion(int infiltratorNewState)
	{
		if(infiltratorNewState != infiltratorState && sensorState == 1)
			return true;
		
		// infiltrator does not got caught so updating his/her status
		infiltratorState = infiltratorNewState;
		return false;
	}
		
	private void printAttributes()
	{
		System.out.print("sensor state is ");
		System.out.println(sensorState);
		
		System.out.print("infiltrator state is ");
		System.out.println(infiltratorState);
		
		System.out.print("p value is ");
		System.out.println(pval_on);
	}
}