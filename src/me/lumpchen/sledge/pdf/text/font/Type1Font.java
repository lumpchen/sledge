package me.lumpchen.sledge.pdf.text.font;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import me.lumpchen.jfreetype.GlyphSlotRec;
import me.lumpchen.jfreetype.JFreeType;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.syntax.document.FontObject;

public class Type1Font extends PDFFont {

	private JFreeType ft;
	
	public Type1Font(FontObject fontObj) {
		super(fontObj);

		FontFile fontFile = this.fontDescriptor.getFontFile();
		if (fontFile == null) {
			fontFile = this.fontDescriptor.getFontFile3(); 
		}
		if (fontFile != null) {
			this.parse(fontFile.getBytes());
			
			try {
				byte[] data = fontFile.getBytes();
				FileOutputStream fos = new FileOutputStream(new File("c:/temp/t1/" + fontObj.getBaseFont().toString()));
				fos.write(data, 0, data.length);
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}    
	}

	private void parse(byte[] fontBytes) {
		this.ft = new JFreeType();
		ft.open(fontBytes, 0);
	}
	
	@Override
	public void renderText(String s, VirtualGraphics gd) {
		int pt = (int) Math.round(gd.currentGState().textState.getFontSize());
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
			glyphs = this.ft.getGlyphSlots(cs);
		} else {
			glyphs = this.ft.getGlyphSlots(s);
		}

		if (glyphs == null) {
			return;
		}

		int leftGlyph = 0;
		int rightGlyph = 0;
		for (GlyphSlotRec glyph : glyphs) {
			double advance = glyph.getHAdvance();

			double adjustH = gd.getAdjustmentH(glyph.getChar());
			advance += adjustH;

			rightGlyph = glyph.getGlyph();
			double kerning = 0;
			if (leftGlyph != 0) {
				kerning = this.ft.getKerning(leftGlyph, rightGlyph);
//				System.out.println(kerning);
			}
			leftGlyph = rightGlyph;
			
			gd.translate(kerning, -glyph.getBearingY());
			gd.drawImage(glyph.getGlyphBitmap(color));
			gd.translate(advance, glyph.getBearingY());
		}
		
	}

	@Override
	public void close() {
		if (this.ft != null) {
			this.ft.close();
		}
	}
}
