package me.lumpchen.sledge.pdf.graphics;

import me.lumpchen.sledge.pdf.syntax.lang.PArray;
import me.lumpchen.sledge.pdf.syntax.lang.PName;
import me.lumpchen.sledge.pdf.syntax.lang.PNumber;
import me.lumpchen.sledge.pdf.syntax.lang.PObject;
import me.lumpchen.sledge.pdf.syntax.lang.PString;

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
	
	public PArray asArray() {
		return (PArray) operand;
	}
	
	public PName asName() {
		return PName.instance(this.operand.toString());
	}
}
