package me.lumpchen.sledge.pdf.syntax;

import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.reader.BytesReader;
import me.lumpchen.sledge.pdf.reader.LineData;
import me.lumpchen.sledge.pdf.reader.LineReader;
import me.lumpchen.sledge.pdf.reader.NotMatchObjectException;

public class PDFXRef {

	public static final String begin = "xref";

	private int currSubSectionNo;
	private int currSubSectionCount;
	private Map<Long, XRefEntry> entryMap;

	public PDFXRef() {
		entryMap = new HashMap<Long, XRefEntry>();
	}

	public String toString() {
		return null;
	}

	private void sortEntry() {
	}

	public PObject readObj(IndirectRef ref) {
		int objNum = ref.getObjNum();
		int genNum = ref.getGenNum();
		
		long key = XRefEntry.createKey(objNum, genNum);
		XRefEntry entry = this.entryMap.get(key);
		if (null == entry) {
			throw new NotMatchObjectException(ref.toString());
		}
		
		return null;
	}
	
	public void read(LineReader reader) {
		while (true) {
			LineData line = reader.readLine();
			if (line == null) {
				break;
			}
			if (line.getBytes()[0] == 'x') {
				continue;
			}
			if (line.getBytes().length < 20) {
				this.readSectionEntry(line);
				continue;
			}

			this.readEntry(line);
		}
	}

	private void readSectionEntry(LineData line) {
		BytesReader bytesReader = new BytesReader(line.getBytes());
		this.currSubSectionNo = bytesReader.readInt();
		this.currSubSectionCount = bytesReader.readInt();
	}

	private void readEntry(LineData line) {
		BytesReader bytesReader = new BytesReader(line.getBytes());

		XRefEntry entry = new XRefEntry();
		entry.offset = bytesReader.readLong();
		entry.genNum = bytesReader.readInt();
		byte inuse = bytesReader.readByte();
		entry.free = 'n' == inuse ? false : true;
		entry.objNum = ++this.currSubSectionNo;

		this.entryMap.put(entry.getKey(), entry);
	}

	static class XRefEntry {
		long offset;
		int genNum;
		boolean free; // or in-use (f/n)

		int objNum;

		long getKey() {
			return createKey(this.objNum, this.genNum);
		}
		
		static long createKey(int objNum, int genNum) {
			return objNum * 100000 + genNum;
		}
	}
}
