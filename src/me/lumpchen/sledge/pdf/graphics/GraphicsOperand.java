package me.lumpchen.sledge.pdf.graphics;

import me.lumpchen.sledge.pdf.syntax.PObject;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.basic.PString;

public class GraphicsOperand {

	private PObject operand;
	
	public GraphicsOperand(PObject operand) {
		this.operand = operand;
	}
	
	public String toString() {
		return this.operand.toString();
	}
	
	public PNumber asNumber() {
		return (PNumber) operand;
	}

	public PString asString() {
		return (PString) operand;
	}
	
	public PName asName() {
		return null;
	}
}
