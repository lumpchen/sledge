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

	public class TextState {
		public double charSpace;
		public double wordSpace;
		public double scale;
		public double leading;
		public double fontSize;
		public double render;
		public double rise;
		public double adjustment;

		public TextState() {
		}

		public TextState(TextState state) {
			this.charSpace = state.charSpace;
			this.wordSpace = state.wordSpace;
			this.scale = state.scale;
			this.leading = state.leading;
			this.fontSize = state.fontSize;
			this.render = state.render;
			this.rise = state.rise;
			this.adjustment = state.adjustment;
		}
	}
	public TextState textState;
	public double baseFontSize;
	
	public PDFFont font;
	public Font awtFont;
	
	public AffineTransform ctm;
	public GeneralPath path;
	public ColorSpace colorspace;
	public Color color;

	public double lineWidth;
	public int lineCap;
	public int lineJoin;
	public double miterLimit;
	public int[] dashArray;
	public int dashPhase;

	// ...

	public GraphicsState() {
		this.textState = new TextState();
	}

	public GraphicsState(GraphicsState current) {
		if (current.ctm != null) {
			this.ctm = new AffineTransform(current.ctm);
		}
		this.font = current.font;
		this.color = current.color;

		this.textState = new TextState(current.textState);
	}
}
