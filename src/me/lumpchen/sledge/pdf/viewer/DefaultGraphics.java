package me.lumpchen.sledge.pdf.viewer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
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

public class DefaultGraphics implements VirtualGraphics {

	static class GraphicsState {
		public AffineTransform ctm;
		public GeneralPath path;
		public ColorSpace colorspace;
		public Color color;
		
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
	
	private Rectangle2D.Double currRect;
	
	private static int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
	
	private double resolution = -1;
	
	public DefaultGraphics(Graphics2D g2) {
		this.g2 = g2;
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
		this.currRect = new Rectangle2D.Double(ix, iy, iWidth, iHeight);
	}

	@Override
	public void clip() {
		if (null != this.currRect) {
			this.g2.clip(this.currRect);
		}
	}

	@Override
	public void closePath() {
		this.currRect = null;
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
		System.out.println(ptDst);
	}

	@Override
	public void setColor(me.lumpchen.sledge.pdf.graphics.Color color) {
		this.gstate.color = color.toJavaColor();
		this.g2.setColor(this.gstate.color);
	}

	@Override
	public void beginText() {
		this.saveGraphicsState();
	}

	@Override
	public void setFont(PDFFont font, float size) {
		this.gstate.fontSize = size;
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
		float fontSize = this.gstate.fontSize;
		Font f = new Font("Arial", Font.BOLD, (int) (this.toPixel(fontSize) + 0.5));
		this.g2.setFont(f);
		this.g2.drawString(text, 0, 0);
	}

	@Override
	public void endText() {
		this.restoreGraphicsState();
	}

	@Override
	public void strokePath() {
		this.g2.draw(this.currRect);
	}

	@Override
	public void fillPath() {
		// TODO Auto-generated method stub
		
	}
	
	private double[] flateMatrix(Matrix matrix) {
		double[] doubleMatrix = matrix.flate();
		doubleMatrix[4] = this.toPixel(doubleMatrix[4]);
		doubleMatrix[5] = this.toPixel(doubleMatrix[5]);
		return doubleMatrix;
	}
}
