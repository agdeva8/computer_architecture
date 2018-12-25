package generic;

public class CPUMulEvent extends Event{
int operand1,operand2; 
	
	public CPUMulEvent(long eventTime, Element requestingElement, Element processingElement, int operand1, int operand2)
	{
		super(eventTime, EventType.CPUMul, requestingElement, processingElement);
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
