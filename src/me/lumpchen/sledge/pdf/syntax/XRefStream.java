package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.basic.PStream;
import me.lumpchen.sledge.pdf.syntax.codec.DecoderChain;

public class XRefStream {
	
	private int size;
	private int[] index;
	private int[] w;
	private int[][] entries;
	
	public XRefStream(PStream stream) {
		this.initialize(stream);
	}
	
	private void initialize(PStream stream) {
		PDictionary dict = stream.getDict();
		if (null == dict) {
			throw new SyntaxException("not found the dictionary of stream.");
		}
		
		PName type = dict.getValueAsName(PName.type);
		if (!type.equals(PName.XRef)) {
			throw new SyntaxException("invalid XRefStream type: " + type);
		}

		PArray arr = dict.getValueAsArray(PName.w);
		if (null == arr) {
			throw new SyntaxException("not found /W entry.");
		}
		this.w = new int[arr.size()];
		for (int i = 0; i < arr.size(); i++) {
			PObject obj = arr.get(i);
			if (!(obj instanceof PNumber)) {
				throw new SyntaxException("invalid /W entry: " + obj.toString());	
			}
			this.w[i] = ((PNumber) obj).intValue();
		}
		
		PNumber num = dict.getValueAsNumber(PName.size);
		if (null == num) {
			throw new SyntaxException("not found /Size");
		}
		this.size = num.intValue();
		
		arr = dict.getValueAsArray(PName.Index);
		this.index = new int[2];
		if (arr != null) {
			if (arr.size() != 2) {
				throw new SyntaxException("invalid /Index entry: " + arr.toString());
			}
			for (int i = 0; i < arr.size(); i++) {
				PObject obj = arr.get(i);
				if (!(obj instanceof PNumber)) {
					throw new SyntaxException("invalid /W entry: " + obj.toString());	
				}
				this.index[i] = ((PNumber) obj).intValue();
			}
		} else {
			index[0] = 0;
			index[1] = this.size;
		}
		
		int count = index[1];
		this.entries = new int[count][];
		
		DecoderChain chain = new DecoderChain();
		byte[] out = chain.decode(stream);
		if (null == out || out.length == 0) {
			throw new SyntaxException("the stream data is empty.");
		}

		int wlen = 0;
		for (int i = 0; i < this.w.length; i++) {
			wlen += w[i]; 
		}
		int pos = 0;
		int i = 0;
		while (i < out.length) {
			this.entries[pos] = new int[wlen];
			for (int j = 0; j < wlen; j++) {
				this.entries[pos][j] = out[i++];
			}
			pos++;
		}
	}
}
