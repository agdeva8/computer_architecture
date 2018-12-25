package generic;
import java.io.DataInputStream;
import java.io.FileInputStream;

import processor.Clock;
import processor.Processor;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	static EventQueue eventQueue = new EventQueue();
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}
	

	static void loadProgram(String assemblyProgramFile)
	{
			
		// taking the input from the file: 
		int pc=0,address=0;
		try
		{
			DataInputStream din = new DataInputStream(new FileInputStream(assemblyProgramFile));
			
			// loading program counter to the register file
			pc = din.readInt();
			processor.getRegisterFile().setProgramCounter(pc-1);
			 
			// loading the static data and the instruction to the main memory;
			while(din.available()>0)
			{
				processor.getMainMemory().setWord(address, din.readInt());
				address = address + 1;
			}
			din.close();
			
		}
		catch(Exception e)
		{
			System.out.println("cannot open file ");
		}
		
		// setting x0, x1 and x2 from the main memory;
	 	processor.getRegisterFile().setValue(0,0);
		processor.getRegisterFile().setValue(1,65535);
		processor.getRegisterFile().setValue(2,65535);
		
	}
	
	public static void simulate()
	{
		// changing simulator function for the pipelne.
		while(simulationComplete == false)
		{
//			if(Clock.getCurrentTime() == 2500)
//				break;
////			
			processor.getRWUnit().performRW();
			processor.getMAUnit().performMA();
			processor.getEXUnit().performEX();
			
			//now processing events
			eventQueue.processEvents();
			
			processor.getOFUnit().performOF();
		 	processor.getIFUnit().performIF();
		 	
		 	Statistics.updateCycles();
		 	Clock.incrementClock();
		 	
//		 	System.out.println("clock cycle is " + Clock.getCurrentTime());
		}
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
	
	public static EventQueue getEventQueue()
	{
		return eventQueue;
	}
}
