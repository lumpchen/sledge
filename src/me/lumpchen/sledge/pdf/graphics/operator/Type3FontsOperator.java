package me.lumpchen.sledge.pdf.graphics.operator;

import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.document.Page;

public class Type3FontsOperator {
	// Type 3 fonts d0, d1
	static class OP_d0 extends GraphicsOperator {

		public OP_d0() {
			super(new byte[] { 'd', '0' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_d1 extends GraphicsOperator {

		public OP_d1() {
			super(new byte[] { 'd', '1' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}
}
