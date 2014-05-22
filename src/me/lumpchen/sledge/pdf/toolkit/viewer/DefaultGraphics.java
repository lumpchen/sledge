package me.lumpchen.sledge.pdf.toolkit.viewer;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Stack;

import me.lumpchen.jfreetype.GlyphSlotRec;
import me.lumpchen.sledge.pdf.graphics.GraphicsState;
import me.lumpchen.sledge.pdf.graphics.Matrix;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.text.font.PDFFont;

public class DefaultGraphics implements VirtualGraphics {

	private Stack<GraphicsState> gsStack = new Stack<GraphicsState>();

	private GraphicsState gstate;
	
	private Graphics2D g2;
	
	private Shape currPath;
	
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
		
		this.gstate.font.renderText(text.toCharArray(), this);
		
//		GlyphSlotRec[] bitmaps = null;
//		if (text.length() == 4) {
//			StringBuilder buf = new StringBuilder();
//			char c0 = text.charAt(0);
//			char c1 = text.charAt(1);
//			c0 = (char) ((c0 << 8) | (c1 & 0xFF)); 
//			
//			char c2 = text.charAt(2);
//			char c3 = text.charAt(3);
//			c2 = (char) ((c2 << 8) | (c3 & 0xFF));
//			
//			buf.append(c0).append(c2);
//			text = buf.toString();
//			
//			int[] gids = new int[2];
//			gids[0] = c0 & 0xFFFF;
//			gids[1] = c2 & 0xFFFF;
//			
//			bitmaps = this.gstate.font.getGlyphBitmap(gids, this.gstate.color);	
//		} else {
//			text = text.replace(' ', 'c');
//			bitmaps = this.gstate.font.getGlyphBitmap(text, this.gstate.color);			
//		}
//
//		for (GlyphSlotRec glyph : bitmaps) {
//			BufferedImage img = glyph.getGlyphBufferImage(this.gstate.color);
//			double advance = glyph.getHAdvance();
//			
//			this.g2.translate(0, -glyph.getBearingY());
//			g2.drawImage(img, null, null);
//			this.g2.translate(advance, glyph.getBearingY());
//		}
		
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
	public void translate(double tx, double ty) {
		this.g2.translate(tx, ty);
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
}
