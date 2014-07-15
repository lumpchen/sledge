package me.lumpchen.sledge.pdf.graphics;

import java.awt.Image;
import java.awt.Shape;

import me.lumpchen.sledge.pdf.text.font.PDFFont;

public interface VirtualGraphics {

	public void saveGraphicsState();
	public void restoreGraphicsState();

	public void beginCanvas(double width, double height);
	
	public void concatenate(Matrix matrix);

	public void setFont(PDFFont font, float size);
	public void beginText();
	public void showText(String text);
	public void endText();
	
	public void transformTextMatrix(Matrix matrix);
	public void transformTextPosition(double tx, double ty);
	
	public GraphicsState currentGState();
	
	public float[] getResolution();
	
	public void translate(double tx, double ty);
	public boolean drawImage(Image img);
	
	public void newPath();
	public void moveTo(double x, double y);
	public void lineTo(double x, double y);
	public void quadTo(double x1, double y1, double x2, double y2);
	public void curveTo(double x1, double y1, double x2, double y2, double x3, double y3);
	public void closePath();
	
	public void clip();
	
	public void stroke();
	public void strokeShape(Shape shape);
	public void strokeRect(double x, double y, double width, double height);	

	public void fill();
	public void fillShape(Shape shape);
	public void fillRect(double x, double y, double width, double height);
	
	public void setColor(PDFColor color);
	public void setStrokeColor(PDFColor color);
	public void setFillColor(PDFColor color);
}
