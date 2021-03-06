package me.lumpchen.sledge.pdf.text.font;

import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.syntax.lang.PName;
import me.lumpchen.sledge.pdf.syntax.lang.PStream;

public abstract class PDFCMap {

	private static Map<PName, PDFCMap> predefinedCMapCache = new HashMap<PName, PDFCMap>();

	protected PDFCMap() {
	}

	public static PDFCMap getCMap(PName predefinedCMap) {
		if (predefinedCMapCache.containsKey(predefinedCMap)) {
			return predefinedCMapCache.get(predefinedCMap);
		}

		loadCMap(predefinedCMap);
		return predefinedCMapCache.get(predefinedCMap);
	}

	public static PDFCMap getCMap(PStream cmapStream) {
		return null;
	}

	private static void loadCMap(PName name) {
		if (name.equals(PName.instance("Identity-H"))) {
			predefinedCMapCache.put(name, new PDFCMap() {
				public int map(int cid) {
					return cid;
				}
			});
		}
	}

	public abstract int map(int cid);
}
