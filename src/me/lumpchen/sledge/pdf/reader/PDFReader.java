package me.lumpchen.sledge.pdf.reader;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import me.lumpchen.sledge.pdf.syntax.PDFTrailer;

public class PDFReader {

	private static int CAPICITY = 10240;
	private SegmentedByteBuffer segmentedByteBuffer;
	private RandomAccessFile raf;
	private FileChannel fc;

	public PDFReader() {
	}

	private void close() {
		if (fc != null) {
			try {
				fc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (raf != null) {
			try {
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void read(File file) throws IOException {
		raf = new RandomAccessFile(file, "r");
		fc = raf.getChannel();
		this.segmentedByteBuffer = new SegmentedByteBuffer(fc);
		
		this.readTrailer();
	}

	private void readt() throws IOException {
		
	}
	
	private void readTrailer() throws IOException {
		ByteBuffer buf = this.segmentedByteBuffer.readTrailerBytes();
		buf.position(buf.position() + buf.remaining() - 1);
		
		List<byte[]> lineArr = new ArrayList<byte[]>();
		boolean found = false;
		while (true) {
			byte[] line = this.readLine(buf, true);
			if (line == null) {
				return;
			}
			if (this.contain(line, PDFTrailer.TRAILER)) {
				found = true;
				break;
			} else {
				lineArr.add(line);
			}
		}
		if (found) {
			PDFTrailer trailer = new PDFTrailer(); 
			for (int i = 0; i < lineArr.size(); i++) {
				if (this.contain(lineArr.get(i), PDFTrailer.STARTXREF)) {
					byte[] startxref = lineArr.get(i - 1);
					System.out.println(new String(startxref));
					trailer.setStartxref(ObjectReader.readIAsLong(startxref));
				}
			}
			
			System.out.println(trailer.getStartxref().toString());
		}
	}

	private boolean contain(byte[] parent, byte[] child) {
		if (parent.length < child.length) {
			return false;
		}
		byte first = child[0];
		int i = 0;
		while (true) {
			if (i >= parent.length) {
				break;
			}
			if (first == parent[i]) {
				int j = 0;
				while (parent[++i] == child[++j]) {
					if (j == child.length - 1) {
						return true;
					}
				}
			}
			i++;
		}
		
		return false;
	}
	
	private byte[] readLine(ByteBuffer buf, boolean reverse) {
		if (reverse) {
			int run = 0;
			int end = buf.position();
			while (true) {
				byte c = buf.get(end);
				buf.position(end);
				if ('\r' == c || '\n' == c) {
					if (run != 0) {
						buf.position(end + 1);
						break;
					}
				} else {
					run++;
				}
				end--;
			}
			if (run > 0) {
				byte[] dst = new byte[run];
				buf.get(dst, 0, run);
				buf.position(buf.position() - run - 1);
				return dst;
			}
		} else {
			int run = 0;
			int start = buf.position();
			while (true) {
				byte c = buf.get();
				if ('\r' == c || '\n' == c) {
					break;
				} else {
					++run;
				}
			}
			if (run > 0) {
				byte[] dst = new byte[run];
				buf.get(dst, start, run);
				return dst;
			}
		}
		return null;
	}
}
