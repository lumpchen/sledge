package me.lumpchen.sledge.pdf.text.font;

import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.basic.PString;

public class PDFEncoding {

	public PDFEncoding(PObject obj) {
		this.read(obj);
	}
	
	private void read(PObject obj) {
		if (obj instanceof PString) {
			
		}
	}
}
