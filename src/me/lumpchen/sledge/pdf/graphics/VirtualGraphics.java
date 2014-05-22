package me.lumpchen.sledge.pdf.graphics;

import java.awt.Image;

import me.lumpchen.sledge.pdf.text.font.PDFFont;

public interface VirtualGraphics {

	/**
	 * Save the current graphics state on the graphics state stack
	 * */
	public void saveGraphicsState();

	/**
	 * Restore the graphics state by removing the most recently saved state from
	 * the stack and making it the current state
	 * */
	public void restoreGraphicsState();

	public void beginCanvas(double width, double height);
	
	/**
	 * Append a rectangle to the current path as a complete subpath, with
	 * lower-left corner (x, y) and dimensions width and height in user space.
	 * */
	public void beginPath(double x, double y, double width, double height);

	/**
	 * Modify the current clipping path by intersecting it with the current
	 * path, using the nonzero winding number rule to determine which regions
	 * lie inside the clipping path.
	 * */
	public void clip();

	/**
	 * End the path object without filling or stroking it. This operator is a
	 * path-painting no-op, used primarily for the side effect of changing the
	 * current clipping path.
	 */
	public void closePath();
	
	public void strokePath();
	public void fillPath();

	/**
	 * Modify the current transformation matrix (CTM) by concatenating the
	 * specified matrix
	 * */
	public void concatenate(Matrix matrix);

	/**
	 * Same as RG but used for nonstroking operations, color space to
	 * DeviceGray.
	 * */
	public void setColor(PDFColor color);

	/**
	 * Begin a text object, initializing the text matrix, Tm , and the text line
	 * matrix, Tlm , to the identity matrix.
	 * */
	public void beginText();

	/**
	 * Set the text font to font and the text font size.
	 * */
	public void setFont(PDFFont font, float size);

	/**
	 * Set the text matrix, and the text line matrix. The matrix specified by
	 * the operands is not concatenated onto the current text matrix, but
	 * replaces it.
	 * */
	public void transformTextMatrix(Matrix matrix);

	/**
	 * Show a text string.
	 * */
	public void showText(String text);

	/**
	 * End a text object, discarding the text matrix.
	 * */
	public void endText();
	
	
	public GraphicsState currentGState();
	
	public float[] getResolution();
	
	public void translate(double tx, double ty);
	public boolean drawImage(Image img);
}
