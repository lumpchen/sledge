package me.lumpchen.sledge.pdf.text.font;

import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.basic.PString;

public class PDFFontEncoding {

	public static final int macRomanEncoding[] = { 391, 154, 167, 140, 146, 192, 221, 197, 226,
			392, 393, 157, 162, 394, 199, 228, 395, 396, 397, 398, 399, 155, 158, 150, 163, 169,
			164, 160, 166, 168, 400, 401, 1, 2, 3, 4, 5, 6, 7, 104, 9, 10, 11, 12, 13, 14, 15, 16,
			17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38,
			39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
			61, 62, 63, 64, 124, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81,
			82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 403, 173, 175, 177, 178, 186,
			189, 195, 200, 203, 201, 202, 205, 204, 206, 207, 210, 208, 209, 211, 214, 212, 213,
			215, 216, 219, 217, 218, 220, 222, 225, 223, 224, 112, 161, 97, 98, 102, 116, 115, 149,
			165, 170, 153, 125, 131, 402, 138, 141, 404, 156, 405, 406, 100, 152, 407, 408, 409,
			410, 411, 139, 143, 412, 144, 147, 123, 96, 151, 413, 101, 414, 415, 106, 120, 121,
			416, 174, 176, 191, 142, 148, 111, 137, 105, 119, 65, 8, 159, 417, 227, 198, 99, 103,
			107, 108, 109, 110, 113, 114, 117, 118, 122, 172, 179, 171, 180, 181, 182, 183, 184,
			185, 187, 188, 418, 190, 193, 194, 196, 145, 126, 127, 128, 129, 130, 132, 133, 134,
			135, 136 };

	public static final int type1CExpertCharset[] = { 1, 229, 230, 231, 232, 233, 234, 235, 236,
			237, 238, 13, 14, 15, 99, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 27, 28,
			249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265,
			266, 109, 110, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280,
			281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 292, 293, 294, 295, 296, 297,
			298, 299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314,
			315, 316, 317, 318, 158, 155, 163, 319, 320, 321, 322, 323, 324, 325, 326, 150, 164,
			169, 327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 337, 338, 339, 340, 341, 342,
			343, 344, 345, 346, 347, 348, 349, 350, 351, 352, 353, 354, 355, 356, 357, 358, 359,
			360, 361, 362, 363, 364, 365, 366, 367, 368, 369, 370, 371, 372, 373, 374, 375, 376,
			377, 378 };

	public static final int winAnsiEncoding[] = { 124, 125, 126, 127, 128, 129, 130, 131, 132, 133,
			134, 135, 136, 145, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4,
			5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
			28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49,
			50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71,
			72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93,
			94, 95, 0, 0, 0, 117, 101, 118, 121, 112, 113, 0, 122, 192, 107, 142, 0, 0, 0, 0, 65,
			8, 105, 119, 116, 111, 137, 0, 153, 221, 108, 148, 0, 0, 198, 1, 96, 97, 98, 103, 100,
			160, 102, 131, 170, 139, 106, 151, 14, 165, 128, 161, 156, 164, 169, 125, 152, 115,
			114, 133, 150, 143, 120, 158, 155, 163, 123, 174, 171, 172, 176, 173, 175, 138, 177,
			181, 178, 179, 180, 185, 182, 183, 184, 154, 186, 190, 187, 188, 191, 189, 168, 141,
			196, 193, 194, 195, 197, 157, 149, 203, 200, 201, 205, 202, 204, 144, 206, 210, 207,
			208, 209, 214, 211, 212, 213, 167, 215, 219, 216, 217, 220, 218, 159, 147, 225, 222,
			223, 224, 226, 162, 227 };

	public static final int STANDARD_ENCODING = 0;
	public static final int CMAP = 1;

	private int type;
	private PDFCMap cmap;

	private int[] baseEncoding;
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
			if (this.baseEncoding != null) {
				if (cid < this.baseEncoding.length) {
					return this.baseEncoding[cid];
				}
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
				if (item.getType() == PObject.TYPE.Number) {
					pos = ((PNumber) item).intValue();
				} else if (item.getType() == PObject.TYPE.String) {
					this.differences.put(pos, ((PString) item).toJavaString());
					pos++;
				} else if (item.getType() == PObject.TYPE.Name) {
					this.differences.put(pos, ((PName) item).toString());
					pos++;
				} else {
					throw new IllegalArgumentException("Unexpected type in differences: " + item.toString());
				}
			}
		}
	}

	private int[] getBaseEncoding(PName encodingName) {
		if (PName.MacRomanEncoding.equals(encodingName)) {
			return macRomanEncoding;
		} else if (PName.MacExpertEncoding.equals(encodingName)) {
			return type1CExpertCharset;
		} else if (PName.WinAnsiEncoding.equals(encodingName)) {
			return winAnsiEncoding;
		} else {
			throw new IllegalArgumentException("Unknown encoding: " + encodingName);
		}
	}
}
