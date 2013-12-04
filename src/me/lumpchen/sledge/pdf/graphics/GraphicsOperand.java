package me.lumpchen.sledge.pdf.graphics;

import me.lumpchen.sledge.pdf.syntax.basic.PInteger;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PReal;
import me.lumpchen.sledge.pdf.syntax.basic.PString;

public class GraphicsOperand {

	private byte[] operandBytes;
	
	public GraphicsOperand(byte[] operandBytes) {
		this.operandBytes = operandBytes;
	}
	
	public String toString() {
		return new String(this.operandBytes);
	}
	
	public PInteger asInt() {
		return null;
	}
	
	public PString asString() {
		return null;
	}
	
	public PReal asReal() {
		return null;
	}
	
	public PName asName() {
		return null;
	}
}
