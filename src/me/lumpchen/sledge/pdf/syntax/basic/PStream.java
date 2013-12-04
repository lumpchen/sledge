package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.reader.InvalidTagException;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.syntax.PObject;
import me.lumpchen.sledge.pdf.syntax.SyntaxException;

public class PStream extends PObject {

	public static final byte[] BEGIN = {'s', 't', 'r', 'e', 'a', 'm'};
	public static final byte[] END = {'e', 'n', 'd', 's', 't', 'r', 'e', 'a', 'm'};
	
	private PDictionary dict;
	private byte[] stream;
	
	public PStream() {
	}
	
	public void setDict(PDictionary dict) {
		this.dict = dict;
	}
	
	public PDictionary getDict() {
		return this.dict;
	}
	
	public byte[] getStream() {
		return this.stream;
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		if (null != this.dict) {
			buf.append(this.dict.toString());
		}
		buf.append("stream");
		buf.append('\n');
		buf.append(new String(stream));
		buf.append('\n');
		buf.append("endstream");
		buf.append('\n');
		return buf.toString();
	}
	
	@Override
	protected void readBeginTag(ObjectReader reader) {
		byte[] obj = reader.readBytes(BEGIN.length);
		for (int i = 0; i < BEGIN.length; i++) {
			if (obj[i] != BEGIN[i]) {
				throw new InvalidTagException();
			}
		}
	}

	@Override
	protected void readBody(ObjectReader reader) {
		if (null == this.dict) {
			throw new SyntaxException("not found stream dictionary.");
		}
		PInteger len = this.dict.getValueAsIntger(PName.Length);
		if (len == null) {
			throw new SyntaxException("not found stream length in dictionary.");
		}
		this.stream = reader.readBytes(len.getValue());
		reader.readEOL();
	}

	@Override
	protected void readEndTag(ObjectReader reader) {
		byte[] obj = reader.readBytes(END.length);
		for (int i = 0; i < END.length; i++) {
			if (obj[i] != END[i]) {
				throw new InvalidTagException();
			}
		}
	}
}
