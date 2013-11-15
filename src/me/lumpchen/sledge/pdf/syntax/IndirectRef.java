package me.lumpchen.sledge.pdf.syntax;

public class IndirectRef {

	public static final byte[] R = { 'R' };

	private int objNum;
	private int genNum;

	public IndirectRef(int objectNumber, int generationNumber) {
		this.objNum = objectNumber;
		this.genNum = generationNumber;
	}
}
