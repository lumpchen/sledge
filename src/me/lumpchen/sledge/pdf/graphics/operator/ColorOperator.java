package me.lumpchen.sledge.pdf.graphics.operator;

import me.lumpchen.sledge.pdf.graphics.GraphicsOperand;
import me.lumpchen.sledge.pdf.graphics.PDFColor;
import me.lumpchen.sledge.pdf.graphics.RGBColor;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.document.Page;

public class ColorOperator {
	// Color CS, cs, SC, SCN, sc, scn, G, g, RG, rg, K, k
	static class OP_CS_ extends GraphicsOperator {

		public OP_CS_() {
			super(new byte[] { 'C', 'S' });
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_cs extends GraphicsOperator {

		public OP_cs() {
			super(new byte[] { 'c', 's' });
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_SC_ extends GraphicsOperator {

		public OP_SC_() {
			super(new byte[] { 'S', 'C' });
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_SCN_ extends GraphicsOperator {

		public OP_SCN_() {
			super(new byte[] { 'S', 'C', 'N' });
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_sc extends GraphicsOperator {

		public OP_sc() {
			super(new byte[] { 's', 'c' });
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_scn extends GraphicsOperator {

		public OP_scn() {
			super(new byte[] { 's', 'c', 'n' });
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_G_ extends GraphicsOperator {

		public OP_G_() {
			super(new byte[] { 'G' });
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_g extends GraphicsOperator {

		public OP_g() {
			super(new byte[] { 'g' });
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_RG_ extends GraphicsOperator {

		public OP_RG_() {
			super(new byte[] { 'R', 'G' });
			this.operandNumber = 3;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_rg extends GraphicsOperator {
		/**
		 * Same as RG but used for nonstroking operations, color space to
		 * DeviceGray.
		 * */
		public OP_rg() {
			super(new byte[] { 'r', 'g' });
			this.operandNumber = 3;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
			GraphicsOperand operand_1 = this.operandList.get(0);
			GraphicsOperand operand_2 = this.operandList.get(1);
			GraphicsOperand operand_3 = this.operandList.get(2);

			float r = operand_1.asNumber().floatValue();
			float g = operand_2.asNumber().floatValue();
			float b = operand_3.asNumber().floatValue();
			PDFColor color = new RGBColor(r, g, b);
			g2d.setColor(color);
		}
	}

	static class OP_K_ extends GraphicsOperator {

		public OP_K_() {
			super(new byte[] { 'K' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_k extends GraphicsOperator {

		public OP_k() {
			super(new byte[] { 'k' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}
}
