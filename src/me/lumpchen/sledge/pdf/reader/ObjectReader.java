package me.lumpchen.sledge.pdf.reader;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.SyntaxException;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PBoolean;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PHexString;
import me.lumpchen.sledge.pdf.syntax.basic.PLiteralString;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNull;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.basic.PString;

public class ObjectReader {
	
	private LineReader lineReader;
	private BytesReader bytesReader;
	private LineData lineData;
	
	public ObjectReader(LineReader reader) {
		this.lineReader = reader;
		this.readNextLine();
	}

	public ObjectReader(LineData lineData) {
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
	
	public IndirectObject readIndirectObject() {
		Token token0 = this.nextToken();
		PNumber objNum = (PNumber) this.readObject(token0);
		Token token1 = this.nextToken();
		PNumber genNum = (PNumber) this.readObject(token1);
		
		Token token2 = this.nextToken();
		if (!token2.match(IndirectObject.BEGIN)) {
			throw new SyntaxException();
		}
		
		IndirectObject obj = new IndirectObject(objNum.intValue(), genNum.intValue());
		while (true) {
			Token token = this.nextToken();
			if (token.match(IndirectObject.END)) {
				break;
			}
			PObject inside = this.readObject(token);
			// handle inside and stream;
		}
		
		return obj;
	}
	
	public PObject readObject(Token token) {
		if (token.match(PArray.BEGIN)) {
			return this.readArray();
		} else if (token.match(PDictionary.BEGIN)) {
			return this.readDict();
		} else if (token.match(PName.BEGIN)) {
			return this.readName();
		} else if (token.match(PNull.NULL)) {
			return new PNull();
		} else if (token.match(PBoolean.TAG_TRUE)) {
			return PBoolean.True;
		} else if (token.match(PBoolean.TAG_FALSE)) {
			return PBoolean.False;
		} else if (token.match(PLiteralString.BEGIN)) {
			this.readLiteralString();
		} else if (token.match(PHexString.BEGIN)) {
			this.readHexString();
		} else {
			this.readNumber();
		}
		return null;
	}
	
	private PNumber readNumber() {
		return null;
	}
	
	private boolean stringBegin = false; 
	private PString readLiteralString() {
		this.stringBegin = true;
		return null;
	}
	
	private PString readHexString() {
		return null;
	}
	
	private PName readName() {
		Token token = this.nextToken();
		PName name = PName.instance(token.getBytes());
		return name;
	}
	
	private PArray readArray() {
		PArray array = new PArray();
		while (true) {
			Token token = this.nextToken();
			if (token.match(PArray.END)) {
				break;
			}
			PObject obj = this.readObject(token);
			array.appendChild(obj);
		}
		
		return array;
	}
	
	private PDictionary readDict() {
		PDictionary dict = new PDictionary();
		while (true) {
			Token token = this.nextToken();
			if (token.match(PDictionary.END)) {
				break;
			}
			PObject key = this.readObject(token);
			if (!(key instanceof PName)) {
				throw new SyntaxException();
			}
			Token next = this.nextToken();
			PObject value = this.readObject(next);
			dict.put((PName) key, value);
		}
		
		return dict;
	}
	
	private Token nextToken() {
		if (this.bytesReader == null || this.bytesReader.remaining() == 0) {
			if (!this.readNextLine()) {
				return null;
			}
		}
		
		Token next = this.readToken();
		return next;
	}

	private Token readToken() {
		byte[] bytes;
		if (this.stringBegin) {
			bytes = this.bytesReader.readStringToken();
		} else {
			bytes = this.bytesReader.readToNextToken();			
		}
		return new Token(bytes);
	}
	
	class Token {

		private byte[] bytes;

		public Token(byte[] bytes) {
			this.bytes = bytes;
		}
		
		public byte[] getBytes() {
			return this.bytes;
		}
		
		public boolean match(byte... tag) {
			return false;
		}
	}
}
