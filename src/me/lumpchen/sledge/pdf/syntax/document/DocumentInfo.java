package me.lumpchen.sledge.pdf.syntax.document;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.lang.PName;
import me.lumpchen.sledge.pdf.syntax.lang.PString;

public class DocumentInfo extends DocObject {

	public DocumentInfo(IndirectObject obj, PDFDocument owner) {
		super(obj, owner);
	}

	public PString getTitle() {
		return this.getValueAsString(PName.Title);
	}
	
	public PString getAuthor() {
		return this.getValueAsString(PName.Author);
	}
	
	public PString getSubject() {
		return this.getValueAsString(PName.Subject);
	}
	
	public PString getKeywords() {
		return this.getValueAsString(PName.Keywords);
	}

	public PString getCreationDate() {
		return this.getValueAsString(PName.CreationDate);
	}
	
	public PString getModDate() {
		return this.getValueAsString(PName.ModDate);
	}
	
	public PString getCreator() {
		return this.getValueAsString(PName.Creator);
	}
	
	public PString getProducer() {
		return this.getValueAsString(PName.Producer);
	}
	
	public PName getTrapped() {
		return this.getValueAsName(PName.Trapped);
	}

	@Override
	public PName getType() {
		return PName.Info;
	}
	
}
