package me.lumpchen.sledge.pdf.toolkit.viewer;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Stack;

import me.lumpchen.sledge.pdf.graphics.GraphicsState;
import me.lumpchen.sledge.pdf.graphics.Matrix;
import me.lumpchen.sledge.pdf.graphics.PDFColor;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.text.font.PDFFont;

public class DefaultGraphics implements VirtualGraphics {

	private Stack<GraphicsState> gsStack = new Stack<GraphicsState>();

	private GraphicsState gstate;
	
	private Graphics2D g2;
	
	private GeneralPath currPath;
	
	private static int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
	
	private float resolution = -1;
	
	private Font defaultFont;
	
	public DefaultGraphics(Graphics2D g2) {
		this.g2 = g2;
		
		this.g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		this.g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	
		this.defaultFont = new Font("Arial", Font.PLAIN, 12);
		
		this.gstate = new GraphicsState();
		this.gstate.ctm = this.g2.getTransform();
	}
	
	public void setResolutoin(float deviceRes) {
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
		Matrix base = new Matrix(1, 0, 0, 1, 0, toPixel(height));
		AffineTransform at = new AffineTransform(base.flate());
		at.preConcatenate(this.gstate.ctm);
		this.gstate.ctm = at;
		this.g2.setTransform(this.gstate.ctm);
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
		if (font == null) {
			return;
		}
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
		double sx = at.getScaleX();
		double sy = at.getScaleY();
		
		double rx = at.getShearX();
		double ry = at.getShearY();
		
		this.gstate.fontSize = (float) this.toPixel(sx);
		
		double tx = at.getTranslateX();
		double ty = at.getTranslateY();
		
		at = new AffineTransform(1, 0, 0, 1, tx, -ty);
		AffineTransform m = new AffineTransform(this.gstate.ctm); 
		m.preConcatenate(at);
		this.g2.setTransform(m);
	}

	@Override
	public void showText(String text) {
		this.saveGraphicsState();
		
		if (this.gstate.font.notEmbed()) {
			this.g2.setFont(this.gstate.awtFont);
			this.g2.drawString(text, 0, 0);	
			return;
		}
		
		try {
			this.gstate.font.renderText(text, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void endText() {
		this.restoreGraphicsState();
	}

	@Override
	public void translate(double tx, double ty) {
		this.g2.translate(tx, ty);
	}

	private double[] flateMatrix(Matrix matrix) {
		double[] doubleMatrix = matrix.flate();
		doubleMatrix[4] = this.toPixel(doubleMatrix[4]);
		doubleMatrix[5] = this.toPixel(doubleMatrix[5]);
		return doubleMatrix;
	}

	@Override
	public GraphicsState currentGState() {
		return this.gstate;
	}

	@Override
	public float[] getResolution() {
		if (this.resolution <= 0) {
			this.resolution = screenRes;
		}
		return new float[]{this.resolution, this.resolution};
	}
	
	@Override
	public boolean drawImage(Image img) {
		return this.g2.drawImage(img, null, null);
	}

	@Override
	public void newPath() {
		this.currPath = new GeneralPath();
	}
	
	@Override
	public void closePath() {
		this.currPath.closePath();
	}
	
	@Override
	public void moveTo(double x, double y) {
		this.currPath.moveTo(this.toPixel(x), this.toPixel(y));
	}

	@Override
	public void lineTo(double x, double y) {
		this.currPath.lineTo(this.toPixel(x), this.toPixel(y));
	}

	@Override
	public void quadTo(double x1, double y1, double x2, double y2) {
		this.currPath.quadTo(this.toPixel(x1), this.toPixel(y1), this.toPixel(x2), this.toPixel(y2));
	}

	@Override
	public void curveTo(double x1, double y1, double x2, double y2, double x3, double y3) {
		this.currPath.curveTo(this.toPixel(x1), this.toPixel(y1), this.toPixel(x2), this.toPixel(y2), 
				this.toPixel(x3), this.toPixel(y3));		
	}

	@Override
	public void stroke() {
		if (this.currPath != null) {
			this.g2.draw(this.currPath);			
		}
	}
	
	@Override
	public void fill() {
		if (this.currPath != null) {
			this.g2.fill(this.currPath);			
		}
	}

	@Override
	public void clip() {
		if (null != this.currPath) {
			this.g2.clip(this.currPath);
		}
	}
	
	@Override
	public void strokeRect(double x, double y, double width, double height) {
		x = this.toPixel(x);
		y = this.toPixel(y);
		width = this.toPixel(width);
		height = this.toPixel(height);
		Rectangle2D.Double rect = new Rectangle2D.Double(x, y, width, height);
		this.g2.draw(rect);
	}

	@Override
	public void fillRect(double x, double y, double width, double height) {
		x = this.toPixel(x);
		y = this.toPixel(y);
		width = this.toPixel(width);
		height = this.toPixel(height);
		Rectangle2D.Double rect = new Rectangle2D.Double(x, y, width, height);
		this.g2.fill(rect);
	}

	@Override
	public void setStrokeColor(PDFColor color) {
		this.gstate.color = color.toJavaColor();
		this.g2.setColor(this.gstate.color);
	}

	@Override
	public void setFillColor(PDFColor color) {
		this.gstate.color = color.toJavaColor();
		this.g2.setColor(this.gstate.color);		
	}

	@Override
	public void strokeShape(Shape shape) {
		this.g2.draw(shape);
	}

	@Override
	public void fillShape(Shape shape) {
		this.g2.fill(shape);
	}
}
