package me.lumpchen.sledge.pdf.graphics;

import me.lumpchen.sledge.pdf.graphics.colorspace.PDFColorspace;
import me.lumpchen.sledge.pdf.syntax.basic.PBoolean;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;

public class GraphicsState {

	// Device-independent graphics state parameters
	public Matrix ctm;
	public ClippingPath clippingPath;
	public PDFColorspace colorspace;
	public Color color;
	public TextState textState;
	public PNumber lineWidth;
	public PNumber lineCap;
	public PNumber lineJoin;
	public PNumber miterLimit;
	public DashPattern dashPattern;
	public PName renderingIntent;
	public PBoolean strokeAdjustment;
	public BlendMode blendMode;
	public SoftMask softMask;
	public PNumber alphaConstant;
	public PBoolean alphaSource;
	
	// Device-dependent graphics state parameters
	public PBoolean overprint;
	public PNumber overprintMode;
	public BlackGeneration blackGeneration;
	public UndercolorRemoval undercolorRemoval;
	public Transfer transfer;
	public Halftone halftone;
	public PNumber flatness;
	public PNumber smoothness;
	
	class TextState {
		double charSpace;
		double wordSpace;
		double scale;
		double leading;
		double fontSize;
		double render;
		double rise;
	}
}
