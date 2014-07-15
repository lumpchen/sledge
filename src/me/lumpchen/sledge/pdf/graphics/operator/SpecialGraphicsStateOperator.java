package me.lumpchen.sledge.pdf.graphics.operator;

import me.lumpchen.sledge.pdf.graphics.GraphicsOperand;
import me.lumpchen.sledge.pdf.graphics.Matrix;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.document.Page;

public class SpecialGraphicsStateOperator {
	// Special graphics state q, Q, cm

	/**
	 * Save the current graphics state on the graphics state stack
	 * */
	static class OP_q extends GraphicsOperator {
		public OP_q() {
			super(new byte[] { 'q' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
			g2d.saveGraphicsState();
		}
	}

	static class OP_Q_ extends GraphicsOperator {
		/**
		 * Restore the graphics state by removing the most recently saved state
		 * from the stack and making it the current state
		 * */
		public OP_Q_() {
			super(new byte[] { 'Q' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
			g2d.restoreGraphicsState();
		}
	}

	static class OP_cm extends GraphicsOperator {
		/**
		 * Modify the current transformation matrix (CTM) by concatenating the
		 * specified matrix
		 * */
		public OP_cm() {
			super(new byte[] { 'c', 'm' });
			this.operandNumber = 6;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
			GraphicsOperand operand_1 = this.operandList.get(0);
			GraphicsOperand operand_2 = this.operandList.get(1);
			GraphicsOperand operand_3 = this.operandList.get(2);
			GraphicsOperand operand_4 = this.operandList.get(3);
			GraphicsOperand operand_5 = this.operandList.get(4);
			GraphicsOperand operand_6 = this.operandList.get(5);

			double a = operand_1.asNumber().doubleValue();
			double b = operand_2.asNumber().doubleValue();
			double c = operand_3.asNumber().doubleValue();
			double d = operand_4.asNumber().doubleValue();
			double e = operand_5.asNumber().doubleValue();
			double f = operand_6.asNumber().doubleValue();

			g2d.concatenate(new Matrix(a, b, c, d, e, f));
		}
	}
}
