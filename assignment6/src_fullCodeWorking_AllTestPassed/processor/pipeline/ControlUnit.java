package processor.pipeline;

public class ControlUnit {
	public static boolean isEndInstruction(int instruction) {
		return instruction == -402653184;
	}
	
	public static String instruction2Bits(int instruction)
	{
		String instructionBits = Integer.toBinaryString(instruction);
		while(instructionBits.length() < 32)
			instructionBits = "0" + instructionBits;
		return instructionBits;
	}
	
	public static boolean isLd(String opcode)
	{
		return opcode.equals("10110");
	}
	
	public static boolean isSt(String opcode)
	{
		return opcode.equals("10111");
	}
	
	public static int BinaryToInt(String binaryInt) {
	    if (binaryInt.charAt(0) == '1') {
	        String invertedInt = invertDigits(binaryInt);
	        int decimalValue = Integer.parseInt(invertedInt, 2);
	        decimalValue = (decimalValue + 1) * -1;
	        return decimalValue;
	    } else {
	        return Integer.parseInt(binaryInt, 2);
	    }
	}
	
	private static String invertDigits(String binaryInt) {
	    String result = binaryInt;
	    result = result.replace("0", " ");
	    result = result.replace("1", "0");
	    result = result.replace(" ", "1");
	    return result;
	}
	
	public static boolean isImmediate(String opcode) {
		boolean isImm = false;
		// general checking for arithmatic types:
		isImm = opcode.charAt(4) == '1'; 
		
		// checking for load: store is covered in arithmatic type:
		if (opcode.equals("10110"))  isImm = true;
		
		// checking for conditional statement
		// setting flase for beq, blt as these are wrongly flagged as true;
		if (opcode.equals("11001") || opcode.equals("11011")) isImm = false;
		
		return isImm;
	}
	
}
