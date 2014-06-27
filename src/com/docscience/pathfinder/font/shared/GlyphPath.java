package com.docscience.pathfinder.font.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GlyphPath implements Serializable {

	private class Iterator implements GlyphPathIterator {

		private double m00, m10, m01, m11, m02, m12;
		private int index;

		public Iterator(double m00, double m10, double m01, double m11,
				double m02, double m12) {
			this.m00 = m00;
			this.m10 = m10;
			this.m01 = m01;
			this.m11 = m11;
			this.m02 = m02;
			this.m12 = m12;
		}

		private void getCoords(int index, double[] result) {
			int i = index * 2;
			double x = coords[i];
			double y = coords[i + 1];
			result[0] = x * m00 + y * m01 + m02;
			result[1] = x * m10 + y * m11 + m12;
		}

		@Override
		public boolean isDone() {
			return index >= size;
		}

		@Override
		public void next() {
			switch (types[index]) {
			case GlyphPathIterator.SEG_MOVETO:
			case GlyphPathIterator.SEG_LINETO:
			case GlyphPathIterator.SEG_CLOSE:
				index++;
				break;
			case GlyphPathIterator.SEG_QUADTO:
				index += 2;
				break;
			case GlyphPathIterator.SEG_CURVETO:
				index += 3;
				break;
			}
		}

		@Override
		public int currentSegment(double[] coords) {
			double[] temp = new double[2];

			byte type = types[index];
			switch (type) {
			case GlyphPathIterator.SEG_MOVETO:
			case GlyphPathIterator.SEG_LINETO:
			case GlyphPathIterator.SEG_CLOSE:
				getCoords(index, temp);
				coords[0] = temp[0];
				coords[1] = temp[1];
				break;
			case GlyphPathIterator.SEG_QUADTO:
				getCoords(index, temp);
				coords[0] = temp[0];
				coords[1] = temp[1];
				getCoords(index + 1, temp);
				coords[2] = temp[0];
				coords[3] = temp[1];
				break;
			case GlyphPathIterator.SEG_CURVETO:
				getCoords(index, temp);
				coords[0] = temp[0];
				coords[1] = temp[1];
				getCoords(index + 1, temp);
				coords[2] = temp[0];
				coords[3] = temp[1];
				getCoords(index + 2, temp);
				coords[4] = temp[0];
				coords[5] = temp[1];
				break;
			}
			return type;
		}

	}

	private static final int INIT_SIZE = 4;

	private double[] coords = new double[INIT_SIZE * 2];
	private byte[] types = new byte[INIT_SIZE];
	private int size;

	private void append(int type, double x, double y) {
		if (size >= types.length) {
			types = ArrayUtil.copyOf(types, types.length * 2);
			coords = ArrayUtil.copyOf(coords, coords.length * 2);
		}
		types[size] = (byte) type;
		coords[size * 2] = x;
		coords[size * 2 + 1] = y;
		size++;
	}

	public void moveTo(double x, double y) {
		append(GlyphPathIterator.SEG_MOVETO, x, y);
	}

	public void lineTo(double x, double y) {
		append(GlyphPathIterator.SEG_LINETO, x, y);
	}

	public void quadTo(double x1, double y1, double x2, double y2) {
		append(GlyphPathIterator.SEG_QUADTO, x1, y1);
		append(GlyphPathIterator.SEG_QUADTO, x2, y2);
	}

	public void curveTo(double x1, double y1, double x2, double y2, double x3,
			double y3) {
		append(GlyphPathIterator.SEG_CURVETO, x1, y1);
		append(GlyphPathIterator.SEG_CURVETO, x2, y2);
		append(GlyphPathIterator.SEG_CURVETO, x3, y3);
	}

	public void closePath() {
		boolean found = false;
		double x = 0;
		double y = 0;
		for (int i = size - 1; i >= 0; i--) {
			if (types[i] == GlyphPathIterator.SEG_MOVETO) {
				x = coords[i * 2];
				y = coords[i * 2 + 1];
				found = true;
			}
		}
		if (!found) {
			throw new IllegalStateException();
		}
		append(GlyphPathIterator.SEG_CLOSE, x, y);
	}

	public void append(GlyphPathIterator iter) {
		double[] coords = new double[6];
		for (; !iter.isDone(); iter.next()) {
			int type = iter.currentSegment(coords);
			switch (type) {
			case GlyphPathIterator.SEG_MOVETO:
				moveTo(coords[0], coords[1]);
				break;
			case GlyphPathIterator.SEG_LINETO:
				lineTo(coords[0], coords[1]);
				break;
			case GlyphPathIterator.SEG_QUADTO:
				quadTo(coords[0], coords[1], coords[2], coords[3]);
				break;
			case GlyphPathIterator.SEG_CURVETO:
				curveTo(coords[0], coords[1], coords[2], coords[3], coords[4],
						coords[5]);
				break;
			case GlyphPathIterator.SEG_CLOSE:
				closePath();
				break;
			}
		}
	}

	public GlyphPathIterator getGlyphPathIterator(double m00, double m10,
			double m01, double m11, double m02, double m12) {
		return new Iterator(m00, m10, m01, m11, m02, m12);
	}
	
	public GlyphPathIterator getGlyphPathIterator(double[] m) {
		return new Iterator(m[0], m[1], m[2], m[3], m[4], m[5]);
	}

	public boolean isEmpty() {
		return size == 0;
	}

}
