package me.lumpchen.sledge.pdf.graphics;

import me.lumpchen.sledge.pdf.syntax.lang.Rectangle;

public class VirtualGraphicsHelper {

	public static void drawRectangle(Rectangle rect, VirtualGraphics g2) {
		g2.saveGraphicsState();
		g2.setColor(PDFColor.red);
		g2.strokeRect(rect.llx().doubleValue(), rect.lly().doubleValue(), 
				rect.urx().doubleValue(), rect.ury().doubleValue());
		g2.setColor(PDFColor.white);
		g2.fillRect(rect.llx().doubleValue(), rect.lly().doubleValue(), 
				rect.urx().doubleValue(), rect.ury().doubleValue());
		g2.restoreGraphicsState();
	}
}
