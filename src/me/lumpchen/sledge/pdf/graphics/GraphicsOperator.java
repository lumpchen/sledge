package me.lumpchen.sledge.pdf.graphics;

import java.util.Stack;

import me.lumpchen.sledge.pdf.syntax.basic.PReal;

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
		return new OPre();
	}
	
	public String toString() {
		return new String(this.operatorBytes);
	}
	
	public int getOperandNumber() {
		return this.operandNumber;
	}

	abstract public void execute(Stack<GraphicsOperand> operandStack, VirtualGraphics g2d);
}

class OPq extends GraphicsOperator {
	
	public OPq() {
		super(new byte[]{'q'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Stack<GraphicsOperand> operandStack, VirtualGraphics g2d) {
		
	}
}


class OPn extends GraphicsOperator {
	
	public OPn() {
		super(new byte[]{'n'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Stack<GraphicsOperand> operandStack, VirtualGraphics g2d) {
		
	}
}

class OPcm extends GraphicsOperator {
	
	public OPcm() {
		super(new byte[]{'c', 'm'});
		this.operandNumber = 4;
	}

	@Override
	public void execute(Stack<GraphicsOperand> operandStack, VirtualGraphics g2d) {
		
	}
}

class OPrg extends GraphicsOperator {
	
	public OPrg() {
		super(new byte[]{'r', 'g'});
		this.operandNumber = 3;
	}

	@Override
	public void execute(Stack<GraphicsOperand> operandStack, VirtualGraphics g2d) {
		
	}
}

class OPW extends GraphicsOperator {
	
	public OPW() {
		super(new byte[]{'W'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Stack<GraphicsOperand> operandStack, VirtualGraphics g2d) {
		
	}
}

class OPre extends GraphicsOperator {
	
	public OPre() {
		super(new byte[]{'r', 'e'});
		this.operandNumber = 4;
	}

	@Override
	public void execute(Stack<GraphicsOperand> operandStack, VirtualGraphics g2d) {
		GraphicsOperand operand_1 = operandStack.pop();
		GraphicsOperand operand_2 = operandStack.pop();
		GraphicsOperand operand_3 = operandStack.pop();
		GraphicsOperand operand_4 = operandStack.pop();
		
		PReal x = operand_1.asReal();
		PReal y = operand_2.asReal();
		PReal w = operand_3.asReal();
		PReal h = operand_4.asReal();
		
	}
}

