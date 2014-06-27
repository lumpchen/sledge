package com.docscience.pathfinder.font.driver.afp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.docscience.pathfinder.font.driver.encoding.Encoding;

/**
 * @author wxin
 *
 */
public class AFPCodePage {
	
	private static final int LINEDATA_MARKER = 0x5A;
	
	private List<MODCAStructureField> structureFields = new LinkedList<MODCAStructureField>();
	
	private SFBeginCodePage sfBeginCodePage;
	private SFEndCodePage sfEndCodePage;
	private SFCodePageDescriptor sfCodePageDescriptor;
	private SFCodePageControl sfCodePageControl;
	private SFCodePageIndex sfCodePageIndex;
	
	public SFBeginCodePage getSFBeginCodePage() {
		return sfBeginCodePage;
	}

	public void setSFBeginCodePage(SFBeginCodePage sfBeginCodePage) {
		this.sfBeginCodePage = sfBeginCodePage;
	}

	public SFEndCodePage getSFEndCodePage() {
		return sfEndCodePage;
	}

	public void setSFEndCodePage(SFEndCodePage sfEndCodePage) {
		this.sfEndCodePage = sfEndCodePage;
	}

	public SFCodePageDescriptor getSFCodePageDescriptor() {
		return sfCodePageDescriptor;
	}

	public void setSFCodePageDescriptor(SFCodePageDescriptor sfCodePageDescriptor) {
		this.sfCodePageDescriptor = sfCodePageDescriptor;
	}

	public SFCodePageControl getSFCodePageControl() {
		return sfCodePageControl;
	}

	public void setSFCodePageControl(SFCodePageControl sfCodePageControl) {
		this.sfCodePageControl = sfCodePageControl;
	}

	public SFCodePageIndex getSFCodePageIndex() {
		return sfCodePageIndex;
	}

	public void setSFCodePageIndex(SFCodePageIndex sfCodePageIndex) {
		this.sfCodePageIndex = sfCodePageIndex;
	}

	public void read(InputStream in) throws IOException {
		while (in.read() == LINEDATA_MARKER) {
			int hiByte = in.read() & 0xff;
			int loByte = in.read() & 0xff;
			int length = (hiByte << 8) | loByte;
			byte[] temp = new byte[length];
			temp[0] = (byte) hiByte;
			temp[1] = (byte) loByte;
			length -= 2;
			while (length > 0) {
				int rd = in.read(temp, temp.length - length, length);
				if (rd == -1) {
					break;
				}
				length -= rd;
			}
			MODCAByteBuffer buffer = new MODCAByteBuffer(temp);
			MODCAStructureField field = new MODCAStructureField();
			field.load(buffer, 0);
			switch(field.getSFID()) {
			case MODCAStructureField.ID_BCP:
				parseBCP(field);
				break;
			case MODCAStructureField.ID_ECP:
				parseECP(field);
				break;
			case MODCAStructureField.ID_CPC:
				parseCPC(field);
				break;
			case MODCAStructureField.ID_CPD:
				parseCPD(field);
				break;
			case MODCAStructureField.ID_CPI:
				parseCPI(field);
				break;
			default:
				// do nothing here
			}
			structureFields.add(field);
		}
	}
	
	private void parseCPI(MODCAStructureField field) {
		sfCodePageIndex = new SFCodePageIndex(sfCodePageControl.getCPIRepeatingGroupLength());
		sfCodePageIndex.setStructureField(field);
	}

	private void parseCPD(MODCAStructureField field) {
		sfCodePageDescriptor = new SFCodePageDescriptor();
		sfCodePageDescriptor.setStructureField(field);
	}

	private void parseCPC(MODCAStructureField field) {
		sfCodePageControl = new SFCodePageControl();
		sfCodePageControl.setStructureField(field);
	}

	private void parseECP(MODCAStructureField field) {
		sfEndCodePage = new SFEndCodePage();
		sfEndCodePage.setStructureField(field);
	}

	private void parseBCP(MODCAStructureField field) {
		sfBeginCodePage = new SFBeginCodePage();
		sfBeginCodePage.setStructureField(field);
	}

	public void write(OutputStream out) throws IOException {
		pack();
		Iterator<MODCAStructureField> itr = structureFields.iterator();
		while (itr.hasNext()) {
			out.write(LINEDATA_MARKER);
			MODCAStructureField field = itr.next();
			MODCAByteBuffer buffer = new MODCAByteBuffer();
			field.save(buffer);
			out.write(buffer.getBytes());
		}
	}
	
	private void pack() {
		sfCodePageControl.setCPIRepeatingGroupLength(sfCodePageIndex.getRepeatingGroupLength());
		
		int sequenceNumber = 1;
		Iterator<MODCAStructureField> itr = structureFields.iterator();
		while (itr.hasNext()) {
			MODCAStructureField field = itr.next();
			field.setSFSequenceNumber(sequenceNumber++);
		}
	}

	public String getGraphicCharacterGID(int codePoint) {
		String gcgid = sfCodePageIndex.findGraphicCharacterGID(codePoint);
		if (gcgid == null) {
			return sfCodePageControl.getDefaultGraphicCharacterGID();
		}
		return gcgid;
	}
	
	public Map<Integer, Integer> getUnicodeToCodepointMap() {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i=0; i<sfCodePageIndex.getNumEntries(); ++i) {
			SFCodePageIndex.Entry entry = sfCodePageIndex.getEntry(i);
			int codepoint = entry.getCodepoint();
			String gcgid = entry.getGraphicCharacterGID();
			int unicode = GCGIDDatabase.getUnicode(gcgid);
			if (unicode >= 0) {
				map.put(new Integer(unicode), new Integer(codepoint));
			}
		}
		return map;
	}
	
	public Map<Integer, Integer> getCodepointToUnicodeMap() {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i=0; i<sfCodePageIndex.getNumEntries(); ++i) {
			SFCodePageIndex.Entry entry = sfCodePageIndex.getEntry(i);
			int codepoint = entry.getCodepoint();
			String gcgid = entry.getGraphicCharacterGID();
			int unicode = GCGIDDatabase.getUnicode(gcgid);
			if (unicode >= 0) {
				map.put(new Integer(codepoint), new Integer(unicode));
			}
		}
		return map;
	}

	private static class MappedEncoding implements Encoding {

		private Map<Integer, Integer> codepointToUnicodeMap;
		private Map<Integer, Integer> unicodeToCodepointMap;
		
		public MappedEncoding(AFPCodePage codepage) {
			codepointToUnicodeMap = codepage.getCodepointToUnicodeMap();
			unicodeToCodepointMap = codepage.getUnicodeToCodepointMap();
		}
		
		@Override
		public String getCharacterName(int codepoint) {
			Integer unicode = codepointToUnicodeMap.get(new Integer(codepoint));
			if (unicode != null) {
				return GCGIDDatabase.getGCGID(unicode.intValue());
			}
			return null;
		}

		@Override
		public String getGlyphName(int codepoint) {
			return getCharacterName(codepoint);
		}

		@Override
		public int getCodePoint(int unicode) {
			Integer codepoint = (Integer) unicodeToCodepointMap.get(new Integer(unicode));
			if (codepoint != null) {
				return codepoint.intValue();
			}
			return 0xFFFF;
		}

		@Override
		public int getMaxCodePoint() {
			return 255;
		}

		@Override
		public int getMinCodePoint() {
			return 0;
		}

		@Override
		public int getUnicode(int codepoint) {
			Integer unicode = codepointToUnicodeMap.get(new Integer(codepoint));
			if (unicode != null) {
				return unicode.intValue();
			}
			return 0xFFFF;
		}

		@Override
		public boolean isDefinedCodePoint(int codepoint) {
			return codepointToUnicodeMap.containsKey(new Integer(codepoint));
		}
		
	}
	
	public Encoding getSingleByteEncoding() {
		return new MappedEncoding(this);
	}
	
}
