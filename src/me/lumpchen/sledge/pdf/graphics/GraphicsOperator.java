package me.lumpchen.sledge.pdf.graphics;

import java.util.Stack;

public abstract class GraphicsOperator {

	public static enum Group {
		GraphicsState, PathConstruction, PathPainting, OtherPainting, Text, MarkedContent
	};
	
	byte[] operatorBytes;
	int operandNumber;
	
	protected GraphicsOperator(byte[] operatorBytes) {
		this.operatorBytes = operatorBytes;
	}
	
	public static final GraphicsOperator create(byte[] operatorBytes) {
		return new OPre(operatorBytes);
	}
	
	public String toString() {
		return new String(this.operatorBytes);
	}
	
	public int getOperandNumber() {
		return this.operandNumber;
	}

	abstract public void execute(Stack<GraphicsOperand> operandStack, VirtualGraphics g2d);
}

class OPre extends GraphicsOperator {
	
	OPre(byte[] operatorBytes) {
		super(operatorBytes);
		this.operandNumber = 4;
	}

	@Override
	public void execute(Stack<GraphicsOperand> operandStack, VirtualGraphics g2d) {
		GraphicsOperand operand_1 = operandStack.pop();
		GraphicsOperand operand_2 = operandStack.pop();
		GraphicsOperand operand_3 = operandStack.pop();
		GraphicsOperand operand_4 = operandStack.pop();
		
		operand_1.asReal();
	}
}

