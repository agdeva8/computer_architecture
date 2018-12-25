package generic;

public class CPUAluEvent extends Event{
int operand1,operand2;
String opcode;
	
	public CPUAluEvent(long eventTime, Element requestingElement, Element processingElement, int operand1, int operand2, String opcode)
	{
		super(eventTime, EventType.CPUAlu, requestingElement, processingElement);
		this.operand1 = operand1;
		this.operand2 = operand2;
		this.opcode = opcode;
	}
	
	public int getOperand1()
	{
		return operand1;
	}
	
	public int getOperand2()
	{
		return operand2;
	}
	
	public String getOpcode()
	{
		return opcode;
	}
}
