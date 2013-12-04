package me.lumpchen.sledge.pdf.syntax;

import java.util.Stack;

import me.lumpchen.sledge.pdf.graphics.GraphicsOperand;
import me.lumpchen.sledge.pdf.graphics.GraphicsOperator;

public class ContentStream {
	
	private Stack<GraphicsOperator> operatorStack;
	private Stack<GraphicsOperand> operandStack;
	
	public ContentStream() {
		this.operatorStack = new Stack<GraphicsOperator>();
		this.operandStack = new Stack<GraphicsOperand>();
	}
	
	public void pushOperator(GraphicsOperator... ops) {
		for (GraphicsOperator op : ops) {
			this.operatorStack.push(op);		
		}
	}
	
	public void pushOperand(GraphicsOperand... operands) {
		for (GraphicsOperand operand : operands) {
			this.operandStack.push(operand);
		}
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		while (true) {
			if (this.operatorStack.empty()) {
				break;
			}
			GraphicsOperator op = this.operatorStack.pop();
			buf.append(op.toString());
			buf.append('\n');			
		}
		buf.append('\n');
		buf.append(this.operandStack.size());
		while (true) {
			if (this.operandStack.empty()) {
				break;
			}
			GraphicsOperand op = this.operandStack.pop();
			buf.append(op.toString());
			buf.append('\n');			
		}
		
		return buf.toString();
	}
}
