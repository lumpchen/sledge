package me.lumpchen.sledge.pdf.graphics;

import me.lumpchen.sledge.pdf.syntax.basic.Rectangle;

public class VirtualGraphicsHelper {

	public static void drawRectangle(Rectangle rect, VirtualGraphics g2) {
		g2.saveGraphicsState();
		g2.beginPath(rect.llx().doubleValue(), rect.lly().doubleValue(), 
				rect.urx().doubleValue(), rect.ury().doubleValue());
		g2.setColor(Color.red);
		g2.strokePath();
		g2.setColor(Color.white);
		g2.fillPath();
		g2.closePath();
		g2.restoreGraphicsState();
	}
}
