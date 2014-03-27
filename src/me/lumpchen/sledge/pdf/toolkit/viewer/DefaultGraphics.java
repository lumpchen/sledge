package me.lumpchen.sledge.pdf.toolkit.viewer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Stack;

import me.lumpchen.sledge.pdf.graphics.Matrix;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.text.font.PDFFont;
import me.lumpchen.sledge.pdf.text.font.PDFGlyph;

public class DefaultGraphics implements VirtualGraphics {

	static class GraphicsState {
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
			gs.ctm = current.ctm;
			
			gs.color = current.color;
			
			gs.fontSize = current.fontSize;
			return gs;
		}
	}
	
	private Stack<GraphicsState> gsStack = new Stack<GraphicsState>();

	private GraphicsState gstate;
	
	private Graphics2D g2;
	
	private Shape currPath;
	
	private static int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
	
	private double resolution = -1;
	
	private Font defaultFont;
	
	public DefaultGraphics(Graphics2D g2) {
		this.g2 = g2;
		
		this.g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		this.g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	
		this.defaultFont = new Font("Arial", Font.PLAIN, 12);
		
		this.gstate = new GraphicsState();
		this.gstate.ctm = this.g2.getTransform();
	}
	
	public void setResolutoin(double deviceRes) {
		this.resolution = deviceRes;
	}
	
	private double toPixel(double d) {
		if (this.resolution <= 0) {
			this.resolution = screenRes;
		}
		return (d / 72) * resolution;
	}
	
	@Override
	public void saveGraphicsState() {
		if (this.gstate != null) {
			this.gsStack.push(gstate);
		}
		
		this.gstate = GraphicsState.clone(this.gstate);
	}

	@Override
	public void restoreGraphicsState() {
		if (this.gsStack.isEmpty()) {
			throw new RuntimeException("unmatched graphics state.");
		}
		this.gstate = this.gsStack.pop();
	}


	@Override
	public void beginCanvas(double width, double height) {
		Matrix base = new Matrix(1, 0, 0, -1, 0, toPixel(height));
		AffineTransform at = new AffineTransform(base.flate());
		at.preConcatenate(this.gstate.ctm);
		this.gstate.ctm = at;
		this.g2.setTransform(at);
	}

	@Override
	public void beginPath(double x, double y, double width, double height) {
		double ix = this.toPixel(x);
		double iy = this.toPixel(y);
		double iWidth = this.toPixel(width);
		double iHeight = this.toPixel(height);
		this.currPath = new Rectangle2D.Double(ix, iy, iWidth, iHeight);
	}

	@Override
	public void clip() {
		if (null != this.currPath) {
			this.g2.clip(this.currPath);
		}
	}

	@Override
	public void closePath() {
		this.currPath = null;
	}

	@Override
	public void concatenate(Matrix matrix) {
		AffineTransform at = new AffineTransform(this.flateMatrix(matrix));
		at.preConcatenate(this.gstate.ctm);
		this.gstate.ctm = at;
		this.g2.setTransform(at);
		
		Point2D ptSrc = new Point2D.Double(0, 0);
		Point2D ptDst = new Point2D.Double();
		this.g2.getTransform().transform(ptSrc, ptDst);
//		System.out.println(ptDst);
	}

	@Override
	public void setColor(me.lumpchen.sledge.pdf.graphics.PDFColor color) {
		this.gstate.color = color.toJavaColor();
		this.g2.setColor(this.gstate.color);
	}

	@Override
	public void beginText() {
		this.saveGraphicsState();
	}

	@Override
	public void setFont(PDFFont font, float size) {
		this.gstate.font = font;
		this.gstate.fontSize = (int) (this.toPixel(size) + 0.5);
		if (font.notEmbed()) {
			this.gstate.awtFont = font.peerAWTFont();
			if (null == this.gstate.awtFont) {
				this.gstate.awtFont= this.defaultFont;
				this.gstate.awtFont = this.gstate.awtFont.deriveFont(this.gstate.fontSize);
			}			
		}
	}

	@Override
	public void transformTextMatrix(Matrix matrix) {
		AffineTransform at = new AffineTransform(this.flateMatrix(matrix));
		AffineTransform mirror = new AffineTransform(1, 0, 0, -1, 0, 0);
		mirror.preConcatenate(at);
		mirror.preConcatenate(this.gstate.ctm);
		this.gstate.ctm = mirror;
		this.g2.setTransform(mirror);
	}

	@Override
	public void showText(String text) {
		this.saveGraphicsState();
//		this.g2.setColor(Color.blue);
		
		if (this.gstate.font.notEmbed()) {
			this.g2.setFont(this.gstate.awtFont);
			this.g2.drawString(text, 0, 0);	
			return;
		}
		
		AffineTransform at = new AffineTransform(1, 0, 0, -1, 0, 0);
		at.scale(this.gstate.fontSize, this.gstate.fontSize);
		at.preConcatenate(this.gstate.ctm);
		
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			PDFFont font = this.gstate.font;
			
			PDFGlyph glyph = font.getGlyph(c);
			GeneralPath gpath = glyph.getGlyph();
			
			gpath.transform(at);
			
			this.g2.draw(gpath);
		}
		this.restoreGraphicsState();
	}

	@Override
	public void endText() {
		this.restoreGraphicsState();
	}

	@Override
	public void strokePath() {
		this.g2.draw(this.currPath);
	}

	@Override
	public void fillPath() {
		this.g2.fill(this.currPath);
	}
	
	private double[] flateMatrix(Matrix matrix) {
		double[] doubleMatrix = matrix.flate();
		doubleMatrix[4] = this.toPixel(doubleMatrix[4]);
		doubleMatrix[5] = this.toPixel(doubleMatrix[5]);
		return doubleMatrix;
	}
}
