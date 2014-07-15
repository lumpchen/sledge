package me.lumpchen.sledge.pdf.graphics.operator;

import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.document.Page;

public class XObjectsOperator {
	// XObjects Do
	static class OP_D_o extends GraphicsOperator {

		public OP_D_o() {
			super(new byte[] { 'D', 'o' });
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}
}
