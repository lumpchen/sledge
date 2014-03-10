package me.lumpchen.sledge.pdf.syntax.document;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.SyntaxException;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.basic.PString;

public class FontObject extends DocObject {

	public FontObject(IndirectObject obj, PDFDocument owner) {
		super(obj, owner);
	}
	
	public String getBaseFont() {
		PObject obj = this.getValue(PName.BaseFont);
		if (null == obj) {
			return null;
		}
		
		if (obj instanceof PString) {
			return ((PString) obj).toJavaString();
		}
		
		if (obj instanceof PName) {
			return ((PName) obj).getName();
		}
		return null;
	}
	
	@Override
	public PName getType() {
		return PName.font;
	}
	
	public PName getName() {
		return this.getValueAsName("Name");
	}
	
	public PName getSubType() {
		return this.getValueAsName(PName.Subtype);
	}
	
	public PNumber getFirstChar() {
		return this.getValueAsNumber(PName.FirstChar);
	}
	
	public PNumber getLastChar() {
		return this.getValueAsNumber(PName.LastChar);
	}
	
	public PArray getWidths() {
		PObject obj = this.getValue(PName.Widths);
		if (null == obj) {
			return null;
		}
		
		if (obj instanceof PArray) {
			return (PArray) obj;
		}
		
		PDFDocument owner = this.getDocument();
		while (obj instanceof IndirectRef) {
			IndirectRef ref = (IndirectRef) obj;
			IndirectObject iobj = owner.getObject(ref);
			PObject inside = iobj.insideObj();
			if (inside instanceof PArray) {
				return (PArray) inside;
			} else if (inside instanceof IndirectRef) {
				continue;
			} else {
				throw new SyntaxException("invalid <Widths> object: " + inside);
			}
		}
		return null;
	}
	
	public PDictionary getFontDescriptor() {
		PObject desc = this.getValue(PName.FontDescriptor);
		if (null == desc) {
			return null;
		}
		
		if (desc instanceof PDictionary) {
			return (PDictionary) desc;
		}
		
		if (desc instanceof IndirectRef) {
			IndirectRef ref = (IndirectRef) desc;
			PDFDocument owner = this.getDocument();
			
			IndirectObject obj = owner.getObject(ref);
			return obj.getDict();
		}
		
		return null;
	}

	public PObject getEncoding() {
		return this.getValue(PName.Encoding);
	}
}
