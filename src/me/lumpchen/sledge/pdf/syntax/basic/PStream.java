package me.lumpchen.sledge.pdf.syntax.basic;


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
}
