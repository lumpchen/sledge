package me.lumpchen.sledge.pdf.text.font;

import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.lang.PArray;
import me.lumpchen.sledge.pdf.syntax.lang.PName;
import me.lumpchen.sledge.pdf.syntax.lang.PNumber;
import me.lumpchen.sledge.pdf.syntax.lang.PObject;
import me.lumpchen.sledge.pdf.syntax.lang.PString;
import me.lumpchen.sledge.pdf.text.font.encoding.Encoding;

public class PDFFontEncoding {

	public static final int STANDARD_ENCODING = 0;
	public static final int CMAP = 1;

	private int type;
	private PDFCMap cmap;

	private Encoding baseEncoding;
	private Map<Integer, String> differences;

	public PDFFontEncoding(String fontType, PName encodingName) {
		if (fontType.equalsIgnoreCase(PDFFont.Type_0)) {
			this.type = CMAP;
			this.cmap = PDFCMap.getCMap(encodingName);
		} else {
			this.type = STANDARD_ENCODING;
			this.baseEncoding = this.getBaseEncoding(encodingName);
		}
	}

	public PDFFontEncoding(String fontType, IndirectObject encodingObj) {
		PName encodingType = encodingObj.getValueAsName(PName.Type);
		if (encodingType.equals(PName.Encoding)) {
			this.type = STANDARD_ENCODING;
			this.parseEncoding(encodingObj);
		} else if (encodingType.equals(PName.CMap)) {
			this.type = CMAP;
			this.cmap = PDFCMap.getCMap(encodingObj.getStream());
		} else {
			throw new IllegalArgumentException("Unexpected type: " + encodingObj.toString());
		}
	}

	public int getType() {
		return this.type;
	}

	public int getGlyphID(int cid) {
		if (this.type == STANDARD_ENCODING) {
			if (this.differences != null) {
				String glyphName = this.differences.get(cid);
				if (glyphName != null) {

				}
			}
			if (this.baseEncoding != null) {
			}
		} else {
			if (this.cmap != null) {
				return this.cmap.map(cid);
			}
		}

		return 0;
	}

	private void parseEncoding(IndirectObject encodingObj) {
		PName encodingName = encodingObj.getValueAsName(PName.BaseEncoding);
		if (encodingName != null) {
			this.baseEncoding = this.getBaseEncoding(encodingName);
		}

		PArray differencesArr = encodingObj.getValueAsArray(PName.Differences);
		if (differencesArr != null) {
			this.differences = new HashMap<Integer, String>();
			int size = differencesArr.size();
			int pos = -1;
			for (int i = 0; i < size; i++) {
				PObject item = differencesArr.get(i);
				if (item.getClassType() == PObject.ClassType.Number) {
					pos = ((PNumber) item).intValue();
				} else if (item.getClassType() == PObject.ClassType.String) {
					this.differences.put(pos, ((PString) item).toJavaString());
					pos++;
				} else if (item.getClassType() == PObject.ClassType.Name) {
					this.differences.put(pos, ((PName) item).toString());
					pos++;
				} else {
					throw new IllegalArgumentException("Unexpected type in differences: "
							+ item.toString());
				}
			}
		}
	}

	private Encoding getBaseEncoding(PName encodingName) {
		return null;
	}
}
