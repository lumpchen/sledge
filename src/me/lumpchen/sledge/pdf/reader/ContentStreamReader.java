package me.lumpchen.sledge.pdf.reader;

import java.util.ArrayList;
import java.util.List;

import me.lumpchen.sledge.pdf.graphics.GraphicsOperand;
import me.lumpchen.sledge.pdf.graphics.GraphicsOperator;
import me.lumpchen.sledge.pdf.syntax.ContentStream;

public class ContentStreamReader {

	public ContentStreamReader() {
	}

	public ContentStream read(byte[] stream) {
		LineReader lineReader = new LineReader(new LineData(stream));
		
		ContentStream contentStream = new ContentStream();
		
		while (true) {
			LineData line = lineReader.readLine();
			if (null == line) {
				break;
			}
			BytesReader bReader = new BytesReader(line.getBytes());
			
			List<byte[]> words = new ArrayList<byte[]>();
			
			while (true) {
				byte[] word = bReader.readToSpace();
				if (null == word) {
					break;
				}
				words.add(0, word);
			}
			
			if (words.size() == 0) {
				continue;
			}
			if (words.size() == 1) {
				GraphicsOperator operator = GraphicsOperator.create(words.get(0));
				contentStream.pushOperator(operator);
			} else {
				GraphicsOperator operator = GraphicsOperator.create(words.get(0));
				contentStream.pushOperator(operator);
				
				for (int i = 1, n = words.size(); i < n; i++) {
					GraphicsOperand operand = new GraphicsOperand(words.get(i));
					contentStream.pushOperand(operand);
				}
			}
		}
		
		return contentStream;
	}
}
