package me.lumpchen.sledge.pdf.syntax.lang;

import java.util.ArrayList;
import java.util.List;

import me.lumpchen.sledge.pdf.syntax.SyntaxException;
import me.lumpchen.sledge.pdf.syntax.filters.DecoderChain;


public class PStream extends PObject {

	public static final byte[] BEGIN = {'s', 't', 'r', 'e', 'a', 'm'};
	public static final byte[] END = {'e', 'n', 'd', 's', 't', 'r', 'e', 'a', 'm'};
	
	private PDictionary dict;
	private byte[] stream;
	private byte[] decodedStream;
	
	public PStream() {
		super.classType = ClassType.Stream;
	}
	
	public PStream(PDictionary dict) {
		super.classType = ClassType.Stream;
		this.dict = dict;
	}
	
	public PStream(PDictionary dict, byte[] stream) {
		super.classType = ClassType.Stream;
		this.dict = dict;
		this.stream = stream;
	}
	
	public void setDict(PDictionary dict) {
		this.dict = dict;
	}
	
	public int getLength() {
		PNumber len = this.dict.getValueAsNumber(PName.Length);
		if (null == len) {
			throw new SyntaxException("not found stream length.");
		}
		return len.intValue();
	}
	
	public PDictionary getDict() {
		return this.dict;
	}
	
	public byte[] getStream() {
		return this.stream;
	}
	
	public byte[] getDecodedStream() {
		if (this.decodedStream != null) {
			return this.decodedStream;
		}
		
		if (this.stream == null) {
			return null;
		}
		
		DecoderChain chain = new DecoderChain();
		this.decodedStream = chain.decode(this);
		return this.decodedStream;
	}

	public List<PName> getFilters() {
		PObject filter = this.dict.get(PName.Filter);
		if (null == filter) {
			return new ArrayList<PName>(0);
		}
		List<PName> filters = new ArrayList<PName>();
		if (filter instanceof PName) {
			filters.add((PName) filter);
		} else if (filter instanceof PArray) {
			PArray arr = (PArray) filter;
			for (int i = 0; i < arr.size(); i++) {
				filters.add((PName) arr.get(i));				
			}
		}
		return filters;
	}
	
	public void setStream(byte[] stream) {
		this.stream = stream;
		
		this.dict.remove(PName.Length);
		PNumber len = new PNumber(this.stream.length);
		this.dict.put(PName.Length, len);
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		if (null != this.dict) {
			buf.append(this.dict.toString());
		}
		buf.append("stream");
		buf.append('\n');
		
		byte[] out = null;
		try {
			out = this.getDecodedStream();	
		} catch (Exception e) {
			// some stream need concatenate to previous stream
		}
		 
		if (out != null) {
			buf.append(new String(out));	
		} else {
			buf.append("Can't filter this stream: ");
			buf.append('\n');
			buf.append(new String(this.getStream()));
		}
		
		buf.append('\n');
		
		buf.append("endstream");
		buf.append('\n');
		return buf.toString();
	}
}
