package me.lumpchen.sledge.pdf.graphics.operator;

import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.document.Page;

public class InlineImagesOperator {
	// Inline images BI, ID, EI
	static class OP_BI_ extends GraphicsOperator {

		public OP_BI_() {
			super(new byte[] { 'B', 'I' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_ID_ extends GraphicsOperator {

		public OP_ID_() {
			super(new byte[] { 'I', 'D' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_EI_ extends GraphicsOperator {

		public OP_EI_() {
			super(new byte[] { 'E', 'I' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

}
