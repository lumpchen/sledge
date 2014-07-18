package me.lumpchen.sledge.pdf.text.font;

import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.basic.PString;

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
				if (item.getType() == PObject.TYPE.Number) {
					pos = ((PNumber) item).intValue();
				} else if (item.getType() == PObject.TYPE.String) {
					this.differences.put(pos, ((PString) item).toJavaString());
					pos++;
				} else if (item.getType() == PObject.TYPE.Name) {
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
