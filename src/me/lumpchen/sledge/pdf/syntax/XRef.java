package me.lumpchen.sledge.pdf.syntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.lumpchen.sledge.pdf.reader.BytesReader;
import me.lumpchen.sledge.pdf.reader.LineData;
import me.lumpchen.sledge.pdf.reader.LineReader;
import me.lumpchen.sledge.pdf.reader.NotMatchObjectException;

public class XRef {

	public static final String begin = "xref";

	private int currSubSectionNo;
	private int currSubSectionCount;
	private List<Section> sectionList;
	private Map<Integer, XRefEntry> entryMap;
	private int entryCount;

	public XRef(int size) {
		this.sectionList = new ArrayList<Section>();
		this.entryMap = new HashMap<Integer, XRefEntry>();
		this.entryCount = size;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("xref");
		buf.append('\n');

		for (Section section : this.sectionList) {
			int sectionNo = section.sectionNo;
			int sectionCount = section.count;
			buf.append(sectionNo + " " + sectionCount);
			buf.append('\n');

			for (int i = sectionNo, n = sectionCount; i < n; i++) {
				XRefEntry entry = this.entryMap.get(i);
				buf.append(entry.toString());
				buf.append('\n');
			}
		}

		return buf.toString();
	}

	public XRefEntry getRefEntry(IndirectRef ref) {
		int objNum = ref.getObjNum();
		int genNum = ref.getGenNum();

		return this.getRefEntry(objNum, genNum);
	}

	private XRefEntry getRefEntry(int objNum, int genNum) {
		XRefEntry entry = this.entryMap.get(objNum);
		if (null == entry) {
			throw new NotMatchObjectException(objNum + " " + genNum + "R");
		}

		if (entry.genNum != genNum) {
			throw new NotMatchObjectException(objNum + " " + genNum + "R");
		}
		
		return entry;
	}

	public void read(LineReader reader) {
		int n = this.entryCount;
		while (true) {
			if (n <= 0) {
				break;
			}
			LineData line = reader.readLine();
			if (line == null) {
				break;
			}
			if (line.getBytes()[0] == 'x') {
				continue;
			}
			if (line.getBytes().length < 16) {
				this.readSectionEntry(line);
				continue;
			}

			this.readEntry(line);
			n--;
		}
	}

	private void readSectionEntry(LineData line) {
		BytesReader bytesReader = new BytesReader(line.getBytes());
		this.currSubSectionNo = bytesReader.readInt();
		this.currSubSectionCount = bytesReader.readInt();
		Section subSection = new Section();
		subSection.sectionNo = this.currSubSectionNo;
		subSection.count = this.currSubSectionCount;
		this.sectionList.add(subSection);
	}

	private void readEntry(LineData line) {
		BytesReader bytesReader = new BytesReader(line.getBytes());

		XRefEntry entry = new XRefEntry();
		entry.offset = bytesReader.readLong();
		entry.genNum = bytesReader.readInt();
		byte inuse = bytesReader.readByte();
		entry.free = 'n' == inuse ? false : true;
		entry.objNum = this.currSubSectionNo++;

		this.entryMap.put(entry.objNum, entry);
	}

	static class Section {
		int sectionNo;
		int count;
	}

	static class XRefEntry {
		long offset;
		int objNum;
		int genNum;
		boolean free; // or in-use (f/n)

		public String offsetString() {
			Long v = new Long(offset);
			String s = v.toString();
			StringBuilder buf = new StringBuilder();
			int zeroFill = 10 - s.length();
			while (zeroFill > 0) {
				buf.append("0");
				zeroFill--;
			}
			buf.append(s);
			return buf.toString();
		}

		public String toString() {
			StringBuilder buf = new StringBuilder();
			buf.append(this.offsetString());
			buf.append(" ");
			buf.append(this.objNum + " ");
			buf.append(this.genNum + " ");
			buf.append(free ? "f" : "n");
			return buf.toString();
		}
	}
}
