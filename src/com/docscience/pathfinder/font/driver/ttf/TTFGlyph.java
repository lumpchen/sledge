package com.docscience.pathfinder.font.driver.ttf;

import java.util.HashMap;

import com.docscience.pathfinder.font.shared.GlyphPath;
import com.docscience.pathfinder.font.shared.GlyphPathIterator;

/**
 * Represent TrueType glyph outline.
 * 
 * @author wxin
 * 
 */
public final class TTFGlyph {

	/**
	 * If this is set, the arguments are words; otherwise, they are bytes.
	 */
	private static final int CFLAG_ARG_1_AND_2_ARE_WORDS = 1 << 0;

	/**
	 * If this is set, the arguments are xy values; otherwise, they are points.
	 */
	private static final int CFLAG_ARGS_ARE_XY_VALUES = 1 << 1;

	/**
	 * For the xy values if the preceding is true.
	 */
	@SuppressWarnings("unused")
	private static final int CFLAG_ROUND_XY_TO_GRID = 1 << 2;

	/**
	 * This indicates that there is a simple scale for the component. Otherwise,
	 * scale = 1.0.
	 */
	private static final int CFLAG_WE_HAVE_A_SCALE = 1 << 3;

	/**
	 * This bit is reserved. Set it to 0.
	 */
	@SuppressWarnings("unused")
	private static final int CFLAG_RESERVED = 1 << 4;

	/**
	 * Indicates at least one more glyph after this one.
	 */
	private static final int CFLAG_MORE_COMPONENTS = 1 << 5;

	/**
	 * The x direction will use a different scale from the y direction.
	 */
	private static final int CFLAG_WE_HAVE_AN_X_AND_Y_SCALE = 1 << 6;

	/**
	 * There is a 2 by 2 transformation that will be used to scale the
	 * component.
	 */
	private static final int CFLAG_WE_HAVE_A_TWO_BY_TWO = 1 << 7;

	/**
	 * Following the last component are instructions for the composite
	 * character.
	 */
	@SuppressWarnings("unused")
	private static final int CFLAG_WE_HAVE_INSTRUCTIONS = 1 << 8;

	/**
	 * If set, this forces the aw and lsb (and rsb) for the composite to be
	 * equal to those from this original glyph. This works for hinted and
	 * unhinted characters.
	 */
	@SuppressWarnings("unused")
	private static final int CFLAG_USE_MY_METRICS = 1 << 9;

	/**
	 * Used by Apple in GX fonts.
	 */
	@SuppressWarnings("unused")
	private static final int CFLAG_OVERLAP_COMPOUND = 1 << 10;

	/**
	 * Composite designed to have the component offset scaled (designed for
	 * Apple rasterizer).
	 */
	@SuppressWarnings("unused")
	private static final int CFLAG_SCALED_COMPONENT_OFFSET = 1 << 11;

	/**
	 * Composite designed not to have the component offset scaled (designed for
	 * the Microsoft TrueType rasterizer).
	 */
	@SuppressWarnings("unused")
	private static final int CFLAG_UNSCALED_COMPONENT_OFFSET = 1 << 12;

	/**
	 * If set, the point is on the curve; otherwise, it is off the curve.
	 */
	private static final int GFLAG_ON_CURVE = 1 << 0;

	/**
	 * If set, the corresponding x-coordinate is 1 byte long. If not set, 2
	 * bytes.
	 */
	private static final int GFLAG_X_SHORT_VECTOR = 1 << 1;

	/**
	 * If set, the corresponding y-coordinate is 1 byte long. If not set, 2
	 * bytes.
	 */
	private static final int GFLAG_Y_SHORT_VECTOR = 1 << 2;

	/**
	 * If set, the next byte specifies the number of additional times this set
	 * of flags is to be repeated. In this way, the number of flags listed can
	 * be smaller than the number of points in a character.
	 */
	private static final int GFLAG_REPEAT = 1 << 3;

	/**
	 * This flag has two meanings, depending on how the x-Short Vector flag is
	 * set. If x-Short Vector is set, this bit describes the sign of the value,
	 * with 1 equalling positive and 0 negative. If the x-Short Vector bit is
	 * not set and this bit is set, then the current x-coordinate is the same as
	 * the previous x-coordinate. If the x-Short Vector bit is not set and this
	 * bit is also not set, the current x-coordinate is a signed 16-bit delta
	 * vector.
	 */
	private static final int GFLAG_THIS_X_IS_SAME = 1 << 4;

	/**
	 * This flag has two meanings, depending on how the y-Short Vector flag is
	 * set. If y-Short Vector is set, this bit describes the sign of the value,
	 * with 1 equalling positive and 0 negative. If the y-Short Vector bit is
	 * not set and this bit is set, then the current y-coordinate is the same as
	 * the previous y-coordinate. If the y-Short Vector bit is not set and this
	 * bit is also not set, the current y-coordinate is a signed 16-bit delta
	 * vector.
	 */
	private static final int GFLAG_THIS_Y_IS_SAME = 1 << 5;

	private short numContours;

	private short xMin;

	private short yMin;

	private short xMax;

	private short yMax;

	private int[] endPointOfContours;

	private byte[] instructions;

	private Point[] points;

	private SubGlyphArg[] subGlyphs;

	public static class Point implements Cloneable {
		boolean onCurve;
		short x;
		short y;

		public boolean onCurve() {
			return onCurve;
		}

		public short getX() {
			return x;
		}

		public short getY() {
			return y;
		}

		@Override
		public Object clone() {
			Object obj = null;
			try {
				obj = super.clone();
			} catch (CloneNotSupportedException e) {
				assert (false) : "never goes here";
			}
			return obj;
		}
	}

	public static class SubGlyphArg {
		private int arg1;
		private int arg2;
		private float xscale = 1;
		private float yscale = 1;
		private float scale0 = 0;
		private float scale1 = 0;
		private int cflags = 0;
		private int glyphID;

		public int getArg1() {
			return arg1;
		}

		public int getArg2() {
			return arg2;
		}

		public float getXScale() {
			return xscale;
		}

		public float getYScale() {
			return yscale;
		}

		public float getScale0() {
			return scale0;
		}

		public float getScale1() {
			return scale1;
		}

		public int getGlyphID() {
			return glyphID;
		}

		public int getCFlags() {
			return cflags;
		}

		public boolean argsAreXYValues() {
			return ((cflags & CFLAG_ARGS_ARE_XY_VALUES) != 0);
		}
	}

	public TTFGlyph(byte[] data) throws TTFGlyphException {
		parse(data);
	}

	public static boolean isCompositedGlyphBytes(byte[] data) {
		if (data.length < 2) {
			return false;
		}
		return (short) (((data[0] & 0xff) << 8) | (data[1] & 0xff)) == -1;
	}

	public boolean isComposited() {
		return numContours == -1;
	}

	public int getNumSubGlyphs() {
		if (!isComposited()) {
			throw new IllegalStateException("glyph is not composited");
		}
		return subGlyphs.length;
	}

	public int getSubGlyphID(int n) {
		if (!isComposited()) {
			throw new IllegalStateException("glyph is not composited");
		}
		return subGlyphs[n].glyphID;
	}

	public SubGlyphArg getSubGlyphArg(int n) {
		if (!isComposited()) {
			throw new IllegalStateException("glyph is not composited");
		}
		return subGlyphs[n];
	}

	public final short getXMax() {
		return xMax;
	}

	public final short getXMin() {
		return xMin;
	}

	public final short getYMax() {
		return yMax;
	}

	public final short getYMin() {
		return yMin;
	}

	public short getNumContours() {
		if (isComposited()) {
			throw new IllegalStateException("glyph is composited");
		}
		return numContours;
	}

	public final int getStartPointOfContour(int index) {
		if (isComposited()) {
			throw new IllegalStateException("glyph is composited");
		}

		if (index == 0) {
			return 0;
		} else {
			return endPointOfContours[index - 1] + 1;
		}
	}

	public final int getEndPointOfContour(int index) {
		if (isComposited()) {
			throw new IllegalStateException("glyph is composited");
		}
		return endPointOfContours[index];
	}

	public final int getNumPointsOfContour(int index) {
		if (isComposited()) {
			throw new IllegalStateException("glyph is composited");
		}

		if (index == 0) {
			return endPointOfContours[index] + 1;
		} else {
			return endPointOfContours[index] - endPointOfContours[index - 1];
		}
	}

	public final int getNumPoints() {
		if (isComposited()) {
			throw new IllegalStateException("glyph is composited");
		}
		return points.length;
	}

	public final Point getPoint(int index) {
		if (isComposited()) {
			throw new IllegalStateException("glyph is composited");
		}
		return points[index];
	}

	public final Point[] getPoints(Point[] array) {
		if (isComposited()) {
			throw new IllegalStateException("glyph is composited");
		}

		if (array == null) {
			array = new Point[getNumPoints()];
		}
		System.arraycopy(points, 0, array, 0, getNumPoints());
		return array;
	}

	public final Point[] getPointsOfContour(int index, Point[] array) {
		if (isComposited()) {
			throw new IllegalStateException("glyph is composited");
		}

		if (array == null) {
			array = new Point[getNumPointsOfContour(index)];
		}
		System.arraycopy(points, getStartPointOfContour(index), array, 0,
				getNumPointsOfContour(index));
		return array;
	}

	public final byte[] getInstructions() {
		if (isComposited()) {
			throw new IllegalStateException("glyph is composited");
		}

		return instructions;
	}

	private static void quad2cubic(float[] quads, float[] cubics) {
		cubics[0] = quads[0];
		cubics[1] = quads[1];

		cubics[6] = quads[4];
		cubics[7] = quads[5];

		cubics[2] = quads[0] + (quads[2] - quads[0]) * 2 / 3;
		cubics[3] = quads[1] + (quads[3] - quads[1]) * 2 / 3;

		cubics[4] = quads[2] + (quads[4] - quads[2]) / 3;
		cubics[5] = quads[3] + (quads[5] - quads[3]) / 3;
	}

	private static float f2dot14(byte[] data, int start) {
		int n = (data[start] << 8) | (data[start + 1] & 0xff);
		return (n >> 14) + (n & ((1 << 14) - 1)) / (float) (1 << 14);
	}

	private void parse(byte[] data) throws TTFGlyphException {
		if (data == null || data.length == 0) {
			numContours = 0;
			return;
		}

		numContours = (short) (((data[0] & 0xff) << 8) | (data[1] & 0xff));
		xMin = (short) (((data[2] & 0xff) << 8) | (data[3] & 0xff));
		yMin = (short) (((data[4] & 0xff) << 8) | (data[5] & 0xff));
		xMax = (short) (((data[6] & 0xff) << 8) | (data[7] & 0xff));
		yMax = (short) (((data[8] & 0xff) << 8) | (data[9] & 0xff));
		int ip = 10;
		if (isComposited()) {
			parseCompositedGlyph(data, ip);
		} else {
			parseSingleGlyph(data, ip);
		}
	}

	private void parseCompositedGlyph(byte[] data, int ip)
			throws TTFGlyphException {
		int ng = 0;
		subGlyphs = new SubGlyphArg[10];
		int cflags;
		int glyphID;
		do {
			SubGlyphArg glyph = new SubGlyphArg();

			// read flags and glyph
			if (ip + 4 > data.length) {
				throw new TTFGlyphException("incomplete data");
			}
			cflags = (((data[ip + 0] & 0xff) << 8) | (data[ip + 1] & 0xff));
			glyphID = (((data[ip + 2] & 0xff) << 8) | (data[ip + 3] & 0xff));
			ip += 4;

			glyph.cflags = cflags;
			glyph.glyphID = glyphID;

			// read argument 1 and 2
			if ((cflags & CFLAG_ARG_1_AND_2_ARE_WORDS) != 0) {
				glyph.arg1 = ((data[ip + 0] << 8) | (data[ip + 1] & 0xff));
				glyph.arg2 = ((data[ip + 2] << 8) | (data[ip + 3] & 0xff));
				ip += 4;
			} else {
				glyph.arg1 = data[ip] & 0xff;
				glyph.arg2 = data[ip + 1] & 0xff;
				ip += 2;
			}

			// read variable
			if ((cflags & CFLAG_WE_HAVE_A_SCALE) != 0) {
				glyph.xscale = f2dot14(data, ip);
				glyph.yscale = glyph.xscale;
				ip += 2;
			} else if ((cflags & CFLAG_WE_HAVE_AN_X_AND_Y_SCALE) != 0) {
				glyph.xscale = f2dot14(data, ip);
				glyph.yscale = f2dot14(data, ip + 2);
				ip += 4;
			} else if ((cflags & CFLAG_WE_HAVE_A_TWO_BY_TWO) != 0) {
				glyph.xscale = f2dot14(data, ip);
				glyph.scale0 = f2dot14(data, ip + 2);
				glyph.scale1 = f2dot14(data, ip + 4);
				glyph.yscale = f2dot14(data, ip + 6);
				ip += 8;
			}

			if (ng >= subGlyphs.length) {
				SubGlyphArg[] temp = new SubGlyphArg[subGlyphs.length * 2];
				System.arraycopy(subGlyphs, 0, temp, 0, subGlyphs.length);
				subGlyphs = temp;
			}
			subGlyphs[ng++] = glyph;

			// check again
			if (ip > data.length) {
				throw new TTFGlyphException("incomplete data");
			}
		} while ((cflags & CFLAG_MORE_COMPONENTS) != 0);

		if (ng < subGlyphs.length) {
			SubGlyphArg[] temp = new SubGlyphArg[ng];
			System.arraycopy(subGlyphs, 0, temp, 0, ng);
			subGlyphs = temp;
		}
		return;
	}

	private void parseSingleGlyph(byte[] data, int ip) {
		endPointOfContours = new int[numContours];
		for (int i = 0; i < numContours; ++i) {
			endPointOfContours[i] = ((data[ip] & 0xff) << 8)
					| (data[ip + 1] & 0xff);
			ip += 2;
		}

		int numInstructions = ((data[ip] & 0xff) << 8) | (data[ip + 1] & 0xff);
		ip += 2;
		instructions = new byte[numInstructions];
		System.arraycopy(data, ip, instructions, 0, numInstructions);
		ip += numInstructions;

		int numPoints = endPointOfContours[numContours - 1] + 1;

		// read point flags
		byte[] flags = new byte[numPoints];
		for (int i = 0; i < numPoints;) {
			byte flag = data[ip];
			ip++;
			if ((flag & GFLAG_REPEAT) != 0) {
				flag &= ~GFLAG_REPEAT;
				int times = (data[ip] & 0xff) + 1;
				ip++;
				for (int j = 0; j < times; ++j) {
					flags[i++] = flag;
				}
			} else {
				flags[i++] = flag;
			}
		}

		// init points array
		points = new Point[numPoints];
		for (int i = 0; i < numPoints; ++i) {
			points[i] = new Point();
			points[i].onCurve = ((flags[i] & GFLAG_ON_CURVE) != 0);
		}

		// read point x value
		short lastX = 0;
		for (int i = 0; i < numPoints; ++i) {
			short value;
			if ((flags[i] & GFLAG_X_SHORT_VECTOR) != 0) {
				value = (short) (data[ip] & 0xff);
				ip++;

				if ((flags[i] & GFLAG_THIS_X_IS_SAME) == 0) {
					value = (short) -value;
				}

				value += lastX;
			} else {
				if ((flags[i] & GFLAG_THIS_X_IS_SAME) != 0) {
					value = lastX;
				} else {
					value = (short) (((data[ip] & 0xff) << 8) | (data[ip + 1] & 0xff));
					ip += 2;

					value += lastX;
				}
			}
			points[i].x = value;
			lastX = value;
		}

		// read point y value
		short lastY = 0;
		for (int i = 0; i < numPoints; ++i) {
			short value;
			if ((flags[i] & GFLAG_Y_SHORT_VECTOR) != 0) {
				value = (short) (data[ip] & 0xff);
				ip++;

				if ((flags[i] & GFLAG_THIS_Y_IS_SAME) == 0) {
					value = (short) -value;
				}

				value += lastY;
			} else {
				if ((flags[i] & GFLAG_THIS_Y_IS_SAME) != 0) {
					value = lastY;
				} else {
					value = (short) (((data[ip] & 0xff) << 8) | (data[ip + 1] & 0xff));
					ip += 2;

					value += lastY;
				}
			}
			points[i].y = value;
			lastY = value;
		}

		return;
	}

	public GlyphPath getGlyphPath(HashMap<Integer, TTFGlyph> dependentGlyphs,
			boolean convertQuad2Cubic) {
		if (isComposited()) {
			GlyphPath path = new GlyphPath();

			for (int i = 0; i < getNumSubGlyphs(); i++) {
				SubGlyphArg subArg = getSubGlyphArg(i);
				TTFGlyph subGlyph = dependentGlyphs.get(subArg.getGlyphID());

				double xoffset, yoffset;
				if (subArg.argsAreXYValues()) {
					xoffset = subArg.getArg1();
					yoffset = subArg.getArg2();
				} else {
					Point p1 = getPoint(subArg.getArg1());
					Point p2 = subGlyph.getPoint(subArg.getArg2());
					xoffset = p1.x - p2.x;
					yoffset = p1.y - p2.y;
				}

				GlyphPath subGlyphPath = subGlyph.getGlyphPath(dependentGlyphs,	convertQuad2Cubic);
				double m00 = subArg.getXScale();
				double m10 = subArg.getScale0();
				double m01 = subArg.getScale1();
				double m11 = subArg.getYScale();
				double m02 = xoffset;
				double m12 = yoffset;
				GlyphPathIterator iter = subGlyphPath.getGlyphPathIterator(m00, m10, m01, m11, m02, m12);
				path.append(iter);
			}
			return path;
		} else {
			return getGlyphPath(convertQuad2Cubic);
		}
	}

	public GlyphPath getGlyphPath(boolean convertQuad2Cubic) {
		if (isComposited()) {
			throw new IllegalStateException("glyph is composited");
		}

		GlyphPath path = new GlyphPath();

		for (int c = 0; c < getNumContours(); ++c) {
			Point[] ptrs = new Point[getNumPointsOfContour(c) + 1];
			getPointsOfContour(c, ptrs);

			ptrs[ptrs.length - 1] = ptrs[0]; // add the first point to end of
												// contour to finish it.

			float x0 = ptrs[0].getX();
			float y0 = ptrs[0].getY();
			float x1 = 0;
			float y1 = 0;

			path.moveTo(x0, y0);

			boolean onCurve = true;

			for (int i = 1; i < ptrs.length; ++i) {
				Point p = ptrs[i];
				if (onCurve) {
					if (p.onCurve()) {
						x0 = p.getX();
						y0 = p.getY();
						path.lineTo(x0, y0);
					} else {
						x1 = p.getX();
						y1 = p.getY();
					}
				} else {
					if (p.onCurve()) {
						float x2 = p.getX();
						float y2 = p.getY();
						if (!convertQuad2Cubic) {
							path.quadTo(x1, y1, x2, y2);
						} else {
							float[] quads = { x0, y0, x1, y1, x2, y2 };
							float[] cubics = new float[8];
							quad2cubic(quads, cubics);
							path.curveTo(cubics[2], cubics[3], cubics[4],
									cubics[5], cubics[6], cubics[7]);
						}
						x0 = x2;
						y0 = y2;
					} else {
						float x2 = (x1 + p.getX()) / 2;
						float y2 = (y1 + p.getY()) / 2;
						if (!convertQuad2Cubic) {
							path.quadTo(x1, y1, x2, y2);
						} else {
							float[] quads = { x0, y0, x1, y1, x2, y2 };
							float[] cubics = new float[8];
							quad2cubic(quads, cubics);
							path.curveTo(cubics[2], cubics[3], cubics[4],
									cubics[5], cubics[6], cubics[7]);
						}
						x0 = x2;
						y0 = y2;
						x1 = p.getX();
						y1 = p.getY();
					}
				}
				onCurve = p.onCurve();
			}

			path.closePath();
		}

		return path;
	}

}
