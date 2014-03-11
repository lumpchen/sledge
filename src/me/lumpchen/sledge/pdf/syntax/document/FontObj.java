package me.lumpchen.sledge.pdf.syntax.document;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.SyntaxException;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.basic.PStream;

public class FontObj extends DocObject {

	private PName name;
	private PName subtype;
	private PName baseFont;
	private PNumber firstChar;
	private PNumber lastChar;
	private PArray widths;
	private FontDescriptorObj fontDescriptor;
	
	private PName predinedEncoding;
	private EncodingObj encoding;
	private PStream toUnicode;

	public FontObj(IndirectObject obj, PDFDocument owner) {
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
			if (iobj != null) {
				this.fontDescriptor = new FontDescriptorObj(iobj, owner);
			}
		}
		
		PObject val = this.getValue(PName.Encoding);
		if (val != null) {
			if (val instanceof PName) {
				this.predinedEncoding = (PName) val;
			} else {
				if (val instanceof IndirectRef) {
					IndirectObject iobj = owner.getObject((IndirectRef) val);
					if (iobj != null) {
						this.encoding = new EncodingObj(iobj, owner);
					}
				}
			}
		}

		val = this.getValue(PName.ToUnicode);
		if (val != null) {
			if (val instanceof IndirectRef) {
				IndirectObject iobj = owner.getObject((IndirectRef) val);
				if (iobj != null) {
					this.toUnicode = iobj.getStream();
				}
			}
		}
	}

	@Override
	public PName getType() {
		return PName.font;
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
	
	public EncodingObj getEncoding() {
		return this.encoding;
	}

	public PStream getToUnicode() {
		return toUnicode;
	}
}
