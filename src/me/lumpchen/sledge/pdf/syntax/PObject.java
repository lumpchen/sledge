package me.lumpchen.sledge.pdf.syntax;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.syntax.basic.PArray;

public abstract class PObject {

	public static Map<String, Class> classMap = new HashMap<String, Class>();

	public boolean read(ByteBuffer buf, PObject parent) {
		int pos = buf.position();
		if (readBegin(buf)) {
			buf.position(pos);
			return false;
		}
		if (readObj(buf)) {
			buf.position(pos);
			return false;
		}
		if (readEnd(buf)) {
			buf.position(pos);
			return false;
		}
		
		if (parent != null) {
			parent.appendChild(this);
		}
		return true;
	}
	
	protected void appendChild(PObject obj) {
		
	}
	
	protected byte getByteNoSpace(ByteBuffer buf) {
		byte b = buf.get();
		
		while (isSpace(b)) {
			b = buf.get();
		}
		
		return b;
	}
	
	private boolean isSpace(byte b) {
		if (b == ' ' || b == '\r' || b == '\n') {
			return true;
		}
		return false;
	}
	
	protected boolean readBegin(ByteBuffer buf) {
		return false;
	}
	
	protected boolean readEnd(ByteBuffer buf) {
		return false;
	}
	
	protected boolean readObj(ByteBuffer buf) {
		return false;
	}
}
