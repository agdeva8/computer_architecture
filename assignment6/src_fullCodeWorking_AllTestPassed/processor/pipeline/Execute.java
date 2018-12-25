package processor.pipeline;

import configuration.Configuration;
import generic.*;
import generic.Event.EventType;
import processor.Clock;
import processor.Processor;

public class Execute implements Element{
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;

	static int immx,branchTarget,op1,op2,operand2,aluResult;
	static int instruction = 0, wrongPCL1 = 0, wrongPCL2 = 0, currentPC=0;
	static String opcode;
	boolean isBranchTaken = false;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	private int calAluResult(int operand1, int operand2, String opcd)
	{
		int aluResult = 0;
		switch (opcd) {
		case "00000":
		case "00001":
			aluResult = operand1 + operand2;
			break;
		case "00010":
		case "00011":
			aluResult = operand1 - operand2;
			break;
		case "01000":
		case "01001":
			aluResult = operand1 & operand2;
			break;
		case "01010":
		case "01011":
			aluResult = operand1 | operand2;
			break;
		case "01100":
		case "01101":
			aluResult = operand1 ^ operand2;
			break;
		case "10000":
		case "10001":
			aluResult = operand1 << operand2;
			break;
		case "10010":
		case "10011":
			aluResult = operand1 >>> operand2;
			break;
		case "10100":
		case "10101":
			aluResult = operand1 >> operand2;
			break;
		case "10110":
		case "10111":
			aluResult = operand1 + operand2;
			break;
		
		}
		if (opcode.equals("01110") || opcode.equals("01111"))
			if(operand1 < operand2) aluResult = 1;
			else 					aluResult = 0;
	return aluResult;
	}
	
	
	private void resumePipeline()
	{
//		System.out.println("EX: Sending pc " + currentPC);
		OF_EX_Latch.setEXBusy(false);
		EX_MA_Latch.setMA_enable(true);
		
		checkBranch(op1,operand2);
		if(isBranchTaken) {
			wrongPCL1 = currentPC;
			wrongPCL2 = branchTarget;
		}
		
		EX_IF_Latch.setWrongPCL1(wrongPCL1);
		EX_IF_Latch.setWrongPCL2(wrongPCL2);
		EX_IF_Latch.setBranchPC(branchTarget);
		EX_IF_Latch.setIsBRanchTaken(isBranchTaken);
		
		OF_EX_Latch.setIsBranchTaken(isBranchTaken);
		
		EX_MA_Latch.setOp2(op2);
		EX_MA_Latch.setAluResult(aluResult);
		EX_MA_Latch.setOpcode(opcode);
		EX_MA_Latch.setInstruction(instruction);
		
	}
	
	public void handleEvent(Event e)
	{
		if(e.getEventType() == EventType.CPUMul)
		{
			CPUMulEvent event = (CPUMulEvent) e;
			Simulator.getEventQueue().addEvent(
					new ExecutionCompleteEvent
					(
							Clock.getCurrentTime(),
							this,
							event.getProcessingElement(),
							event.getOperand1() * event.getOperand2(),
							-1));
			return;
		}
		
		if(e.getEventType() == EventType.CPUDiv)
		{
			CPUDivEvent event = (CPUDivEvent) e;
			Simulator.getEventQueue().addEvent(
					new ExecutionCompleteEvent
					(
							Clock.getCurrentTime(),
							this,
							event.getProcessingElement(),
							event.getOperand1() / event.getOperand2(),
							event.getOperand1() % event.getOperand2()));
			return;
		}
		
		if(e.getEventType() == EventType.CPUAlu)
		{
			CPUAluEvent event = (CPUAluEvent) e;
			Simulator.getEventQueue().addEvent(
					new ExecutionCompleteEvent
					(
							Clock.getCurrentTime(),
							this,
							event.getProcessingElement(),
							calAluResult(event.getOperand1(),event.getOperand2(),event.getOpcode()),
							-1));
			return;
		}
		if(e.getEventType() == EventType.ExecutionComplete)
		{
			if(EX_MA_Latch.isMABusy())
			{
				e.setEventTime(Clock.getCurrentTime()+1);
				Simulator.getEventQueue().addEvent(e);
			}
			else {
				ExecutionCompleteEvent event = (ExecutionCompleteEvent) e;
				aluResult = event.getAluResult();
				int remainder = event.getRemainder();
				if(remainder != -1)
					containingProcessor.getRegisterFile().setValue(31,event.getRemainder());
				
				resumePipeline();
			}
		}
		return;
	}
	
	public int getInstrucion()
	{
		return instruction;
	}
	
	private void implementAlu(int operand1, int operand2)
	{
		switch (opcode) {
		case "00100":
		case "00101":
			Simulator.getEventQueue().addEvent(
					new CPUMulEvent(
							Clock.getCurrentTime()+Configuration.multiplier_latency,
							this,
							containingProcessor.getEXUnit(),
							operand1,
							operand2));
			break;
			
		case "00110":
		case "00111":
			Simulator.getEventQueue().addEvent(
					new CPUDivEvent(
							Clock.getCurrentTime()+Configuration.divider_latency,
							this,
							containingProcessor.getEXUnit(),
							operand1,
							operand2));
			break;
		
		default:
			Simulator.getEventQueue().addEvent(
					new CPUAluEvent(
							Clock.getCurrentTime()+Configuration.ALU_latency,
							this,
							containingProcessor.getEXUnit(),
							operand1,
							operand2,
							opcode));
			break;
		}
		OF_EX_Latch.setEXBusy(true);
	}
	
	private void checkBranch(int operand1,int operand2)
	{
		if ((opcode.equals("11001") && operand1 == operand2) ||
			(opcode.equals("11010") && operand1 != operand2) ||
			(opcode.equals("11011") && operand1 < operand2)  ||
			(opcode.equals("11100") && operand1 > operand2)  ||
			(opcode.equals("11000")))
				isBranchTaken = true;
	}
	
	
	public void performEX()
	{
		if(isBranchTaken)
		{
//			System.out.println("EX: branch is taken");
			instruction = 0;
			isBranchTaken = false;
			
			EX_IF_Latch.setIsBRanchTaken(false);
			
			OF_EX_Latch.setIsBranchTaken(false);
			OF_EX_Latch.setEXBusy(false);
			
			EX_MA_Latch.setMA_enable(false);
			return;
		}
		
		if(OF_EX_Latch.isEXBusy())
		{
//			System.out.println("EX: busy");
			// EX stage has its own work to do;
			EX_MA_Latch.setMA_enable(false);
			return;
		}
		
		isBranchTaken = false;
		// getting the value of immx and others.
		if (OF_EX_Latch.isEX_enable())
		{
//			System.out.println("EX: enabled");
			// EX has got some work from the OF stage;
			
			immx = OF_EX_Latch.getImmx();
			branchTarget = OF_EX_Latch.getBranchTarget();
			op1 = OF_EX_Latch.getOp1();
			op2 = OF_EX_Latch.getOp2();
			opcode = OF_EX_Latch.getOpcode();
			instruction = OF_EX_Latch.getInstruction();
			currentPC = OF_EX_Latch.getPC();
			
			
			// performing the alu result:
			operand2 = op2;
			if (ControlUnit.isImmediate(opcode))
				operand2 = immx;
			
			// for the sake of correctness: 
			isBranchTaken = false;
			
			implementAlu(op1, operand2);
			EX_IF_Latch.setIsBRanchTaken(false);
		}
		else{
//			System.out.println("EX not enabled");
			instruction = 0;
			EX_MA_Latch.setMA_enable(false);
		}
	}

}
