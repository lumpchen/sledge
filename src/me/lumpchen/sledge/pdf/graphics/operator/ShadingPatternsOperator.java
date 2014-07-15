package me.lumpchen.sledge.pdf.graphics.operator;

import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.document.Page;

public class ShadingPatternsOperator {
	// Shading patterns sh
	static class OP_sh extends GraphicsOperator {

		public OP_sh() {
			super(new byte[] { 's', 'h' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

}
