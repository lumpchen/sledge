package me.lumpchen.sledge.pdf.text.font;

import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.lang.PArray;
import me.lumpchen.sledge.pdf.syntax.lang.PName;
import me.lumpchen.sledge.pdf.syntax.lang.PNumber;
import me.lumpchen.sledge.pdf.syntax.lang.PObject;
import me.lumpchen.sledge.pdf.syntax.lang.PString;

public class Type1Encoding {

	private PName baseEncodingName;
	private Map<Integer, String> differences;

	static {
		String[] MacRomanEncoding = new String[256];
		MacRomanEncoding[0] = "A";
		MacRomanEncoding[65] = "A";		
	}
	
	
	public Type1Encoding(IndirectObject encodingObj) {
		this.parseEncoding(encodingObj);
	}
	
	public PName getBaseEncodingName() {
		return this.baseEncodingName;
	}
	
	private void parseEncoding(IndirectObject encodingObj) {
		this.baseEncodingName = encodingObj.getValueAsName(PName.BaseEncoding);

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
					this.differences.put(pos, ((PString) item).toString());
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

}
