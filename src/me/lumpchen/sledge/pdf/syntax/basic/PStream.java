package me.lumpchen.sledge.pdf.syntax.basic;

import java.util.ArrayList;
import java.util.List;

import me.lumpchen.sledge.pdf.syntax.SyntaxException;
import me.lumpchen.sledge.pdf.syntax.decoder.DecoderChain;


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
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		if (null != this.dict) {
			buf.append(this.dict.toString());
		}
		buf.append("stream");
		buf.append('\n');
		
		DecoderChain chain = new DecoderChain();
		byte[] out = chain.decode(this);
		buf.append(new String(out));
		buf.append('\n');
		
		buf.append("endstream");
		buf.append('\n');
		return buf.toString();
	}
}
