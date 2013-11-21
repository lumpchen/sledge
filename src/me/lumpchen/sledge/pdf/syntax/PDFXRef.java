package me.lumpchen.sledge.pdf.syntax;

import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.reader.LineData;
import me.lumpchen.sledge.pdf.reader.LineReader;
import me.lumpchen.sledge.pdf.reader.ObjectReader;

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

	public void read(LineReader reader) {
		while (true) {
			LineData line = reader.getLine();
			if (line == null) {
				break;
			}
			if (line.getData()[0] == 'x') {
				continue;
			}
			if (line.getData().length < 20) {
				this.readSectionEntry(line);
				continue;
			}

			this.readEntry(line);
		}
	}

	private void readSectionEntry(LineData line) {
		ObjectReader objReader = new ObjectReader(line.getData());
		this.currSubSectionNo = objReader.readInt();
		this.currSubSectionCount = objReader.readInt();
	}

	private void readEntry(LineData line) {
		ObjectReader objReader = new ObjectReader(line.getData());

		XRefEntry entry = new XRefEntry();
		entry.offset = objReader.readLong();
		entry.genNum = objReader.readInt();
		byte inuse = objReader.readByte();
		entry.free = 'n' == inuse ? false : true;
		entry.objNum = ++this.currSubSectionNo;

		this.entryMap.put(entry.getKey(), entry);
	}

	class XRefEntry {
		long offset;
		int genNum;
		boolean free; // or in-use (f/n)

		int objNum;

		long getKey() {
			return objNum * 100000 + genNum;
		}
	}
}
