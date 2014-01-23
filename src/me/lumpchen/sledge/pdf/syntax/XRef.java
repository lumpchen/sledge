package me.lumpchen.sledge.pdf.syntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.lumpchen.sledge.pdf.reader.BytesReader;
import me.lumpchen.sledge.pdf.reader.LineData;
import me.lumpchen.sledge.pdf.reader.LineReader;
import me.lumpchen.sledge.pdf.reader.NotMatchObjectException;
import me.lumpchen.sledge.pdf.syntax.XRefStream.WEntry;
import me.lumpchen.sledge.pdf.syntax.basic.PStream;

public class XRef {

	public static final String begin = "xref";

	private int currSubSectionNo;
	private int currSubSectionCount;
	private List<Section> sectionList;
	private Map<Integer, XRefEntry> entryMap;
	private int size;

	public XRef() {
		this.sectionList = new ArrayList<Section>();
		this.entryMap = new HashMap<Integer, XRefEntry>();
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("xref");
		buf.append('\n');
		
		for (int i = 0; i < this.size; i++) {
			if (!this.entryMap.containsKey(i)) {
				continue;
			}
			XRefEntry entry = this.entryMap.get(i);
			if (entry.free) {
				continue;
			}
			buf.append(entry.toString());
			buf.append('\n');
		}

//		for (Section section : this.sectionList) {
//			int sectionNo = section.sectionNo;
//			int sectionCount = section.count;
//			buf.append(sectionNo + " " + sectionCount);
//			buf.append('\n');
//
//			for (int i = sectionNo, n = sectionNo + sectionCount; i < n; i++) {
//				XRefEntry entry = this.entryMap.get(i);
//				if (entry.free) {
//					continue;
//				}
//				buf.append(entry.toString());
//				buf.append('\n');
//			}
//		}

		return buf.toString();
	}

	public List<XRefEntry> getEntryList() {
		List<XRefEntry> list = new ArrayList<XRefEntry>();
		for (int i = 0; i < this.size; i++) {
			XRefEntry entry = this.entryMap.get(i);
			list.add(entry);
		}
		return list;
	}
	
	public XRefEntry getRefEntry(IndirectRef ref) {
		int objNum = ref.getObjNum();
		int genNum = ref.getGenNum();

		return this.getRefEntry(objNum, genNum);
	}
	
	private XRefEntry getRefEntry(int objNum, int genNum) {
		XRefEntry entry = this.entryMap.get(objNum);
		if (null == entry) {
			throw new NotMatchObjectException(objNum + " " + genNum + " R");
		}

		if (entry.genNum != genNum) {
			throw new NotMatchObjectException(objNum + " " + genNum + " R");
		}
		
		return entry;
	}
	
	public void addSubSection(Section subSection) {
		for (int i = 0; i < this.sectionList.size(); i++) {
			Section prev = this.sectionList.get(i);
			if (prev.sectionNo + prev.count >= subSection.sectionNo) {
				this.sectionList.add(i + 1, subSection);
				return;
			}
		}
		
		this.sectionList.add(subSection);
	}
	
	public void addEntry(XRefEntry entry) {
		if (!this.entryMap.containsKey(entry.objNum)) {
			this.entryMap.put(entry.objNum, entry);
			this.size++;
		} else {
			if (this.entryMap.get(entry.objNum).free) {
				// override free entry
				this.entryMap.put(entry.objNum, entry);	
			}
		}
	}
	
	public boolean read(LineReader reader) {
		boolean found = false;
		List<LineData> lineArr = new ArrayList<LineData>();
		while (true) {
			LineData line = reader.readLine();
			if (line == null) {
				break;
			}
			if (line.startsWith(begin)) {
				found = true;
			} 
			
			if (line.startsWith(Trailer.STARTXREF)) {
				break;
			}
			
			lineArr.add(line);
		}
			
		if (!found) {
			return false;
		} else {
			boolean xrefBegin = false;
			for (int i = 0, n = lineArr.size(); i < n;  i++) {
				LineData line = lineArr.get(i);
				
				if (line.startsWith(Trailer.TRAILER)) {
					break;
				}
					
				if (line.startsWith(XRef.begin)) {
					xrefBegin = true;
					continue;
				}
				
				if (!xrefBegin) {
					continue;
				}
				
				if (line.getBytes().length < 16) {
					this.readSectionEntry(line);
					continue;
				}

				this.readEntry(line);
			}
			
			return true;
		}
	}

	private void readSectionEntry(LineData line) {
		BytesReader bytesReader = new BytesReader(line.getBytes());
		this.currSubSectionNo = bytesReader.readInt();
		this.currSubSectionCount = bytesReader.readInt();
		Section subSection = new Section();
		subSection.sectionNo = this.currSubSectionNo;
		subSection.count = this.currSubSectionCount;
		this.addSubSection(subSection);
	}

	private void readEntry(LineData line) {
		BytesReader bytesReader = new BytesReader(line.getBytes());

		XRefEntry entry = new XRefEntry();
		entry.offset = bytesReader.readLong();
		entry.genNum = bytesReader.readInt();
		byte inuse = bytesReader.readByte();
		entry.free = 'n' == inuse ? false : true;
		entry.objNum = this.currSubSectionNo++;

		this.addEntry(entry);
	}

	public static class Section {
		int sectionNo;
		int count;
		
		public boolean inRange(int start) {
			if (start > sectionNo && start < sectionNo + count) {
				return true;
			}
			return false;
		}
	}

	public static class XRefEntry {

		public long offset = -1;
		public int objNum = 0;
		public int genNum = 0;
		public boolean free; // or in-use (f/n)
		
		public boolean inObjectStream = false;
		public int objStreamNum = -1;
		public int objIndex = -1;

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
	
	public void readStream(PStream stream) {
		XRefStream xrefstm = new XRefStream(stream);
		
		int size = xrefstm.getIndexCount();
		for (int i = 0; i < size; i++) {
			int start = xrefstm.getIndex(i)[0];
			int count = xrefstm.getIndex(i)[1];
			
			Section section = new Section();
			section.sectionNo = start;
			section.count = count;
			
			this.addSubSection(section);
			
			for (int j = start; j < start + count; j++) {
				WEntry we = xrefstm.getEntry(j);
				if (we.type == WEntry.TYPE_2) {
					XRefEntry e = new XRefEntry();
					e.inObjectStream = true;
					e.objNum = j;
					
					e.objStreamNum = we.objNum;
					e.objIndex = we.index;
					e.free = false;
					this.addEntry(e);
				} else if (we.type == WEntry.TYPE_1) {
					XRefEntry e = new XRefEntry();
					e.inObjectStream = false;
					e.objNum = j;
					
					e.offset = we.offset;
					e.genNum = we.genNum;
					e.free = false;
					this.addEntry(e);
				} else if (we.type == WEntry.TYPE_0) {
					XRefEntry e = new XRefEntry();
					e.inObjectStream = false;
					e.objNum = j;

					e.genNum = we.genNum;
					e.free = true;
					this.addEntry(e);
				}
			}
		}
	}
}
