package me.lumpchen.sledge.pdf.syntax;

import java.util.LinkedList;
import java.util.Queue;

import me.lumpchen.sledge.pdf.graphics.GraphicsOperand;
import me.lumpchen.sledge.pdf.graphics.GraphicsOperator;

public class ContentStream {
	
	private Queue<GraphicsOperator> operatorStack;
	private Queue<GraphicsOperand> operandStack;
	
	public ContentStream() {
		this.operatorStack = new LinkedList<GraphicsOperator>();
		this.operandStack = new LinkedList<GraphicsOperand>();
	}
	
	public void pushOperator(GraphicsOperator... ops) {
		for (GraphicsOperator op : ops) {
			this.operatorStack.add(op);		
		}
	}
	
	public void pushOperand(GraphicsOperand... operands) {
		for (GraphicsOperand operand : operands) {
			this.operandStack.add(operand);
		}
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		while (true) {
			if (this.operatorStack.isEmpty()) {
				break;
			}
			GraphicsOperator op = this.operatorStack.poll();
			buf.append(op.toString());
			
			int num = op.getOperandNumber();
			while (num > 0) {
				buf.append(' ');
				GraphicsOperand operand = this.operandStack.poll();
				buf.append(operand.toString());
				num--;
			}
			
			buf.append('\n');			
		}
		return buf.toString();
	}
}
