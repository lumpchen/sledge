package me.lumpchen.sledge.pdf.graphics.operator;

import me.lumpchen.sledge.pdf.graphics.GraphicsOperand;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.basic.PString;
import me.lumpchen.sledge.pdf.syntax.document.Page;

public class TextShowingOperator {
	// Text showing Tj, TJ, ', "
	static class OP_T_j extends GraphicsOperator {
		/**
		 * Show a text string.
		 * */
		public OP_T_j() {
			super(new byte[] { 'T', 'j' });
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
			GraphicsOperand operand = this.operandList.get(0);
			PString s = operand.asString();

			g2d.showText(s.toJavaString());
		}
	}

	static class OP_TJ_ extends GraphicsOperator {

		public OP_TJ_() {
			super(new byte[] { 'T', 'J' });
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
			GraphicsOperand operand = this.operandList.get(0);

			PArray arr = operand.asArray();
			StringBuilder buf = new StringBuilder();
			for (int i = 0; i < arr.size(); i++) {
				PObject obj = arr.get(i);
				if (obj instanceof PString) {
					buf.append(((PString) obj).toJavaString());
				} else if (obj instanceof PNumber) {

				}
			}

			g2d.showText(buf.toString());
		}
	}

	static class OP_39 extends GraphicsOperator {

		public OP_39() {
			super(new byte[] { '\'' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_34 extends GraphicsOperator {

		public OP_34() {
			super(new byte[] { '\"' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}
}
