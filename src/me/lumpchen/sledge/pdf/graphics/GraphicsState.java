package me.lumpchen.sledge.pdf.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import me.lumpchen.sledge.pdf.syntax.basic.PBoolean;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.text.font.PDFFont;

public class GraphicsState {

	// Device-independent graphics state parameters
	// public Matrix ctm;
	// public ClippingPath clippingPath;
	// public PDFColorSpace colorspace;
	// public PDFColor color;
	// public TextState textState;
	// public PNumber lineWidth;
	// public PNumber lineCap;
	// public PNumber lineJoin;
	// public PNumber miterLimit;
	// public DashPattern dashPattern;
	// public PName renderingIntent;
	// public PBoolean strokeAdjustment;
	// public BlendMode blendMode;
	// public SoftMask softMask;
	// public PNumber alphaConstant;
	// public PBoolean alphaSource;

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

	public AffineTransform ctm;
	public GeneralPath path;
	public ColorSpace colorspace;
	public Color color;

	public PDFFont font;
	public Font awtFont;
	public float charSpace;
	public float wordSpace;
	public float scale;
	public float leading;
	public float fontSize;
	public float render;
	public float rise;

	public float lineWidth;
	public int lineCap;
	public int lineJoin;
	public float miterLimit;
	public int[] dashArray;
	public int dashPhase;

	// ...

	public static GraphicsState clone(GraphicsState current) {
		GraphicsState gs = new GraphicsState();
		
		if (current.ctm != null) {
			gs.ctm = new AffineTransform(current.ctm);			
		}
//		gs.font = current.font;
		gs.color = current.color;

		gs.fontSize = current.fontSize;
		return gs;
	}
}
