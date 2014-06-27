package com.docscience.pathfinder.font.driver.type42;

import com.docscience.pathfinder.font.driver.ps.PSArray;
import com.docscience.pathfinder.font.driver.ps.PSDictionary;
import com.docscience.pathfinder.font.driver.ps.PSFontDictionary;
import com.docscience.pathfinder.font.driver.ps.PSInteger;
import com.docscience.pathfinder.font.driver.ps.PSName;
import com.docscience.pathfinder.font.driver.ps.PSReal;
import com.docscience.pathfinder.font.driver.ps.PSString;

/**
 * @author wxin
 *
 */
public class Type42FontDictionary extends PSFontDictionary {

	public static final int PAINTTYPE_FILL   = 0;
	public static final int PAINTTYPE_STROKE = 2;
	
	public Type42FontDictionary() {
		setFontType(42);
	}
	
	public void setPaintType(int paintType) {
		assert(paintType == PAINTTYPE_FILL || paintType == PAINTTYPE_STROKE);
		psDict.put(PSName.PaintType, PSInteger.valueOf(paintType));
	}

	public void setStrokeWidth(double width) {
		psDict.put(PSName.StrokeWidth, new PSReal(width));
	}
	
	public void setMetrics(PSDictionary dict) {
		psDict.put(PSName.Metrics, dict);
	}
	
	public void setMetrics2(PSDictionary dict) {
		psDict.put(PSName.Metrics2, dict);
	}
	
	public void setCDevProc(PSArray array) {
		if (array.isExecutable()) {
			throw new IllegalArgumentException("array must be executable");
		}
		psDict.put(PSName.CDevProc, array);
	}
	
	public void setCharStrings(Type42CharStringsDictionary value) {
		psDict.put(PSName.CharStrings, value.getPSObject());
	}
	
	public void setSfnts(byte[][] sfnts) {
		PSArray array = new PSArray();
		for (int i=0; i<sfnts.length; ++i) {
			byte[] bytes = sfnts[i];
			array.add(new PSString(bytes, 0, bytes.length));
		}
		psDict.put(PSName.sfnts, array);
	}
	
	public void setGlyphDirectory(Type42GlyphDirectory gdir) {
		psDict.put(PSName.GlyphDirectory, gdir.getPSObject());
	}
	
	public void setMetricsCount(int count) {
		if (count != 0 && count != 2 && count != 4) {
			throw new IllegalArgumentException("metrics count must be either 0, 2 or 4");
		}
		psDict.put(PSName.MetricsCount, PSInteger.valueOf(count));
	}
	
}
