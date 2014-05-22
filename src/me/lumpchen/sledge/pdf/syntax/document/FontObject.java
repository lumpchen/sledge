package me.lumpchen.sledge.pdf.syntax.document;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.SyntaxException;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.basic.PStream;

public class FontObject extends DocObject {

	private PName name;
	private PName subtype;
	private PName baseFont;
	private PNumber firstChar;
	private PNumber lastChar;
	private PArray widths;
	private FontDescriptorObj fontDescriptor;
	
	private PName predinedEncoding;
	private IndirectObject encodingObj;
	private PStream toUnicode;
	
	private FontObject descendantFonts;

	public FontObject(IndirectObject obj, PDFDocument owner) {
		super(obj, owner);
		
		this.name = this.getValueAsName("Name");
		this.subtype = this.getValueAsName(PName.Subtype);
		this.baseFont = this.getValueAsName(PName.BaseFont);
		this.firstChar = this.getValueAsNumber(PName.FirstChar);
		this.lastChar = this.getValueAsNumber(PName.LastChar);
		
		PObject arr = this.getValue(PName.Widths);
		if (null != arr) {
			if (arr instanceof PArray) {
				this.widths = (PArray) arr;
			} else {
				if (arr instanceof IndirectRef) {
					IndirectRef ref = (IndirectRef) arr;
					IndirectObject iobj = owner.getObject(ref);
					PObject inside = iobj.insideObj();
					if (inside instanceof PArray) {
						this.widths = (PArray) inside;
					} else {
						throw new SyntaxException("invalid <Widths> object: " + inside);
					}
				}				
			}
		}
		
		IndirectRef ref = this.getValueAsRef(PName.FontDescriptor);
		if (ref != null) {
			IndirectObject iobj = owner.getObject(ref);
			this.fontDescriptor = new FontDescriptorObj(iobj, owner);
		}
		
		PObject val = this.getValue(PName.Encoding);
		if (val != null) {
			if (val instanceof PName) {
				this.predinedEncoding = (PName) val;
				
				
			} else {
				if (val instanceof IndirectRef) {
					this.encodingObj = owner.getObject((IndirectRef) val);
				}
			}
		}

		val = this.getValue(PName.ToUnicode);
		if (val != null) {
			if (val instanceof IndirectRef) {
				IndirectObject iobj = owner.getObject((IndirectRef) val);
				this.toUnicode = iobj.getStream();
			}
		}
		
		PArray array = this.getValueAsArray(PName.DescendantFonts);
		if (array != null && array.size() > 0) {
			PObject item = array.get(0);
			if (item instanceof IndirectRef) {
				IndirectObject iobj = owner.getObject((IndirectRef) item);
				this.descendantFonts = new FontObject(iobj, owner);
			}
		}
	}

	@Override
	public PName getType() {
		return PName.Font;
	}

	public PName getBaseFont() {
		return this.baseFont;
	}

	public PName getName() {
		return this.name;
	}

	public PName getSubType() {
		return this.subtype;
	}

	public PNumber getFirstChar() {
		return this.firstChar;
	}

	public PNumber getLastChar() {
		return this.lastChar;
	}

	public PArray getWidths() {
		return this.widths;
	}

	public FontDescriptorObj getFontDescriptor() {
		return this.fontDescriptor;
	}

	public PName getPredefinedEncoding() {
		return this.predinedEncoding;
	}
	
	public IndirectObject getEncoding() {
		return this.encodingObj;
	}

	public PStream getToUnicode() {
		return toUnicode;
	}
	
	public FontObject getDescendantFonts() {
		return this.descendantFonts;
	}
}
