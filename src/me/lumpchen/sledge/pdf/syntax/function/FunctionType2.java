package me.lumpchen.sledge.pdf.syntax.function;

import java.io.IOException;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.SyntaxException;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;

/**
 * A type 2 function is an exponential interpolation function, which maps from
 * one input value to n output values using a simple exponential formula.
 */
public class FunctionType2 extends PDFFunction {
	/** the function's value at zero for the n outputs */
	private float[] c0 = new float[] { 0f };

	/** the function's value at one for the n outputs */
	private float[] c1 = new float[] { 1f };

	/** the exponent */
	private float n;

	/** Creates a new instance of FunctionType2 */
	public FunctionType2() {
		super(TYPE_2);
	}

	/**
	 * Read the zeros, ones and exponent
	 */
	protected void parse(IndirectObject obj) throws IOException {
		PDictionary dict = obj.getDict();
		// read the exponent (required)

		PNumber nObj = dict.getValueAsNumber(PName.instance("N"));
		if (nObj == null) {
			throw new SyntaxException("Exponent required for function type 2!");
		}
		setN(nObj.intValue());

		// read the zeros array (optional)
		PArray cZeroAry = dict.getValueAsArray(PName.instance("C0"));
		if (cZeroAry != null) {
			float[] cZero = new float[cZeroAry.size()];
			for (int i = 0; i < cZeroAry.size(); i++) {
				PNumber n = (PNumber) cZeroAry.get(i);
				cZero[i] = n.floatValue();
			}
			setC0(cZero);
		}

		// read the ones array (optional)

		PArray cOneAry = dict.getValueAsArray(PName.instance("C1"));
		if (cOneAry != null) {
			float[] cOne = new float[cOneAry.size()];
			for (int i = 0; i < cOneAry.size(); i++) {
				PNumber n = (PNumber) cOneAry.get(i);
				cOne[i] = n.floatValue();
			}
			setC1(cOne);
		}
	}

	/**
	 * Calculate the function value for the input. For each output (j), the
	 * function value is: C0(j) + x^N * (C1(j) - C0(j))
	 */
	protected void doFunction(float[] inputs, int inputOffset, float[] outputs,
			int outputOffset) {
		// read the input value
		float input = inputs[inputOffset];

		// calculate the output values
		for (int i = 0; i < getNumOutputs(); i++) {
			outputs[i + outputOffset] = getC0(i)
					+ (float) (Math.pow(input, getN()) * (getC1(i) - getC0(i)));
		}
	}

	/**
	 * Get the exponent
	 */
	public float getN() {
		return n;
	}

	/**
	 * Set the exponent
	 */
	protected void setN(float n) {
		this.n = n;
	}

	/**
	 * Get the values at zero
	 */
	public float getC0(int index) {
		return c0[index];
	}

	/**
	 * Set the values at zero
	 */
	protected void setC0(float[] c0) {
		this.c0 = c0;
	}

	/**
	 * Get the values at one
	 */
	public float getC1(int index) {
		return c1[index];
	}

	/**
	 * Set the values at one
	 */
	protected void setC1(float[] c1) {
		this.c1 = c1;
	}
}