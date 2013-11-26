package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class PDFDocumentInfo {

	private IndirectObject info;

	public PDFDocumentInfo() {
	}
	
	public String toString() {
		if (this.info != null) {
			return this.info.toString();
		}
		return "";
	}
	
	public void setObj(IndirectObject obj) {
		this.info = obj;
	}
	
	public String getCreator() {
		return this.info.getValue(PName.creator).toString();
	}
	
	public String getProducer() {
		return this.info.getValue(PName.producer).toString();
	}
	
}
