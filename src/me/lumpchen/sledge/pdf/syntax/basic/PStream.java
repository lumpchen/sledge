package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.reader.InvalidTagException;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.syntax.SyntaxException;
import me.lumpchen.sledge.pdf.writer.ObjectWriter;

public class PStream extends PObject {

	public static final byte[] BEGIN = {'s', 't', 'r', 'e', 'a', 'm'};
	public static final byte[] END = {'e', 'n', 'd', 's', 't', 'r', 'e', 'a', 'm'};
	
	private PDictionary dict;
	private byte[] stream;
	
	public PStream() {
		super.type = Type.Stream;
	}
	
	public PStream(PDictionary dict) {
		this.dict = dict;
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
	
	public void setStream(byte[] stream) {
		this.stream = stream;
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
		PNumber len = this.dict.getValueAsNumber(PName.Length);
		if (len == null) {
			throw new SyntaxException("not found stream length in dictionary.");
		}
		this.stream = reader.readBytes(len.intValue());
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

	@Override
	protected void writeBeginTag(ObjectWriter writer) {
		writer.writeBytes(PStream.BEGIN);
	}

	@Override
	protected void writeBody(ObjectWriter writer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeEndTag(ObjectWriter writer) {
		writer.writeBytes(PStream.END);
	}
}
