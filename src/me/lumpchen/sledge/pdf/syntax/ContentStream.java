package me.lumpchen.sledge.pdf.syntax;

import java.util.LinkedList;
import java.util.List;

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
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		int size = this.operatorList.size();
		for (int i = 0; i < size; i++) {
			GraphicsOperator op = this.operatorList.get(i);
			buf.append(op.toString());
			buf.append('\n');
		}
		
		return buf.toString();
	}

	public void matchOperands() {
		int size = this.operatorList.size();
		int count = 0;
		for (int i = 0; i < size; i++) {
			GraphicsOperator op = this.operatorList.get(i);
			int operandCount = op.getOperandNumber();
			while (operandCount > 0) {
				GraphicsOperand operand = this.operandList.get(count++);
				op.addOperator(operand);
				operandCount--;
			}
		}
	}
	
	@Override
	public void render(VirtualGraphics g2) {
		int size = this.operatorList.size();
		for (int i = 0; i < size; i++) {
			GraphicsOperator op = this.operatorList.get(i);
			op.execute(g2);
		}
	}
}
