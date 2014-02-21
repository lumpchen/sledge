package me.lumpchen.sledge.pdf.reader;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

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
import me.lumpchen.sledge.pdf.syntax.document.PDFDocument;

public class ObjectReader {

	private RandomByteReader randomReader;
	private Tokenizer tokenizer;
	private Queue<Token> tokenQueue = new ArrayDeque<Token>();

	public ObjectReader(RandomByteReader reader) {
		this.randomReader = reader;
		this.tokenizer = new Tokenizer(this.randomReader);
	}
	
	public ObjectReader(byte[] data) {
		this.tokenizer = new Tokenizer(data);
	}
	
	public ObjectReader(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}

	public ObjectReader(LineData lineData) {
		this.tokenizer = new Tokenizer(lineData.getBytes());
	}

	public IndirectObject readIndirectObject(PDFDocument pdfDoc) {
		try {
			PObject nextObj = this.readNextObj();
			if (nextObj == null || !(nextObj instanceof IndirectObject)) {
				throw new SyntaxException("not matched object begin tag: " + nextObj.toString());
			}
			
			IndirectObject obj = (IndirectObject) nextObj;
			while (true) {
				Token token = this.nextToken();
				if (null == token) {
					throw new SyntaxException("not matched object begin tag: "
							+ obj.toString());
				}
				if (token.match(IndirectObject.END)) {
					break;
				}
				
				if (token.match(PStream.BEGIN)) {
					PDictionary dict = obj.getDict();
					PStream stream = this.readStream(token, dict, pdfDoc);
					obj.setInsideObj(stream);
					continue;
				}
				
				PObject inside = this.readObject(token);
				if (inside instanceof PDictionary) {
					obj.setInsideObj(inside);
				}
			}
	
			return obj;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public PDictionary readDict() {
		try {
			Token token = this.nextToken();
			return this.readDict(token);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public PObject readNextObj() {
		try {
			Token token = this.nextToken();
			return this.readObject(token);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private PStream readStream(Token begin, PDictionary dict, PDFDocument pdfDoc) throws IOException {
		if (!begin.match(PStream.BEGIN)) {
			throw new SyntaxException(begin.toString());
		}

		PStream stream = new PStream(dict);
		
		PNumber length = null;
		PObject objLen = dict.get(PName.Length);
		if (objLen instanceof PNumber) {
			length = (PNumber) objLen; 	
		} else if (objLen instanceof IndirectRef) {
			IndirectObject obj = pdfDoc.getObject((IndirectRef) objLen);
			length = (PNumber) obj.insideObj();
		}
		
		byte[] bytes = this.tokenizer.readBytes(length.intValue());
		stream.setStream(bytes);

//		this.bytesReader.readEOL();

		Token end = this.nextToken();
		if (!end.match(PStream.END)) {
			throw new SyntaxException(end.toString());
		}

		return stream;
	}

	private PObject readObject(Token token) throws IOException {
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
				Token second = this.peekNextToken(null);
				if (second.isNumber()) {
					Token third = this.peekNextToken(second);
					if (third.match(IndirectRef.BEGIN)) {
						PNumber objNum = this.readNumber(token);
						second = this.nextToken();
						PNumber genNum = this.readNumber(second);
						third = this.nextToken();
						return new IndirectRef(objNum.intValue(),
								genNum.intValue());
					} else if(third.match(IndirectObject.BEGIN)) {
						PNumber objNum = this.readNumber(token);
						second = this.nextToken();
						PNumber genNum = this.readNumber(second);
						third = this.nextToken();
						return new IndirectObject(objNum.intValue(),
								genNum.intValue());
					} else {
						return this.readNumber(token);
					}
				}

				return this.readNumber(token);
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

	private PString readLiteralString(Token begin) throws IOException {
		if (!begin.match(PLiteralString.BEGIN)) {
			throw new SyntaxException(begin.toString());
		}

		Token token = this.nextToken(PLiteralString.END);

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

	private PString readHexString(Token begin) throws IOException {
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

	private PName readName() throws IOException {
		Token token = this.nextToken();
		PName name = PName.instance(token.getBytes());
		return name;
	}

	private PArray readArray(Token begin) throws IOException {
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
			array.add(obj);
		}

		return array;
	}

	private PDictionary readDict(Token begin) throws IOException {
		if (!begin.match(PDictionary.BEGIN)) {
			throw new SyntaxException(begin.toString());
		}
		PDictionary dict = new PDictionary();
		while (true) {
			Token token = this.nextToken();
			if (null == token) {
				throw new SyntaxException("not found end tag: "
						+ begin.toString());
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

	private Token peekNextToken(Token current) throws IOException {
		if (this.tokenQueue.isEmpty()) {
			Token next = this.tokenizer.nextToken();
			this.tokenQueue.add(next);
			return next;			
		} else {
			if (null == current || !this.tokenQueue.contains(current)) {
				return this.tokenQueue.element();
			}
			Iterator<Token> it = this.tokenQueue.iterator();
			while (it.hasNext()) {
				if (it.next().equals(current)) {
					if (it.hasNext()) {
						return it.next();	
					} else {
						Token next = this.tokenizer.nextToken();
						this.tokenQueue.add(next);
						return next;
					}
				}
			}
		} 
		return null;
	}
	
	private Token nextToken() throws IOException {
		if (!this.tokenQueue.isEmpty()) {
			return this.tokenQueue.poll();
		}
		Token next = this.tokenizer.nextToken();
		return next;
	}

	private Token nextToken(byte end) throws IOException {
		return this.tokenizer.nextToken(end);
	}
}
