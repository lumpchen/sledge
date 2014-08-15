package me.lumpchen.sledge.pdf.syntax;

import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.reader.BytesReader;
import me.lumpchen.sledge.pdf.syntax.lang.PDictionary;
import me.lumpchen.sledge.pdf.syntax.lang.PName;
import me.lumpchen.sledge.pdf.syntax.lang.PNumber;
import me.lumpchen.sledge.pdf.syntax.lang.PStream;

public class ObjectStream {

	private int count;
	private int first;
	
	private byte[] data;
	
	private Map<Integer, Integer> indexTable = new HashMap<Integer, Integer>();
	
	public ObjectStream(PStream stream) {
		this.initialize(stream);
	}
	
	private void initialize(PStream stream) {
		PDictionary dict = stream.getDict();
		if (null == dict) {
			throw new SyntaxException("not found the dictionary of stream.");
		}
		
		PName type = dict.getValueAsName(PName.Type);
		if (!type.equals(PName.ObjStm)) {
			throw new SyntaxException("invalid ObjStream type: " + type);
		}
		
		PNumber n = dict.getValueAsNumber(PName.N);
		if (null == n) {
			throw new SyntaxException("not found /N entry.");
		}
		this.count = n.intValue();
		
		n = dict.getValueAsNumber(PName.First);
		if (null == n) {
			throw new SyntaxException("not found /First entry.");
		}
		this.first = n.intValue();
		
		this.data = stream.getDecodedStream();
		if (null == this.data || this.data.length == 0) {
			throw new SyntaxException("the stream data is empty.");
		}
		
		this.readIndexTable();
	}
	
	private void readIndexTable() {
		byte[] header = new byte[this.first];
		System.arraycopy(this.data, 0, header, 0, this.first);
		
		BytesReader reader = new BytesReader(header);
		while (true) {
			Integer objNum = reader.readInt();
			if (null == objNum) {
				break;
			}
			Integer offset = reader.readInt();
			if (null == offset) {
				throw new SyntaxException("index not matched");
			}
			this.indexTable.put(objNum, offset);
		}
		
		if (this.count != this.indexTable.size()) {
			throw new SyntaxException("object count not matched");
		}
	}
	
	public byte[] getContent(int index) {
		if (!this.indexTable.containsKey(index)) {
			return null;
		}
		Integer offset = this.indexTable.get(index);
		if (null == offset) {
			throw new SyntaxException("not found object: " + index);
		}
		
		int start = offset + this.first;
		
		long end = 0;
		if (this.indexTable.containsKey(index + 1)) {
			end = this.indexTable.get(index + 1) + this.first;
			if (end <= start) {
				end = this.data.length;
			}
		} else {
			end = this.data.length;
		}
		
		int objLen = (int) (end - start);
		byte[] objData = new byte[objLen];
		System.arraycopy(this.data, start, objData, 0, objLen);
		return objData;
	}
}
