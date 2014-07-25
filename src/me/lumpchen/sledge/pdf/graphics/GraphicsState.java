package me.lumpchen.sledge.pdf.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import me.lumpchen.sledge.pdf.syntax.lang.PBoolean;
import me.lumpchen.sledge.pdf.syntax.lang.PNumber;
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

//		this.textState = new TextState(current.textState);
	}
	
	public class TextState2 {
		public double charSpace;
		public double wordSpace;
		public double scale;
		public double leading;
		public double fontSize;
		public double render;
		public double rise;
		public double adjustment;

		public TextState2() {
		}

		public TextState2(TextState2 state) {
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
	
	public class TextState {
		private double charSpace;
		private double wordSpace;
		private double scale;
		private double leading;
		private double fontSize;
		private double renderMode;
		private double rise;
		private double adjustment;

		private AffineTransform lineMatrix;
		
		public TextState() {
			this.reset();
		}
		
		public void reset() {
			this.charSpace = 0;
			this.wordSpace = 0;
			this.scale = 1.0;
			this.leading = 0;
			this.fontSize = 1.0;
			this.renderMode = 0;
			this.rise = 0;
			this.adjustment = 0;
			
			if (this.lineMatrix != null) {
				this.lineMatrix.setToIdentity();				
			}
		}
		
		public void setTextMatrix(Matrix matrix) {
			this.lineMatrix = new AffineTransform(matrix.flate());
		}
		
		public void setCharSpace(double charSpace) {
			this.charSpace = charSpace;
		}
		
		public void setWordSpace(double wordSpace) {
			this.wordSpace = wordSpace;
		}
		
		public void Tr(double scale) {
			this.scale = scale;
		}
		
		public void setLeading(double leading) {
			this.leading = leading;
		}
		
		public void setFontSize(double size) {
			this.fontSize = size;
		}
		
		public double getFontSize() {
			return this.fontSize;
		}
		
		public void moveToNextLine() {
			this.moveTo(0, -this.leading);
		}
		
		public void moveTo(double tx, double ty) {
			this.lineMatrix.concatenate(AffineTransform.getTranslateInstance(tx, ty));
		}
		
		public void advance(double advance, char c) {
			if (c == ' ') {
				advance += this.wordSpace;
			}
			advance += this.charSpace;
			this.moveTo(advance, 0);
		}
	}
}
