package me.lumpchen.sledge.pdf.viewer;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;

import me.lumpchen.sledge.pdf.graphics.Color;
import me.lumpchen.sledge.pdf.graphics.Matrix;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.text.font.PDFFont;

public class DefaultGraphicsPath implements VirtualGraphics {

	private Graphics2D g2;
	
	private Rectangle currRect;
	
	private static int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
	
	private double resolution = -1;
	
	public DefaultGraphicsPath(Graphics2D g2) {
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
		
	}

	@Override
	public void restoreGraphicsState() {
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColor(Color color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beginText() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFont(PDFFont font, float size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transformTextMatrix(Matrix matrix) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showText(String text) {
		this.g2.drawString(text, 0, 0);
	}

	@Override
	public void endText() {
		
	}


}
