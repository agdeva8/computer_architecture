package generic;

public class CPUDivEvent extends Event{
int operand1,operand2; 
	
	public CPUDivEvent(long eventTime, Element requestingElement, Element processingElement, int operand1, int operand2)
	{
		super(eventTime, EventType.CPUDiv, requestingElement, processingElement);
		this.operand1 = operand1;
		this.operand2 = operand2;
	}
	
	public int getOperand1()
	{
		return operand1;
	}
	
	public int getOperand2()
	{
		return operand2;
	}
}
