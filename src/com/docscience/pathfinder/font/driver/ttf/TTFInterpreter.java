package com.docscience.pathfinder.font.driver.ttf;

import java.util.HashMap;

/**
 * @author wxin
 *
 */
public class TTFInterpreter {

    private static final int INS_AA = 0x7F;
    private static final int INS_ABS = 0x64;
    private static final int INS_ADD = 0x60;
    private static final int INS_ALIGNPTS = 0x27;
    private static final int INS_ALIGNRP = 0x3C;
    private static final int INS_AND = 0x5A;
    private static final int INS_CALL = 0x2B;
    private static final int INS_CEILING = 0x67;
    private static final int INS_CINDEX = 0x25;
    private static final int INS_CLEAR = 0x22;
    private static final int INS_DEBUG = 0x4F;
    private static final int INS_DELTAC1 = 0x73;
    private static final int INS_DELTAC2 = 0x74;
    private static final int INS_DELTAC3 = 0x75;
    private static final int INS_DELTAP1 = 0x5D;
    private static final int INS_DELTAP2 = 0x71;
    private static final int INS_DELTAP3 = 0x72;
    private static final int INS_DEPTH = 0x24;
    private static final int INS_DIV = 0x62;
    private static final int INS_DUP = 0x20;
    private static final int INS_EIF = 0x59;
    private static final int INS_ELSE = 0x1B;
    private static final int INS_ENDF = 0x2D;
    private static final int INS_EQ = 0x54;
    private static final int INS_EVEN = 0x57;
    private static final int INS_FDEF = 0x2C;
    private static final int INS_FLIPOFF = 0x4E;
    private static final int INS_FLIPON = 0x4D;
    private static final int INS_FLIPPT = 0x80;
    private static final int INS_FLIPRGOFF = 0x82;
    private static final int INS_FLIPRGON = 0x81;
    private static final int INS_FLOOR = 0x66;
    private static final int INS_GC = 0x46;
    private static final int INS_GETINFO = 0x88;
    private static final int INS_GFV = 0x0D;
    private static final int INS_GPV = 0x0C;
    private static final int INS_GT = 0x52;
    private static final int INS_GTEQ = 0x53;
    private static final int INS_IDEF = 0x89;
    private static final int INS_IF = 0x58;
    private static final int INS_INSTCTRL = 0x8E;
    private static final int INS_IP = 0x39;
    private static final int INS_ISECT = 0x0F;
    private static final int INS_IUP = 0x30;
    private static final int INS_JMPR = 0x1C;
    private static final int INS_JROF = 0x79;
    private static final int INS_JROT = 0x78;
    private static final int INS_LOOPCALL = 0x2A;
    private static final int INS_LT = 0x50;
    private static final int INS_LTEQ = 0x51;
    private static final int INS_MAX = 0X8B;
    private static final int INS_MD = 0x49;
    private static final int INS_MDAP = 0x2E;
    private static final int INS_MDRP = 0xC0;
    private static final int INS_MIAP = 0x3E;
    private static final int INS_MIN = 0X8C;
    private static final int INS_MINDEX = 0x26;
    private static final int INS_MIRP = 0xE0;
    private static final int INS_MPPEM = 0x4B;
    private static final int INS_MPS = 0x4C;
    private static final int INS_MSIRP = 0x3A;
    private static final int INS_MUL = 0x63;
    private static final int INS_NEG = 0x65;
    private static final int INS_NEQ = 0x55;
    private static final int INS_NOT = 0x5C;
    private static final int INS_NPUSHB = 0x40;
    private static final int INS_NPUSHW = 0x41;
    private static final int INS_NROUND = 0x6C;
    private static final int INS_ODD = 0x56;
    private static final int INS_OR = 0x5B;
    private static final int INS_POP = 0x21;
    private static final int INS_PUSHB = 0xB0;
    private static final int INS_PUSHW = 0xB8;
    private static final int INS_RCVT = 0x45;
    private static final int INS_RDTG = 0x7D;
    private static final int INS_ROFF = 0x7A;
    private static final int INS_ROLL = 0x8a;
    private static final int INS_ROUND = 0x68;
    private static final int INS_RS = 0x43;
    private static final int INS_RTDG = 0x3D;
    private static final int INS_RTG = 0x18;
    private static final int INS_RTHG = 0x19;
    private static final int INS_RUTG = 0x7C;
    private static final int INS_S45ROUND = 0x77;
    private static final int INS_SANGW = 0x7E;
    private static final int INS_SCANCTRL = 0x85;
    private static final int INS_SCANTYPE = 0x8D;
    private static final int INS_SCFS = 0x48;
    private static final int INS_SCVTCI = 0x1D;
    private static final int INS_SDB = 0x5E;
    private static final int INS_SDPVTL = 0x86;
    private static final int INS_SDS = 0x5F;
    private static final int INS_SFVFS = 0x0B;
    private static final int INS_SFVTCA = 0x04;
    private static final int INS_SFVTL = 0x08;
    private static final int INS_SFVTPV = 0x0E;
    private static final int INS_SHC = 0x34;
    private static final int INS_SHP = 0x32;
    private static final int INS_SHPIX = 0x38;
    private static final int INS_SHZ = 0x36;
    private static final int INS_SLOOP = 0x17;
    private static final int INS_SMD = 0x1A;
    private static final int INS_SPVFS = 0x0A;
    private static final int INS_SPVTCA = 0x02;
    private static final int INS_SPVTL = 0x06;
    private static final int INS_SROUND = 0x76;
    private static final int INS_SRP0 = 0x10;
    private static final int INS_SRP1 = 0x11;
    private static final int INS_SRP2 = 0x12;
    private static final int INS_SSW = 0x1F;
    private static final int INS_SSWCI = 0x1E;
    private static final int INS_SUB = 0x61;
    private static final int INS_SVTCA = 0x00;
    private static final int INS_SWAP = 0x23;
    private static final int INS_SZP0 = 0x13;
    private static final int INS_SZP1 = 0x14;
    private static final int INS_SZP2 = 0x15;
    private static final int INS_SZPS = 0x16;
    private static final int INS_UTP = 0x29;
    private static final int INS_WCVTF = 0x70;
    private static final int INS_WCVTP = 0x44;
    private static final int INS_WS = 0x42;

    private static final byte[] INSTRUCTION_ARG_BITS = new byte[256];
    
    private static final int ROUND_TO_HALF_GRID = 0;
    private static final int ROUND_TO_GRID = 1;
    private static final int ROUND_TO_DOUBLE_GRID = 2;
    private static final int ROUND_DOWN_TO_GRID = 3;
    private static final int ROUND_UP_TO_GRID = 4;
    private static final int ROUND_OFF = 5;
    

    private static final int ARG_X_AXIS = 1;
    private static final int ARG_Y_AXIS = 0;
    
    private static final int ARG_PARALLEL = 0;
    private static final int ARG_PERPENDICULAR = 1;
    
    private static final int TWILIGHT_ZONE = 0;
    private static final int GLYPH_ZONE = 1;
    
    private static final int TRUE = 1;
    private static final int FALSE = 0;
    
    static {
        for (int n=0; n<INSTRUCTION_ARG_BITS.length; ++n) {
            INSTRUCTION_ARG_BITS[n] = 0;
        }
        for (int n=0; n<(1 << 1); ++n) {
            INSTRUCTION_ARG_BITS[INS_GC + n] = 1;
            INSTRUCTION_ARG_BITS[INS_IUP + n] = 1;
            INSTRUCTION_ARG_BITS[INS_MD + n] = 1;
            INSTRUCTION_ARG_BITS[INS_MDAP + n] = 1;
            INSTRUCTION_ARG_BITS[INS_MIAP + n] = 1;
            INSTRUCTION_ARG_BITS[INS_MSIRP + n] = 1;
            INSTRUCTION_ARG_BITS[INS_SDPVTL + n] = 1;
            INSTRUCTION_ARG_BITS[INS_SFVTCA + n] = 1;
            INSTRUCTION_ARG_BITS[INS_SFVTL + n] = 1;
            INSTRUCTION_ARG_BITS[INS_SHC + n] = 1;
            INSTRUCTION_ARG_BITS[INS_SHP + n] = 1;
            INSTRUCTION_ARG_BITS[INS_SHZ + n] = 1;
            INSTRUCTION_ARG_BITS[INS_SPVTCA + n] = 1;
            INSTRUCTION_ARG_BITS[INS_SPVTL + n] = 1;
            INSTRUCTION_ARG_BITS[INS_SVTCA + n] = 1;           
        }
        for (int n=0; n<(1 << 2); ++n) {
            INSTRUCTION_ARG_BITS[INS_NROUND + n] = 2;
            INSTRUCTION_ARG_BITS[INS_ROUND + n] = 2;
        }
        for (int n=0; n<(1 << 3); ++n) {
            INSTRUCTION_ARG_BITS[INS_PUSHB + n] = 3;
            INSTRUCTION_ARG_BITS[INS_PUSHW + n] = 3;
        }
        for (int n=0; n<(1 << 5); ++n) {
            INSTRUCTION_ARG_BITS[INS_MDRP + n] = 5;
            INSTRUCTION_ARG_BITS[INS_MIRP + n] = 5;
        }
    }
    
    // runtime.
    private int[] storages;
    private int[] stack;
    private int deeps;
    private int ip;
    private double pointSize;
    private double resolution;
    private double unitsPerEm;
    private double[] controlValueTable = new double[512];
    private double gridPeriod = 1.0;
    private byte[] instructions;
    private byte[][] functions;
    private HashMap<Integer, byte[]>  extraINSs = new HashMap<Integer, byte[]>();
    
    // graphic state.    
    @SuppressWarnings("unused")
	private boolean autoFlip = true;
    private double controlValueCutIn = 17.0 / 16.0;
    @SuppressWarnings("unused")
	private int deltaBase = 9;
    @SuppressWarnings("unused")
	private int deltaShift = 3;
    private Vector dualProjectionVector = new Vector();
    private Vector freedomVector = new Vector(1, 0);
    @SuppressWarnings("unused")
	private boolean instructControl = false;
    private int loop = 1;
    private double minimumDistance = 1.0;
    private Vector projectionVector = new Vector(1, 0);
    private int roundState = ROUND_TO_GRID;
    private int rp0 = 0;
    private int rp1 = 0;
    private int rp2 = 0;
    @SuppressWarnings("unused")
	private boolean scanControl = false;
    private double singleWidthValue = 0.0;
    private double singleWidthCutIn = 0.0;
    private int zp0 = GLYPH_ZONE;
    private int zp1 = GLYPH_ZONE;
    private int zp2 = GLYPH_ZONE;
    
    private static class Point {
        private double x;
        private double y;
        private double orgX;
        private double orgY;
        private boolean onCurve;
        private boolean touched;
        
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
            this.orgX = x;
            this.orgY = y;
        }
        
        public double getX() {
            return x;
        }
        
        public double getY() {
            return y;
        }
        
        public void setX(double x) {
            this.x = x;
        }
        
        public void setY(double y) {
            this.y = y;
        }
        
        @SuppressWarnings("unused")
		public void setXY(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        public double getOrgX() {
            return orgX;
        }
        
        public double getOrgY() {
            return orgY;
        }
                
        @SuppressWarnings("unused")
		public boolean isOnCurve() {
            return onCurve;
        }
        
        public void setOnCurve(boolean b) {
            onCurve = b;
        }
        
        public void flip() {
            onCurve = !onCurve;
        }
        
        @SuppressWarnings("unused")
		public boolean isTouched() {
            return touched;
        }
        
        public void setTouched(boolean b) {
            touched = b;
        }
    }
    
    private static class Vector {
        private double x;
        private double y;
        
        public Vector() {
            setXY(1, 1);
        }
        
        public Vector(double x, double y) {
            setXY(x, y);
        }
        
        public void setXY(double x, double y) {
            double x2 = x * x;
            double y2 = y * y;
            double sq = Math.sqrt(x2 + y2);
            this.x = x / sq;
            this.y = y / sq;
        }
        
        public double getX() {
            return x;
        }
        
        public double getY() {
            return y;
        }
    }
        
    private Point[] glyphPoints;
    private Point[] twilightPoints;
    
    public TTFInterpreter(double pointSize, double resolution, double unitsPerEm, int maxStorage, int maxStackDeeps, int maxFunctionDefs) {
        this.pointSize = pointSize;
        this.resolution = resolution;
        this.unitsPerEm = unitsPerEm;
        this.storages = new int[maxStorage];
        this.stack = new int[maxStackDeeps];
        this.functions = new byte[maxFunctionDefs][];
    }
    
    public void loadTTFGlyphPoints(TTFGlyph.Point[] points) {
        // TODO: ADD CODE HERE
    	assert(false) : "not implement yet";
    }

    public void loadControlValueTable(short[] cvt) {
        // TODO: ADD CODE HERE
    	assert(false) : "not implement yet";
    }

    public void execute(byte[] instructions) throws TTFException {
        this.instructions = instructions;
        this.ip = 0;
        while (ip < instructions.length) {
            int ins = nextByte();
            int arg = 0;
            int n = INSTRUCTION_ARG_BITS[ins];
            arg = ins & ((1 << n) - 1);
            ins = ins & (0xff << n);
            dispatch(ins, arg);
        }
    }
    
    private void dispatch(int ins, int arg) throws TTFException {
        switch(ins) {
        case INS_AA: insAA(); break;
        case INS_ABS: insABS(); break;
        case INS_ADD: insADD(); break; 
        case INS_ALIGNPTS: insALIGNPTS(); break;
        case INS_ALIGNRP: insALIGNRP(); break;
        case INS_AND: insAND(); break;
        case INS_CALL: insCALL(); break;
        case INS_CEILING: insCEILING(); break;
        case INS_CINDEX: insCINDEX(); break;
        case INS_CLEAR: insCLEAR(); break;
        case INS_DEBUG: insDEBUG(); break;
        case INS_DELTAC1: insDELTAC1(); break;
        case INS_DELTAC2: insDELTAC2(); break;
        case INS_DELTAC3: insDELTAC3(); break;
        case INS_DELTAP1: insDELTAP1(); break;
        case INS_DELTAP2: insDELTAP2(); break;
        case INS_DELTAP3: insDELTAP3(); break;
        case INS_DEPTH: insDEPTH(); break;
        case INS_DIV: insDIV(); break;
        case INS_DUP: insDUP(); break;
        case INS_EIF: insEIF(); break;
        case INS_ELSE: insELSE(); break;
        case INS_ENDF: insENDF(); break;
        case INS_EQ: insEQ(); break;
        case INS_EVEN: insEVEN(); break;
        case INS_FDEF: insFDEF(); break;
        case INS_FLIPOFF: insFLIPOFF(); break;
        case INS_FLIPON: insFLIPON(); break;
        case INS_FLIPPT: insFLIPPT(); break;
        case INS_FLIPRGOFF: insFLIPRGOFF(); break;
        case INS_FLIPRGON: insFLIPRGON(); break;
        case INS_FLOOR: insFLOOR(); break;
        case INS_GC: insGC(arg); break;
        case INS_GETINFO: insGETINFO(); break;
        case INS_GFV: insGFV(); break;
        case INS_GPV: insGPV(); break;
        case INS_GT: insGT(); break;
        case INS_GTEQ: insGTEQ(); break;
        case INS_IDEF: insIDEF(); break;
        case INS_IF: insIF(); break;
        case INS_INSTCTRL: insINSTCTRL(); break;
        case INS_IP: insIP(); break;
        case INS_ISECT: insISECT(); break;
        case INS_IUP: insIUP(arg); break;
        case INS_JMPR: insJMPR(); break;
        case INS_JROF: insJROF(); break;
        case INS_JROT: insJROT(); break;
        case INS_LOOPCALL: insLOOPCALL(); break;
        case INS_LT: insLT(); break;
        case INS_LTEQ: insLTEQ(); break;
        case INS_MAX: insMAX(); break;
        case INS_MD: insMD(arg); break;
        case INS_MDAP: insMDAP(arg); break;
        case INS_MDRP: insMDRP(arg); break;
        case INS_MIAP: insMIAP(arg); break;
        case INS_MIN: insMIN(); break;
        case INS_MINDEX: insMINDEX(); break;
        case INS_MIRP: insMIRP(arg); break;
        case INS_MPPEM: insMPPEM(); break;
        case INS_MPS: insMPS(); break;
        case INS_MSIRP: insMSIRP(arg); break;
        case INS_MUL: insMUL(); break;
        case INS_NEG: insNEG(); break;
        case INS_NEQ: insNEQ(); break;
        case INS_NOT: insNOT(); break;
        case INS_NPUSHB: insNPushB(); break;
        case INS_NPUSHW: insNPUSHW(); break;
        case INS_NROUND: insNROUND(arg); break;
        case INS_ODD: insODD(); break;
        case INS_OR: insOR(); break;
        case INS_POP: insPOP(); break;
        case INS_PUSHB: insPUSHB(arg); break;
        case INS_PUSHW: insPUSHW(arg); break;
        case INS_RCVT: insRCVT(); break;
        case INS_RDTG: insRDTG(); break;
        case INS_ROFF: insROFF(); break;
        case INS_ROLL: insROLL(); break;
        case INS_ROUND: insROUND(arg); break;
        case INS_RS: insRS(); break;
        case INS_RTDG: insRTDG(); break;
        case INS_RTG: insRTG(); break;
        case INS_RTHG: insRTHG(); break;
        case INS_RUTG: insRUTG(); break;
        case INS_S45ROUND: insS45ROUND(); break;
        case INS_SANGW: insSANGW(); break;
        case INS_SCANCTRL: insSCANCTRL(); break;
        case INS_SCANTYPE: insSCANTYPE(); break;
        case INS_SCFS: insSCFS(); break;
        case INS_SCVTCI: insSCVTCI(); break;
        case INS_SDB: insSDB(); break;
        case INS_SDPVTL: insSDPVTL(arg); break;
        case INS_SDS: insSDS(); break;
        case INS_SFVFS: insSFVFS(); break;
        case INS_SFVTCA: insSFVTCA(arg); break;
        case INS_SFVTL: insSFVTL(arg); break;
        case INS_SFVTPV: insSFVTPV(); break;
        case INS_SHC: insSHC(arg); break;
        case INS_SHP: insSHP(arg); break;
        case INS_SHPIX: insSHPIX(); break;
        case INS_SHZ: insSHZ(arg); break;
        case INS_SLOOP: insSLOOP(); break;
        case INS_SMD: insSMD(); break;
        case INS_SPVFS: insSPVFS(); break;
        case INS_SPVTCA: insSPVTCA(arg); break;
        case INS_SPVTL: insSPVTL(arg); break;
        case INS_SROUND: insSROUND(); break;
        case INS_SRP0: insSRP0(); break;
        case INS_SRP1: insSRP1(); break;
        case INS_SRP2: insSRP2(); break;
        case INS_SSW: insSSW(); break;
        case INS_SSWCI: insSSWCI(); break;
        case INS_SUB: insSUB(); break;
        case INS_SVTCA: insSVTCA(arg); break;
        case INS_SWAP: insSWAP(); break;
        case INS_SZP0: insSZP0(); break;
        case INS_SZP1: insSZP1(); break;
        case INS_SZP2: insSZP2(); break;
        case INS_SZPS: insSZPS(); break;
        case INS_UTP: insUTP(); break;
        case INS_WCVTF: insWCVTF(); break;
        case INS_WCVTP: insWCVTP(); break;
        case INS_WS: insWS(); break;
        default:
            callExtraINS(ins);
        }
    }
    
    private double funit2pixels(int n) {
        return n * resolution * pointSize / (72 * unitsPerEm);  
    }
    
    private double pixelPerEm() {
        return resolution * pointSize / 72;
    }
    
    /**
     * Get next byte from instruction stream.
     * 
     * @return 8 bits, unsigned interger
     * @throws TTFException
     */
    private int nextByte() throws TTFException {
        if (ip >= instructions.length) {
            throw new TTFException("out of instructions range");
        }
        return instructions[ip++] & 0xff;
    }
    
    /**
     * Get next word from instruction stream.
     * @return 16 bits, signed integer
     * @throws TTFException
     */
    private int nextWord() throws TTFException {
        int b1, b2; 
        if (ip + 1>= instructions.length) {
            throw new TTFException("out of instructions range");
        }
        b1 = instructions[ip++];
        b2 = instructions[ip++];
        return (b1 << 8) | (b2 & 0xff);
    }
    
    private void seek(int n) throws TTFException {
        if (ip + n > instructions.length) {
            throw new TTFException("out of instructions range");
        }
        ip += n;
    }
    
    private int until(int[] targets) throws TTFException {
        while (ip < instructions.length) {
            int ins = nextByte();
            int arg = 0;
            int n = INSTRUCTION_ARG_BITS[ins];
            arg = ins & ((1 << n) - 1);
            ins = ins & (0xff << n);
            for (int i=0; i<targets.length; ++i) {
                if (targets[i] == ins) {
                    seek(-1);
                    return ins;
                }
            }
            switch(ins) {
            case INS_NPUSHB: 
                n = nextByte(); 
                seek(n); 
                break;
            case INS_NPUSHW:
                n = nextByte();
                seek(n * 2);
                break;
            case INS_PUSHB:
                seek(arg + 1);
                break;
            case INS_PUSHW:
                seek((arg + 1) * 2);
                break;
            }
        }
        throw new TTFException("unmatched instructions");
    }
    
    private int pop() throws TTFException {
        if (deeps <= 0) {
            throw new TTFException("interpreter inserand stack underflow");
        }
        return stack[--deeps];
    }
    
    private void push(int i) throws TTFException {
        if (deeps >= stack.length) {
            throw new TTFException("interpreter inserand stack overflow");
        }
        stack[deeps++] = i;
    }
    
    private void defineFunction(int funcID, byte[] ins) throws TTFException {
        if (funcID < 0 || funcID >= functions.length) {
            throw new TTFException("out of interpreter function define range");
        }
        functions[funcID] = ins;
    }
    
    private void callFunction(int funcID) throws TTFException {
        if (funcID < 0 || funcID >= functions.length) {
            throw new TTFException("out of interpreter function define range");
        }
        if (functions[funcID] == null) {
            throw new TTFException("interpreter function " + funcID + " not defined");
        }
        byte[] prevInstructions = instructions;
        int prevIP = ip;
        execute(functions[funcID]);
        instructions = prevInstructions;
        ip = prevIP;
    }
    
    private void defineExtraINS(int insID, byte[] ins) {
        extraINSs.put(new Integer(insID), ins);
    }
    
    private void callExtraINS(int insID) throws TTFException {
        byte[] ins = extraINSs.get(new Integer(insID));
        if (ins == null) {
            throw new TTFException("call undefined extra instructions");
        }
        byte[] prevInstructions = instructions;
        int prevIP = ip;
        execute(ins);
        instructions = prevInstructions;
        ip = prevIP;
    }
    
    private int getTop(int i) throws TTFException {
        if (i < 0 || i >= deeps) {
            throw new TTFException("out of interpreter data stack");
        }
        return stack[deeps - i - 1];
    }
    
    private void setTop(int i, int v) throws TTFException {
        if (i < 0 || i >= deeps) {
            throw new TTFException("out of interpreter data stack");
        }
        stack[deeps - i - 1] = v;        
    }
    
    private double getCVT(int i) throws TTFException {
        if (i < 0 || i >= controlValueTable.length) {
            throw new TTFException("out of interpreter control value table");
        }
        return controlValueTable[i];
    }
    
    private void setCVT(int i, double d) throws TTFException {
        if (i < 0 || i >= controlValueTable.length) {
            throw new TTFException("out of interpreter control value table");
        }
        controlValueTable[i] = d;
    }
    
    private Point getZonePoint(int zp, int np) throws TTFException {
        if (zp == GLYPH_ZONE) {
            if (glyphPoints == null) {
                throw new TTFException("glyph zone is not exist");
            }
            if (np < 0 || np >= glyphPoints.length) {
                throw new TTFException("out of interpreter glyph zone points");
            }
            return glyphPoints[np];
        }
        else if (zp == TWILIGHT_ZONE) {
            if (twilightPoints == null) {
                throw new TTFException("twilight zone is not exist");
            }
            if (np < 0 || np >= twilightPoints.length) {
                throw new TTFException("out of interpreter twilight zone points");
            }
            if (twilightPoints[np] == null) {
                twilightPoints[np] = new Point(0, 0);
            }
            return twilightPoints[np];
        }
        else {
            throw new TTFException("bad zone point number " + zp);
        }
    }
    
    private int load(int i) throws TTFException {
        if (i < 0 || i >= storages.length) {
            throw new TTFException("out of interpreter storages' range");
        }
        return storages[i];
    }
    
    private void save(int i, int v) throws TTFException {
        if (i < 0 || i >= storages.length) {
            throw new TTFException("out of interpreter storages' range");
        }
        storages[i] = v;
    }
    
    private double round(double v) throws TTFException {
        switch (roundState) {
        case ROUND_TO_HALF_GRID:
            return Math.round(v + 0.5) - 0.5;
        case ROUND_TO_GRID:
            return Math.round(v);
        case ROUND_TO_DOUBLE_GRID:
            return Math.round(v * 2) / 2;
        case ROUND_DOWN_TO_GRID:
            return Math.floor(v);
        case ROUND_UP_TO_GRID:
            return Math.ceil(v);
        case ROUND_OFF:
            return v;
        default:
            return superRound(v);
        }
    }
    
    private double superRound(double v) throws TTFException {
        double period = 0;
        switch ((roundState >> 6) & 0x3) {
        case 0: period = 0.5 * gridPeriod; break; 
        case 1: period = 1.0 * gridPeriod; break;
        case 2: period = 2.0 * gridPeriod; break;
        case 3: throw new TTFException("reserved period in round state");
        }
        
        double phase = 0;
        switch ((roundState >> 4) & 0x3) {
        case 0: phase = 0; break;
        case 1: phase = period / 4.0; break;
        case 2: phase = period / 2.0; break;
        case 3: phase = period * 3.0 / 4.0; break;
        }
        
        double threshold = 0;
        switch (roundState & 0xf) {
        case 0:  threshold = period - 1; break;
        case 1:  threshold = -3.0 / 8.0 * period; break;
        case 2:  threshold = -2.0 / 8.0 * period; break;
        case 3:  threshold = -1.0 / 8.0 * period; break;
        case 4:  threshold =  0; break;
        case 5:  threshold =  1.0 / 8.0 * period; break;
        case 6:  threshold =  2.0 / 8.0 * period; break;
        case 7:  threshold =  3.0 / 8.0 * period; break;
        case 8:  threshold =  4.0 / 8.0 * period; break;
        case 9:  threshold =  5.0 / 8.0 * period; break;
        case 10: threshold =  6.0 / 8.0 * period; break;
        case 11: threshold =  7.0 / 8.0 * period; break;
        case 12: threshold =  period; break;
        case 13: threshold =  9.0 / 8.0 * period; break;
        case 14: threshold = 10.0 / 8.0 * period; break;
        case 15: threshold = 11.0 / 8.0 * period; break;
        }
        
        double n = v; 
        // 1. add engine compensation to n. How?
        // 2. subtract the phase from n.
        n -= phase;
        // 3. add the threshold to n.
        n += threshold;     
        // 4. truncate n to the next lowest periodic value (ignore the phase).
        n = Math.round(n / gridPeriod) * gridPeriod;
        // 5. add the phase back to n.
        n += phase;
        // 6. if rounding caused a positive number to become negative, set n to the positive round value closest to 0.
        if (v > 0 && n < 0) {
            n = gridPeriod;
        }
        // 7. if rounding caused a negative number of become positive, set n to the negative round value closest to 0.
        if (v < 0 && n > 0) {
            n = -gridPeriod;
        }
        return n;
    }
    
    private static double mapDistanceAlongVector(Vector srcVector, Vector dstVector, double distance) {
        double a1 = calcAngle(srcVector.getX(), srcVector.getY());
        double a2 = calcAngle(dstVector.getX(), dstVector.getY());
        double aa = a2 - a1;
        double ds = distance / Math.cos(aa);
        return ds;
    }
    
    private static double calcDistanceAlongVector(double vx, double vy, double x1, double y1, double x2, double y2) {
        double dy = y2 - y1;
        double dx = x2 - x1;
        double dd = Math.sqrt((dx * dx) + (dy * dy));
        double a1 = calcAngle(vx, vy);
        double a2 = calcAngle(dx, dy);
        double aa = a2 - a1;
        double ds = Math.cos(aa) * dd;
        return ds;        
    }
    
    private static double calcDistanceAlongVector(Vector vector, double x1, double y1, double x2, double y2) {
        return calcDistanceAlongVector(vector.getX(), vector.getY(), x1, y1, x2, y2);
    }
    
    private static double calcDistanceAgainstVector(Vector vector, double x1, double y1, double x2, double y2) {
        return calcDistanceAlongVector(-vector.getY(), vector.getX(), x1, y1, x2, y2);
    }
    
    private static double calcAngle(double x, double y) {
        if (x == 0) {
            if (y > 0) {
                return Math.PI / 2;
            }
            else {
                return - Math.PI / 2;
            }
        }
        else {
            double a = Math.atan(y / x);
            if (x < 0) {
                a += Math.PI;
            }
            return a;
        }
    }
    
    private static void movePointAlongVector(Point point, double vx, double vy, double distance) {
        double aa = calcAngle(vx, vy);
        double dx = Math.cos(aa) * distance;
        double dy = Math.sin(aa) * distance;
        point.setX(point.getX() + dx);
        point.setY(point.getY() + dy);        
    }
    
    private static void movePointAlongVector(Point point, Vector vector, double distance) {
        movePointAlongVector(point, vector.getX(), vector.getY(), distance);
    }
    
    private static void movePointAgainstVector(Point point, Vector vector, double distance) {
        movePointAlongVector(point, -vector.getY(), vector.getX(), distance);
    }
    
    /**
     * Convert F26Dot6 to double
     * 
     * @param x F26Dot6 number
     * @return double value
     */
    private static double f26Dot6ToDouble(int x) {
        int i = x >> 6;
        int d = x & 0x003f;
        return i + (d / 64);
    }
    
    /**
     * Convert double to F26Dot6
     * 
     * @param f double number
     * @return F26Dot6 value
     */
    private static int doubleToF26Dot6(double f) {
        int i = (int) Math.floor(f);
        int d = (int) ((f - i) * 64);
        return (i << 6) + d;
    }
    
    /**
     * Convert fraction to F26Dot6
     * 
     * @param numerator numerator number
     * @param denominator denominator number
     * @return F26Dot6 value
     */
    @SuppressWarnings("unused")
	private static int fractionToF26Dot6(int numerator, int denominator) {
        int i = numerator / denominator;
        int d = (int) (((numerator % denominator) / denominator) * 64);
        return (i << 6) + d;
    }    
    
    /**
     * Convert F2Dot14 to double
     * 
     * @param x F2Dot14 number
     * @return double value
     */
    private static double f2Dot14ToDouble(int x) {
        int i = ((short) x) >> 14;
        int d = x & 0x3fff;
        return i + (d / 16384);
    }

    /**
     * Convert double to F2Dot14
     * 
     * @param f double number
     * @return F2Dot14 value
     */
    private static short doubleToF2Dot14(double f) {
        int i = (int) Math.floor(f);
        int d = (int) ((f - i) * 16384);
        return (short) ((i << 14) + d);
    }
    
    /**
     * Convert fraction to F26Dot6
     * 
     * @param numerator numerator number
     * @param denominator denominator number
     * @return F26Dot6 value
     */
    @SuppressWarnings("unused")
	private static short fractionToF2Dot14(int numerator, int denominator) {
        int i = numerator / denominator;
        int d = (int) (((numerator % denominator) / denominator) * 16384);
        return (short) ((i << 14) + d);
    } 
    
    private void insAA() throws TTFException {
        pop();
    }
    
    private void insABS() throws TTFException {
        int t = pop();
        push(Math.abs(t));
    }

    private void insADD() throws TTFException {
        int n2 = pop();
        int n1 = pop();
        push(n1 + n2);
    }

    private void insALIGNPTS() throws TTFException {
        int np2 = pop();
        int np1 = pop();
        Point p2 = getZonePoint(zp0, np2);
        Point p1 = getZonePoint(zp1, np1);
        double d2 = calcDistanceAlongVector(projectionVector, 0, 0, p2.getX(), p2.getY());
        double d1 = calcDistanceAlongVector(projectionVector, 0, 0, p1.getX(), p1.getY());
        double d0 = (d2 + d1) / 2;
        
        double dd;
        dd = mapDistanceAlongVector(projectionVector, freedomVector, d0 - d2);
        movePointAlongVector(p2, freedomVector, dd);
        dd = mapDistanceAlongVector(projectionVector, freedomVector, d0 - d1);
        movePointAlongVector(p1, freedomVector, dd);
    }

    private void insALIGNRP() throws TTFException {
        Point rp = getZonePoint(zp0, rp0);
        for (int i=0; i<loop; ++i) {
            int np = pop();
            Point p = getZonePoint(zp1, np);
            
            double dp = calcDistanceAlongVector(projectionVector, p.getX(), p.getY(), rp.getX(), rp.getY());
            double df = mapDistanceAlongVector(projectionVector, freedomVector, dp);
            movePointAlongVector(p, freedomVector, df);
        }
    }

    private void insAND() throws TTFException {
        boolean e2 = (pop() != 0);
        boolean e1 = (pop() != 0);
        push((e1 & e2) ? TRUE : FALSE);
    }

    private void insCALL() throws TTFException {
        int funcID = pop();
        callFunction(funcID);
    }

    private void insCEILING() throws TTFException {
        int n = pop();
        if (n >= 0) {
            n = ((n + 63) / 64) * 64;
        }
        else {
            n = (n / 64) * 64;
        }
        push(n);
    }

    private void insCINDEX() throws TTFException {
        int k = pop();
        int v = getTop(k - 1);
        push(v);
    }

    private void insCLEAR() {
        deeps = 0;
    }

    private void insDEBUG() throws TTFException {
        pop();
    }

    private void insDELTAC1() throws TTFException {
        throw new TTFException("not implements yet :)");
    }

    private void insDELTAC2() throws TTFException {
        throw new TTFException("not implements yet :)");
    }

    private void insDELTAC3() throws TTFException {
        throw new TTFException("not implements yet :)");
    }

    private void insDELTAP1() throws TTFException {
        throw new TTFException("not implements yet :)");
    }

    private void insDELTAP2() throws TTFException {
        throw new TTFException("not implements yet :)");
    }

    private void insDELTAP3() throws TTFException {
        throw new TTFException("not implements yet :)");
    }

    private void insDEPTH() throws TTFException {
        push(deeps);
    }

    private void insDIV() throws TTFException {
        int n2 = pop();
        int n1 = pop();
        push((n1 * 64) / n2);
    }

    private void insDUP() throws TTFException {
        int n = getTop(0);
        push(n);
    }

    private void insEIF() throws TTFException {
        throw new TTFException("unmatched instruction EIF");
    }

    private void insELSE() throws TTFException {
        throw new TTFException("unmatched instruction ELSE");
    }

    private void insENDF() throws TTFException {
        throw new TTFException("unmatched instruction ENDF");
    }

    private void insEQ() throws TTFException {
        int e2 = pop();
        int e1 = pop();
        push((e2 == e1) ? TRUE : FALSE);
    }

    private void insEVEN() throws TTFException {
        int e = pop();
        if (round(e) % 2 == 0) {
            push(1);
        }
        else {
            push(0);
        }
    }

    private void insFDEF() throws TTFException {
        /*
         * FDEF | . | ... | ENDF | . | ...
         *      start             end 
         */
        int funcID = pop();
        int start = ip;
        int match = 1;
        while (match != 0) {
            int r = until(new int[]{INS_FDEF, INS_IDEF, INS_ENDF});
            if (r == INS_FDEF || r == INS_IDEF) {
                match++;
            }
            else { // r == INS_ENDF
                match--;
            }
            nextByte(); // eat last instruction
        }
        int end = ip;
        byte[] temp = new byte[end - start - 1];
        System.arraycopy(instructions, start, temp, 0, end - start - 1);
        defineFunction(funcID, temp);
    }

    private void insFLIPOFF() {
        autoFlip = false;
    }

    private void insFLIPON() {
        autoFlip = true;
    }

    private void insFLIPPT() throws TTFException {
        for (int i=0; i<loop; ++i) {
            int np = pop();
            Point p = getZonePoint(zp0, np);
            p.flip();
        }
    }

    private void insFLIPRGOFF() throws TTFException {
        int ph = pop();
        int pl = pop();
        if (ph < pl) {
            throw new TTFException("instruction FLIPRGON high range little than low range");
        }
        for (int i=pl; i<ph+1; ++i) {
            Point p = getZonePoint(zp0, i);
            p.setOnCurve(false);
        }
    }

    private void insFLIPRGON() throws TTFException {
        int ph = pop();
        int pl = pop();
        if (ph < pl) {
            throw new TTFException("instruction FLIPRGON high range little than low range");
        }
        for (int i=pl; i<ph+1; ++i) {
            Point p = getZonePoint(zp0, i);
            p.setOnCurve(true);
        }
    }

    private void insFLOOR() throws TTFException {
        int n = pop();
        if (n >= 0) {
            n = (n / 64) * 64;
        }
        else {
            n = ((n - 63) / 64) * 64;
        }
        push(n);
    }
    
    private void insGC(int arg) throws TTFException {
        if (arg != 0 && arg != 1) {
            throw new TTFException("bad arg " + arg + " for instruction GC");                        
        }
        int np = pop();
        Point p = getZonePoint(zp2, np);
        double distance;
        if (arg == 0) {
            distance = calcDistanceAlongVector(projectionVector, 0, 0, p.getX(), p.getY());
        }
        else {
            distance = calcDistanceAlongVector(dualProjectionVector, 0, 0, p.getOrgX(), p.getOrgY());
        }
        push(doubleToF26Dot6(distance));
    }

    private void insGETINFO() throws TTFException {
        throw new TTFException("not implements yet :)");
    }

    private void insGFV() throws TTFException {
        push(doubleToF2Dot14(freedomVector.x));
        push(doubleToF2Dot14(freedomVector.y));
    }

    private void insGPV() throws TTFException {
        push(doubleToF2Dot14(projectionVector.x));
        push(doubleToF2Dot14(projectionVector.y));        
    }

    private void insGT() throws TTFException {
        int e2 = pop();
        int e1 = pop();
        push((e1 > e2) ? TRUE : FALSE);
    }

    private void insGTEQ() throws TTFException {
        int e2 = pop();
        int e1 = pop();
        push((e1 >= e2) ? TRUE : FALSE); 
    }

    private void insIDEF() throws TTFException {
        /*
         * IDEF | . | ... | ENDF | . | ...
         *      start             end 
         */
        int funcID = pop();
        int start = ip;
        int match = 1;
        while (match != 0) {
            int r = until(new int[]{INS_FDEF, INS_IDEF, INS_ENDF});
            if (r == INS_FDEF || r == INS_IDEF) {
                match++;
            }
            else { // r == INS_ENDF
                match--;
            }
            nextByte(); // eat last instruction
        }
        int end = ip;
        byte[] temp = new byte[end - start - 1];
        System.arraycopy(instructions, start, temp, 0, end - start - 1);
        defineExtraINS(funcID, temp);
    }

    private void insIF() throws TTFException {
        int e = pop();
        /*
         * IF | . | ... | ELSE | . | ... | EIF | . | ...
         *    start            middle           end
         */
        int start = ip;
        int middle = 0;
        int match = 1;
        while (match != 0) {
            int r = until(new int[] {INS_IF, INS_ELSE, INS_EIF});
            if (r == INS_IF) {
                match++;
            }
            else if (r == INS_EIF){
                match--;
            }
            else { // r == INS_ELSE
                if (match == 1) {
                    middle = ip + 1;
                }
            }
            nextByte();
        }
        int end = ip;

        if (e != 0) { // run IF part
            if (middle != 0) {
                end = middle;
            }
        }
        else { // run ELSE part
            if (middle != 0) {
                start = middle;
            }
            else {
                start = -1;
            }
        }
        
        if (start != -1) {
            byte[] temp = new byte[end - start - 1];
            System.arraycopy(instructions, start, temp, 0, end - start - 1);
            byte[] prevInstructions = instructions;
            int prevIP = ip;
            execute(temp);
            instructions = prevInstructions;
            ip = prevIP;
        }
    }

    private void insINSTCTRL() throws TTFException {
        throw new TTFException("not implements yet :)");
    }

    private void insIP() throws TTFException {
        Point r1 = getZonePoint(zp0, rp1);
        Point r2 = getZonePoint(zp1, rp2);
        // tips: r1nd == Reference point 1 Current Distance
        double r1cd = calcDistanceAlongVector(projectionVector, 0, 0, r1.getX(), r1.getY());
        double r2cd = calcDistanceAlongVector(projectionVector, 0, 0, r2.getX(), r2.getY());
        double rcdd = r2cd - r1cd;
        double r1pd = calcDistanceAlongVector(dualProjectionVector, 0, 0, r1.getOrgX(), r1.getOrgY());
        double r2pd = calcDistanceAlongVector(dualProjectionVector, 0, 0, r2.getOrgX(), r2.getOrgY());
        double rpdd = r2pd - r1pd;
        for (int i=0; i<loop; ++i) {
            int np = pop();
            Point p = getZonePoint(zp2, np);
            double pcd = calcDistanceAlongVector(projectionVector, 0, 0, p.getX(), p.getY());
            double ppd = calcDistanceAlongVector(dualProjectionVector, 0, 0, p.getOrgX(), p.getOrgY());
            double ptd = ((ppd - r1pd) / rpdd) * rcdd + r1cd;
            double mov = mapDistanceAlongVector(projectionVector, freedomVector, ptd - pcd);
            movePointAlongVector(p, freedomVector, mov);
        }
    }

    private void insISECT() throws TTFException {
        throw new TTFException("not implements yet :)");
    }

    private void insIUP(int arg) throws TTFException {
        throw new TTFException("not implements yet :)");
    }

    private void insJMPR() throws TTFException {
        int n = pop();
        seek(n - 1);
    }

    private void insJROF() throws TTFException {
        int b = pop();
        int n = pop();
        if (b == 0) {
            seek(n - 1);
        }        
    }

    private void insJROT() throws TTFException {
        int b = pop();
        int n = pop();
        if (b != 0) {
            seek(n - 1);
        }
    }

    private void insLOOPCALL() throws TTFException {
        int funcID = pop();
        int counts = pop();
        for (int i=0; i<counts; ++i) {
            callFunction(funcID);
        }
    }

    private void insLT() throws TTFException {
        int e2 = pop();
        int e1 = pop();
        push((e1 < e2) ? TRUE : FALSE);
    }

    private void insLTEQ() throws TTFException {
        int e2 = pop();
        int e1 = pop();
        push((e1 <= e2) ? TRUE : FALSE);
    }

    private void insMAX() throws TTFException {
        int e2 = pop();
        int e1 = pop();
        push(Math.max(e1, e2));
    }

    private void insMD(int arg) throws TTFException {
        if (arg != 0 && arg != 1) {
            throw new TTFException("bad arg " + arg + " for instruction MD");
        }
        int np2 = pop();
        int np1 = pop();
        Point p2 = getZonePoint(zp1, np2);
        Point p1 = getZonePoint(zp0, np1);
        double distance;
        if (arg == 0) {
            distance = calcDistanceAlongVector(projectionVector, p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }
        else {
            distance = calcDistanceAlongVector(dualProjectionVector, p1.getOrgX(), p1.getOrgY(), p2.getOrgX(), p2.getOrgY());
        }
        push(doubleToF26Dot6(distance));
    }

    private void insMDAP(int arg) throws TTFException {
        if (arg != 0 && arg != 1) {
            throw new TTFException("bad arg " + arg + " for instruction MDAP");
        }
        int np = pop();
        Point p = getZonePoint(zp0, np);
        p.setTouched(true);
        if (arg == 1) {
            double distance;
            double delta;
            distance = calcDistanceAlongVector(projectionVector, 0, 0, p.getX(), p.getY());
            delta = round(distance) - distance;
            movePointAlongVector(p, projectionVector, delta);
            distance = calcDistanceAgainstVector(projectionVector, 0, 0, p.getX(), p.getY());
            delta = round(distance) - distance;
            movePointAgainstVector(p, projectionVector, delta);
        }
        rp0 = np;
        rp1 = np;
    }

    private void insMDRP(int arg) throws TTFException {
        int a = (arg >> 4) & 1;
        int b = (arg >> 3) & 1;
        int c = (arg >> 2) & 1;
//      int de = arg & 3;
        int np = pop();
        Point p = getZonePoint(zp1, np);
        Point r = getZonePoint(zp0, rp0);
        double pd = calcDistanceAlongVector(dualProjectionVector, r.getOrgX(), r.getOrgY(), p.getOrgX(), p.getOrgY());
        double cd = calcDistanceAlongVector(projectionVector, r.getX(), r.getY(), p.getX(), p.getY());
        if (Math.abs(pd - singleWidthValue) <= Math.abs(singleWidthCutIn)) {
            pd = singleWidthValue;
        }
        if (c == 1) {
            pd = round(pd);
        }
        if (b == 1) {
            pd = Math.max(minimumDistance, pd);
        }
        double dd = mapDistanceAlongVector(projectionVector, freedomVector, pd - cd);
        movePointAlongVector(p, freedomVector, dd);
        rp1 = rp0;
        rp2 = np;
        if (a == 1) {
            rp0 = np;
        }
    }

    private void insMIAP(int arg) throws TTFException {
        if (arg != 0 && arg != 1) {
            throw new TTFException("bad arg " + arg + " for instruction MIAP");
        }
        int c = pop();
        int n = pop();
        double td = getCVT(c);
        Point p = getZonePoint(zp0, n);
        double cd = calcDistanceAlongVector(projectionVector, 0, 0, p.getX(), p.getY());
        double dd = 0;
        if (arg == 1) {
            if (Math.abs(td - cd) <= Math.abs(controlValueCutIn)) {
                dd = round(cd) - cd;
            }
            else {
                dd = round(td) - cd;
            }
        }
        else {
            dd = cd - td;
        }
        dd = mapDistanceAlongVector(projectionVector, freedomVector, dd);
        movePointAlongVector(p, freedomVector, dd);
        rp0 = n;
        rp1 = n;
    }

    private void insMIN() throws TTFException {
        int e2 = pop();
        int e1 = pop();
        push(Math.min(e1, e2));
    }

    private void insMINDEX() throws TTFException {
        int k = pop();
        int t = getTop(k - 1);
        for (int i = k - 1; i > 0; i--) {
            setTop(i, getTop(i-1));
        }
        setTop(0, t);
    }

    private void insMIRP(int arg) throws TTFException {
        int a = (arg >> 4) & 1;
        int b = (arg >> 3) & 1;
        int c = (arg >> 2) & 1;
//      int de = arg & 3;
        int nc = pop();
        int np = pop();
        double cv = getCVT(nc);
        Point p = getZonePoint(zp1, np);
        Point r = getZonePoint(zp0, rp0);
        double pd = calcDistanceAlongVector(dualProjectionVector, r.getOrgX(), r.getOrgY(), p.getOrgX(), p.getOrgY());
        double cd = calcDistanceAlongVector(projectionVector, r.getX(), r.getY(), p.getX(), p.getY());
        if (Math.abs(pd - cv) <= Math.abs(controlValueCutIn)) {
            pd = cv;
        }
        if (Math.abs(pd - singleWidthValue) <= Math.abs(singleWidthCutIn)) {
            pd = singleWidthValue;
        }
        if (c == 1) {
            pd = round(pd);
        }
        if (b == 1) {
            pd = Math.max(minimumDistance, pd);
        }
        double dd = mapDistanceAlongVector(projectionVector, freedomVector, pd - cd);
        movePointAlongVector(p, freedomVector, dd);
        rp1 = rp0;
        rp2 = np;
        if (a == 1) {
            rp0 = np;
        }
    }

    private void insMPPEM() throws TTFException {
        push((int) pixelPerEm());
    }

    private void insMPS() throws TTFException {
        push((int) pointSize);
    }

    private void insMSIRP(int arg) throws TTFException {
        if (arg != 0 && arg != 1) {
            throw new TTFException("bad arg " + arg + " instruction MSIRP");
        }
        int nd = pop();
        int np = pop();
        double ds = f26Dot6ToDouble(nd);
        Point p = getZonePoint(zp1, np);
        Point r = getZonePoint(zp0, rp0);
        double rd = calcDistanceAlongVector(projectionVector, 0, 0, r.getX(), r.getY());
        double td = rd + ds;
        double pd = calcDistanceAlongVector(projectionVector, 0, 0, p.getX(), p.getY());
        double dd = mapDistanceAlongVector(projectionVector, freedomVector, td - pd);
        movePointAlongVector(p, freedomVector, dd);
        if (arg == 1) {
            rp0 = np;
        }
    }

    private void insMUL() throws TTFException {
        int n2 = pop();
        int n1 = pop();
        push((n2 * n1) / 64);
    }

    private void insNEG() throws TTFException {
        int n = pop();
        push(-n);
    }

    private void insNEQ() throws TTFException {
        int e2 = pop();
        int e1 = pop();
        push((e1 != e2) ? TRUE : FALSE);
    }

    private void insNOT() throws TTFException {
        int e = pop();
        push((e != 0) ? 0 : 1);
    }

    private void insNPushB() throws TTFException {
        int n = nextByte();
        for (int i=0; i<n; ++i) {
            int b = nextByte();
            push(b);
        }
    }

    private void insNPUSHW() throws TTFException {
        int n = nextByte();
        for (int i=0; i<n; ++i) {
            int w = nextWord();
            push(w);
        }
    }

    private void insNROUND(int arg) {
        // DO NOTHING
    }

    private void insODD() throws TTFException {
        int e = pop();
        if (round(e) % 2 != 0) {
            push(1);
        }
        else {
            push(0);
        }
    }

    private void insOR() throws TTFException {
        boolean e2 = (pop() != 0);
        boolean e1 = (pop() != 0);
        push((e1 | e2) ? TRUE : FALSE);
    }

    private void insPOP() throws TTFException {
        pop();
    }

    private void insPUSHB(int arg) throws TTFException {
        int n = arg + 1;
        for (int i=0; i<n; ++i) {
            int b = nextByte();
            push(b);
        }
    }

    private void insPUSHW(int arg) throws TTFException {
        int n = arg + 1;
        for (int i=0; i<n; ++i) {
            int w = nextWord();
            push(w);
        }
    }

    private void insRCVT() throws TTFException {
        int i = pop();
        double v = getCVT(i);
        push(doubleToF26Dot6(v));
    }

    private void insRDTG() {
        roundState = ROUND_DOWN_TO_GRID;
    }

    private void insROFF() {
        roundState = ROUND_OFF;
    }

    private void insROLL() throws TTFException {
        int a = pop();
        int b = pop();
        int c = pop();
        push(b);
        push(a);
        push(c);
    }

    private void insROUND(int arg) throws TTFException {
        int x = pop();
        double f = f26Dot6ToDouble(x);
        double r = round(f);
        push(doubleToF26Dot6(r));
    }

    private void insRS() throws TTFException {
        int d = pop();
        push(load(d));
    }

    private void insRTDG() {
        roundState = ROUND_TO_DOUBLE_GRID;
    }

    private void insRTG() {
        roundState = ROUND_TO_GRID;
    }

    private void insRTHG() {
        roundState = ROUND_TO_HALF_GRID;
    }

    private void insRUTG() {
        roundState = ROUND_UP_TO_GRID;
    }

    private void insSHC(int arg) throws TTFException {
        throw new TTFException("not implements yet :)");
    }

    private void insSHP(int arg) throws TTFException {
        int rp = 0;
        int zp = 0;
        if (arg == 0) {
            rp = rp2;
            zp = zp1;
        }
        else if (arg == 1) {
            rp = rp1;
            zp = zp0;
        }
        else {
            throw new TTFException("bad arg " + arg + " for instruction SHP ");
        }
        
        Point r = getZonePoint(zp, rp);
        double distance;
        distance = calcDistanceAlongVector(projectionVector, r.getOrgX(), r.getOrgY(), r.getX(), r.getY());
        distance = mapDistanceAlongVector(projectionVector, freedomVector, distance);
        for (int i=0; i<loop; ++i) {
            int np = pop();
            Point p = getZonePoint(zp2, np);
            movePointAlongVector(p, freedomVector, distance);
        }
    }

    private void insSHZ(int arg) throws TTFException {
        Point r = null;
        if (arg == 0) {
            r = getZonePoint(zp1, rp2);
        }
        else if (arg == 1){
            r = getZonePoint(zp0, rp1);
        }
        else {
            throw new TTFException("bad arg " + arg + " for instruction SHZ ");
        }
        
        double distance;
        distance = calcDistanceAlongVector(projectionVector, r.getOrgX(), r.getOrgY(), r.getX(), r.getY());
        distance = mapDistanceAlongVector(projectionVector, freedomVector, distance);
    
        Point[] points = null;
        int z = pop();
        if (z == GLYPH_ZONE) {
            points = glyphPoints;
        }
        else if (z == TWILIGHT_ZONE) {
            points = twilightPoints;
        }
        else {
            throw new TTFException("bad zone point number " + z);
        }
        
        for (int i=0; i<points.length; ++i) {
            movePointAlongVector(points[i], freedomVector, distance);
        }
    }

    private void insS45ROUND() throws TTFException {
        int n = pop();
        roundState = n | 0xff000000;
        gridPeriod = Math.sqrt(2) / 2;
    }

    private void insSANGW() throws TTFException {
        pop();
    }

    private void insSCANCTRL() throws TTFException {
        throw new TTFException("not implements yet :)");
    }

    private void insSCANTYPE() throws TTFException {
        throw new TTFException("not implements yet :)");
    }

    private void insSCFS() throws TTFException {
        throw new TTFException("not implements yet :)");
    }

    private void insSCVTCI() throws TTFException {
        int n = pop();
        controlValueCutIn = f26Dot6ToDouble(n);
    }

    private void insSDB() throws TTFException {
        deltaBase = pop();
    }

    private void insSDPVTL(int arg) throws TTFException {
        int np2 = pop();
        int np1 = pop();
        Point p2 = getZonePoint(zp2, np2);
        Point p1 = getZonePoint(zp1, np1);
        double dx = p1.getOrgX() - p2.getOrgX();
        double dy = p1.getOrgY() - p2.getOrgY();
        if (arg == ARG_PARALLEL) {
            dualProjectionVector.setXY(dx, dy);
        }
        else if (arg == ARG_PERPENDICULAR){
            dualProjectionVector.setXY(-dy, dx);
        }
        else {
            throw new TTFException("bad SDPVTL arg " + arg);
        }
    }

    private void insSDS() throws TTFException {
        deltaShift = pop();
    }

    private void insSFVFS() throws TTFException {
        int y = pop();
        int x = pop();
        freedomVector.setXY(f2Dot14ToDouble(x), f2Dot14ToDouble(y));
    }

    private void insSFVTPV() {
        freedomVector.x = projectionVector.x;
        freedomVector.y = projectionVector.y;
    }

    private void insSFVTCA(int arg) throws TTFException {
        if (arg == ARG_X_AXIS) {
            freedomVector.setXY(1, 0);
        }
        else if (arg == ARG_Y_AXIS) {
            freedomVector.setXY(0, 1);
        }
        else {
            throw new TTFException("bad SFVTCA arg " + arg);
        }
    }

    private void insSFVTL(int arg) throws TTFException {
        int np2 = pop();
        int np1 = pop();
        Point p2 = getZonePoint(zp2, np2);
        Point p1 = getZonePoint(zp1, np1);
        double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();
        if (arg == ARG_PARALLEL) {
            freedomVector.setXY(dx, dy);
        }
        else if (arg == ARG_PERPENDICULAR){
            freedomVector.setXY(-dy, dx);
        }
        else {
            throw new TTFException("bad SFVTL arg " + arg);
        }
    }

    private void insSHPIX() throws TTFException {
        int d = pop();
        double distance = f26Dot6ToDouble(d);
        for (int i=0; i<loop; ++i) {
            int np = pop();
            Point p = getZonePoint(zp2, np);
            movePointAlongVector(p, freedomVector, distance);
        }
    }

    private void insSLOOP() throws TTFException {
        int n = pop();
        if (n <= 0) {
            throw new TTFException("bad interpreter loop value " + loop);
        }
        loop = n;
    }

    private void insSMD() throws TTFException {
        int n = pop();
        minimumDistance = f26Dot6ToDouble(n);
    }

    private void insSPVFS() throws TTFException {
        int y = pop();
        int x = pop();
        projectionVector.setXY(f2Dot14ToDouble(x), f2Dot14ToDouble(y));
    }

    private void insSPVTCA(int arg) throws TTFException {
        if (arg == ARG_X_AXIS) {
            projectionVector.setXY(1, 0);
        }
        else if (arg == ARG_Y_AXIS) {
            projectionVector.setXY(0, 1);
        }
        else {
            throw new TTFException("bad SPVTCA arg " + arg);
        }
    }

    private void insSPVTL(int arg) throws TTFException {
        int np2 = pop();
        int np1 = pop();
        Point p2 = getZonePoint(zp2, np2);
        Point p1 = getZonePoint(zp1, np1);
        double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();
        if (arg == ARG_PARALLEL) {
            projectionVector.setXY(dx, dy);
        }
        else if (arg == ARG_PERPENDICULAR){
            projectionVector.setXY(-dy, dx);
        }
        else {
            throw new TTFException("bad SPVTL arg " + arg);
        }
    }

    private void insSVTCA(int arg) throws TTFException {
        if (arg == ARG_X_AXIS) {
            projectionVector.setXY(1, 0);
            freedomVector.setXY(1, 0);
        }
        else if (arg == ARG_Y_AXIS) {
            projectionVector.setXY(0, 1);
            freedomVector.setXY(0, 1);           
        }
        else {
            throw new TTFException("bad SVTCA arg " + arg);
        }
    }

    private void insSROUND() throws TTFException {
        int n = pop();
        roundState = n | 0xff000000;
        gridPeriod = 1.0;
    }

    private void insSRP0() throws TTFException {
        int n = pop();
        rp0 = n;
    }

    private void insSRP1() throws TTFException {
        int n = pop();
        rp1 = n;
    }

    private void insSRP2() throws TTFException {
        int n = pop();
        rp2 = n;
    }

    private void insSSW() throws TTFException {
        int n = pop();
        singleWidthValue = f26Dot6ToDouble(n); 
    }

    private void insSSWCI() throws TTFException {
        int n = pop();
        singleWidthCutIn = f26Dot6ToDouble(n);
    }

    private void insSUB() throws TTFException {
        int n2 = pop();
        int n1 = pop();
        push(n1 - n2);
    }

    private void insSWAP() throws TTFException {
        int e2 = pop();
        int e1 = pop();
        push(e2);
        push(e1);
    }

    private void insSZP0() throws TTFException {
        int n = pop();
        if (n != TWILIGHT_ZONE && n != GLYPH_ZONE) {
            throw new TTFException("bad zone pointer number " + n);
        }
        zp0 = n;
    }

    private void insSZP1() throws TTFException {
        int n = pop();
        if (n != TWILIGHT_ZONE && n != GLYPH_ZONE) {
            throw new TTFException("bad zone pointer number " + n);
        }
        zp1 = n;
    }

    private void insSZP2() throws TTFException {
        int n = pop();
        if (n != TWILIGHT_ZONE && n != GLYPH_ZONE) {
            throw new TTFException("bad zone pointer number " + n);
        }
        zp2 = n;
    }

    private void insSZPS() throws TTFException {
        int n = pop();
        if (n != TWILIGHT_ZONE && n != GLYPH_ZONE) {
            throw new TTFException("bad zone pointer number " + n);
        }
        zp0 = n;
        zp1 = n;
        zp2 = n;
    }

    private void insUTP() throws TTFException {
        throw new TTFException("not implements yet :)");
    }

    private void insWCVTF() throws TTFException {
        int v = pop();
        int i = pop();
        setCVT(i, funit2pixels(v));
    }

    private void insWCVTP() throws TTFException {
        int v = pop();
        int i = pop();
        setCVT(i, f26Dot6ToDouble(v));
    }

    private void insWS() throws TTFException {
        int v = pop();
        int i = pop();
        save(i, v);
    }

}
