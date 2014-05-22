package me.lumpchen.sledge.pdf.text.font;

import java.awt.Color;

import me.lumpchen.jfreetype.GlyphSlotRec;
import me.lumpchen.jfreetype.JFreeType;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.document.FontObject;

public class JTrueTypeFont extends PDFFont {

	private JFreeType ft;

	public JTrueTypeFont(FontObject fontObj) {
		super(fontObj);

		FontFile fontFile = this.fontDescriptor.getFontFile2();
		if (fontFile != null) {
			this.parse(fontFile.getBytes());
		}
	}

	private void parse(byte[] fontBytes) {
		this.ft = new JFreeType();
		ft.open(fontBytes, 0);
	}

	@Override
	public void renderText(char[] cs, VirtualGraphics gd) {
		int pt = Math.round(gd.currentGState().fontSize);
		int hRes = Math.round(gd.getResolution()[0]);
		int vRes = Math.round(gd.getResolution()[1]);

		Color color = gd.currentGState().color;
		this.ft.setCharSize(pt, hRes, vRes);
		
		if (this.encoding != null) {
			for (int i = 0; i < cs.length; i++) {
				cs[i] = (char) this.encoding.getGlyphID(cs[i]);				
			}
		} else {
		}
		GlyphSlotRec[] glyphs = this.ft.getGlyphSlots(cs, color);
		
		for (GlyphSlotRec glyph : glyphs) {
			double advance = glyph.getHAdvance();
			
			gd.translate(0, -glyph.getBearingY());
			gd.drawImage(glyph.getGlyphBufferImage(null));
			gd.translate(advance, glyph.getBearingY());
		}
	}
}
