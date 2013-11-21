package me.lumpchen.sledge.pdf.reader;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.PObject;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PBoolean;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PHexString;
import me.lumpchen.sledge.pdf.syntax.basic.PInteger;
import me.lumpchen.sledge.pdf.syntax.basic.PLiteralString;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PStream;

public class ObjectReader {

	private ByteBuffer buf;

	public ObjectReader(byte[] data) {
		this.buf = ByteBuffer.wrap(data);
	}
	
	public ObjectReader(ByteBuffer buf) {
		this.buf = buf;
	}

	public PObject readNextObj() {
		PObject next = read();
		return next;
	}

	private PObject read() {
		PObject obj = null;
		
		this.skipSpace();
		byte first = this.buf.get(this.buf.position());

		switch (first) {
		case PName.BEGIN: {
			// read name here, '/'
			this.buf.get();	// skip '/'
			byte[] name = this.readToSpace();
			obj = PName.instance(name);
			break;
		}
		case PArray.BEGIN: {
			obj = new PArray();
			break;
		}
		case '<': {
			byte next = buf.get(this.buf.position() + 1);
			if (next == PDictionary.BEGIN[1]) {
				// Dictionary
				obj = new PDictionary();
			} else {
				// HexString
				obj = new PHexString();
			}
			break;
		}
		case PLiteralString.BEGIN: {
			obj = new PLiteralString();
			break;
		}
		default: {
			if (this.match(PBoolean.TAG_FALSE) || this.match(PBoolean.TAG_TRUE)) {
				obj = new PBoolean(); 
			} else if (this.match(PStream.BEGIN)) {
				obj = new PStream();
			} else {
				// 9 0 obj || 9 0 R
				byte[] num0 = this.peekToSpace(0);
				if (this.isNumber(num0)) {
					int run = num0.length + 1;
					byte[] num1 = this.peekToSpace(run);
					if (this.isNumber(num1)) {
						run += num1.length + 1;
						byte tag = this.buf.get(this.buf.position() + run);
						if (tag == IndirectObject.BEGIN[0]) {
							obj = new IndirectObject();
						} else if (tag == IndirectRef.BEGIN) {
							obj = new IndirectRef();
						}
					}
				} else if (this.isNumber(first)) {
					obj = new PInteger();
				}
			}
			break;
		}
		}

		if (obj != null) {
			obj.read(this);		
		}
		return obj;
	}

	private boolean match(byte... tag) {
		int pos = this.buf.position();
		for (int i = 0, n = tag.length; i < n; i++) {
			if (this.buf.get(pos + i) != tag[i]) {
				return false;
			}
		}
		return true;
	}

	private void skipSpace() {
		while (true) {
			byte b = this.buf.get();
			if (!this.isSpace(b)) {
				break;
			}
		}
		this.buf.position(this.buf.position() - 1);
	}
	
	public byte[] readToSpace() {
		int i = 0;
		while (true) {
			if (isSpace(buf.get(this.buf.position() + i))) {
				break;
			}
			i++;
		}
		byte[] bytes = new byte[i];
		buf.get(bytes);
		
		this.skipSpace();
		return bytes;
	}

	public byte readByte() {
		return this.buf.get();
	}

	public byte[] readToFlag(byte flag) {
		int pos = this.buf.position();
		int run = 0;
		while (true) {
			byte next = this.buf.get(pos + run);
			if (next == flag) {
				break;
			}
			run++;
		}
		
		byte[] bytes = new byte[run];
		this.buf.get(bytes);
		
		return bytes;
	}
	
	public byte[] readBytes(int size) {
		byte[] bytes = new byte[size];
		this.buf.get(bytes);
//		this.buf.position(this.buf.position() + size);
		return bytes;
	}

	public int readInt() {
		List<Integer> num = new ArrayList<Integer>();
		while (true) {
			byte b = this.buf.get();
			if (!Character.isDigit(b)) {
				if (!this.isSpace(b)) {
					this.buf.position(this.buf.position() - 1);					
				}
				break;
			}
			num.add(Character.digit(b, 10));
		}
		
		int value = 0;
		for (int i = 0, n = num.size(); i < n; i++) {
			value += num.get(i) * (int) (Math.pow(10, n - i - 1) + 0.5);
		}
		return value;
	}
	
	private boolean isSpace(byte b) {
		if (b == ' ' || b == '\r' || b == '\n') {
			return true;
		}
		return false;
	}

	private boolean isNumber(byte... bytes) {
		for (byte b : bytes) {
			if (!Character.isDigit(b)) {
				return false;
			}
		}
		return true;
	}

	private byte[] peekToSpace(int offset) {
		int pos = this.buf.position();
		int remain = this.buf.remaining();
		
		int run = 0;
		while (true) {
			if (remain - offset == run) {
				break;
			}
			byte next = this.buf.get(pos + offset + run);
			if (this.isSpace(next)) {
				break;
			}
			run++;
		}

		this.buf.position(pos + offset);
		byte[] bytes = new byte[run];
		this.buf.get(bytes, 0, run);

		this.buf.position(pos); // back to original position
		return bytes;
	}
}
