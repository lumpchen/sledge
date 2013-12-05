package me.lumpchen.sledge.pdf.graphics;

public interface VirtualGraphics {

	public void saveGraphicsState();
	
	public void restoreGraphicsState();
	
	public void beginPath(float x, float y);
	public void beginPath(float x, float y, float width, float height);
}
