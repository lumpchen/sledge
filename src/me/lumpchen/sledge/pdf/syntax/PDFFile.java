package me.lumpchen.sledge.pdf.syntax;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class PDFFile {

	public static final String R = "r";
	public static final String RW = "rw";

	private RandomAccessFile raf;
	private FileChannel fc;
	private byte[] password;

	public PDFFile(String path) throws IOException {
		this(new File(path));
	}

	public PDFFile(File f) throws IOException {
		this.raf = new RandomAccessFile(f, "r");
	}

	public PDFFile(File f, String mode) throws IOException {
		this.raf = new RandomAccessFile(f, mode);
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}
	
	public byte[] getPassword() {
		return this.password;
	}
	
	public FileChannel getFileChannel() {
		this.fc = this.raf.getChannel();
		return this.fc;
	}

	public void close() {
		if (this.fc != null) {
			try {
				this.fc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (this.raf != null) {
			try {
				this.raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
