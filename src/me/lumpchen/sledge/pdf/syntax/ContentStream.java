package me.lumpchen.sledge.pdf.syntax;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import me.lumpchen.sledge.pdf.graphics.GraphicsOperand;
import me.lumpchen.sledge.pdf.graphics.GraphicsOperator;
import me.lumpchen.sledge.pdf.graphics.RenderObject;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;

public class ContentStream implements RenderObject {

	private List<GraphicsOperator> operatorList;
	private List<GraphicsOperand> operandList;

	public ContentStream() {
		this.operatorList = new LinkedList<GraphicsOperator>();
		this.operandList = new LinkedList<GraphicsOperand>();
	}

	public void pushOperator(GraphicsOperator... ops) {
		for (GraphicsOperator op : ops) {
			this.operatorList.add(op);
		}
	}

	public void pushOperand(GraphicsOperand... operands) {
		for (GraphicsOperand operand : operands) {
			this.operandList.add(operand);
		}
	}
	
	private Queue<GraphicsOperator> operatorStack() {
		Queue<GraphicsOperator> stack = new LinkedList<GraphicsOperator>();
		stack.addAll(this.operatorList);
		return stack;
	}
	
	private Queue<GraphicsOperand> operandStack() {
		Queue<GraphicsOperand> stack = new LinkedList<GraphicsOperand>();
		stack.addAll(this.operandList);
		return stack;
	}
	
	public String toString() {
		Queue<GraphicsOperator> operatorStack = operatorStack();
		Queue<GraphicsOperand> operandStack = operandStack();
		StringBuilder buf = new StringBuilder();
		while (true) {
			if (operatorStack.isEmpty()) {
				break;
			}
			GraphicsOperator op = operatorStack.poll();
			buf.append(op.toString());

			int num = op.getOperandNumber();
			while (num > 0) {
				buf.append(' ');
				GraphicsOperand operand = operandStack.poll();
				if (operand == null) {
					System.out.println(op);
				}
				buf.append(operand.toString());
				num--;
			}

			buf.append('\n');
		}
		return buf.toString();
	}

	@Override
	public void render(VirtualGraphics g2) {
		Queue<GraphicsOperator> operatorStack = operatorStack();
		Queue<GraphicsOperand> operandStack = operandStack();
		while (true) {
			if (operatorStack.isEmpty()) {
				break;
			}
			GraphicsOperator op = operatorStack.poll();
			op.execute(operandStack, g2);
		}
	}
}
