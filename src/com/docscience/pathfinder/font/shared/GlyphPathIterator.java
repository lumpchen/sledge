package com.docscience.pathfinder.font.shared;

public interface GlyphPathIterator {

	public static final int SEG_MOVETO = 0;
	public static final int SEG_LINETO = 1;
	public static final int SEG_QUADTO = 2;
	public static final int SEG_CURVETO = 3;
	public static final int SEG_CLOSE = 4;
	
	boolean isDone();
	
	void next();
	
	int currentSegment(double[] coords);
	
}
