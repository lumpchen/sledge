package me.lumpchen.sledge.pdf.graphics.operator;

import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.document.Page;

public class GeneralGraphicsStateOperator {
	
	//General graphics state w, J, j, M, d, ri, i, gs
	static class OP_w extends GraphicsOperator {

		public OP_w() {
			super(new byte[] { 'w'});
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}
	
	static class OP_J_ extends GraphicsOperator {

		public OP_J_() {
			super(new byte[] { 'J'});
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_j extends GraphicsOperator {

		public OP_j() {
			super(new byte[] { 'j'});
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_M_ extends GraphicsOperator {

		public OP_M_() {
			super(new byte[] { 'M'});
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_d extends GraphicsOperator {

		public OP_d() {
			super(new byte[] { 'd'});
			this.operandNumber = 2;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_ri extends GraphicsOperator {

		public OP_ri() {
			super(new byte[] { 'r', 'i'});
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_i extends GraphicsOperator {

		public OP_i() {
			super(new byte[] { 'i'});
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_gs extends GraphicsOperator {

		public OP_gs() {
			super(new byte[] { 'g', 's'});
			this.operandNumber = 1;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}
}
