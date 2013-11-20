package me.lumpchen.sledge.pdf.reader;

import java.nio.ByteBuffer;
import java.util.Stack;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.PObject;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PBoolean;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PHexString;
import me.lumpchen.sledge.pdf.syntax.basic.PLiteralString;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PStream;

public class ObjectReader {

	private ByteBuffer buf;
	private Stack<Byte> tagStack;

	public ObjectReader(byte[] data) {
		this.buf = ByteBuffer.wrap(data);
		this.tagStack = new Stack<Byte>();
	}
	
	public ObjectReader(ByteBuffer buf) {
		this.buf = buf;
		this.tagStack = new Stack<Byte>();
	}

	public PObject readNextObj() {
		PObject next = read();
		return next;
	}

	private PObject read() {
		PObject obj = null;
		byte first = buf.get(0);

		switch (first) {
		case PName.BEGIN: {
			// read name here, '/'
			this.buf.get();	// skip '/'
			byte[] name = this.readToSpace();
			obj = PName.instance(name);
			break;
		}
		case PArray.BEGIN: {
			this.pushTag(PArray.BEGIN);
			obj = new PArray();
			break;
		}
		case PArray.END: {
			if (!this.popTag(PArray.BEGIN)) {
				throw new NotMatchedTagException();
			}
			break;
		}
		case '<': {
			byte next = buf.get(1);
			if (next == PDictionary.BEGIN[1]) {
				// Dictionary
				this.pushTag(PDictionary.BEGIN);
				obj = new PDictionary();
			} else {
				// HexString
				this.pushTag(PHexString.BEGIN);
				obj = new PHexString();
			}
			break;
		}
		case '>': {
			byte next = buf.get(0);
			if (next == PDictionary.END[1]) {
				if (!this.popTag(PDictionary.BEGIN)) {
					throw new NotMatchedTagException();
				}
			} else {
				// end of HexString
				if (!this.popTag(PHexString.BEGIN)) {
					throw new NotMatchedTagException();
				}
			}
			break;
		}
		case PLiteralString.BEGIN: {
			this.pushTag(PLiteralString.BEGIN);
			obj = new PLiteralString();
			break;
		}
		case PLiteralString.END: {
			if (!this.popTag(PLiteralString.BEGIN)) {
				throw new NotMatchedTagException();
			}
			break;
		}
		default: {
			if (this.match(PBoolean.TAG_FALSE) || this.match(PBoolean.TAG_TRUE)) {
				obj = new PBoolean(); 
			} else if (this.match(IndirectObject.END)) {
				if (!this.popTag(IndirectObject.BEGIN)) {
					throw new NotMatchedTagException();
				}
			} else if (this.match(PStream.BEGIN)) {
				obj = new PStream();
			} else if (this.match(PStream.END)) {
				if (!this.popTag(PStream.BEGIN)) {
					throw new NotMatchedTagException();
				}
			} else {
				// 9 0 obj || 9 0 R
				int currPos = this.buf.position();
				byte[] num0 = this.peekToSpace(currPos);
				int runPos = currPos + num0.length + 1;
				if (this.isNumber(num0)) {
					byte[] num1 = this.peekToSpace(runPos);
					if (this.isNumber(num1)) {
						runPos += num1.length + 1;
						byte tag = this.buf.get(runPos);
						if (tag == IndirectObject.BEGIN[0]) {
							this.pushTag(IndirectObject.BEGIN);
							obj = new IndirectObject();
						} else if (tag == IndirectRef.BEGIN[0]) {
							obj = new IndirectRef();
						}
					}
				}
			}
			break;
		}
		}

		if (obj != null) {
			if (!(obj instanceof PStream) && !(obj instanceof PName)) {
				obj.read(this);				
			}
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
	
	private void pushTag(byte... begin) {
		for (byte b : begin) {
			this.tagStack.push(b);
		}
	}

	private boolean popTag(byte... begin) {
		int n = begin.length;
		while (n > 0) {
			n--;
			if (begin[n] != this.tagStack.pop()) {
				return false;
			}
		}
		return true;
	}

	public byte[] readToSpace() {
		int i = 0;
		while (true) {
			if (isSpace(buf.get(i))) {
				break;
			}
			i++;
		}
		byte[] name = new byte[i];
		buf.get(name);
		return name;
	}

	public byte readByte() {
		return this.buf.get();
	}

	public byte[] readBytes(int size) {
		byte[] bytes = new byte[size];
		this.buf.get(bytes);
		this.buf.position(this.buf.position() + size);
		return bytes;
	}

	public int readInt() {
		byte[] bytes = this.readToSpace();
		int len = bytes.length;
		int value = 0;
		for (int i = 0, n = bytes.length; i < n; i++) {
			int c = Character.digit(bytes[i], 10);
			value += c * (int) (Math.pow(10, len - i - 1) + 0.5);
		}
		return value;
	}
	
	private boolean isSpace(byte b) {
		if (b == ' ' || b == '\r' || b == '\n') {
			return true;
		}
		return false;
	}

	private boolean isNumber(byte[] bytes) {
		for (byte b : bytes) {
			if (!Character.isDigit(b)) {
				return false;
			}
		}
		return true;
	}

	private byte[] peekToSpace(int pos) {
		int run = 0;
		while (true) {
			byte next = this.buf.get(pos);
			if (this.isSpace(next)) {
				break;
			}
			run++;
		}
		byte[] bytes = new byte[run];
		this.buf.get(bytes, 0, run);
		return bytes;
	}
}
