package com.docscience.pathfinder.font.driver.type1;

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
public class Type1FontDictionary extends PSFontDictionary {
	
	public static final int PAINTTYPE_FILL   = 0;
	public static final int PAINTTYPE_STROKE = 2;
	
	public Type1FontDictionary() {
		setFontType(1);
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
	
	public void setCharStrings(PSDictionary value) {
		psDict.put(PSName.CharStrings, value);
	}
	
	public void setPrivate(Type1PrivateDictionary value) {
		psDict.put(PSName.Private, value.getPSObject());
	}
	
	public void setWeightVector(double[] values) {
		psDict.put(PSName.WeightVector, PSArray.convert(values));
	}
	
	public void addCharString(String name, byte[] charString) {
		PSDictionary dict = (PSDictionary) psDict.get(PSName.CharStrings);
		if (dict == null) {
			dict = new PSDictionary();
			psDict.put(PSName.CharStrings, dict);
		}
		dict.put(new PSName(name), new PSString(charString));
	}
	
}
