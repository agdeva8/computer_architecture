package generic;

import java.io.PrintWriter;

public class Statistics {
	
	static int numberOfInstructions = 0;
	static int numberOfCycles = 1;

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);

			//writer.println("Number of cycles taken = " + numberOfCycles);
			//writer.println("Number of dynamic instructions = " + numberOfInstructions);
				
			writer.println(numberOfCycles+","+numberOfInstructions);
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	

	public static void updateInstruction() {
		numberOfInstructions += 1;
	}
	
	public static void updateCycles() { 
		numberOfCycles += 1;
	}
}
