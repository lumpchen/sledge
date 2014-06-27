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
	public void renderText(String s, VirtualGraphics gd) {
		int pt = Math.round(gd.currentGState().fontSize);
		int hRes = Math.round(gd.getResolution()[0]);
		int vRes = Math.round(gd.getResolution()[1]);

		Color color = gd.currentGState().color;
		if (color == null) {
			color = Color.BLACK;
		}
		
		this.ft.setCharSize(pt, hRes, vRes);

		GlyphSlotRec[] glyphs = null;
		if (this.encoding != null && this.encoding.getType() == PDFFontEncoding.CMAP) {
			char[] cs = s.toCharArray();
			for (int i = 0; i < cs.length; i++) {
				int gid = this.encoding.getGlyphID((int) cs[i]);
				cs[i] = (char) (gid);
			}
			glyphs = this.ft.getGlyphSlots(cs, color);
		} else {
			s = s.replace(' ', 'c');
			glyphs = this.ft.getGlyphSlots(s, color);
		}

		if (glyphs == null) {
			return;
		}

		for (GlyphSlotRec glyph : glyphs) {
			double advance = glyph.getHAdvance();

			gd.translate(0, -glyph.getBearingY());
			gd.drawImage(glyph.getGlyphBufferImage(null));
			gd.translate(advance, glyph.getBearingY());
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
}
