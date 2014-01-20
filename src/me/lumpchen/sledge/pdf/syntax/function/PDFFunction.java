package me.lumpchen.sledge.pdf.syntax.function;

import java.io.IOException;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.SyntaxException;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;

/**
 * <p>
 * PDF Functions are defined in the reference as Section 3.9.
 * </p>
 * 
 * <p>
 * A PDF function maps some set of <i>m</i> inputs into some set of <i>n</i>
 * outputs. There are 4 types of functions:
 * <ul>
 * <li>Type 0: Sampled functions. (PDF 1.2)<br>
 * A sampled function (type 0) uses a table of sample values to define the
 * function. Various techniques are used to interpolate values between the
 * sample values (see Section 3.9.1, "Type 0 (Sampled) Functions").</li>
 * <li>Type 2: Exponential Interpolation. (PDF 1.3)<br>
 * An exponential interpolation function (type 2) defines a set of coefficients
 * for an exponential function (see Section 3.9.2,
 * "Type 2 (Exponential Interpolation) Functions").</li>
 * <li>Type 3: Stitching functions. (PDF 1.3)<br>
 * A stitching function (type 3) is a combination of other functions,
 * partitioned across a domain (see Section 3.9.3,
 * "Type 3 (Stitching) Functions").</li>
 * <li>Type 4: Postscript calculations. (PDF 1.3)<br>
 * A PostScript calculator function (type 4) uses operators from the PostScript
 * language to describe an arithmetic expression (see Section 3.9.4,
 * "Type 4 (PostScript Calculator) Functions").</li>
 * </ul>
 * </p>
 * 
 * <p>
 * The function interface contains a single method, <i>calculate</i> which takes
 * an array of <i>m</i> floats an interprets them into an array of </i>n</i>
 * floats.
 * <p>
 * PDFFunctions do not have accessible constructors. Instead, use the static
 * <i>getFunction()</i> method to read a functions from a PDF Object.
 * 
 */
public abstract class PDFFunction {

	/** Sampled function */
	public static final int TYPE_0 = 0;

	/** Exponential interpolation function */
	public static final int TYPE_2 = 2;

	/** Stitching function. */
	public static final int TYPE_3 = 3;

	/** PostScript calculator function. */
	public static final int TYPE_4 = 4;

	/** the type of this function from the list of known types */
	private int type;

	/** the input domain of this function, an array of 2 * <i>m</i> floats */
	private float[] domain;

	/**
	 * the output range of this functions, and array of 2 * <i>n</i> floats.
	 * required for type 0 and 4 functions
	 */
	private float[] range;

	/** Creates a new instance of PDFFunction */
	protected PDFFunction(int type) {
		this.type = type;
	}

	/**
	 * Get a PDFFunction from a PDFObject
	 */
	public static PDFFunction getFunction(IndirectObject obj) throws IOException {
		PDFFunction function;
		int type;
		float[] domain = null;
		float[] range = null;

		PDictionary dict = obj.getDict();
		// read the function type (required)
		PNumber typeObj = dict.getValueAsNumber(PName.instance("FunctionType"));
		if (typeObj == null) {
			throw new SyntaxException("No FunctionType specified in function!");
		}
		type = typeObj.intValue();

		// read the function's domain (required)
		PArray domainAry = dict.getValueAsArray(PName.instance("Domain"));
		if (domainAry == null) {
			throw new SyntaxException("No Domain specified in function!");
		}

		domain = new float[domainAry.size()];
		for (int i = 0; i < domainAry.size(); i++) {
			PNumber n = (PNumber) domainAry.get(i);
			domain[i] = n.floatValue();
		}

		// read the function's range (optional)
		PArray rangeAry = dict.getValueAsArray(PName.instance("Range"));
		if (rangeAry != null) {
			range = new float[rangeAry.size()];
			for (int i = 0; i < rangeAry.size(); i++) {
				PNumber n = (PNumber) rangeAry.get(i);
				range[i] = n.floatValue();
			}
		}

		// now create the acual function object
		switch (type) {
		case TYPE_0:
			if (rangeAry == null) {
				throw new SyntaxException(
						"No Range specified in Type 0 Function!");
			}
			function = new FunctionType0();
			break;
		case TYPE_2:
			function = new FunctionType2();
			break;
		case TYPE_3:
			function = new FunctionType3();
			break;
		case TYPE_4:
			if (rangeAry == null) {
				throw new SyntaxException(
						"No Range specified in Type 4 Function!");
			}
			function = new FunctionType4();
			break;
		default:
			throw new SyntaxException("Unsupported function type: " + type);
		}

		// fill in the domain and optionally the range
		function.setDomain(domain);
		if (range != null) {
			function.setRange(range);
		}

		// now initialize the function
		function.parse(obj);

		return function;
	}

	/**
	 * Get the type of this function
	 * 
	 * @return one of the types of function (0-4)
	 */
	public int getType() {
		return type;
	}

	/**
	 * Get the number of inputs, <i>m</i>, required by this function
	 * 
	 * @return the number of input values expected by this function
	 */
	public int getNumInputs() {
		return (domain.length / 2);
	}

	/**
	 * Get the number of outputs, <i>n</i>, returned by this function
	 * 
	 * @return the number of output values this function will return
	 */
	public int getNumOutputs() {
		if (range == null) {
			return 0;
		}
		return (range.length / 2);
	}

	/**
	 * Get a component of the domain of this function
	 * 
	 * @param i
	 *            the index into the domain array, which has size 2 * <i>m</i>.
	 *            the <i>i</i>th entry in the array has index 2<i>i</i>,
	 *            2<i>i</i> + 1
	 * @return the <i>i</i>th entry in the domain array
	 */
	protected float getDomain(int i) {
		return domain[i];
	}

	/**
	 * Set the domain of this function
	 */
	protected void setDomain(float[] domain) {
		this.domain = domain;
	}

	/**
	 * Get a component of the range of this function
	 * 
	 * @param i
	 *            the index into the range array, which has size 2 * <i>n</i>.
	 *            the <i>i</i>th entry in the array has index 2<i>i</i>,
	 *            2<i>i</i> + 1
	 * @return the <i>i</i>th entry in the range array
	 */
	protected float getRange(int i) {
		if (range == null) {
			if ((i % 2) == 0) {
				return Float.MIN_VALUE;
			} else {
				return Float.MAX_VALUE;
			}
		}
		return range[i];
	}

	/**
	 * Set the range of this function
	 */
	protected void setRange(float[] range) {
		this.range = range;
	}

	/**
	 * Map from <i>m</i> input values to <i>n</i> output values. The number of
	 * inputs <i>m</i> must be exactly one half the size of the domain. The
	 * number of outputs should match one half the size of the range.
	 * 
	 * @param inputs
	 *            an array of >= <i>m</i> input values
	 * @return the array of <i>n</i> output values
	 */
	public float[] calculate(float[] inputs) {
		float[] outputs = new float[getNumOutputs()];
		calculate(inputs, 0, outputs, 0);
		return outputs;
	}

	/**
	 * Map from <i>m</i> input values to <i>n</i> output values. The number of
	 * inputs <i>m</i> must be exactly one half the size of the domain. The
	 * number of outputs should match one half the size of the range.
	 * 
	 * @param inputs
	 *            an array of >= <i>m</i> input values
	 * @param inputOffset
	 *            the offset into the input array to read from
	 * @param outputs
	 *            an array of size >= <i>n</i> which will be filled with the
	 *            output values
	 * @param outputOffset
	 *            the offset into the output array to write to
	 * @return the array of <i>n</i> output values
	 */
	public float[] calculate(float[] inputs, int inputOffset, float[] outputs,
			int outputOffset) {
		// check the inputs
		if (inputs.length - inputOffset < getNumInputs()) {
			throw new IllegalArgumentException(
					"Wrong number of inputs to function!");
		}

		// check the outputs
		if (range != null && outputs.length - outputOffset < getNumOutputs()) {
			throw new IllegalArgumentException(
					"Wrong number of outputs for function!");
		}

		// clip the inputs to domain
		for (int i = 0; i < inputs.length; i++) {
			// clip to the domain -- min(max(x<i>, domain<2i>), domain<2i+1>)
			inputs[i] = Math.max(inputs[i], getDomain(2 * i));
			inputs[i] = Math.min(inputs[i], getDomain((2 * i) + 1));
		}

		// do the actual calculation
		doFunction(inputs, inputOffset, outputs, outputOffset);

		// clip the outputs to range
		for (int i = 0; range != null && i < outputs.length; i++) {
			// clip to range -- min(max(r<i>, range<2i>), range<2i + 1>)
			outputs[i] = Math.max(outputs[i], getRange(2 * i));
			outputs[i] = Math.min(outputs[i], getRange((2 * i) + 1));
		}

		return outputs;
	}

	/**
	 * Subclasses must implement this method to perform the actual function on
	 * the given set of data. Note that the inputs are guaranteed to be clipped
	 * to the domain, while the outputs will be automatically clipped to the
	 * range after being returned from this function.
	 * 
	 * @param inputs
	 *            guaranteed to be at least as big as
	 *            <code>getNumInputs()</code> and all values within range
	 * @param inputOffset
	 *            the offset into the inputs array to read from
	 * @param outputs
	 *            guaranteed to be at least as big as
	 *            <code>getNumOutputs()</code>, but not yet clipped to domain
	 * @param outputOffset
	 *            the offset into the output array to write to
	 */
	protected abstract void doFunction(float[] inputs, int inputOffset,
			float[] outputs, int outputOffset);

	/** Read the function information from a PDF Object */
	protected abstract void parse(IndirectObject obj) throws IOException;
}
