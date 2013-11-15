package me.lumpchen.sledge.pdf.syntax;

public abstract class PObject {

	static void self_identification(byte[] data) {
		if (data == null || data.length <= 0) {
			// null object
		}
		
		byte c = data[0];
	}
}
