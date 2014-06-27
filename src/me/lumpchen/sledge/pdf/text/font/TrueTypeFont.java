package me.lumpchen.sledge.pdf.text.font;

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.io.IOException;

import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.document.FontObject;

import com.docscience.pathfinder.font.driver.FontReader;
import com.docscience.pathfinder.font.driver.FontReaderFactory;
import com.docscience.pathfinder.font.shared.GlyphDescription;
import com.docscience.pathfinder.font.shared.GlyphPath;
import com.docscience.pathfinder.font.shared.GlyphPathIterator;

public class TrueTypeFont extends PDFFont {

	private FontReader reader;

	TrueTypeFont(FontObject fontObj) throws IOException {
		super(fontObj);

		FontFile fontFile = this.fontDescriptor.getFontFile2();
		if (fontFile != null) {
			this.reader = FontReaderFactory.getInstance().createFontReader(fontFile.getBytes());
			reader.selectFont(0);
		}
	}

	@Override
	public void renderText(String s, VirtualGraphics gd) throws IOException {
		int upm = this.reader.getMetrics().getUnitsPerEm();
		float[] res = gd.getResolution();
		float pt = gd.currentGState().fontSize;
		
		boolean cmapDone = false;
		if (this.encoding != null && this.encoding.getType() == PDFFontEncoding.CMAP) {
			cmapDone = true;
		}
		
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			int gid = cmapDone ? c : this.reader.getEncoding().getGlyphId(c);
			GlyphDescription glyph = reader.getGlyph(gid);
			this.drawGlyph(glyph, gd);
			
			int advance = this.reader.getMetrics().getAdvancedWidth(gid);
			double scale = (pt / 72) * res[0] / upm;
			gd.translate(advance * scale, 0);
		}
	}
	
	private void drawGlyph(GlyphDescription glyph, VirtualGraphics gd) throws IOException {
		GlyphPath path = glyph.getPath();
		if (path == null) {
			return;
		}
		
		GeneralPath gpath = new GeneralPath();
		
		int upm = this.reader.getMetrics().getUnitsPerEm();
		float pt = gd.currentGState().fontSize;
		float[] res = gd.getResolution();
		AffineTransform at = new AffineTransform(1, 0, 0, -1, 0, 0);
		at.scale((pt / 72) * res[0] / upm , (pt / 72) * res[1] / upm);
		double[] m = new double[6];
		at.getMatrix(m);
		
		GlyphPathIterator iter = path.getGlyphPathIterator(m);
		double[] coords = new double[6];
		for (; !iter.isDone(); iter.next()) {
			int type = iter.currentSegment(coords);
			switch (type) {
			case GlyphPathIterator.SEG_MOVETO:
				gpath.moveTo(coords[0], coords[1]);
				break;
			case GlyphPathIterator.SEG_LINETO:
				gpath.lineTo(coords[0], coords[1]);
				break;
			case GlyphPathIterator.SEG_QUADTO:
				gpath.quadTo(coords[0], coords[1], coords[2], coords[3]);
				break;
			case GlyphPathIterator.SEG_CURVETO:
				gpath.curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
				break;
			case GlyphPathIterator.SEG_CLOSE:
				gpath.closePath();
				break;
			}
		}
		
		gd.fillShape(gpath);
	}

	@Override
	public void close() throws IOException {
		this.reader.close();
	}

}
