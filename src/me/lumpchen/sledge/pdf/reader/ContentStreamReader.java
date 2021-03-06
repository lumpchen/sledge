package me.lumpchen.sledge.pdf.reader;

import me.lumpchen.sledge.pdf.graphics.GraphicsOperand;
import me.lumpchen.sledge.pdf.graphics.operator.GraphicsOperator;
import me.lumpchen.sledge.pdf.syntax.ContentStream;
import me.lumpchen.sledge.pdf.syntax.lang.PObject;

public class ContentStreamReader {

	public ContentStreamReader() {
	}

	public ContentStream read(byte[] stream) {
		ContentStream contentStream = new ContentStream();
		ObjectReader objReader = new ObjectReader(stream);
		
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
		
		contentStream.matchOperands();
		return contentStream;
	}
}
