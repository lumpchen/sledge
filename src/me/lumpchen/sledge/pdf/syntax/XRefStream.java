package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.basic.PStream;
import me.lumpchen.sledge.pdf.syntax.decoder.DecoderChain;

public class XRefStream {
	
	private int size;
	private int start;
	private int count;
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
		if (arr != null) {
			if (arr.size() != 2) {
				throw new SyntaxException("invalid /Index entry: " + arr.toString());
			}
			PObject obj = arr.get(0);
			if (!(obj instanceof PNumber)) {
				throw new SyntaxException("invalid /Index entry: " + obj.toString());	
			}
			this.start = ((PNumber) obj).intValue();
			obj = arr.get(1);
			if (!(obj instanceof PNumber)) {
				throw new SyntaxException("invalid /Index entry: " + obj.toString());	
			}
			this.count = ((PNumber) obj).intValue();
		} else {
			this.start = 0;
			this.count = this.size;
		}
		
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
				this.entries[pos][j] = out[i++] & 0xFF;
			}
			pos++;
		}
	}
	
	public int getStart() {
		return this.start;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public WEntry getEntry(int index) {
		if (index < this.start || index > this.start + this.count) {
			throw new SyntaxException("out of range: " + index);
		}
		int pos = index - this.start;
		
		int[] entry = this.entries[pos];
		
		int f1, f2, f3;
		if (entry.length == 4) {
			f1 = entry[0];
			f2 = ((entry[1] & 0x000000FF) << 8) | (entry[2] & 0xFF);
			f3 = entry[3];
		} else {
			f1 = entry[0];
			f2 = entry[1];
			f3 = entry[2];
		}
		return new WEntry(f1, f2, f3);
	}
	
	public static class WEntry {
		public static final int TYPE_0 = 0;
		public static final int TYPE_1 = 1;
		public static final int TYPE_2 = 2;

		public int type = -1;
		public int offset = -1;
		public int objNum = -1;
		public int genNum = -1;
		public int index = -1;
		
		public WEntry(int field_1, int field_2, int field_3) {
			if (field_1 == TYPE_0) {
				this.type = TYPE_0;
				this.objNum = field_2;
				this.genNum = field_3;
			} else if (field_1 == TYPE_1) {
				this.type = TYPE_1;
				this.offset = field_2;
				this.genNum = field_3;
			} else if (field_1 == TYPE_2) {
				this.type = TYPE_2;
				this.objNum = field_2;
				this.index = field_3;				
			}
		}
	}
}
