package me.lumpchen.sledge.pdf.graphics.operator;

import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.document.Page;

public class MarkedContentOperator {
	// Marked content MP, DP, BMC, BDC, EMC
	static class OP_MP_ extends GraphicsOperator {

		public OP_MP_() {
			super(new byte[] { 'M', 'P' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_DP_ extends GraphicsOperator {

		public OP_DP_() {
			super(new byte[] { 'D', 'P' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_BMC_ extends GraphicsOperator {

		public OP_BMC_() {
			super(new byte[] { 'B', 'M', 'C' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_BDC_ extends GraphicsOperator {

		public OP_BDC_() {
			super(new byte[] { 'B', 'D', 'C' });
			this.operandNumber = 2;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_EMC_ extends GraphicsOperator {

		public OP_EMC_() {
			super(new byte[] { 'E', 'M', 'C' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

}
