package me.lumpchen.sledge.pdf.graphics.operator;

import me.lumpchen.sledge.pdf.graphics.GraphicsOperand;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.document.Page;

public class PathConstructionOperator {
	// Path construction m, l, c, v, y, h, re
	static class OP_m extends GraphicsOperator {

		public OP_m() {
			super(new byte[] { 'm' });
			this.operandNumber = 2;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_l extends GraphicsOperator {

		public OP_l() {
			super(new byte[] { 'l' });
			this.operandNumber = 2;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_c extends GraphicsOperator {

		public OP_c() {
			super(new byte[] { 'c' });
			this.operandNumber = 6;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_v extends GraphicsOperator {

		public OP_v() {
			super(new byte[] { 'v' });
			this.operandNumber = 4;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_y extends GraphicsOperator {

		public OP_y() {
			super(new byte[] { 'y' });
			this.operandNumber = 4;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_h extends GraphicsOperator {

		public OP_h() {
			super(new byte[] { 'h' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_re extends GraphicsOperator {
		/**
		 * Append a rectangle to the current path as a complete subpath, with
		 * lower-left corner (x, y) and dimensions width and height in user
		 * space.
		 * */
		public OP_re() {
			super(new byte[] { 'r', 'e' });
			this.operandNumber = 4;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
			GraphicsOperand operand_1 = this.operandList.get(0);
			GraphicsOperand operand_2 = this.operandList.get(1);
			GraphicsOperand operand_3 = this.operandList.get(2);
			GraphicsOperand operand_4 = this.operandList.get(3);

			double x = operand_1.asNumber().doubleValue();
			double y = operand_2.asNumber().doubleValue();
			double w = operand_3.asNumber().doubleValue();
			double h = operand_4.asNumber().doubleValue();

			// g2d.rect(x, y, w, h);
		}
	}
}
