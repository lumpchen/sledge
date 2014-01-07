package me.lumpchen.sledge.pdf.reader;

import me.lumpchen.sledge.pdf.graphics.GraphicsOperator;
import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
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
import me.lumpchen.sledge.pdf.syntax.basic.PStream;
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
			throw new SyntaxException("not matched object begin tag: " + token2.toString());
		}
		
		IndirectObject obj = new IndirectObject(objNum.intValue(), genNum.intValue());
		while (true) {
			Token token = this.nextToken();
			if (null == token) {
				throw new SyntaxException("not matched object begin tag: " + obj.toString());
			}
			if (token.match(IndirectObject.END)) {
				break;
			}
			PObject inside = this.readObject(token);
			
			if (inside instanceof PDictionary) {
				PDictionary dict = (PDictionary) inside;
				if (dict.get(PName.Length) != null) {
					PStream stream = this.readStream(dict);
					obj.setInsideObj(stream);
					continue;
				}
			}
			
			obj.setInsideObj(inside);
		}
		
		return obj;
	}
	
	public PDictionary readDict() {
		Token token = this.nextToken();
		return this.readDict(token);
	}
	
	public PObject readNextObj() {
		Token token = this.nextToken();
		return this.readObject(token);
	}
	
	private PStream readStream(PDictionary dict) {
		Token token = this.nextToken();
		if (!token.match(PStream.BEGIN)) {
			throw new SyntaxException(token.toString());
		}
		
		PStream stream = new PStream(dict);
		PNumber length = (PNumber) dict.get(PName.Length);
		byte[] bytes = this.readBytes(length.intValue());
		stream.setStream(bytes);
		
		this.bytesReader.readEOL();
		
		Token end = this.nextToken();
		if (!end.match(PStream.END)) {
			throw new SyntaxException(token.toString());
		}
		
		return stream;
	}
	
	private PObject readObject(Token token) {
		if (null == token) {
			return null;
		}
		if (token.match(PArray.BEGIN)) {
			return this.readArray(token);
		} else if (token.match(PDictionary.BEGIN)) {
			return this.readDict(token);
		} else if (token.match(PName.BEGIN)) {
			return this.readName();
		} else if (token.match(PNull.NULL)) {
			return new PNull();
		} else if (token.match(PBoolean.TAG_TRUE)) {
			return PBoolean.True;
		} else if (token.match(PBoolean.TAG_FALSE)) {
			return PBoolean.False;
		} else if (token.match(PLiteralString.BEGIN)) {
			return this.readLiteralString(token);
		} else if (token.match(PHexString.BEGIN)) {
			return this.readHexString(token);
		} else if (GraphicsOperator.isGraphicsOperator(token.getBytes())) {
			return this.readGraphicsOperator(token);
		} else {
			if (token.isNumber()) {
				boolean isRef = false;
				Token second = new Token(this.bytesReader.peekNextToken(0));
				if (second.isNumber()) {
					Token third = new Token(this.bytesReader.peekNextToken(second.size()));
					if (third.match(IndirectRef.BEGIN)) {
						isRef = true;
						PNumber objNum = this.readNumber(token);
						second = new Token(this.bytesReader.readToken());
						PNumber genNum = this.readNumber(second);
						third = new Token(this.bytesReader.readToken());
						return new IndirectRef(objNum.intValue(), genNum.intValue());
					} else {
						return this.readNumber(token);
					}
				}
				
				if (!isRef) {
					return this.readNumber(token);
				}
			}
		}
		return null;
	}
	
	private GraphicsOperator readGraphicsOperator(Token token) {
		return GraphicsOperator.create(token.getBytes());
	}
	
	private PNumber readNumber(Token token) {
		String s = new String(token.getBytes());
		PNumber number;
		if (s.indexOf('.') >= 0) {
			number = new PNumber(Double.parseDouble(s));
		} else {
			number = new PNumber(Integer.parseInt(s, 10));
		}
		return number;
	}
	
	private PString readLiteralString(Token begin) {
		if (!begin.match(PLiteralString.BEGIN)) {
			throw new SyntaxException(begin.toString());
		}
		
		Token token = this.readToken(true);
		
		PLiteralString s = null;
		if (token.size() == 0) {
			s = new PLiteralString();
		} else {
			s = new PLiteralString(token.getBytes());			
		}
		
		Token end = this.nextToken();
		if (!end.match(PLiteralString.END)) {
			throw new SyntaxException(token.toString());
		}
		return s;
	}
	
	private PString readHexString(Token begin) {
		if (!begin.match(PHexString.BEGIN)) {
			throw new SyntaxException(begin.toString());
		}
		
		PHexString hex = null;
		while (true) {
			Token next = this.nextToken();
			if (null == next) {
				throw new SyntaxException("not found end tag: " + begin.toString());
			}
			if (next.match(PHexString.END)) {
				break;
			}
			
			if (next.size() == 0) {
				hex = new PHexString();
			} else {
				hex = new PHexString(next.getBytes());
			}
		}
		
		return hex;
	}
	
	private PName readName() {
		Token token = this.nextToken();
		PName name = PName.instance(token.getBytes());
		return name;
	}
	
	private PArray readArray(Token begin) {
		if (!begin.match(PArray.BEGIN)) {
			throw new SyntaxException(begin.toString());
		}
		PArray array = new PArray();
		while (true) {
			Token token = this.nextToken();
			if (null == token) {
				throw new SyntaxException("not found end tag: " + begin.toString());
			}
			
			if (token.match(PArray.END)) {
				break;
			}
			
			PObject obj = this.readObject(token);
			array.appendChild(obj);
		}
		
		return array;
	}
	
	private PDictionary readDict(Token begin) {
		if (!begin.match(PDictionary.BEGIN)) {
			throw new SyntaxException(begin.toString());
		}
		PDictionary dict = new PDictionary();
		while (true) {
			Token token = this.nextToken();
			if (null == token) {
				throw new SyntaxException("not found end tag: " + begin.toString());
			}
			
			if (token.match(PDictionary.END)) {
				break;
			}
			
			PObject key = this.readObject(token);
			if (!(key instanceof PName)) {
				throw new SyntaxException(key.toString());
			}
			Token next = this.nextToken();
			PObject value = this.readObject(next);
			dict.put((PName) key, value);
		}
		
		return dict;
	}

	private byte[] readBytes(int size) {
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
	
	private Token nextToken() {
		return this.nextToken(false);
	}
	
	private Token nextToken(boolean readString) {
		if (this.bytesReader == null || this.bytesReader.remaining() == 0) {
			if (!this.readNextLine()) {
				return null;
			}
		}
		
		Token next = this.readToken(readString);
		return next;
	}

	private Token readToken(boolean readString) {
		byte[] bytes;
		if (readString) {
			bytes = this.bytesReader.readStringToken();
		} else {
			bytes = this.bytesReader.readNextToken();
		}
		return new Token(bytes);
	}
	
	class Token {

		private byte[] bytes;

		public Token(byte[] bytes) {
			this.bytes = bytes;
		}
		
		public String toString() {
			if (bytes != null) {
				return new String(bytes);
			}
			return "";
		}
		
		public byte[] getBytes() {
			return this.bytes;
		}

		public int size() {
			if (bytes != null) {
				return this.bytes.length;				
			}
			return 0;
		}
		
		public boolean match(byte... tag) {
			if (this.bytes.length != tag.length) {
				return false;
			}
			for (int i = 0, n = tag.length; i < n; i++) {
				if (this.bytes[i] != tag[i]) {
					return false;
				}
			}
			return true;	
		}
		
		public boolean isNumber() {
			return BytesReader.isNumber(bytes);
		}
	}
}
