package generic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.regex.Pattern;
import java.util.Scanner;

public class Simulator {	
	static FileInputStream inputcodeStream = null;

	public static void setupSimulation(String assemblyProgramFile, String objectProgramFile)
	{	
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}
	
	private static String getOpcode(Instruction.OperationType op)
	{
		String opcode="";
		switch(op)
		{
		case add:
			opcode="00000";
			break;
		case addi:
			opcode="00001";
			break;
		case sub:
			opcode="00010";
			break;
		case subi:
			opcode="00011";
			break;
		case mul:
			opcode="00100";
			break;
		case muli:
			opcode="00101";
			break;
		case div:
			opcode="00110";
			break;
		case divi:
			opcode="00111";
			break;
		case and:
			opcode="01000";
			break;
		case andi:
			opcode="01001";
			break;
		case or:
			opcode="01010";
			break;
		case ori:
			opcode="01011";
			break;
		case xor:
			opcode="01100";
			break;
		case xori:
			opcode="01101";
			break;
		case slt:
			opcode="01110";
			break;
		case slti:
			opcode="01111";
			break;
		case sll:
			opcode="10000";
			break;
		case slli:
			opcode="10001";
			break;
		case srl:
			opcode="10010";
			break;
		case srli:
			opcode="10011";
			break;
		case sra:
			opcode="10100";
			break;
		case srai:
			opcode="10101";
			break;
		case jmp:
			opcode="11000";
			break;
		case beq:
			opcode="11001";
			break;
		case bne:
			opcode="11010";
			break;
		case blt:
			opcode="11011";
			break;
		case bgt:
			opcode="11100";
			break;
		case load:
			opcode="10110";
			break;
		case store:
			opcode="10111";
			break;
		case end:
			opcode="11101";
			break;
			default:
				System.out.println("Something is wrong with the code!");
		}
		return opcode;
	}
	private static int BinaryToInt(String binaryInt) {
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
	
	private static String intBinary(int val,int nbits)
	{
		String strBinary = Integer.toBinaryString(val);
		if(val>=0) while(strBinary.length()<nbits) strBinary = "0"+strBinary;
		else strBinary = strBinary.substring(32-nbits);
		return strBinary;	
	}
	private static void writeToFile(String objectProgramFile,int val)
	{
		//System.out.print("writeToFile says: ");	## for debuggin purpose
		//System.out.println(val); 			## for debugging purpose
		FileOutputStream fos = null;
		DataOutputStream dos = null;
		
		try {
			fos = new FileOutputStream(objectProgramFile,true);
			dos = new DataOutputStream(fos);
			dos.writeInt(val);
		
		}
		catch(Exception e) {
			System.out.println("error occured");
		}
	}
	private static void saveDataNumbers(String assemblyProgramFile,String objectProgramFile)
	{
		FileInputStream inputStream = null;
		try
		{
			inputStream = new FileInputStream(assemblyProgramFile);
		}
		catch(FileNotFoundException e) {
			Misc.printErrorAndExit(e.toString());
		}
		
		Scanner sc=new Scanner(inputStream);							
		String line=sc.nextLine();
			
		if(line.contains(".data"))
			line=sc.next();
		int dataTemp=0;
		while(!line.contains(".text"))
		{
			if(line.matches(".*\\d+.*"))
			{
				dataTemp=Integer.parseInt(line);
				writeToFile(objectProgramFile,dataTemp);
			}
			line = sc.next();
		}
			sc.close();	
	}
	public static void assemble(String assemblyProgramFile,String objectProgramFile)
	{
		int pc= ParsedProgram.firstCodeAddress;
		writeToFile(objectProgramFile,pc);
		saveDataNumbers(assemblyProgramFile,objectProgramFile);
		int lenInstructions = ParsedProgram.code.size();
		for(int l=0;l<lenInstructions;l++) {	
			Instruction newInstruction = ParsedProgram.getInstructionAt(pc);
		
			Operand srcOp1 = newInstruction.getSourceOperand1();
			Operand srcOp2 = newInstruction.getSourceOperand2();
			Operand dstOp = newInstruction.getDestinationOperand();
		
		
			Instruction.OperationType op = newInstruction.getOperationType();
			String opcode=getOpcode(op);

			String final_instruct=opcode;
		
			if(srcOp1==null)
			{
				if(dstOp==null)
					final_instruct += "000000000000000000000000000";
				else if(dstOp.getOperandType() == Operand.OperandType.Label)
				{
					int imm_val = ParsedProgram.symtab.get(dstOp.getLabelValue())-pc;
					String imm_bin = intBinary(imm_val,22);
					final_instruct += "00000" + imm_bin;
				}
				
			}
			else
			{
				int rs1_val = srcOp1.getValue();
				String rs1B = intBinary(rs1_val,5);
			
				if(dstOp.getOperandType() == Operand.OperandType.Register)
				{
					int rd_val = dstOp.getValue();
					String rdB = intBinary(rd_val,5);
				
					if(srcOp2.getOperandType() == Operand.OperandType.Register)
					{
						int rs2_val = srcOp2.getValue();
						String rs2B = intBinary(rs2_val,5);
				
						final_instruct += rs1B+rs2B+rdB+"000000000000"; 
					}
					else
					{
						int imm_val=0;
						if(srcOp2.getOperandType() == Operand.OperandType.Label)
							imm_val = ParsedProgram.symtab.get(srcOp2.getLabelValue());
						else
							imm_val = srcOp2.getValue();
						String immB = intBinary(imm_val,17);
						final_instruct += rs1B+rdB+immB;
					}
				}
				else if(dstOp.getOperandType() == Operand.OperandType.Label)
				{
					int rs2_val = srcOp2.getValue();
					String rs2B = intBinary(rs2_val,5);
				
					int imm_val = ParsedProgram.symtab.get(dstOp.getLabelValue())-pc;
				
					String imm_bin = intBinary(imm_val,17);	
				
					final_instruct += rs1B+rs2B+imm_bin;
				}
			
				
			}		
			int final_instruct_int = BinaryToInt(final_instruct); 
			writeToFile(objectProgramFile,final_instruct_int);
			pc += 1;
		
		}
		//System.out.println(pc); for debugging;
	}
	
}