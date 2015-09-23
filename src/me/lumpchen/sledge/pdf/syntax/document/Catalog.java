package me.lumpchen.sledge.pdf.syntax.document;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.lang.PArray;
import me.lumpchen.sledge.pdf.syntax.lang.PDictionary;
import me.lumpchen.sledge.pdf.syntax.lang.PName;

public class Catalog extends DocObject {

	private PDictionary acroForm;
	
	public Catalog(IndirectObject obj, PDFDocument owner) {
		super(obj, owner);
		
		this.acroForm = this.getValueAsDict(PName.AcroForm);
	}

	public PName getType() {
		return PName.Catalog;
	}

	public IndirectRef getPages() {
		return this.getValueAsRef(PName.Pages);
	}
	
	public PDictionary getAcroForm() {
		return this.acroForm;
	}
	
	public PArray getFields() {
		if (this.acroForm == null) {
			return null;
		}
		return this.acroForm.getValueAsArray(PName.Fields);
	}
	
	public Annot getAnnot(IndirectRef ref) {
		IndirectObject annot = this.owner.getObject(ref);
		if (annot == null) {
			return null;
		}
		return new Annot(annot, owner);
	}
	
}
