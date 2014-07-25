package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.syntax.lang.PArray;
import me.lumpchen.sledge.pdf.syntax.lang.PDictionary;
import me.lumpchen.sledge.pdf.syntax.lang.PName;

public class Resource {

	private PDictionary extGState;
	private PDictionary colorSpace;
	private PDictionary pattern;
	private PDictionary shading;
	private PDictionary xObject;
	private PDictionary font;
	private PArray procSet;
	private PDictionary properties;

	private PDictionary dict;

	public Resource(PDictionary dict) {
		this.dict = dict;
	}

	public PDictionary getExtGState() {
		return extGState;
	}

	public void setExtGState(PDictionary extGState) {
		this.extGState = extGState;
	}

	public PDictionary getColorSpace() {
		return colorSpace;
	}

	public void setColorSpace(PDictionary colorSpace) {
		this.colorSpace = colorSpace;
	}

	public PDictionary getPattern() {
		return pattern;
	}

	public void setPattern(PDictionary pattern) {
		this.pattern = pattern;
	}

	public PDictionary getShading() {
		return shading;
	}

	public void setShading(PDictionary shading) {
		this.shading = shading;
	}

	public PDictionary getxObject() {
		return xObject;
	}

	public void setxObject(PDictionary xObject) {
		this.xObject = xObject;
	}

	public PDictionary getFont() {
		if (null == this.font) {
			this.font = this.dict.getValueAsDict(PName.Font);			
		}
		return this.font;
	}

	public void setFont(PDictionary font) {
		this.font = font;
	}

	public PArray getProcSet() {
		return procSet;
	}

	public void setProcSet(PArray procSet) {
		this.procSet = procSet;
	}

	public PDictionary getProperties() {
		return properties;
	}

	public void setProperties(PDictionary properties) {
		this.properties = properties;
	}

}
