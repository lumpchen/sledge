package com.docscience.pathfinder.font.driver.type1;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Parser and generate a Type1 CharString.
 * 
 * @author wxin
 *
 */
public class Type1CharString {
	
	private static class ByteBuffer {
		private byte[] bytes = new byte[256];
		private int size = 0;
		
		@SuppressWarnings("unused")
		public int size() {
			return size;
		}
		
		public void put(int b) {
			if (size >= bytes.length) {
				byte[] temp = new byte[bytes.length * 2];
				System.arraycopy(bytes, 0, temp, 0, bytes.length);
				bytes = temp;
			}
			bytes[size++] = (byte) b;
		}
		
		public byte[] getBytes() {
			byte[] result = new byte[size];
			System.arraycopy(bytes, 0, result, 0, size);
			return result;
		}
	}

	public static interface Command {
		public void putBytes(ByteBuffer bBuffer);
		public boolean isOperator();
	}
	
	private static final int ESCAPE_BYTE = 12;

    public static final Operator HSTEM = new Operator("hstem", 1);
    public static final Operator VSTEM = new Operator("vstem", 3);
    public static final Operator VMOVETO = new Operator("vmoveto", 4);
    public static final Operator RLINETO = new Operator("rlineto", 5);
    public static final Operator HLINETO = new Operator("hlineto", 6);
    public static final Operator VLINETO = new Operator("vlineto", 7);
    public static final Operator RRCURVETO = new Operator("rrcurveto", 8);
    public static final Operator CLOSEPATH = new Operator("closepath", 9);
    public static final Operator CALLSUBR = new Operator("callsubr", 10);
    public static final Operator RETURN = new Operator("return", 11);
    public static final Operator HSBW = new Operator("hsbw", 13);
    public static final Operator ENDCHAR = new Operator("endchar", 14);
    public static final Operator RMOVETO = new Operator("rmoveto", 21);
    public static final Operator HMOVETO = new Operator("hmoveto", 22);
    public static final Operator VHCURVETO = new Operator("vhcurveto", 30);
    public static final Operator HVCURVETO = new Operator("hvcurveto", 31);
    public static final Operator DOTSECTION = new Operator("dotsection", ESCAPE_BYTE, 0);
    public static final Operator VSTEM3 = new Operator("vstem3", ESCAPE_BYTE, 1);
    public static final Operator HSTEM3 = new Operator("hstem3", ESCAPE_BYTE, 2);
    public static final Operator SEAC = new Operator("seac", ESCAPE_BYTE, 6);
    public static final Operator SBW = new Operator("sbw", ESCAPE_BYTE, 7);
    public static final Operator DIV = new Operator("div", ESCAPE_BYTE, 12);
    public static final Operator CALLOTHERSUBR = new Operator("callothersubr", ESCAPE_BYTE, 16);
    public static final Operator POP = new Operator("pop", ESCAPE_BYTE, 17);
    public static final Operator SETCURRENTPOINT = new Operator("setcurrentpoint", ESCAPE_BYTE, 33);
    
	public static class Operator implements Command {
		private String name;
		private byte hiByte;
		private byte loByte;
		
		private Operator(String name, int hiByte) {
			this.name = name;
			this.hiByte = (byte) hiByte;
		}
		
		private Operator(String name, int hiByte, int loByte) {
			this.name = name;
			this.hiByte = (byte) hiByte;
			this.loByte = (byte) loByte;
		}
		
		@Override
		public void putBytes(ByteBuffer bBuffer) {
			if (this.hiByte != ESCAPE_BYTE) {
				bBuffer.put(hiByte);
			}
			else {
				bBuffer.put(hiByte);
				bBuffer.put(loByte);
			}
		}
		
		@Override
		public String toString() {
			return name;
		}
		
		public String getName() {
			return name;
		}
		
		@Override
		public boolean isOperator() {
			return true;
		}
	}
	
	public static class Number implements Command {
		private static final Number[] numberCache = new Number[256];
		
		static {
			for (int i=0; i<256; ++i) {
				numberCache[i] = new Number(i - 128);
			}
		}
		
		public static Number valueOf(int value) {
			if (value >= -128 && value <= 127) {
				return numberCache[value + 128];
			}
			else {
				return new Number(value); 
			}
		}

		private final int number;
		
		public Number(int b) {
			number = b;
		}
		
		@Override
		public void putBytes(ByteBuffer bBuffer) {
            if (number < -1131 || number > 1131) {
                // five byte number
                bBuffer.put(255);
                bBuffer.put(number >> 24);
                bBuffer.put(number >> 16);
                bBuffer.put(number >> 8);
                bBuffer.put(number);
            }
            else if (number < -107) {
                // -108 ~ -1131
                int p = -(number + 108);
                int w = p & 0xff;
                int v = (p >> 8) + 251;
                assert(v >= 251 && v <= 254);
                bBuffer.put(v);
                bBuffer.put(w);
            }
            else if (number <= 107) {
                // -107 ~ 107
                bBuffer.put(number + 139);
            }
            else {
                // 108 ~ 1131
                int p = number - 108;
                int w = p & 0xff;
                int v = (p >> 8) + 247;
                assert(v >= 247 && v <= 250) : "v=" + v + ",w=" + w + ",n=" + number;
                bBuffer.put(v);
                bBuffer.put(w);
            }
		}
		
		@Override
		public String toString() {
			return Integer.toString(number);
		}
		
		@Override
		public boolean isOperator() {
			return false;
		}
		
		public int intValue() {
			return number;
		}
	}
	
	private List<Command> commands = new LinkedList<Command>();
	
	public byte[] getBytes() {
		ByteBuffer bBuffer = new ByteBuffer();
		Iterator<Command> itr = commands.iterator();
		while (itr.hasNext()) {
			Command cmd = itr.next();
			cmd.putBytes(bBuffer);
		}
		return bBuffer.getBytes();
	}
	
	public byte[] getEncryptedBytes(byte[] leading) {
		byte[] src = getBytes();
		byte[] enc = new byte[src.length + leading.length];
		Type1Encryption.encryptCharString(src, enc, leading);
		return enc;
	}
	
	public byte[] getEncryptedBytes(int nLeading) {
		byte[] src = getBytes();
		byte[] enc = new byte[src.length + nLeading];
		Type1Encryption.encryptCharString(src, enc, nLeading);
		return enc;
	}
	
	public void loadFromEncryptedBytes(byte[] encryptedBytes) throws Type1CharStringException {
		byte[] src = new byte[encryptedBytes.length - Type1Encryption.DEFAULT_LEADING_BYTES_NUMBER];
		Type1Encryption.decryptCharString(encryptedBytes, src);
		loadFromBytes(src);
	}
	
	public void loadFromEncryptedBytes(byte[] encryptedBytes, int numLeading) throws Type1CharStringException {
		byte[] src = new byte[encryptedBytes.length - numLeading];
		Type1Encryption.decryptCharString(encryptedBytes, src, numLeading);
		loadFromBytes(src);		
	}

	public void loadFromBytes(byte[] bytes) throws Type1CharStringException {
		commands.clear();
		int i = 0;
        int c;
        while (i < bytes.length && (c = (bytes[i++] & (byte) 0xff)) != -1) {
            if (c < 32) {
                switch(c) {
                case 1: hstem(); break;
                case 3: vstem(); break;
                case 4: vmoveto(); break;
                case 5: rlineto(); break;
                case 6: hlineto(); break;
                case 7: vlineto(); break;
                case 8: rrcurveto(); break;
                case 9: closepath(); break;
                case 10: callsubr(); break;
                case 11: callreturn(); break;
                case 12: {
                    int d = (bytes[i++] & 0xff);
                    switch(d) {
                    case 0: dotsection(); break;
                    case 1: vstem3(); break;
                    case 2: hstem3(); break;
                    case 6: seac(); break;
                    case 7: sbw(); break;
                    case 12: div(); break;
                    case 16: callothersubr(); break;
                    case 17: pop(); break;
                    case 33: setcurrentpoint(); break;
                    default:
                        throw new Type1CharStringException("known command");
                    }
                    break;
                }
                case 13: hsbw(); break;
                case 14: endchar(); break;
                case 21: rmoveto(); break;
                case 22: hmoveto(); break;
                case 30: vhcurveto(); break;
                case 31: hvcurveto(); break;
                default:
                    throw new Type1CharStringException("known command");
                }
            }
            else if (c <= 246) {
                number(c - 139);
            }
            else if (c <= 250) {
                int d = (bytes[i++] & 0xff);
                number((c - 247) * 256 + d + 108);
            }
            else if (c <= 254) {
                int d = (bytes[i++] & 0xff);
                number(-(c - 251) * 256 - d - 108);
            }
            else {
                assert(c == 255);
                int v = 0;
                for (int j=0; j<4; ++j) {
                    int d = (bytes[i++] & 0xff);
                    v = (v << 8) + d;
                }
                number(v);
            }
        }
	}

	public void number(int i) {
		commands.add(Number.valueOf(i));
	}
		
	public void callothersubr() {
		commands.add(CALLOTHERSUBR);
	}
	
    public void callothersubr(int[] args, int othersubr) {
        for (int i=0; i<args.length; ++i) {
            number(args[i]);
        }
        number(args.length);
        number(othersubr);
        callothersubr();
    }

    public void callreturn() {
    	commands.add(RETURN);
    }
    
    public void callsubr() {
    	commands.add(CALLSUBR);
    }

    public void callsubr(int subr) {
        number(subr);
        callsubr();
    }

    public void closepath() {
    	commands.add(CLOSEPATH);
    }
    
    public void div() {
    	commands.add(DIV);
    }

    public void div(int a, int b) {
        number(a);
        number(b);
        div();
    }

    public void dotsection() {
    	commands.add(DOTSECTION);
    }

    public void endchar() {
    	commands.add(ENDCHAR);
    }

    public void hlineto() {
    	commands.add(HLINETO);
    }
    
    public void hlineto(int dx) {
        number(dx);
        hlineto();
    }
    
    public void hmoveto() {
    	commands.add(HMOVETO);
    }

    public void hmoveto(int dx) {
        number(dx);
        hmoveto();
    }

    public void hsbw() {
    	commands.add(HSBW);
    }
    
    public void hsbw(int sbx, int wx) {
        number(sbx);
        number(wx);
        hsbw();
    }
    
    public void hstem() {
    	commands.add(HSTEM);
    }

    public void hstem(int y, int dy) {
        number(y);
        number(dy);
        hstem();
    }
    
    public void hstem3() {
    	commands.add(HSTEM3);
    }

    public void hstem3(int y0, int dy0, int y1, int dy1, int y2, int dy2) {
        number(y0);
        number(dy0);
        number(y1);
        number(dy1);
        number(y2);
        number(dy2);
        hstem3();
    }
    
    public void hvcurveto() {
    	commands.add(HVCURVETO);
    }

    public void hvcurveto(int dx1, int dx2, int dy2, int dy3) {
        number(dx1);
        number(dx2);
        number(dy2);
        number(dy3);
        hvcurveto();
    }

    public void pop() {
    	commands.add(POP);
    }

    public void rlineto() {
    	commands.add(RLINETO);
    }
    
    public void rlineto(int dx, int dy) {
        number(dx);
        number(dy);
        rlineto();
    }

    public void rmoveto() {
    	commands.add(RMOVETO);
    }
    
    public void rmoveto(int dx, int dy) {
        number(dx);
        number(dy);
        rmoveto();
    }

    public void rrcurveto() {
    	commands.add(RRCURVETO);    	
    }
    
    public void rrcurveto(int dx1, int dy1, int dx2, int dy2, int dx3, int dy3) {
        number(dx1);
        number(dy1);
        number(dx2);
        number(dy2);
        number(dx3);
        number(dy3);
        rrcurveto();
    }

    public void sbw() {
    	commands.add(SBW);
    }
    
    public void sbw(int sbx, int sby, int wx, int wy) {
        number(sbx);
        number(sby);
        number(wx);
        number(wy);
        sbw();
    }

    public void seac() {
    	commands.add(SEAC);
    }
    
    public void seac(int asb, int adx, int ady, int bchar, int achar) {
        number(asb);
        number(adx);
        number(ady);
        number(bchar);
        number(achar);
        seac();
    }

    public void setcurrentpoint() {
    	commands.add(SETCURRENTPOINT);
    }
    
    public void setcurrentpoint(int x, int y) {
        number(x);
        number(y);
        setcurrentpoint();
    }
    
    public void vhcurveto() {
    	commands.add(VHCURVETO);
    }

    public void vhcurveto(int dy1, int dx2, int dy2, int dx3) {
        number(dy1);
        number(dx2);
        number(dy2);
        number(dx3);
        vhcurveto();
    }

    public void vlineto() {
    	commands.add(VLINETO);
    }
    
    public void vlineto(int dy) {
        number(dy);
        vlineto();
    }

    public void vmoveto() {
    	commands.add(VMOVETO);
    }
    
    public void vmoveto(int dy) {
        number(dy);
        vmoveto();
    }

    public void vstem() {
    	commands.add(VSTEM);
    }
    
    public void vstem(int x, int dx) {
        number(x);
        number(dx);
        vstem();
    }
    
    public void vstem3() {
    	commands.add(VSTEM3);
    }

    public void vstem3(int x0, int dx0, int x1, int dx1, int x2, int dx2) {
        number(x0);
        number(dx0);
        number(x1);
        number(dx1);
        number(x2);
        number(dx2);
        vstem3();
    }

    @Override
	public String toString() {
    	StringBuffer sb = new StringBuffer();
    	Iterator<Command> itr = commands.iterator();
    	while (itr.hasNext()) {
    		Command cmd = itr.next();
    		sb.append(cmd.toString());
    		sb.append(' ');
    	}
    	return sb.toString();
    }
}
