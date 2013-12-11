package me.lumpchen.sledge.pdf.viewer;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import me.lumpchen.sledge.pdf.graphics.Color;
import me.lumpchen.sledge.pdf.graphics.Matrix;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.text.font.PDFFont;

public class DefaultGraphics implements VirtualGraphics {

	private Graphics2D g2;
	
	private Rectangle currRect;
	
	private static int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
	
	private double resolution = -1;
	
	private AffineTransform currCTM;
	
	public DefaultGraphics(Graphics2D g2) {
		this.g2 = g2;
	}
	
	public void setResolutoin(double deviceRes) {
		this.resolution = deviceRes;
	}
	
	private int toPixel(double d) {
		if (this.resolution <= 0) {
			this.resolution = screenRes;
		}
		return (int) ((d / 72) * resolution + 0.5);
	}
	
	@Override
	public void saveGraphicsState() {
		this.currCTM = this.g2.getTransform();
	}

	@Override
	public void restoreGraphicsState() {
		this.g2.setTransform(this.currCTM);
	}

	@Override
	public void beginPath(double x, double y, double width, double height) {
		int ix = this.toPixel(x);
		int iy = this.toPixel(y);
		int iWidth = this.toPixel(width);
		int iHeight = this.toPixel(height);
		this.currRect = new Rectangle(ix, iy, iWidth, iHeight);
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
		AffineTransform at = new AffineTransform(matrix.flate());
		try {
			at.invert();
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		at.concatenate(this.currCTM);
		this.g2.transform(at);
	}

	@Override
	public void setColor(Color color) {
		// TODO Auto-generated method stub
		this.g2.setColor(java.awt.Color.red);
	}

	@Override
	public void beginText() {
		this.saveGraphicsState();
	}

	@Override
	public void setFont(PDFFont font, float size) {
		// TODO Auto-generated method stub
	}

	@Override
	public void transformTextMatrix(Matrix matrix) {
		AffineTransform at = new AffineTransform(matrix.flate());
		at.concatenate(this.currCTM);
		this.g2.transform(at);
	}

	@Override
	public void showText(String text) {
		Point2D ptSrc = new Point2D.Double(0, 0);
		Point2D ptDst = new Point2D.Double();
//		try {
//			this.g2.getTransform().inverseTransform(ptSrc, ptDst);
//		} catch (NoninvertibleTransformException e) {
//			e.printStackTrace();
//		}
		this.g2.getTransform().transform(ptSrc, ptDst);
		
		System.out.println(ptDst);
		
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


}
