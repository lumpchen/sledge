package me.lumpchen.sledge.pdf.graphics.operator;

import me.lumpchen.sledge.pdf.graphics.GraphicsOperand;
import me.lumpchen.sledge.pdf.graphics.Matrix;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.document.Page;

public class TextPositioningOperator {
	// Text positioning Td, TD, Tm, T*
	static class OP_T_d extends GraphicsOperator {

		public OP_T_d() {
			super(new byte[] { 'T', 'd' });
			this.operandNumber = 2;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
			GraphicsOperand operand_1 = this.operandList.get(0);
			GraphicsOperand operand_2 = this.operandList.get(1);
			
			double tx = operand_1.asNumber().doubleValue();
			double ty = operand_2.asNumber().doubleValue();
			g2d.transformTextPosition(tx, ty);
		}
	}

	static class OP_TD_ extends GraphicsOperator {

		public OP_TD_() {
			super(new byte[] { 'T', 'D' });
			this.operandNumber = 2;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
			GraphicsOperand operand_1 = this.operandList.get(0);
			GraphicsOperand operand_2 = this.operandList.get(1);
			
			double tx = operand_1.asNumber().doubleValue();
			double ty = operand_2.asNumber().doubleValue();
			
			g2d.setTextLeading(ty);
			g2d.transformTextPosition(tx, ty);
		}
	}

	static class OP_T_m extends GraphicsOperator {

		public OP_T_m() {
			super(new byte[] { 'T', 'm' });
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

			g2d.transformTextMatrix(new Matrix(a, b, c, d, e, f));
		}
	}

	static class OP_T_42 extends GraphicsOperator {

		public OP_T_42() {
			super(new byte[] { 'T', '*' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
			g2d.moveToNextTextLine();
		}
	}
}
