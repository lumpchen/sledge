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
	int[][] index;
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
		
		PName type = dict.getValueAsName(PName.Type);
		if (!type.equals(PName.XRef)) {
			throw new SyntaxException("invalid XRefStream type: " + type);
		}

		PArray arr = dict.getValueAsArray(PName.W);
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
		
		PNumber num = dict.getValueAsNumber(PName.Size);
		if (null == num) {
			throw new SyntaxException("not found /Size");
		}
		this.size = num.intValue();
		
		arr = dict.getValueAsArray(PName.Index);
		if (arr != null) {
			if (arr.size() % 2 != 0) {
				throw new SyntaxException("invalid /Index entry: " + arr.toString());
			}
			this.index = new int[arr.size() / 2][];
			int j = 0;
			for (int i = 0, n = arr.size(); i < n; i++) {
				this.index[j] = new int[2];
				
				PObject obj = arr.get(i);
				if (!(obj instanceof PNumber)) {
					throw new SyntaxException("invalid /Index entry: " + obj.toString());	
				}
				this.index[j][0] = ((PNumber) obj).intValue();	
			
				obj = arr.get(++i);
				if (!(obj instanceof PNumber)) {
					throw new SyntaxException("invalid /Index entry: " + obj.toString());	
				}
				this.index[j][1] = ((PNumber) obj).intValue();
				j++;
			}
		} else {
			this.index = new int[1][];
			this.index[0] = new int[]{0, this.size};
		}
		
		int count = 0;
		for (int i = 0; i < this.index.length; i++) {
			count += this.index[i][1];
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
	
	public int[] getIndex(int i) {
		return this.index[i];
	}
	
	public int getIndexCount() {
		return this.index.length;
	}
	
	public WEntry getEntry(int genNum) {
		int start = -1;
		int pos = 0;
		for (int i = 0; i < this.index.length; i++) {
			int[] section = this.index[i];
			
			if (genNum >= section[0] && genNum < section[0] + section[1]) {
				start = section[0];
				break;
			} else {
				pos += section[1];
			}
		}
		
		if (start < 0) {
			throw new SyntaxException("out of range: " + index);
		}
		
		pos = genNum - start + pos;
		
		int[] entry = this.entries[pos];
		
		int f[] = new int[this.w.length];
		int j = 0;
		for (int i = 0; i < this.w.length; i++) {
			int m = this.w[i];
			
			if (m == 0) {
				f[i] = 0;
			} else if (m == 1) {
				f[i] = 0xFF & entry[j];
			} else if (m == 2) {
				f[i] = ((entry[j] & 0x000000FF) << 8) | (entry[j + 1] & 0xFF);
			} else if (m == 3) {
				f[i] = ((entry[j] & 0x000000FF) << 16) | ((entry[j + 1] & 0x000000FF) << 8) | (entry[j + 2] & 0xFF);
			} else if (m == 4) {
				f[i] = ((entry[j] & 0x000000FF) << 24) | ((entry[j + 1] & 0x000000FF) << 16) 
						| ((entry[j + 2] & 0x000000FF) << 8) | (entry[j + 3] & 0xFF);
			}
			
			j += m;
		}
		return new WEntry(f[0], f[1], f[2]);
	}
	
	public static class WEntry {
		public static final int TYPE_0 = 0;
		public static final int TYPE_1 = 1;
		public static final int TYPE_2 = 2;

		public int type = -1;
		public long offset = -1;
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
				this.offset = (0xFFFFFFFFL & field_2);
				this.genNum = field_3;
			} else if (field_1 == TYPE_2) {
				this.type = TYPE_2;
				this.objNum = field_2;
				this.index = field_3;				
			}
		}
	}
}
