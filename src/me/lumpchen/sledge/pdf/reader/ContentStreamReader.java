package me.lumpchen.sledge.pdf.reader;

import me.lumpchen.sledge.pdf.graphics.GraphicsOperand;
import me.lumpchen.sledge.pdf.graphics.GraphicsOperator;
import me.lumpchen.sledge.pdf.syntax.ContentStream;
import me.lumpchen.sledge.pdf.syntax.PObject;

public class ContentStreamReader {

	public ContentStreamReader() {
	}

	public ContentStream read(byte[] stream) {
		ContentStream contentStream = new ContentStream();
		LineReader lineReader = new LineReader(new LineData(stream));
		ObjectReader objReader = new ObjectReader(lineReader);
		
		while (true) {
			PObject next = objReader.readNextObj();
			if (next == null) {
				break;
			}
			
			if (next instanceof GraphicsOperator) {
				contentStream.pushOperator((GraphicsOperator) next);
			} else {
				contentStream.pushOperand(new GraphicsOperand(next));
			}
		}
		
		return contentStream;
	}
}