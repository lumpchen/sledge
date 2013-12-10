package me.lumpchen.sledge.pdf.graphics;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.syntax.PObject;
import me.lumpchen.sledge.pdf.syntax.SyntaxException;
import me.lumpchen.sledge.pdf.syntax.basic.PString;

public abstract class GraphicsOperator extends PObject {

	public static enum Group {
		GraphicsState, PathConstruction, PathPainting, OtherPainting, Text, MarkedContent
	};

	public static final Map<String, Class<? extends GraphicsOperator>> operatorMap = 
			new HashMap<String, Class<? extends GraphicsOperator>>();

	static {
		// General graphics state
		operatorMap.put("w", OP_w.class);
		operatorMap.put("J", OP_J_.class);
		operatorMap.put("j", OP_j.class);
		operatorMap.put("M", OP_M_.class);
		operatorMap.put("d", OP_d.class);
		operatorMap.put("ri", OP_ri.class);
		operatorMap.put("i", OP_i.class);
		operatorMap.put("gs", OP_gs.class);
		
		// Special graphics state
		operatorMap.put("q", OP_q.class);
		operatorMap.put("Q", OP_Q_.class);
		operatorMap.put("cm", OP_cm.class);
		
		// Path construction
		operatorMap.put("m", OP_m.class);
		operatorMap.put("l", OP_l.class);
		operatorMap.put("c", OP_c.class);
		operatorMap.put("v", OP_v.class);
		operatorMap.put("y", OP_y.class);
		operatorMap.put("h", OP_h.class);
		operatorMap.put("re", OP_re.class);
		
		// Path painting
		operatorMap.put("S", OP_S_.class);
		operatorMap.put("s", OP_s.class);
		operatorMap.put("f", OP_f.class);
		operatorMap.put("F", OP_F_.class);
		operatorMap.put("f*", OP_f42.class);
		operatorMap.put("B", OP_B_.class);
		operatorMap.put("B*", OP_B_42.class);
		operatorMap.put("b", OP_b.class);
		operatorMap.put("b*", OP_b42.class);
		operatorMap.put("n", OP_n.class);
		
		// Clipping paths
		operatorMap.put("W", OP_W_.class);
		operatorMap.put("W*", OP_W_42.class);
		
		// Text objects
		operatorMap.put("BT", OP_BT_.class);
		operatorMap.put("ET", OP_ET_.class);
		
		// Text state
		operatorMap.put("Tc", OP_T_c.class);
		operatorMap.put("Tw", OP_T_w.class);
		operatorMap.put("Tz", OP_T_z.class);
		operatorMap.put("TL", OP_TL_.class);
		operatorMap.put("Tf", OP_T_f.class);
		operatorMap.put("Tr", OP_T_r.class);
		operatorMap.put("Ts", OP_T_s.class);
		
		// Text positioning
		operatorMap.put("Td", OP_T_d.class);
		operatorMap.put("TD", OP_TD_.class);
		operatorMap.put("Tm", OP_T_m.class);
		operatorMap.put("T*", OP_T_42.class);
		
		// Text showing
		operatorMap.put("Tj", OP_T_j.class);
		operatorMap.put("TJ", OP_TJ_.class);
		operatorMap.put("\'", OP_39.class);
		operatorMap.put("\"", OP_34.class);
		
		// Type 3 fonts
		operatorMap.put("d0", OP_d0.class);
		operatorMap.put("d1", OP_d1.class);
		
		// Color
		operatorMap.put("CS", OP_CS_.class);
		operatorMap.put("cs", OP_cs.class);
		operatorMap.put("SC", OP_SC_.class);
		operatorMap.put("SCN", OP_SCN_.class);
		operatorMap.put("sc", OP_sc.class);
		operatorMap.put("scn", OP_scn.class);
		operatorMap.put("G", OP_G_.class);
		operatorMap.put("g", OP_g.class);
		operatorMap.put("RG", OP_RG_.class);
		operatorMap.put("rg", OP_rg.class);
		operatorMap.put("K", OP_K_.class);
		operatorMap.put("k", OP_k.class);
		
		// Shading patterns
		operatorMap.put("sh", OP_sh.class);
		
		// Inline images
		operatorMap.put("BI", OP_BI_.class);
		operatorMap.put("ID", OP_ID_.class);
		operatorMap.put("EI", OP_EI_.class);
		
		// XObjects
		operatorMap.put("Do", OP_D_o.class);
		
		// Marked content
		operatorMap.put("MP", OP_MP_.class);
		operatorMap.put("DP", OP_DP_.class);
		operatorMap.put("BMC", OP_BMC_.class);
		operatorMap.put("BDC", OP_BDC_.class);
		operatorMap.put("EMC", OP_EMC_.class);
		
		// Compatibility
		operatorMap.put("BX", OP_BX_.class);
		operatorMap.put("EX", OP_EX_.class);
	}

	byte[] operatorBytes;
	int operandNumber;

	protected GraphicsOperator(byte[] operatorBytes) {
		this.operatorBytes = operatorBytes;
	}

	public static final GraphicsOperator create(byte[] operatorBytes) {
		return create(new String(operatorBytes));
	}

	public static final GraphicsOperator create(String operator) {
		Class<? extends GraphicsOperator> c = operatorMap.get(operator);
		if (null == c) {
			throw new SyntaxException("Illegal operator: " + operator);
		}
		try {
			GraphicsOperator op = c.newInstance();
			return op;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean isGraphicsOperator(byte[] operatorBytes) {
		if (null == operatorBytes || operatorBytes.length <= 0) {
			return false;
		}
		String op = new String(operatorBytes);
		return operatorMap.containsKey(op);
	}

	public String toString() {
		return new String(this.operatorBytes);
	}

	public int getOperandNumber() {
		return this.operandNumber;
	}

	abstract public void execute(Queue<GraphicsOperand> operandStack,
			VirtualGraphics g2d);
	
	@Override
	protected void readBeginTag(ObjectReader reader) {
	}

	@Override
	protected void readBody(ObjectReader reader) {
		reader.readBytes(this.operatorBytes.length);
	}

	@Override
	protected void readEndTag(ObjectReader reader) {
	}
}

//General graphics state w, J, j, M, d, ri, i, gs
class OP_w extends GraphicsOperator {

	public OP_w() {
		super(new byte[] { 'w'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_J_ extends GraphicsOperator {

	public OP_J_() {
		super(new byte[] { 'J'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_j extends GraphicsOperator {

	public OP_j() {
		super(new byte[] { 'j'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_M_ extends GraphicsOperator {

	public OP_M_() {
		super(new byte[] { 'M'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_d extends GraphicsOperator {

	public OP_d() {
		super(new byte[] { 'd'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_ri extends GraphicsOperator {

	public OP_ri() {
		super(new byte[] { 'r', 'i'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_i extends GraphicsOperator {

	public OP_i() {
		super(new byte[] { 'i'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_gs extends GraphicsOperator {

	public OP_gs() {
		super(new byte[] { 'g', 's'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

// Special graphics state q, Q, cm

/**
 * Save the current graphics state on the graphics state stack
 * */
class OP_q extends GraphicsOperator {
	public OP_q() {
		super(new byte[]{'q'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
		g2d.saveGraphicsState();
	}
}

class OP_Q_ extends GraphicsOperator {
	/**
	 * Restore the graphics state by removing the most recently saved state from
	 * the stack and making it the current state
	 * */
	public OP_Q_() {
		super(new byte[] { 'Q'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
		g2d.restoreGraphicsState();
	}
}

class OP_cm extends GraphicsOperator {
	/**
	 * Modify the current transformation matrix (CTM) by concatenating the
	 * specified matrix
	 * */
	public OP_cm() {
		super(new byte[] { 'c', 'm'});
		this.operandNumber = 6;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
		GraphicsOperand operand_1 = operandStack.poll();
		GraphicsOperand operand_2 = operandStack.poll();
		GraphicsOperand operand_3 = operandStack.poll();
		GraphicsOperand operand_4 = operandStack.poll();
		GraphicsOperand operand_5 = operandStack.poll();
		GraphicsOperand operand_6 = operandStack.poll();

		double a = operand_1.asNumber().doubleValue();
		double b = operand_2.asNumber().doubleValue();
		double c = operand_3.asNumber().doubleValue();
		double d = operand_4.asNumber().doubleValue();
		double e = operand_5.asNumber().doubleValue();
		double f = operand_6.asNumber().doubleValue();
		
		g2d.concatenate(new Matrix(a, b, c, d, e, f));
	}
}

// Path construction m, l, c, v, y, h, re
class OP_m extends GraphicsOperator {

	public OP_m() {
		super(new byte[] { 'm'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_l extends GraphicsOperator {

	public OP_l() {
		super(new byte[] { 'l'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_c extends GraphicsOperator {

	public OP_c() {
		super(new byte[] { 'c'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_v extends GraphicsOperator {

	public OP_v() {
		super(new byte[] { 'v'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_y extends GraphicsOperator {

	public OP_y() {
		super(new byte[] { 'y'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_h extends GraphicsOperator {

	public OP_h() {
		super(new byte[] { 'h'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_re extends GraphicsOperator {
	/**
	 * Append a rectangle to the current path as a complete subpath, 
	 * with lower-left corner (x, y) and dimensions width and height in user space.
	 * */
	public OP_re() {
		super(new byte[] { 'r', 'e' });
		this.operandNumber = 4;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
		GraphicsOperand operand_1 = operandStack.poll();
		GraphicsOperand operand_2 = operandStack.poll();
		GraphicsOperand operand_3 = operandStack.poll();
		GraphicsOperand operand_4 = operandStack.poll();

		double x = operand_1.asNumber().doubleValue();
		double y = operand_2.asNumber().doubleValue();
		double w = operand_3.asNumber().doubleValue();
		double h = operand_4.asNumber().doubleValue();
		
		g2d.beginPath(x, y, w, h);
	}
}

// Path painting S, s, f, F, f*, B, B*, b, b*, n
class OP_S_ extends GraphicsOperator {

	public OP_S_() {
		super(new byte[] { 'S'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_s extends GraphicsOperator {

	public OP_s() {
		super(new byte[] { 's'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_f extends GraphicsOperator {

	public OP_f() {
		super(new byte[] { 'f'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_F_ extends GraphicsOperator {

	public OP_F_() {
		super(new byte[] { 'F'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_f42 extends GraphicsOperator {

	public OP_f42() {
		super(new byte[] { 'f', '*'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_B_ extends GraphicsOperator {

	public OP_B_() {
		super(new byte[] { 'B'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_B_42 extends GraphicsOperator {

	public OP_B_42() {
		super(new byte[] { 'B', '*'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_b extends GraphicsOperator {

	public OP_b() {
		super(new byte[] { 'b'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_b42 extends GraphicsOperator {

	public OP_b42() {
		super(new byte[] { 'b', '*'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_n extends GraphicsOperator {
	/**
	 * End the path object without filling or stroking it. 
	 * This operator is a path-painting no-op, used primarily for the 
	 * side effect of changing the current clipping path
	 * */
	public OP_n() {
		super(new byte[] { 'n'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
		g2d.closePath();
	}
}

// Clipping paths W, W*
class OP_W_ extends GraphicsOperator {
	/**
	 * Modify the current clipping path by intersecting it with the current path, 
	 * using the nonzero winding number rule to determine which regions lie 
	 * inside the clipping path.
	 * */
	public OP_W_() {
		super(new byte[] { 'W'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_W_42 extends GraphicsOperator {

	public OP_W_42() {
		super(new byte[] { 'W', '*'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

// Text objects BT, ET
class OP_BT_ extends GraphicsOperator {
	/**
	 * Begin a text object, initializing the text matrix, Tm , and the text line
	 * matrix, Tlm , to the identity matrix.
	 * */
	public OP_BT_() {
		super(new byte[] { 'B', 'T'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
		g2d.beginText();
	}
}

class OP_ET_ extends GraphicsOperator {
	/**
	 * End a text object, discarding the text matrix.
	 * */
	public OP_ET_() {
		super(new byte[] { 'E', 'T'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
		g2d.endText();
	}
}

//Text state Tc, Tw, Tz, TL, Tf, Tr, Ts
class OP_T_c extends GraphicsOperator {

	public OP_T_c() {
		super(new byte[] { 'T', 'c'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_T_w extends GraphicsOperator {

	public OP_T_w() {
		super(new byte[] { 'T', 'w'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_T_z extends GraphicsOperator {

	public OP_T_z() {
		super(new byte[] { 'T', 'z'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_TL_ extends GraphicsOperator {

	public OP_TL_() {
		super(new byte[] { 'T', 'L'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_T_f extends GraphicsOperator {
	/**
	 * Set the text font to font and the text font size.
	 * */
	public OP_T_f() {
		super(new byte[] { 'T', 'f'});
		this.operandNumber = 2;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
		GraphicsOperand operand_1 = operandStack.poll();
		GraphicsOperand operand_2 = operandStack.poll();
	}
}

class OP_T_r extends GraphicsOperator {

	public OP_T_r() {
		super(new byte[] { 'T', 'r'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_T_s extends GraphicsOperator {

	public OP_T_s() {
		super(new byte[] { 'T', 's'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

// Text positioning Td, TD, Tm, T*
class OP_T_d extends GraphicsOperator {

	public OP_T_d() {
		super(new byte[] { 'T', 'd'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_TD_ extends GraphicsOperator {

	public OP_TD_() {
		super(new byte[] { 'T', 'D'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_T_m extends GraphicsOperator {

	public OP_T_m() {
		super(new byte[] {'T', 'm'});
		this.operandNumber = 6;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
		GraphicsOperand operand_1 = operandStack.poll();
		GraphicsOperand operand_2 = operandStack.poll();
		GraphicsOperand operand_3 = operandStack.poll();
		GraphicsOperand operand_4 = operandStack.poll();
		GraphicsOperand operand_5 = operandStack.poll();
		GraphicsOperand operand_6 = operandStack.poll();

		double a = operand_1.asNumber().doubleValue();
		double b = operand_2.asNumber().doubleValue();
		double c = operand_3.asNumber().doubleValue();
		double d = operand_4.asNumber().doubleValue();
		double e = operand_5.asNumber().doubleValue();
		double f = operand_6.asNumber().doubleValue();
		
		g2d.transformTextMatrix(new Matrix(a, b, c, d, e, f));
	}
}

class OP_T_42 extends GraphicsOperator {

	public OP_T_42() {
		super(new byte[] { 'T', '*'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

// Text showing Tj, TJ, ', "
class OP_T_j extends GraphicsOperator {
	/**
	 * Show a text string.
	 * */
	public OP_T_j() {
		super(new byte[] { 'T', 'j'});
		this.operandNumber = 1;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
		GraphicsOperand operand = operandStack.poll();
		PString s = operand.asString();
		
		g2d.showText(s.toJavaString());
	}
}

class OP_TJ_ extends GraphicsOperator {

	public OP_TJ_() {
		super(new byte[] { 'T', 'J'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_39 extends GraphicsOperator {

	public OP_39() {
		super(new byte[] { '\'' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_34 extends GraphicsOperator {

	public OP_34() {
		super(new byte[] { '\"' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

// Type 3 fonts d0, d1
class OP_d0 extends GraphicsOperator {

	public OP_d0() {
		super(new byte[] { 'd', '0' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_d1 extends GraphicsOperator {

	public OP_d1() {
		super(new byte[] { 'd', '1' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

// Color CS, cs, SC, SCN, sc, scn, G, g, RG, rg, K, k
class OP_CS_ extends GraphicsOperator {

	public OP_CS_() {
		super(new byte[] { 'C', 'S' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_cs extends GraphicsOperator {

	public OP_cs() {
		super(new byte[] { 'c', 's' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_SC_ extends GraphicsOperator {

	public OP_SC_() {
		super(new byte[] { 'S', 'C' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_SCN_ extends GraphicsOperator {

	public OP_SCN_() {
		super(new byte[] { 'S', 'C', 'N' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_sc extends GraphicsOperator {

	public OP_sc() {
		super(new byte[] { 's', 'c' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_scn extends GraphicsOperator {

	public OP_scn() {
		super(new byte[] { 's', 'c', 'n'});
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_G_ extends GraphicsOperator {

	public OP_G_() {
		super(new byte[] { 'G' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_g extends GraphicsOperator {

	public OP_g() {
		super(new byte[] { 'g' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_RG_ extends GraphicsOperator {

	public OP_RG_() {
		super(new byte[] { 'R', 'G' });
		this.operandNumber = 3;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_rg extends GraphicsOperator {
	/**
	 * Same as RG but used for nonstroking operations, color space to DeviceGray.
	 * */
	public OP_rg() {
		super(new byte[] { 'r', 'g' });
		this.operandNumber = 3;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
		GraphicsOperand operand_1 = operandStack.poll();
		GraphicsOperand operand_2 = operandStack.poll();
		GraphicsOperand operand_3 = operandStack.poll();
	}
}

class OP_K_ extends GraphicsOperator {

	public OP_K_() {
		super(new byte[] { 'K' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_k extends GraphicsOperator {

	public OP_k() {
		super(new byte[] { 'k' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

// Shading patterns sh
class OP_sh extends GraphicsOperator {

	public OP_sh() {
		super(new byte[] { 's', 'h' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

// Inline images BI, ID, EI
class OP_BI_ extends GraphicsOperator {

	public OP_BI_() {
		super(new byte[] { 'B', 'I' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_ID_ extends GraphicsOperator {

	public OP_ID_() {
		super(new byte[] { 'I', 'D' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_EI_ extends GraphicsOperator {

	public OP_EI_() {
		super(new byte[] { 'E', 'I' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

// XObjects Do
class OP_D_o extends GraphicsOperator {

	public OP_D_o() {
		super(new byte[] { 'D', 'o' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

// Marked content MP, DP, BMC, BDC, EMC
class OP_MP_ extends GraphicsOperator {

	public OP_MP_() {
		super(new byte[] { 'M', 'P' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_DP_ extends GraphicsOperator {

	public OP_DP_() {
		super(new byte[] { 'D', 'P' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_BMC_ extends GraphicsOperator {

	public OP_BMC_() {
		super(new byte[] { 'B', 'M', 'C' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_BDC_ extends GraphicsOperator {

	public OP_BDC_() {
		super(new byte[] { 'B', 'D', 'C' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_EMC_ extends GraphicsOperator {

	public OP_EMC_() {
		super(new byte[] { 'E', 'M', 'C' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

// Compatibility BX, EX
class OP_BX_ extends GraphicsOperator {

	public OP_BX_() {
		super(new byte[] { 'B', 'X' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}

class OP_EX_ extends GraphicsOperator {

	public OP_EX_() {
		super(new byte[] { 'E', 'X' });
		this.operandNumber = 0;
	}

	@Override
	public void execute(Queue<GraphicsOperand> operandStack, VirtualGraphics g2d) {
	}
}