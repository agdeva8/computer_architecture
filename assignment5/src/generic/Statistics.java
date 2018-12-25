package generic;

import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	static int numberOfInstructions;
	static int numberOfCycles = 1;
	static int numberOfStall = 0; 
	static int numberOfInstructWrongPath;

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);

			writer.println("Number of cycles taken = " + numberOfCycles);
			writer.println("Number of OF Stall     = " + numberOfStall);
			writer.println("Number of times an instruction on a wrong branch path entered= " + numberOfInstructWrongPath);
			
			// TODO add code here to print statistics in the output file
			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	
	public static void updateOFStall()
	{
		numberOfStall += 1;
	}

	public static void updateBranchStall()
	{
		numberOfInstructWrongPath += 1;
	}

	public static void updateInstruction() {
		numberOfInstructions += 1;
	}
	
	public static void updateCycles() { 
		numberOfCycles += 1;
	}
}
