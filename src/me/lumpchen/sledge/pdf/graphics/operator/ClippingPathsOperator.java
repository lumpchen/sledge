package me.lumpchen.sledge.pdf.graphics.operator;

import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.document.Page;

public class ClippingPathsOperator {
	// Clipping paths W, W*
	static class OP_W_ extends GraphicsOperator {
		/**
		 * Modify the current clipping path by intersecting it with the current
		 * path, using the nonzero winding number rule to determine which
		 * regions lie inside the clipping path.
		 * */
		public OP_W_() {
			super(new byte[] { 'W' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_W_42 extends GraphicsOperator {

		public OP_W_42() {
			super(new byte[] { 'W', '*' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

}
