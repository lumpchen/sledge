package me.lumpchen.sledge.pdf.graphics;

public abstract class GraphicsOperator {

	GraphicsOperator() {
	}

	abstract public void execute(OperandStack operandStack, VirtualGraphics g2d);
}

class OPre extends GraphicsOperator {

	@Override
	public void execute(OperandStack operandStack, VirtualGraphics g2d) {
	}

}