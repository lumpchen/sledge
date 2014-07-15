package me.lumpchen.sledge.pdf.graphics.operator;

import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.document.Page;

public class TextObjectsOperator {
	// Text objects BT, ET
	static class OP_BT_ extends GraphicsOperator {
		/**
		 * Begin a text object, initializing the text matrix, Tm , and the text
		 * line matrix, Tlm , to the identity matrix.
		 * */
		public OP_BT_() {
			super(new byte[] { 'B', 'T' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
			g2d.beginText();
		}
	}

	static class OP_ET_ extends GraphicsOperator {
		/**
		 * End a text object, discarding the text matrix.
		 * */
		public OP_ET_() {
			super(new byte[] { 'E', 'T' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
			g2d.endText();
		}
	}

}
