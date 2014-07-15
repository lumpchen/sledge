package me.lumpchen.sledge.pdf.graphics.operator;

import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.document.Page;

public class CompatibilityOperator {
	// Compatibility BX, EX
	static class OP_BX_ extends GraphicsOperator {

		public OP_BX_() {
			super(new byte[] { 'B', 'X' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_EX_ extends GraphicsOperator {

		public OP_EX_() {
			super(new byte[] { 'E', 'X' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}
}
