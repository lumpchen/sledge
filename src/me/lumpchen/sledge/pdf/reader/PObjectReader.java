package me.lumpchen.sledge.pdf.reader;

import me.lumpchen.sledge.pdf.graphics.GraphicsOperator;
import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PBoolean;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PHexString;
import me.lumpchen.sledge.pdf.syntax.basic.PLiteralString;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNull;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.basic.PStream;

public class PObjectReader {

	private LineReader lineReader;
	private BytesReader bytesReader;
	private LineData lineData;

	public PObjectReader(LineReader reader) {
		this.lineReader = reader;
		this.readNextLine();
	}

	public PObjectReader(LineData lineData) {
		this.lineData = lineData;
		this.readNextLine();
	}

	private boolean readNextLine() {
		if (this.lineReader != null) {
			this.lineData = this.lineReader.readLine();
		}
		if (null == this.lineData) {
			return false;
		}
		this.bytesReader = new BytesReader(this.lineData.getBytes());
		return true;
	}

	public PObject readNextObj() {
		if (this.bytesReader == null || this.bytesReader.remaining() == 0) {
			if (!this.readNextLine()) {
				return null;
			}
		}

		PObject next = read();
		return next;
	}

	private PObject read() {
		PObject obj = null;

		this.bytesReader.skipSpace();
		byte first = this.bytesReader.getByte(this.bytesReader.position());

		switch (first) {
		case PName.BEGIN: {
			// read name here, '/'
			this.bytesReader.readByte(); // skip '/'
			byte[] name = this.bytesReader.readToNextToken();
			obj = PName.instance(name);
			break;
		}
		case PArray.BEGIN: {
			obj = new PArray();
			break;
		}
		case '<': {
			byte next = this.bytesReader.getByte(this.bytesReader.position() + 1);
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
			} else if (this.match(PNull.NULL)) {
				obj = new PNull();
			} else if (this.match(PStream.BEGIN)) {
				obj = new PStream();
			} else {
				// 9 0 obj || 9 0 R
				byte[] num0 = this.bytesReader.peekToSpace(0);
				if (BytesReader.isNumber(num0)) {
					int run = num0.length + 1;
					byte[] num1 = this.bytesReader.peekToSpace(run);
					if (BytesReader.isNumber(num1)) {
						run += num1.length + 1;
						byte tag = this.bytesReader.getByte(this.bytesReader.position() + run);
						if (tag == IndirectObject.BEGIN[0]) {
							obj = new IndirectObject();
						} else if (tag == IndirectRef.BEGIN) {
							obj = new IndirectRef();
						} else {
							obj = new PNumber();
						}
					} else {
						obj = new PNumber();
					}
				} else if (BytesReader.isNumber(first)) {
					obj = new PNumber();
				}
			}
			break;
		}
		}

		if (null == obj) {
			byte[] bytes = this.bytesReader.peekToToken(0);
			if (GraphicsOperator.isGraphicsOperator(bytes)) {
				obj = GraphicsOperator.create(bytes);				
			}
		}
		
		if (null != obj) {
			if (!(obj instanceof PStream)) {
				obj.read(this);	
			}
		}
		return obj;
	}

	private boolean match(byte... tag) {
		if (this.bytesReader.remaining() < tag.length) {
			return false;
		}
		int pos = this.bytesReader.position();
		for (int i = 0, n = tag.length; i < n; i++) {
			if (this.bytesReader.getByte(pos + i) != tag[i]) {
				return false;
			}
		}
		return true;	
	}

	public byte readByte() {
		return this.bytesReader.readByte();
	}

	public byte[] readToFlag(byte flag) {
		return this.bytesReader.readToFlag(flag);
	}

	public byte[] readBytes(int size) {
		if (size > this.bytesReader.remaining()) {
			if (this.lineReader != null) {
				this.lineData = this.lineReader.readBytesDirect(size + 32);
			}
			if (null == this.lineData) {
				throw new ReadException();
			}
			this.bytesReader = new BytesReader(this.lineData.getBytes());
		}
		return this.bytesReader.readBytes(size);
	}

	public void readEOL() {
		this.bytesReader.readEOL();
	}
	
	public int readInt() {
		return this.bytesReader.readInt();
	}

	public long readLong() {
		return this.bytesReader.readLong();
	}
	
	public double readDouble() {
		return this.bytesReader.readDouble();
	}
	
	public byte[] readToNextToken() {
		return this.bytesReader.readToNextToken();
	}
}
