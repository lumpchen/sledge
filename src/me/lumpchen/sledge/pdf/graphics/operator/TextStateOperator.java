package me.lumpchen.sledge.pdf.graphics.operator;

import me.lumpchen.sledge.pdf.graphics.GraphicsOperand;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.document.Page;
import me.lumpchen.sledge.pdf.text.font.PDFFont;

public class TextStateOperator {
	// Text state Tc, Tw, Tz, TL, Tf, Tr, Ts
	static class OP_T_c extends GraphicsOperator {

		public OP_T_c() {
			super(new byte[] { 'T', 'c' });
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
			GraphicsOperand operand_1 = this.operandList.get(0);
			PNumber charSpace = operand_1.asNumber();
			g2d.setCharSpacing(charSpace.doubleValue());
		}
	}

	static class OP_T_w extends GraphicsOperator {

		public OP_T_w() {
			super(new byte[] { 'T', 'w' });
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
			GraphicsOperand operand_1 = this.operandList.get(0);
			PNumber wordSpace = operand_1.asNumber();
			g2d.setWordSpacing(wordSpace.doubleValue());
		}
	}

	static class OP_T_z extends GraphicsOperator {

		public OP_T_z() {
			super(new byte[] { 'T', 'z' });
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
			throw new GraphicsOperatorException("not implement yet!");
		}
	}

	static class OP_TL_ extends GraphicsOperator {

		public OP_TL_() {
			super(new byte[] { 'T', 'L' });
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
			GraphicsOperand operand_1 = this.operandList.get(0);
			PNumber leading = operand_1.asNumber();
			g2d.setTextLeading(leading.doubleValue());
		}
	}

	static class OP_T_f extends GraphicsOperator {
		/**
		 * Set the text font to font and the text font size.
		 * */
		public OP_T_f() {
			super(new byte[] { 'T', 'f' });
			this.operandNumber = 2;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
			GraphicsOperand operand_1 = this.operandList.get(0);
			GraphicsOperand operand_2 = this.operandList.get(1);

			PName fontIndex = operand_1.asName();
			PNumber fontSize = operand_2.asNumber();

			PDFFont font = page.getFont(fontIndex);
			g2d.setFont(font, fontSize.floatValue());
		}
	}

	static class OP_T_r extends GraphicsOperator {

		public OP_T_r() {
			super(new byte[] { 'T', 'r' });
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_T_s extends GraphicsOperator {

		public OP_T_s() {
			super(new byte[] { 'T', 's' });
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}
}
