package me.lumpchen.sledge.pdf.graphics.operator;

import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.document.Page;

public class PathPaintingOperator {
	// Path painting S, s, f, F, f*, B, B*, b, b*, n
	static class OP_S_ extends GraphicsOperator {

		public OP_S_() {
			super(new byte[] { 'S' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_s extends GraphicsOperator {

		public OP_s() {
			super(new byte[] { 's' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_f extends GraphicsOperator {

		public OP_f() {
			super(new byte[] { 'f' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_F_ extends GraphicsOperator {

		public OP_F_() {
			super(new byte[] { 'F' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_f42 extends GraphicsOperator {

		public OP_f42() {
			super(new byte[] { 'f', '*' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_B_ extends GraphicsOperator {

		public OP_B_() {
			super(new byte[] { 'B' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_B_42 extends GraphicsOperator {

		public OP_B_42() {
			super(new byte[] { 'B', '*' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_b extends GraphicsOperator {

		public OP_b() {
			super(new byte[] { 'b' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_b42 extends GraphicsOperator {

		public OP_b42() {
			super(new byte[] { 'b', '*' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
		}
	}

	static class OP_n extends GraphicsOperator {
		/**
		 * End the path object without filling or stroking it. This operator is
		 * a path-painting no-op, used primarily for the side effect of changing
		 * the current clipping path
		 * */
		public OP_n() {
			super(new byte[] { 'n' });
			this.operandNumber = 0;
		}

		@Override
		public void execute(VirtualGraphics g2d, Page page) {
			// g2d.closePath();
		}
	}
}
