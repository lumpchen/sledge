package me.lumpchen.sledge.pdf.graphics.operator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.lumpchen.sledge.pdf.graphics.GraphicsOperand;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.graphics.operator.ClippingPathsOperator.OP_W_;
import me.lumpchen.sledge.pdf.graphics.operator.ClippingPathsOperator.OP_W_42;
import me.lumpchen.sledge.pdf.graphics.operator.ColorOperator.OP_CS_;
import me.lumpchen.sledge.pdf.graphics.operator.ColorOperator.OP_G_;
import me.lumpchen.sledge.pdf.graphics.operator.ColorOperator.OP_K_;
import me.lumpchen.sledge.pdf.graphics.operator.ColorOperator.OP_RG_;
import me.lumpchen.sledge.pdf.graphics.operator.ColorOperator.OP_SCN_;
import me.lumpchen.sledge.pdf.graphics.operator.ColorOperator.OP_SC_;
import me.lumpchen.sledge.pdf.graphics.operator.ColorOperator.OP_cs;
import me.lumpchen.sledge.pdf.graphics.operator.ColorOperator.OP_g;
import me.lumpchen.sledge.pdf.graphics.operator.ColorOperator.OP_k;
import me.lumpchen.sledge.pdf.graphics.operator.ColorOperator.OP_rg;
import me.lumpchen.sledge.pdf.graphics.operator.ColorOperator.OP_sc;
import me.lumpchen.sledge.pdf.graphics.operator.ColorOperator.OP_scn;
import me.lumpchen.sledge.pdf.graphics.operator.CompatibilityOperator.OP_BX_;
import me.lumpchen.sledge.pdf.graphics.operator.CompatibilityOperator.OP_EX_;
import me.lumpchen.sledge.pdf.graphics.operator.GeneralGraphicsStateOperator.OP_J_;
import me.lumpchen.sledge.pdf.graphics.operator.GeneralGraphicsStateOperator.OP_M_;
import me.lumpchen.sledge.pdf.graphics.operator.GeneralGraphicsStateOperator.OP_d;
import me.lumpchen.sledge.pdf.graphics.operator.GeneralGraphicsStateOperator.OP_gs;
import me.lumpchen.sledge.pdf.graphics.operator.GeneralGraphicsStateOperator.OP_i;
import me.lumpchen.sledge.pdf.graphics.operator.GeneralGraphicsStateOperator.OP_j;
import me.lumpchen.sledge.pdf.graphics.operator.GeneralGraphicsStateOperator.OP_ri;
import me.lumpchen.sledge.pdf.graphics.operator.GeneralGraphicsStateOperator.OP_w;
import me.lumpchen.sledge.pdf.graphics.operator.InlineImagesOperator.OP_BI_;
import me.lumpchen.sledge.pdf.graphics.operator.InlineImagesOperator.OP_EI_;
import me.lumpchen.sledge.pdf.graphics.operator.InlineImagesOperator.OP_ID_;
import me.lumpchen.sledge.pdf.graphics.operator.MarkedContentOperator.OP_BDC_;
import me.lumpchen.sledge.pdf.graphics.operator.MarkedContentOperator.OP_BMC_;
import me.lumpchen.sledge.pdf.graphics.operator.MarkedContentOperator.OP_DP_;
import me.lumpchen.sledge.pdf.graphics.operator.MarkedContentOperator.OP_EMC_;
import me.lumpchen.sledge.pdf.graphics.operator.MarkedContentOperator.OP_MP_;
import me.lumpchen.sledge.pdf.graphics.operator.PathConstructionOperator.OP_c;
import me.lumpchen.sledge.pdf.graphics.operator.PathConstructionOperator.OP_h;
import me.lumpchen.sledge.pdf.graphics.operator.PathConstructionOperator.OP_l;
import me.lumpchen.sledge.pdf.graphics.operator.PathConstructionOperator.OP_m;
import me.lumpchen.sledge.pdf.graphics.operator.PathConstructionOperator.OP_re;
import me.lumpchen.sledge.pdf.graphics.operator.PathConstructionOperator.OP_v;
import me.lumpchen.sledge.pdf.graphics.operator.PathConstructionOperator.OP_y;
import me.lumpchen.sledge.pdf.graphics.operator.PathPaintingOperator.OP_B_;
import me.lumpchen.sledge.pdf.graphics.operator.PathPaintingOperator.OP_B_42;
import me.lumpchen.sledge.pdf.graphics.operator.PathPaintingOperator.OP_F_;
import me.lumpchen.sledge.pdf.graphics.operator.PathPaintingOperator.OP_S_;
import me.lumpchen.sledge.pdf.graphics.operator.PathPaintingOperator.OP_b;
import me.lumpchen.sledge.pdf.graphics.operator.PathPaintingOperator.OP_b42;
import me.lumpchen.sledge.pdf.graphics.operator.PathPaintingOperator.OP_f;
import me.lumpchen.sledge.pdf.graphics.operator.PathPaintingOperator.OP_f42;
import me.lumpchen.sledge.pdf.graphics.operator.PathPaintingOperator.OP_n;
import me.lumpchen.sledge.pdf.graphics.operator.PathPaintingOperator.OP_s;
import me.lumpchen.sledge.pdf.graphics.operator.ShadingPatternsOperator.OP_sh;
import me.lumpchen.sledge.pdf.graphics.operator.SpecialGraphicsStateOperator.OP_Q_;
import me.lumpchen.sledge.pdf.graphics.operator.SpecialGraphicsStateOperator.OP_cm;
import me.lumpchen.sledge.pdf.graphics.operator.SpecialGraphicsStateOperator.OP_q;
import me.lumpchen.sledge.pdf.graphics.operator.TextObjectsOperator.OP_BT_;
import me.lumpchen.sledge.pdf.graphics.operator.TextObjectsOperator.OP_ET_;
import me.lumpchen.sledge.pdf.graphics.operator.TextPositioningOperator.OP_TD_;
import me.lumpchen.sledge.pdf.graphics.operator.TextPositioningOperator.OP_T_42;
import me.lumpchen.sledge.pdf.graphics.operator.TextPositioningOperator.OP_T_d;
import me.lumpchen.sledge.pdf.graphics.operator.TextPositioningOperator.OP_T_m;
import me.lumpchen.sledge.pdf.graphics.operator.TextShowingOperator.OP_34;
import me.lumpchen.sledge.pdf.graphics.operator.TextShowingOperator.OP_39;
import me.lumpchen.sledge.pdf.graphics.operator.TextShowingOperator.OP_TJ_;
import me.lumpchen.sledge.pdf.graphics.operator.TextShowingOperator.OP_T_j;
import me.lumpchen.sledge.pdf.graphics.operator.TextStateOperator.OP_TL_;
import me.lumpchen.sledge.pdf.graphics.operator.TextStateOperator.OP_T_c;
import me.lumpchen.sledge.pdf.graphics.operator.TextStateOperator.OP_T_f;
import me.lumpchen.sledge.pdf.graphics.operator.TextStateOperator.OP_T_r;
import me.lumpchen.sledge.pdf.graphics.operator.TextStateOperator.OP_T_s;
import me.lumpchen.sledge.pdf.graphics.operator.TextStateOperator.OP_T_w;
import me.lumpchen.sledge.pdf.graphics.operator.TextStateOperator.OP_T_z;
import me.lumpchen.sledge.pdf.graphics.operator.Type3FontsOperator.OP_d0;
import me.lumpchen.sledge.pdf.graphics.operator.Type3FontsOperator.OP_d1;
import me.lumpchen.sledge.pdf.graphics.operator.XObjectsOperator.OP_D_o;
import me.lumpchen.sledge.pdf.syntax.SyntaxException;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.document.Page;

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

	protected byte[] operatorBytes;
	protected int operandNumber;
	protected  List<GraphicsOperand> operandList;

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
		StringBuilder sbuf = new StringBuilder();
		sbuf.append(new String(this.operatorBytes));
		
		if (this.operandList != null && this.operandList.size() == this.operandNumber) {
			for (int i = 0; i < this.operandNumber; i++) {
				sbuf.append(" ");
				sbuf.append(this.operandList.get(i).toString());
			}			
		}
		
		return sbuf.toString();
	}

	public int getOperandNumber() {
		return this.operandNumber;
	}
	
	public void addOperator(GraphicsOperand oparand) {
		if (null == this.operandList) {
			this.operandList = new ArrayList<GraphicsOperand>();
		}
		if (this.operandNumber < this.operandList.size()) {
			throw new SyntaxException("exceed the limit size of operator: " + this.toString());
		}
		this.operandList.add(oparand);
	}

	abstract public void execute(VirtualGraphics g2d, Page page);

}
