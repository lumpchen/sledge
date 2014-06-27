package com.docscience.pathfinder.font.driver.afp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wxin
 *
 */
public class AFPCharacterSet {
	
	private static final int LINEDATA_MARKER = 0x5A;
	
	private List<MODCAStructureField> structureFields = new LinkedList<MODCAStructureField>();
	
	private SFBeginFont sfBeginFont;
	private SFEndFont sfEndFont;
	private SFFontDescriptor sfFontDescriptor;
	private SFFontControl sfFontControl;
	private SFFontOrientation sfFontOrientation;
	private List<SFFontIndex> sfFontIndexList = new LinkedList<SFFontIndex>();
	private SFFontPosition sfFontPosition;
	private SFFontNameMap sfFontNameMap;
	private SFFontPatterns sfFontPatterns;
	private SFFontPatternMap sfFontPatternMap;
	
	private List<MODCAStructureField> fnnStructureFields = new LinkedList<MODCAStructureField>();
	private List<MODCAStructureField> fngStructureFields = new LinkedList<MODCAStructureField>();
	
	public int getNumFontIndexes() {
		return sfFontIndexList.size();
	}
	
	public SFFontIndex getFontIndex(int index) {
		return (SFFontIndex) sfFontIndexList.get(index);
	}
	
	public void addFontIndex(SFFontIndex index) {
		assert(index.getRepeatingGroupLength() == sfFontControl.getFNIRepeatingGroupLength());
		sfFontIndexList.add(index);
	}
	
	public SFBeginFont getSFBeginFont() {
		return sfBeginFont;
	}

	public void setSFBeginFont(SFBeginFont sfBeginFont) {
		this.sfBeginFont = sfBeginFont;
	}

	public SFEndFont getSFEndFont() {
		return sfEndFont;
	}

	public void setSFEndFont(SFEndFont sfEndFont) {
		this.sfEndFont = sfEndFont;
	}

	public SFFontDescriptor getSFFontDescriptor() {
		return sfFontDescriptor;
	}

	public void setSFFontDescriptor(SFFontDescriptor sfFontDescriptor) {
		this.sfFontDescriptor = sfFontDescriptor;
	}

	public SFFontControl getSFFontControl() {
		return sfFontControl;
	}

	public void setSFFontControl(SFFontControl sfFontControl) {
		this.sfFontControl = sfFontControl;
	}

	public SFFontOrientation getSFFontOrientation() {
		return sfFontOrientation;
	}

	public void setSFFontOrientation(SFFontOrientation sfFontOrientation) {
		this.sfFontOrientation = sfFontOrientation;
	}

	public SFFontPosition getSFFontPosition() {
		return sfFontPosition;
	}

	public void setSFFontPosition(SFFontPosition sfFontPosition) {
		this.sfFontPosition = sfFontPosition;
	}

	public SFFontNameMap getSFFontNameMap() {
		return sfFontNameMap;
	}

	public void setSFFontNameMap(SFFontNameMap sfFontNameMap) {
		this.sfFontNameMap = sfFontNameMap;
	}

	public SFFontPatterns getSFFontPatterns() {
		return sfFontPatterns;
	}

	public void setSFFontPatterns(SFFontPatterns sfFontPatterns) {
		this.sfFontPatterns = sfFontPatterns;
	}

	public SFFontPatternMap getSFFontPatternMap() {
		return sfFontPatternMap;
	}

	public void setSFFontPatternMap(SFFontPatternMap sfFontPatternMap) {
		this.sfFontPatternMap = sfFontPatternMap;
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
			case MODCAStructureField.ID_BFN:
				parseBFN(field);
				break;
			case MODCAStructureField.ID_EFN:
				parseEFN(field);
				break;
			case MODCAStructureField.ID_FND:
				parseFND(field);
				break;
			case MODCAStructureField.ID_FNC:
				parseFNC(field);
				break;
			case MODCAStructureField.ID_FNO:
				parseFNO(field);
				break;
			case MODCAStructureField.ID_FNI:
				parseFNI(field);
				break;
			case MODCAStructureField.ID_FNP:
				parseFNP(field);
				break;
			case MODCAStructureField.ID_FNG:
				parseFNG(field);
				break;
			case MODCAStructureField.ID_FNN:
				parseFNN(field);
				break;
			case MODCAStructureField.ID_FNM:
				parseFNM(field);
				break;
			default:
				// do nothing here
			}
			structureFields.add(field);
		}
		finishFNN();
		finishFNG();
	}
	
	private void parseFNC(MODCAStructureField field) {
		sfFontControl = new SFFontControl();
		sfFontControl.setStructureField(field);
	}

	private void parseFND(MODCAStructureField field) {
		sfFontDescriptor = new SFFontDescriptor();
		sfFontDescriptor.setStructureField(field);
	}

	private void parseEFN(MODCAStructureField field) {
		sfEndFont = new SFEndFont();
		sfEndFont.setStructureField(field);
	}

	private void parseBFN(MODCAStructureField field) {
		sfBeginFont = new SFBeginFont();
		sfBeginFont.setStructureField(field);
	}

	private void parseFNO(MODCAStructureField field) {
		sfFontOrientation = new SFFontOrientation();
		sfFontOrientation.setStructureField(field);
	}
	
	private void parseFNI(MODCAStructureField field) {
		SFFontIndex sfFontIndex = new SFFontIndex(sfFontControl.getFNIRepeatingGroupLength());
		sfFontIndex.setStructureField(field);
		sfFontIndexList.add(sfFontIndex);
	}
	
	private void parseFNP(MODCAStructureField field) {
		sfFontPosition = new SFFontPosition();
		sfFontPosition.setStructureField(field);
	}
	
	private void parseFNN(MODCAStructureField field) {
		fnnStructureFields.add(field);
	}
	
	private void parseFNG(MODCAStructureField field) {
		fngStructureFields.add(field);
	}

	private void parseFNM(MODCAStructureField field) {
		sfFontPatternMap = new SFFontPatternMap();
		sfFontPatternMap.setStructureField(field, null);
	}
	
	private void finishFNN() {
		sfFontNameMap = new SFFontNameMap();
		sfFontNameMap.setNameCount(sfFontControl.getFNNNameCount());
		sfFontNameMap.setDataCount(sfFontControl.getFNNDataCount());
		sfFontNameMap.setRepeatingGroupLength(sfFontControl.getFNNRepeatingGroupLength());
		sfFontNameMap.setStructureFields(
				fnnStructureFields.toArray(
						new MODCAStructureField[fnnStructureFields.size()]));
	}
	
	private void finishFNG() {
		sfFontPatterns = new SFFontPatterns(sfFontControl.getPatternTech());
		sfFontPatterns.setStructureFields(
				fngStructureFields.toArray(
						new MODCAStructureField[fngStructureFields.size()]));
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
		MODCAStructureField[] fontPatternsFields = sfFontPatterns.getStructureFields();
		if (sfFontNameMap != null) {
			sfFontNameMap.pack();
			sfFontControl.setFNNDataCount(sfFontNameMap.getDataCount());
			sfFontControl.setFNNNameCount(sfFontNameMap.getNameCount());
			sfFontControl.setFNNRepeatingGroupLength(sfFontNameMap.getRepeatingGroupLength());
			int patternDataCount = 0;
			for (int i=0; i<fontPatternsFields.length; ++i) {
				patternDataCount += fontPatternsFields[i].getSFParameterLength();
			}
			sfFontControl.setOutlinePatternDataCount(patternDataCount);
		} else {
			sfFontControl.setFNNDataCount(0);
			sfFontControl.setFNNNameCount(0);
			sfFontControl.setFNNRepeatingGroupLength(0);
			sfFontControl.setOutlinePatternDataCount(0);
		}
		
		structureFields.clear();
		structureFields.add(sfBeginFont.getStructureField());
		structureFields.add(sfFontDescriptor.getStructureField());
		structureFields.add(sfFontControl.getStructureField());
		if (sfFontPatternMap != null) {
			structureFields.add(sfFontPatternMap.getStructureField());
		}
		structureFields.add(sfFontOrientation.getStructureField());
		structureFields.add(sfFontPosition.getStructureField());
		
		Iterator<SFFontIndex> fiItr = sfFontIndexList.iterator();
		while (fiItr.hasNext()) {
			SFFontIndex fontIndex = fiItr.next();
			structureFields.add(fontIndex.getStructureField());
		}
		
		if (sfFontNameMap != null) {
			MODCAStructureField[] fontNameFields = sfFontNameMap.getStructureFields();
			for (int i=0; i<fontNameFields.length; ++i) {
				structureFields.add(fontNameFields[i]);
			}
		}
		
		for (int i=0; i<fontPatternsFields.length; ++i) {
			structureFields.add(fontPatternsFields[i]);
		}
		structureFields.add(sfEndFont.getStructureField());
		
		int sequenceNumber = 1;
		Iterator<MODCAStructureField> sfItr = structureFields.iterator();
		while (sfItr.hasNext()) {
			MODCAStructureField field = sfItr.next();
			field.setSFSequenceNumber(sequenceNumber++);
		}
	}
	
}
