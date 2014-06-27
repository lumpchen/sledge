package com.docscience.pathfinder.font.driver.type2;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.docscience.pathfinder.font.driver.cff.CFFSubrIndex;
import com.docscience.pathfinder.font.shared.GlyphPath;

/**
 * Parse and Represent Type2 CharString format.
 * 
 * @author wxin
 * 
 */
public final class Type2CharString {
    
    public static final int PATH_MOVETO = 0;
    public static final int PATH_LINETO = 1;
    public static final int PATH_CURVETO = 2;
    public static final int PATH_CLOSE = 3;
    
    public static final int ARGSSTACK_LIMIT = 48;
    public static final int SUBRSTACK_LIMIT = 11;
    public static final int TRANARRAY_LIMIT = 32;
    
    private static final int OP_HSTEM = 0x01;
    private static final int OP_VSTEM = 0x03;
    private static final int OP_VMOVETO = 0x04;
    private static final int OP_RLINETO = 0x05;
    private static final int OP_HLINETO = 0x06;
    private static final int OP_VLINETO = 0x07;
    private static final int OP_RRCURVETO = 0x08;
    private static final int OP_CALLSUBR = 0x0a;
    private static final int OP_RETURN = 0x0b;
    private static final int OP_ESCAPE = 0x0c;
    private static final int OP_ENDCHAR = 0x0e;
    private static final int OP_HSTEMHM = 0x12;
    private static final int OP_HINTMASK = 0x13;
    private static final int OP_CNTRMASK = 0x14;
    private static final int OP_RMOVETO = 0x15;
    private static final int OP_HMOVETO = 0x16;
    private static final int OP_VSTEMHM = 0x17;
    private static final int OP_RCURVELINE = 0x18;
    private static final int OP_RLINECURVE = 0x19;
    private static final int OP_VVCURVETO = 0x1a;
    private static final int OP_HHCURVETO = 0x1b;
    private static final int OP_CALLGSUBR = 0x1d;
    private static final int OP_VHCURVETO = 0x1e;
    private static final int OP_HVCURVETO = 0x1f;
    
    private static final int OP_DOTSECTION = 0x0c00;
    private static final int OP_AND = 0x0c03;
    private static final int OP_OR = 0x0c04;
    private static final int OP_NOT = 0x0c05;
    private static final int OP_ABS = 0x0c09;
    private static final int OP_ADD = 0x0c0a;
    private static final int OP_SUB = 0x0c0b;
    private static final int OP_DIV = 0x0c0c;
    private static final int OP_NEG = 0x0c0e;
    private static final int OP_EQ = 0x0c0f;
    private static final int OP_DROP = 0x0c12;
    private static final int OP_PUT = 0x0c14;
    private static final int OP_GET = 0x0c15;
    private static final int OP_IFELSE = 0x0c16;
    private static final int OP_RANDOM = 0x0c17;
    private static final int OP_MUL = 0x0c18;
    private static final int OP_SQRT = 0x0c1a;
    private static final int OP_DUP = 0x0c1b;
    private static final int OP_EXCH = 0x0c1c;
    private static final int OP_INDEX = 0x0c1d;
    private static final int OP_ROLL = 0x0c1e;
    private static final int OP_HFLEX = 0x0c22;
    private static final int OP_FLEX = 0x0c23;
    private static final int OP_HFLEX1 = 0x0c24;
    private static final int OP_FLEX1 = 0x0c25;
    
    private transient SubrCS[] subrStack;
    private transient int subrDeeps;
    private transient double[] argsStack;
    private transient int argsDeeps;
    private transient boolean finished;
    private transient double[] tranArray;
    private transient double x;
    private transient double y;
    private transient int nHints;
    
    private CFFSubrIndex globalSubrs;
    private CFFSubrIndex localSubrs;
    
    private double deltaWidth = Double.NaN;
    private ArrayList<PathNode> glyphPath;
    private ArrayList<double[]> hstems;
    private ArrayList<double[]> vstems;
    private SEAC seac;
    
    private static class SubrCS {
        byte[] code;
        int ip;
    };
    
    public static class PathNode {
        public int tag;
        public double x1;
        public double y1;
        public double x2;
        public double y2;
        public double x3;
        public double y3;
    };
    
    public static class SEAC {
        // double asb // always zero
        public double adx;
        public double ady;
        public int bchar;
        public int achar;
    };
    
    /**
     * Parse charString bytes array, generate a Type2 CharString instance. Both 
     * globalSubrs and localSubrs can be null.
     * 
     * @param globalSubrs
     * @param localSubrs
     * @param charString
     * @throws Type2CharStringException
     */
    public Type2CharString(CFFSubrIndex globalSubrs, CFFSubrIndex localSubrs, byte[] charString) throws Type2CharStringException {
        assert(charString != null);
        this.globalSubrs = globalSubrs;
        this.localSubrs = localSubrs;
        this.subrStack = new SubrCS[SUBRSTACK_LIMIT];
        this.argsStack = new double[ARGSSTACK_LIMIT];
        this.tranArray = new double[TRANARRAY_LIMIT];
        this.glyphPath = new ArrayList<PathNode>();
        this.hstems = new ArrayList<double[]>();
        this.vstems = new ArrayList<double[]>();
        pushSubr(charString);
        execute();
    }
    
    public double getDeltaWidth() {
        return deltaWidth;
    }
    
    public SEAC getSEAC() {
        return seac;
    }
    
    public PathNode[] getPathNodes() {
        return glyphPath.toArray(new PathNode[glyphPath.size()]);
    }
       
    public Rectangle2D getBoundingBox() {
    	double xMax = Double.MIN_VALUE;
    	double yMax = Double.MIN_VALUE;
    	double xMin = Double.MAX_VALUE;
    	double yMin = Double.MAX_VALUE;
    	for (int i=0; i<glyphPath.size(); ++i) {
    		PathNode node = glyphPath.get(i);
    		switch (node.tag) {
    		case PATH_MOVETO:
    			xMax = Math.max(xMax, node.x1);
    			yMax = Math.max(yMax, node.y1);
    			xMin = Math.min(xMin, node.x1);
    			yMin = Math.min(yMin, node.y1);
    			break;
    		case PATH_LINETO:
    			xMax = Math.max(xMax, node.x1);
    			yMax = Math.max(yMax, node.y1);
    			xMin = Math.min(xMin, node.x1);
    			yMin = Math.min(yMin, node.y1);
    			break;
    		case PATH_CURVETO:
    			xMax = Math.max(xMax, node.x1);
    			yMax = Math.max(yMax, node.y1);
    			xMin = Math.min(xMin, node.x1);
    			yMin = Math.min(yMin, node.y1);
    			xMax = Math.max(xMax, node.x2);
    			yMax = Math.max(yMax, node.y2);
    			xMin = Math.min(xMin, node.x2);
    			yMin = Math.min(yMin, node.y2);
    			xMax = Math.max(xMax, node.x3);
    			yMax = Math.max(yMax, node.y3);
    			xMin = Math.min(xMin, node.x3);
    			yMin = Math.min(yMin, node.y3);
    			break;
    		default:
    			// do nothing here
    		}
    	}
    	return new Rectangle2D.Double(xMin, yMin, xMax-xMin, yMax-yMin);
    }
    
    public GlyphPath getPath() {
    	GlyphPath path = new GlyphPath();
        for (int i=0; i<glyphPath.size(); ++i) {
            PathNode node = glyphPath.get(i);
            switch (node.tag) {
            case PATH_MOVETO:
                path.moveTo((float) node.x1, (float) node.y1);
                break;
            case PATH_LINETO:
                path.lineTo((float) node.x1, (float) node.y1);
                break;
            case PATH_CURVETO:
                path.curveTo((float) node.x1, (float) node.y1, 
                          (float) node.x2, (float) node.y2, 
                          (float) node.x3, (float) node.y3);
                break;
            case PATH_CLOSE:
                path.closePath();
                break;
            default:
                assert(false) : "never goes here";
            }
        }
        return path;
    }
        
    public int getNumHStems() {
        return hstems.size();
    }
    
    public double[] getHStem(int i) {
        return hstems.get(i);
    }
    
    public int getNumVStems() {
        return vstems.size();
    }
    
    public double[] getVStem(int i) {
        return vstems.get(i);
    }
    
    private void execute() throws Type2CharStringException {
        boolean customizeWidth = true;
        x = 0;
        y = 0;
        nHints = 0;
        
        while (!finished) {
            int c = nextByte();
            assert(c >= 0 && c <= 255);
            if (c == 28) {
                // following 2 byte is a 16-bit number.
                double d = (short)((nextByte() << 8) + nextByte());
                if (argsDeeps >= ARGSSTACK_LIMIT) {
                    throw new Type2CharStringException("argument stack overflow");
                }
                argsStack[argsDeeps++] = d;
            }
            else if (c >= 32) {
                if (argsDeeps >= ARGSSTACK_LIMIT) {
                    throw new Type2CharStringException("argument stack overflow");
                }
                if (c <= 246) {
                    argsStack[argsDeeps++] = c - 139;
                }
                else if (c <= 250) {
                    double d = (c - 247) * 256 + nextByte() + 108;
                    argsStack[argsDeeps++] = d;
                }
                else if (c <= 254) {
                    double d = -((c - 251) * 256) - nextByte() - 108;
                    argsStack[argsDeeps++] = d;
                }
                else if (c == 255) {
                    // following 4 byte is a 16/16 fixed number.
                    double i = (nextByte() << 8) + nextByte();
                    double f = 1.0 / (double) ((nextByte() << 8) + nextByte());
                    argsStack[argsDeeps++] = i + f;
                }
                else {
                    assert(false) : "never goes here";
                }
            }
            else {
                if (customizeWidth) {
                    switch(c) {
                    case OP_HSTEM:
                    case OP_VSTEM:
                    case OP_HSTEMHM:
                    case OP_VSTEMHM:
                    case OP_RMOVETO:
                    case OP_CNTRMASK:
                    case OP_HINTMASK:
                        if ((argsDeeps & 1) != 0) {
                            customizeWidth = false;
                            deltaWidth = argsStack[0];
                            for (int i=0; i<argsDeeps-1; ++i) {
                                argsStack[i] = argsStack[i+1];
                            }
                            argsDeeps--;
                        }
                        break;
                    case OP_HMOVETO:
                    case OP_VMOVETO:
                        if (argsDeeps == 2) {
                            customizeWidth = false;
                            deltaWidth = argsStack[0];
                            for (int i=0; i<argsDeeps-1; ++i) {
                                argsStack[i] = argsStack[i+1];
                            }
                            argsDeeps--;
                        }
                        break;
                    case OP_ENDCHAR:
                        if (argsDeeps == 5 || argsDeeps == 1) {
                            customizeWidth = false;
                            deltaWidth = argsStack[0];
                            for (int i=0; i<argsDeeps-1; ++i) {
                                argsStack[i] = argsStack[i+1];
                            }
                            argsDeeps--;
                        }
                        break;
                    }
                }
                dispatch(c);
            }
        } // end of while
        subrStack = null;
        subrDeeps = 0;
        argsStack = null;
        argsDeeps = 0;
        tranArray = null;
    }
    
    private void dispatch(int c) throws Type2CharStringException {
        switch(c) {
        case OP_HSTEM:
        case OP_HSTEMHM:
            {
                if (argsDeeps == 0 || argsDeeps % 2 != 0) {
                    throw new Type2CharStringException("illegal arguments number for operator hstem/hstemhm");
                }
                double[] stem = new double[argsDeeps];
                System.arraycopy(argsStack, 0, stem, 0, argsDeeps);
                hstems.add(stem);
                nHints += argsDeeps / 2;
                argsDeeps = 0;
            }
            break;
        case OP_VSTEM:
        case OP_VSTEMHM:
            {
                if (argsDeeps == 0 || argsDeeps % 2 != 0) {
                    throw new Type2CharStringException("illegal arguments number for operator vstem/vstemhm");
                }
                double[] stem = new double[argsDeeps];
                System.arraycopy(argsStack, 0, stem, 0, argsDeeps);
                vstems.add(stem);
                nHints += argsDeeps / 2;
                argsDeeps = 0;
            }
            break;
        case OP_HINTMASK:
            {
                nHints += argsDeeps / 2;
                int nBytes = (nHints + 7) / 8;
                for (int i=0; i<nBytes; ++i) {
                    nextByte();
                }
                argsDeeps = 0;
            }
            break;
        case OP_CNTRMASK:
            {
                nHints += argsDeeps / 2;
                int nBytes = (nHints + 7) / 8;
                for (int i=0; i<nBytes; ++i) {
                    nextByte();
                }
                argsDeeps = 0;
            }
            break;
        case OP_VMOVETO:
            {
                if (argsDeeps != 1) {
                    throw new Type2CharStringException("illegal arguments number for operator vmoveto");
                }
                y += argsStack[0];
                argsDeeps = 0;
                moveTo(x, y);
            }
            break;
        case OP_RLINETO:
            {
                if (argsDeeps < 2 || argsDeeps % 2 != 0) {
                    throw new Type2CharStringException("illegal arguments number for operator rlineto");
                }
                for (int i=0; i<argsDeeps; i += 2) {
                    double dx = argsStack[i];
                    double dy = argsStack[i+1];
                    x += dx;
                    y += dy;
                    lineTo(x, y);
                }
                argsDeeps = 0;
            }
            break;
        case OP_HLINETO:
            {
                if (argsDeeps < 1) {
                    throw new Type2CharStringException("illegal arguments number for operator hlineto");
                }
                int p = 0;
                while (p < argsDeeps) {
                    if ((p & 1) == 0) {
                        x += argsStack[p];
                    }
                    else {
                        y += argsStack[p];
                    }
                    lineTo(x, y);
                    p++;
                }
                argsDeeps = 0;
            }
            break;
        case OP_VLINETO:
            {
                if (argsDeeps < 1) {
                    throw new Type2CharStringException("illegal arguments number for operator vlineto");
                }
                int p = 0;
                while (p < argsDeeps) {
                    if ((p & 1) == 0) {
                        y += argsStack[p];
                    }
                    else {
                        x += argsStack[p];
                    }
                    lineTo(x, y);
                    p++;
                }
                argsDeeps = 0;
            }
            break;
        case OP_RRCURVETO:
            {
                if (argsDeeps % 6 != 0) {
                    throw new Type2CharStringException("illegal arguments number for operator rrcurveto");
                }
                int p = 0;
                while (p < argsDeeps) {
                    x += argsStack[p++];
                    y += argsStack[p++];
                    double x1 = x;
                    double y1 = y;
                    x += argsStack[p++];
                    y += argsStack[p++];
                    double x2 = x;
                    double y2 = y;
                    x += argsStack[p++];
                    y += argsStack[p++];
                    double x3 = x;
                    double y3 = y;
                    curveTo(x1, y1, x2, y2, x3, y3);
                }
                argsDeeps = 0;
            }
            break;
        case OP_CALLSUBR:
            {
                if (argsDeeps < 1) {
                    throw new Type2CharStringException("illegal arguments number for operator callsubr");
                }
                if (localSubrs == null) {
                    throw new Type2CharStringException("local subrs is null");
                }
                int index = (int) (argsStack[argsDeeps - 1]) + localSubrs.getBias();
                argsDeeps--;
                if (index < 0 || index >= localSubrs.getNumSubrs()) {
                    throw new Type2CharStringException("bad local subr index: " + index + " (max: " + localSubrs.getNumSubrs() + ")");
                }
                byte[] subr = localSubrs.getSubr(index);
                pushSubr(subr);
            }
            break;
        case OP_RETURN:
            {
                popSubr();
            }
            break;
        case OP_ESCAPE:
            dispatchEscape(nextByte());
            break;
        case OP_ENDCHAR:
            {
                if (argsDeeps == 0) {
                    finished = true;
                    closePath();
                }
                else if (argsDeeps == 4) {
                    finished = true;
                    closePath();
                    seac = new SEAC();
                    seac.adx = argsStack[0];
                    seac.ady = argsStack[1];
                    seac.bchar = (int) argsStack[2];
                    seac.achar = (int) argsStack[3];
                }
                else {
                    throw new Type2CharStringException("illegal arguments number for operator endchar");
                }
            }
            break;
        case OP_RMOVETO:
            {
                if (argsDeeps != 2) {
                    throw new Type2CharStringException("illegal arguments number for operator rmoveto");
                }
                x += argsStack[0];
                y += argsStack[1];
                argsDeeps = 0;
                moveTo(x, y);
            }
            break;
        case OP_HMOVETO:
            {
                if (argsDeeps != 1) {
                    throw new Type2CharStringException("illegal arguments number for operator hmoveto");
                }
                x += argsStack[0];
                argsDeeps = 0;
                moveTo(x, y);
            }
            break;
        case OP_RCURVELINE:
            {
                if (argsDeeps < 8 || (argsDeeps - 2) % 6 != 0) {
                    throw new Type2CharStringException("illegal arguments number for operator rcurveline");
                }
                int nCurve = (argsDeeps - 2) / 6;
                int p = 0;
                for (int i=0; i<nCurve; ++i) {
                    x += argsStack[p++];
                    y += argsStack[p++];
                    double x1 = x;
                    double y1 = y;
                    x += argsStack[p++];
                    y += argsStack[p++];
                    double x2 = x;
                    double y2 = y;
                    x += argsStack[p++];
                    y += argsStack[p++];
                    double x3 = x;
                    double y3 = y;
                    curveTo(x1, y1, x2, y2, x3, y3);
                }
                x += argsStack[p++];
                y += argsStack[p++];
                lineTo(x, y);
                assert(p == argsDeeps);
                argsDeeps = 0;
            }
            break;
        case OP_RLINECURVE:
            {
                if (argsDeeps < 8 || (argsDeeps - 6) % 2 != 0) {
                    throw new Type2CharStringException("illegal arguments number for operator rlinecurve");
                }
                int nLine = (argsDeeps - 6) / 2;
                int p = 0;
                for (int i=0; i<nLine; ++i) {
                    x += argsStack[p++];
                    y += argsStack[p++];
                    lineTo(x, y);
                }
                x += argsStack[p++];
                y += argsStack[p++];
                double x1 = x;
                double y1 = y;
                x += argsStack[p++];
                y += argsStack[p++];
                double x2 = x;
                double y2 = y;
                x += argsStack[p++];
                y += argsStack[p++];
                double x3 = x;
                double y3 = y;
                curveTo(x1, y1, x2, y2, x3, y3);
                assert(p == argsDeeps);
                argsDeeps = 0;
            }
            break;
        case OP_VVCURVETO:
            {
                int p = 0;
                if (argsDeeps % 4 == 1) {
                    x += argsStack[0];
                    p = 1;
                }
                if ((argsDeeps - p) % 4 != 0) {
                    throw new Type2CharStringException("illegal arguments number for operator vvcurveto");
                }
                while (p < argsDeeps) {
                    y += argsStack[p++];
                    double x1 = x;
                    double y1 = y;
                    x += argsStack[p++];
                    y += argsStack[p++];
                    double x2 = x;
                    double y2 = y;
                    y += argsStack[p++];
                    double x3 = x;
                    double y3 = y;
                    curveTo(x1, y1, x2, y2, x3, y3);
                }
                assert(p == argsDeeps);
                argsDeeps = 0;
            }
            break;
        case OP_HHCURVETO:
            {
                int p = 0;
                if (argsDeeps % 4 == 1) {
                    y += argsStack[0];
                    p = 1;
                }
                if ((argsDeeps - p) % 4 != 0) {
                    throw new Type2CharStringException("illegal arguments number for operator hhcurveto");
                }
                while (p < argsDeeps) {
                    x += argsStack[p++];
                    double x1 = x;
                    double y1 = y;
                    x += argsStack[p++];
                    y += argsStack[p++];
                    double x2 = x;
                    double y2 = y;
                    x += argsStack[p++];
                    double x3 = x;
                    double y3 = y;
                    curveTo(x1, y1, x2, y2, x3, y3);
                }
                assert(p == argsDeeps);
                argsDeeps = 0;
            }
            break;
        case OP_CALLGSUBR:
            {
                if (argsDeeps < 1) {
                    throw new Type2CharStringException("illegal arguments number for operator callgsubr");
                }
                int index = (int) (argsStack[argsDeeps - 1]) + globalSubrs.getBias();
                argsDeeps--;
                if (index < 0 || index >= globalSubrs.getNumSubrs()) {
                    throw new Type2CharStringException("bad global subr index");
                }
                byte[] subr = globalSubrs.getSubr(index);
                pushSubr(subr);
            }
            break;
        case OP_VHCURVETO:
        case OP_HVCURVETO:
            {
                if (c == OP_HVCURVETO) {
                    if (argsDeeps < 4 || (argsDeeps % 4) > 1) {
                        throw new Type2CharStringException("illegal arguments number for operator hvcurveto");
                    }
                }
                else {
                    if (argsDeeps < 4 || (argsDeeps % 4) > 1) {
                        throw new Type2CharStringException("illegal arguments number for operator vhcurveto");
                    }
                }

                boolean horizontal = (c == OP_HVCURVETO);
                int p = 0;
                while (p < argsDeeps) {
                    if (horizontal) {
                        x += argsStack[p++];
                        double x1 = x;
                        double y1 = y;
                        x += argsStack[p++];
                        y += argsStack[p++];
                        double x2 = x;
                        double y2 = y;
                        y += argsStack[p++];
                        if (p == argsDeeps - 1) {
                            x += argsStack[p++];
                        }
                        double x3 = x;
                        double y3 = y;
                        curveTo(x1, y1, x2, y2, x3, y3);
                    }
                    else {
                        y += argsStack[p++];
                        double x1 = x;
                        double y1 = y;
                        x += argsStack[p++];
                        y += argsStack[p++];
                        double x2 = x;
                        double y2 = y;
                        x += argsStack[p++];
                        if (p == argsDeeps - 1) {
                            y += argsStack[p++];
                        }
                        double x3 = x;
                        double y3 = y;
                        curveTo(x1, y1, x2, y2, x3, y3);                        
                    }
                    horizontal = !horizontal;
                }
                assert(p == argsDeeps);
                argsDeeps = 0;
            }
            break;
        default:
            throw new Type2CharStringException("unknown operator: " + c);
        }
    }

    private void dispatchEscape(int c) throws Type2CharStringException {
        switch(c + (OP_ESCAPE << 8)) {
        case OP_DOTSECTION:
            // do nothing here.
            break;
        case OP_AND:
            {
                if (argsDeeps < 2) {
                    throw new Type2CharStringException("illegal arguments number for operator and");
                }
                double a = argsStack[argsDeeps - 2];
                double b = argsStack[argsDeeps - 1];
                if (a == 0 || b == 0) {
                    argsStack[argsDeeps - 2] = 0;
                    argsDeeps--;
                }
                else {
                    argsStack[argsDeeps - 2] = 1;
                    argsDeeps--;
                }
            }
            break;
        case OP_OR:
            {
                if (argsDeeps < 2) {
                    throw new Type2CharStringException("illegal arguments number for operator or");
                }
                double a = argsStack[0];
                double b = argsStack[1];
                if (a == 0 && b == 0) {
                    argsStack[0] = 0;
                    argsDeeps = 1;
                }
                else {
                    argsStack[0] = 1;
                    argsDeeps = 1;
                }   
            }
            break;
        case OP_NOT:
            {
                if (argsDeeps < 1) {
                    throw new Type2CharStringException("illegal arguments number for operator not");
                }
                if (argsStack[argsDeeps - 1] == 0) {
                    argsStack[argsDeeps - 1] = 1;
                }
                else {
                    argsStack[argsDeeps - 1] = 0;
                }
            }
            break;
        case OP_ABS:
            {
                if (argsDeeps < 1) {
                    throw new Type2CharStringException("illegal arguments number for operator abs");
                }
                argsStack[argsDeeps - 1] = Math.abs(argsStack[argsDeeps - 1]); 
            }
            break;
        case OP_ADD: 
            {
                if (argsDeeps < 2) {
                    throw new Type2CharStringException("illegal arguments number for operator add");
                }
                double a = argsStack[argsDeeps - 2];
                double b = argsStack[argsDeeps - 1];
                argsStack[argsDeeps - 2] = a + b;
                argsDeeps--;
            }
            break;
        case OP_SUB: 
            {
                if (argsDeeps < 2) {
                    throw new Type2CharStringException("illegal arguments number for operator sub");
                }
                double a = argsStack[argsDeeps - 2];
                double b = argsStack[argsDeeps - 1];
                argsStack[argsDeeps - 2] = a - b;
                argsDeeps--;
            } 
            break;
        case OP_DIV:
            {
                if (argsDeeps < 2) {
                    throw new Type2CharStringException("illegal arguments number for operator div");
                }
                double a = argsStack[argsDeeps - 2];
                double b = argsStack[argsDeeps - 1];
                argsStack[argsDeeps - 2] = a / b;
                argsDeeps--;
            } 
            break;
        case OP_NEG:
            {
                if (argsDeeps < 1) {
                    throw new Type2CharStringException("illegal arguments number for operator neg");
                }
                argsStack[argsDeeps - 1] = -argsStack[argsDeeps - 1];
            }
            break;
        case OP_EQ:
            {
                if (argsDeeps < 2) {
                    throw new Type2CharStringException("illegal arguments number for operator eq");
                }
                double a = argsStack[argsDeeps - 2];
                double b = argsStack[argsDeeps - 1];
                if (Math.abs(a - b) < (1 / 65536)) {
                    argsStack[argsDeeps - 2] = 1;
                    argsDeeps--;
                }
                else {
                    argsStack[argsDeeps - 2] = 0;
                    argsDeeps--;
                }
            }
            break;
        case OP_DROP:
            {
                if (argsDeeps < 1) {
                    throw new Type2CharStringException("illegal arguments number for operator drop");
                }
                argsDeeps--;
            }
            break;
        case OP_PUT:
            {
                if (argsDeeps < 2) {
                    throw new Type2CharStringException("illegal arguments number for operator put");
                }
                int i =  (int) argsStack[argsDeeps - 1];
                tranArray[i] = argsStack[argsDeeps - 2];
                argsDeeps -= 2;
            }
            break;
        case OP_GET:
            {
                if (argsDeeps < 1) {
                    throw new Type2CharStringException("illegal arguments number for operator get");
                }
                int i = (int) argsStack[argsDeeps - 1];
                argsStack[argsDeeps - 1] = tranArray[i];
            }
            break;
        case OP_IFELSE:
            {
                if (argsDeeps < 4) {
                    throw new Type2CharStringException("illegal arguments number for operator ifelse");
                }
                double s1 = argsStack[argsDeeps - 4];
                double s2 = argsStack[argsDeeps - 3];
                double v1 = argsStack[argsDeeps - 2];
                double v2 = argsStack[argsDeeps - 1];
                if (v1 <= v2) {
                    argsStack[argsDeeps - 4] = s1; 
                }
                else {
                    argsStack[argsDeeps - 4] = s2;
                }
                argsDeeps -= 3;
            }
            break;
        case OP_RANDOM:
            {
                double d = 0;
                while (d == 0) {
                    d = Math.random();
                }
                if (argsDeeps >= ARGSSTACK_LIMIT) {
                    throw new Type2CharStringException("argument stack overflow");
                }
                argsStack[argsDeeps++] = d;
            }
            break;
        case OP_MUL:
            {
                if (argsDeeps < 2) {
                    throw new Type2CharStringException("illegal arguments number for operator mul");
                }
                double a = argsStack[argsDeeps - 2];
                double b = argsStack[argsDeeps - 1];
                argsStack[argsDeeps - 2] = a * b;
                argsDeeps--;
            } 
            break;
        case OP_SQRT:
            {
                if (argsDeeps < 1) {
                    throw new Type2CharStringException("illegal arguments number for operator sqrt");
                }
                argsStack[argsDeeps - 1] = Math.sqrt(argsStack[argsDeeps - 1]);
            }
            break;
        case OP_DUP:
            {
                if (argsDeeps < 1) {
                    throw new Type2CharStringException("illegal arguments number for operator dup");
                }
                if (argsDeeps >= ARGSSTACK_LIMIT) {
                    throw new Type2CharStringException("argument stack overflow");
                }
                argsStack[argsDeeps] = argsStack[argsDeeps - 1];
                argsDeeps++;
            }
            break;
        case OP_EXCH:
            {
                if (argsDeeps < 2) {
                    throw new Type2CharStringException("illegal arguments number for operator exch");
                }
                double t = argsStack[argsDeeps - 1];
                argsStack[argsDeeps - 1] = argsStack[argsDeeps - 2];
                argsStack[argsDeeps - 2] = t;
            }
            break;
        case OP_INDEX:
            {
                if (argsDeeps < 1) {
                    throw new Type2CharStringException("illegal arguments number for operator index");
                }
                int i = (int) argsStack[argsDeeps - 1];
                if (i < 0) {
                    i = 0;
                }
                if (i + 2 > argsDeeps) {
                    throw new Type2CharStringException("out of illegal arguments stack bounding");
                }
                argsStack[argsDeeps - 1] = argsStack[argsDeeps - 2 - i];
            }
            break;
        case OP_ROLL:
            {
                if (argsDeeps < 2) {
                    throw new Type2CharStringException("illegal arguments number for operator roll");
                }
                final int J = (int) argsStack[argsDeeps - 1];
                final int N = (int) argsStack[argsDeeps - 2];
                argsDeeps -= 2;
                if (N <= 0) {
                    throw new Type2CharStringException("illegal argument N for operator roll");
                }
                if (argsDeeps < N) {
                    throw new Type2CharStringException("out of illegal arguments stack bounding");
                }
                double[] temp = new double[N];
                for (int i=0; i<N; i++) {
                    temp[i] = argsStack[argsDeeps - 1 - i];
                }
                for (int i=0; i<N; i++) {
                    argsStack[argsDeeps - 1 - i] = temp[(J + i) % N];
                }
            }
            break;
        case OP_HFLEX:
            {
                if (argsDeeps != 7) {
                    throw new Type2CharStringException("illegal arguments number for operator hflex");
                }
                double initial = y;
                x += argsStack[0];
                double x1 = x;
                double y1 = y;
                x += argsStack[1];
                y += argsStack[2];
                double x2 = x;
                double y2 = y;
                x += argsStack[3];
                double x3 = x;
                double y3 = y;
                x += argsStack[4];
                double x4 = x;
                double y4 = y;
                x += argsStack[5];
                y  = initial;
                double x5 = x;
                double y5 = y;
                x += argsStack[6];
                y  = initial;
                double x6 = x;
                double y6 = y;
                curveTo(x1, y1, x2, y2, x3, y3);
                curveTo(x4, y4, x5, y5, x6, y6);
                argsDeeps = 0;
            }
            break;
        case OP_FLEX:
            {
                if (argsDeeps != 13) {
                    throw new Type2CharStringException("illegal arguments number for operator flex");
                }
                x += argsStack[0];
                y += argsStack[1];
                double x1 = x;
                double y1 = y;
                x += argsStack[2];
                y += argsStack[3];
                double x2 = x;
                double y2 = y;
                x += argsStack[4];
                y += argsStack[5];
                double x3 = x;
                double y3 = y;
                x += argsStack[6];
                y += argsStack[7];
                double x4 = x;
                double y4 = y;
                x += argsStack[8];
                y += argsStack[9];
                double x5 = x;
                double y5 = y;
                x += argsStack[10];
                y += argsStack[11];
                double x6 = x;
                double y6 = y;
                curveTo(x1, y1, x2, y2, x3, y3);
                curveTo(x4, y4, x5, y5, x6, y6);
                argsDeeps = 0;
            }
            break;
        case OP_HFLEX1:
            {
                if (argsDeeps != 9) {
                    throw new Type2CharStringException("illegal arguments number for operator hflex1");
                }
                double initial = y;
                x += argsStack[0];
                y += argsStack[1];
                double x1 = x;
                double y1 = y;
                x += argsStack[2];
                y += argsStack[3];
                double x2 = x;
                double y2 = y;
                x += argsStack[4];
                double x3 = x;
                double y3 = y;
                x += argsStack[5];
                double x4 = x;
                double y4 = y;
                x += argsStack[6];
                y += argsStack[7];
                double x5 = x;
                double y5 = y;
                x += argsStack[8];
                y  = initial;
                double x6 = x;
                double y6 = y;
                curveTo(x1, y1, x2, y2, x3, y3);
                curveTo(x4, y4, x5, y5, x6, y6);
                argsDeeps = 0;
            }
            break;
        case OP_FLEX1:
            {
                if (argsDeeps != 11) {
                    throw new Type2CharStringException("illegal arguments number for operator flex1");
                }
                double initialX = x;
                double initialY = y;
                double dx = 0;
                double dy = 0;
                for (int i=0; i<5; ++i) {
                    dx += argsStack[i * 2];
                    dy += argsStack[i * 2 + 1];
                }
                
                x += argsStack[0];
                y += argsStack[1];
                double x1 = x;
                double y1 = y;
                x += argsStack[2];
                y += argsStack[3];
                double x2 = x;
                double y2 = y;
                x += argsStack[4];
                y += argsStack[5];
                double x3 = x;
                double y3 = y;
                x += argsStack[6];
                y += argsStack[7];
                double x4 = x;
                double y4 = y;
                x += argsStack[8];
                y += argsStack[9];
                double x5 = x;
                double y5 = y;
                
                if (Math.abs(dx) > Math.abs(dy)) {
                    x += argsStack[10];
                    y  = initialY;
                }
                else {
                    x  = initialX;
                    y += argsStack[10];
                }
                double x6 = x;
                double y6 = y;
                
                curveTo(x1, y1, x2, y2, x3, y3);
                curveTo(x4, y4, x5, y5, x6, y6);
                argsDeeps = 0;
            }
            break;
        default:
            throw new Type2CharStringException("unknown operator");
        }
    }
            
    private void moveTo(double x, double y) {
        PathNode node = new PathNode();
        node.tag = PATH_MOVETO;
        node.x1 = x;
        node.y1 = y;
        glyphPath.add(node);
    }

    private void lineTo(double x, double y) {
        PathNode node = new PathNode();
        node.tag = PATH_LINETO;
        node.x1 = x;
        node.y1 = y;
        glyphPath.add(node);        
    }
    
    private void curveTo(double x1, double y1, double x2, double y2, double x3, double y3) {
        PathNode node = new PathNode();
        node.tag = PATH_CURVETO;
        node.x1 = x1;
        node.y1 = y1;
        node.x2 = x2;
        node.y2 = y2;
        node.x3 = x3;
        node.y3 = y3;
        glyphPath.add(node);
    }
    
    private void closePath() {
        if (glyphPath.isEmpty()) {
            return;
        }
        PathNode node = new PathNode();
        node.tag = PATH_CLOSE;
        glyphPath.add(node);
    }
    
    private void pushSubr(byte[] subr) throws Type2CharStringException {
        if (subrDeeps >= SUBRSTACK_LIMIT) {
            throw new Type2CharStringException("too many nested subr invokation");
        }
        SubrCS top = new SubrCS();
        top.ip = 0;
        top.code = subr;
        subrStack[subrDeeps] = top;
        subrDeeps++;
    }
    
    private void popSubr() throws Type2CharStringException {
        if (subrDeeps <= 0) {
            throw new Type2CharStringException("subr stack underflow");
        }
        subrDeeps--;
        subrStack[subrDeeps] = null;
    }
        
    private int nextByte() throws Type2CharStringException {
        if (subrDeeps <= 0) {
            throw new Type2CharStringException("no subr");
        }
        SubrCS top = subrStack[subrDeeps-1];
        if (top.ip < 0 || top.ip >= top.code.length) {
            throw new Type2CharStringException("end of a subr");
        }
        return top.code[top.ip++] & 0xff;
    }
}
