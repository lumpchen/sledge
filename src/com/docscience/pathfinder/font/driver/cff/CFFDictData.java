package com.docscience.pathfinder.font.driver.cff;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author wxin
 * 
 */
public final class CFFDictData {

    /**
     * Maping the Operand conception in Adobe CFF Spec. We find the name
     * operator and operand make some confusing, so we use key and value
     * instead.
     * 
     * @author wxin
     * 
     */
    public static final class Value {

        public static final int TYPE_NUMBER = 0;

        public static final int TYPE_BOOLEAN = 1;

        public static final int TYPE_SID = 2;

        public static final int TYPE_ARRAY = 3;

        public static final int TYPE_DELTA = 4;

        private int type;

        private Object value;

        public static Value createNumber(double d) {
            Value op = new Value();
            op.type = TYPE_NUMBER;
            op.value = new Double(d);
            return op;
        }

        public static Value createBoolean(boolean b) {
            Value op = new Value();
            op.type = TYPE_BOOLEAN;
            op.value = Boolean.valueOf(b);
            return op;
        }

        public static Value createSID(int i) {
            Value op = new Value();
            op.type = TYPE_SID;
            op.value = new Integer(i);
            return op;
        }

        public static Value createArray(double[] src) {
            double[] dest = new double[src.length];
            System.arraycopy(src, 0, dest, 0, src.length);

            Value op = new Value();
            op.type = TYPE_ARRAY;
            op.value = dest;
            return op;
        }

        public static Value createDelta(double[] src) {
            double[] dest = new double[src.length];
            dest[0] = src[0];
            for (int i = 1; i < src.length; ++i) {
                dest[i] = src[i] + dest[i - 1];
            }

            Value op = new Value();
            op.type = TYPE_DELTA;
            op.value = dest;
            return op;
        }

        private Value() {
            // do nothing here.
        }

        public int getType() {
            return type;
        }
        
        public boolean isNumber() {
            return type == TYPE_NUMBER;
        }
        
        public boolean isBoolean() {
            return type == TYPE_BOOLEAN;
        }
        
        public boolean isArray() {
            return type == TYPE_ARRAY;
        }
        
        public boolean isDelta() {
            return type == TYPE_DELTA;
        }
        
        public boolean isSID() {
            return type == TYPE_SID;
        }

        public boolean booleanValue() {
            assert (type == TYPE_BOOLEAN);
            return ((Boolean) value).booleanValue();
        }

        public int intValue() {
            switch (type) {
            case TYPE_NUMBER:
                return ((Double) value).intValue();
            case TYPE_BOOLEAN:
                return ((Boolean) value).booleanValue() ? 1 : 0;
            case TYPE_SID:
                return ((Integer) value).intValue();
            default:
                assert (false) : "never goes here";
                return 0;
            }
        }

        public double doubleValue() {
            assert (type == TYPE_NUMBER);
            return ((Double) value).doubleValue();
        }

        public double[] arrayValue() {
            assert (type == TYPE_ARRAY || type == TYPE_DELTA);
            return (double[]) value;
        }

        @Override
		public String toString() {
            StringBuffer sb = new StringBuffer();
            switch(type) {
            case TYPE_BOOLEAN:
                sb.append("boolean");
                break;
            case TYPE_NUMBER:
                sb.append("number");
                break;
            case TYPE_SID:
                sb.append("SID");
                break;
            case TYPE_ARRAY:
                sb.append("array");
                break;
            case TYPE_DELTA:
                sb.append("delta");
                break;
            default:
                assert(false) : "never goes here";
            }
            sb.append(' ');
            
            if (type == TYPE_ARRAY || type == TYPE_DELTA) {
                double[] array = (double[]) value;
                sb.append('{');
                for (int i=0; i<array.length; ++i) {
                    sb.append(array[i]);
                    if (i != array.length - 1) {
                        sb.append(", ");
                    }
                }
                sb.append('}');
            }
            else {
                sb.append(value.toString());
            }
            return sb.toString();
        }

    }

    /**
     * Maping the Operator conception in Adobe CFF Spec. We find the name
     * operator and operand make some confusing, so we use key and value
     * instead.
     * 
     * @author wxin
     * 
     */
    public static final class Key {

        public static final Key KEY_VERSION = new Key("version",
                Value.TYPE_SID, null);

        public static final Key KEY_NOTICE = new Key("Notice", Value.TYPE_SID,
                null);

        public static final Key KEY_FULLNAME = new Key("FullName",
                Value.TYPE_SID, null);

        public static final Key KEY_FAMILYNAME = new Key("FamilyName",
                Value.TYPE_SID, null);

        public static final Key KEY_WEIGHT = new Key("Weight", Value.TYPE_SID,
                null);

        public static final Key KEY_FONTBBOX = new Key("FontBBox",
                Value.TYPE_ARRAY, Value
                        .createArray(new double[] { 0, 0, 0, 0 }));

        public static final Key KEY_BLUEVALUES = new Key("BlueValues",
                Value.TYPE_DELTA, null);

        public static final Key KEY_OTHERBLUES = new Key("OtherBlues",
                Value.TYPE_DELTA, null);

        public static final Key KEY_FAMILYBLUES = new Key("FamilyBlues",
                Value.TYPE_DELTA, null);

        public static final Key KEY_FAMILYOTHERBLUES = new Key(
                "FamilyOtherBlues", Value.TYPE_DELTA, null);

        public static final Key KEY_STDHW = new Key("StdHW", Value.TYPE_NUMBER,
                null);

        public static final Key KEY_STDVW = new Key("StdVW", Value.TYPE_NUMBER,
                null);

        public static final Key KEY_UNIQUEID = new Key("UniqueID",
                Value.TYPE_NUMBER, null);

        public static final Key KEY_XUID = new Key("XUID", Value.TYPE_ARRAY,
                null);

        public static final Key KEY_CHARSET = new Key("charset",
                Value.TYPE_NUMBER, Value.createNumber(0));

        public static final Key KEY_ENCODING = new Key("Encoding",
                Value.TYPE_NUMBER, Value.createNumber(0));

        public static final Key KEY_CHARSTRINGS = new Key("CharStrings",
                Value.TYPE_NUMBER, null);

        public static final Key KEY_PRIVATE = new Key("Private",
                Value.TYPE_ARRAY, null);

        public static final Key KEY_SUBRS = new Key("Subrs", Value.TYPE_NUMBER,
                null);

        public static final Key KEY_DEFAULTWIDTHX = new Key("defaultWidthX",
                Value.TYPE_NUMBER, Value.createNumber(0));

        public static final Key KEY_NOMINALWIDTHX = new Key("nominalWidthX",
                Value.TYPE_NUMBER, Value.createNumber(0));

        public static final Key KEY_COPYRIGHT = new Key("Copyright",
                Value.TYPE_SID, null);

        public static final Key KEY_ISFIXEDPITCH = new Key("isFixedPitch",
                Value.TYPE_BOOLEAN, Value.createBoolean(false));

        public static final Key KEY_ITALICANGLE = new Key("italicAngle",
                Value.TYPE_NUMBER, Value.createNumber(0));

        public static final Key KEY_UNDERLINEPOSITION = new Key(
                "UnderlinePosition", Value.TYPE_NUMBER, Value
                        .createNumber(-100));

        public static final Key KEY_UNDERLINETHICKNESS = new Key(
                "UnderlineThickness", Value.TYPE_NUMBER, Value.createNumber(50));

        public static final Key KEY_PAINTTYPE = new Key("PaintType",
                Value.TYPE_NUMBER, Value.createNumber(0));

        public static final Key KEY_CHARSTRINGTYPE = new Key("CharstringType",
                Value.TYPE_NUMBER, Value.createNumber(2));

        public static final Key KEY_FONTMATRIX = new Key("FontMatrix",
                Value.TYPE_ARRAY, Value.createArray(new double[] { 0.001, 0, 0,
                        0.001, 0, 0 }));

        public static final Key KEY_STROKEWIDTH = new Key("StrokeWidth",
                Value.TYPE_NUMBER, Value.createNumber(0));

        public static final Key KEY_BLUESCALE = new Key("BlueScale",
                Value.TYPE_NUMBER, Value.createNumber(0.039625));

        public static final Key KEY_BLUESHIFT = new Key("BlueShift",
                Value.TYPE_NUMBER, Value.createNumber(7));

        public static final Key KEY_BLUEFUZZ = new Key("BlueFuzz",
                Value.TYPE_NUMBER, Value.createNumber(1));

        public static final Key KEY_STEMSNAPH = new Key("StemSnapH",
                Value.TYPE_DELTA, null);

        public static final Key KEY_STEMSNAPV = new Key("StemSnapV",
                Value.TYPE_DELTA, null);

        public static final Key KEY_FORCEBOLD = new Key("ForceBold",
                Value.TYPE_BOOLEAN, Value.createBoolean(false));

        public static final Key KEY_LANGUAGEGROUP = new Key("LanguageGroup",
                Value.TYPE_NUMBER, Value.createNumber(0));

        public static final Key KEY_EXPANSIONFACTOR = new Key(
                "ExpansionFactor", Value.TYPE_NUMBER, Value.createNumber(0.06));

        public static final Key KEY_INITIALRANDOMSEED = new Key(
                "initalRandomSeed", Value.TYPE_NUMBER, Value.createNumber(0));

        public static final Key KEY_SYNTHETICBASE = new Key("SyntheticBase",
                Value.TYPE_NUMBER, null);

        public static final Key KEY_POSTSCRIPT = new Key("PostScript",
                Value.TYPE_SID, null);

        public static final Key KEY_BASEFONTNAME = new Key("BaseFontName",
                Value.TYPE_SID, null);

        public static final Key KEY_BASEFONTBLEND = new Key("BaseFontBlend",
                Value.TYPE_DELTA, null);

        public static final Key KEY_ROS = new Key("ROS", Value.TYPE_ARRAY, null);

        public static final Key KEY_CIDFONTVERSION = new Key("CIDFontVersion",
                Value.TYPE_NUMBER, Value.createNumber(0));

        public static final Key KEY_CIDFONTREVISION = new Key(
                "CIDFontRevision", Value.TYPE_NUMBER, Value.createNumber(0));

        public static final Key KEY_CIDFONTTYPE = new Key("CIDFontType",
                Value.TYPE_NUMBER, Value.createNumber(0));

        public static final Key KEY_CIDCOUNT = new Key("CIDCount",
                Value.TYPE_NUMBER, Value.createNumber(8720));

        public static final Key KEY_UIDBASE = new Key("UIDBase",
                Value.TYPE_NUMBER, null);

        public static final Key KEY_FDARRAY = new Key("FDArray",
                Value.TYPE_NUMBER, null);

        public static final Key KEY_FDSELECT = new Key("FDSelect",
                Value.TYPE_NUMBER, null);

        public static final Key KEY_FONTNAME = new Key("FontName",
                Value.TYPE_SID, null);

        private final String name;

        private final int attachedType;

        private final Value defaultValue;

        private Key(String name, int attachedType, Value defaultValue) {
            this.name = name;
            this.attachedType = attachedType;
            this.defaultValue = defaultValue;
        }

        public String getName() {
            return name;
        }

        public Value getDefaultValue() {
            return defaultValue;
        }

        public int getAttachedType() {
            return attachedType;
        }
        
        public boolean hasDefaultValue() {
            return defaultValue != null;
        }

        @Override
		public String toString() {
            return name;
        }
    }

    private static final String[] NIBBLE_STRING = new String[] { "0", "1", "2",
            "3", "4", "5", "6", "7", "8", "9", ".", "E", "E-", null, "-", null, };

    private HashMap<Key, Value> map = new HashMap<Key, Value>();

    /**
     * Return a Map which key type is CFFDictData.Key, and value type is
     * CFFDictData.Value
     * 
     * @return Map
     */
	public Map<Key, Value> getMap() {
        return Collections.unmodifiableMap(map);
    }

    public void read(long offset, long length, CFFRandomReader rd)
            throws IOException, CFFFormatException {
        final long limit = offset + length;
        Vector<Object> operands = new Vector<Object>();

        rd.setPosition(offset);
        while (rd.getPosition() < limit) {
            Object data = readData(rd);
            if (data.getClass() == Key.class) {
                put((Key) data, operands);
                operands.clear();
            } else if (data.getClass() == Double.class) {
                operands.add(data);
            } else {
                assert (false) : "never goes here";
            }
        }

    }
    
    public boolean contains(Key key) {
        return map.containsKey(key);
    }

    public Value get(Key key) {
        return map.get(key);
    }

    private void put(Key key, Vector<Object> values) throws CFFFormatException {
        if (values.isEmpty()) {
            map.put(key, key.getDefaultValue());
            return;
        }

        Value value = null;
        switch (key.getAttachedType()) {
        case Value.TYPE_NUMBER:
            if (values.size() != 1) {
                throw new CFFFormatException(
                        "invalid dict data, should be number");
            }
            value = Value.createNumber(((Double) values.get(0)).doubleValue());
            break;
        case Value.TYPE_BOOLEAN:
            if (values.size() != 1) {
                throw new CFFFormatException(
                        "invalid dict data, should be boolean");
            }
            value = Value
                    .createBoolean(((Double) values.get(0)).doubleValue() != 0);
            break;
        case Value.TYPE_SID:
            if (values.size() != 1) {
                throw new CFFFormatException("invalid dict data, should be SID");
            }
            value = Value.createSID(((Double) values.get(0)).intValue());
            break;
        case Value.TYPE_ARRAY: {
            double[] arr = new double[values.size()];
            for (int i = 0; i < values.size(); ++i) {
                arr[i] = ((Double) values.get(i)).doubleValue();
            }
            value = Value.createArray(arr);
        }
            break;
        case Value.TYPE_DELTA: {
            double[] arr = new double[values.size()];
            for (int i = 0; i < values.size(); ++i) {
                arr[i] = ((Double) values.get(i)).doubleValue();
            }
            value = Value.createDelta(arr);
        }
            break;
        default:
            assert (false) : "never goes here";
        }
        map.put(key, value);
    }

    private Object readData(CFFRandomReader rd) throws IOException,
            CFFFormatException {
        int c = rd.readCFFCard8();
        assert (c >= 0 && c < 256);
        if (c < 22) {
            return readKey(c, rd);
        } else if (c == 28) {
            // read 3 bytes integer
            int b1 = rd.readCFFCard8();
            int b2 = rd.readCFFCard8();
            return new Double((short) (b1 << 8) | b2);
        } else if (c == 29) {
            // read 5 bytes integer
            int b1 = rd.readCFFCard8();
            int b2 = rd.readCFFCard8();
            int b3 = rd.readCFFCard8();
            int b4 = rd.readCFFCard8();
            return new Double((int) ((b1 << 24) | (b2 << 16) | (b3 << 8) | b4));
        } else if (c == 30) {
            // return real number
            StringBuffer sb = new StringBuffer();
            int nibble = 0;
            while (true) {
                nibble = rd.readCFFCard8();
                int n1 = (nibble >> 4) & 0x0f;
                if (n1 == 0x0f) {
                    break;
                } else if (n1 == 0x0d) {
                    throw new CFFFormatException("invalid real number", rd
                            .getPosition() - 1);
                } else {
                    sb.append(NIBBLE_STRING[n1]);
                }
                int n2 = nibble & 0x0f;
                if (n2 == 0x0f) {
                    break;
                } else if (n2 == 0x0d) {
                    throw new CFFFormatException("invalid real number", rd
                            .getPosition() - 1);
                } else {
                    sb.append(NIBBLE_STRING[n2]);
                }
            }
            return Double.valueOf(sb.toString());
        } else if (c >= 32 && c <= 246) {
            return new Double(c - 139);
        } else if (c >= 247 && c <= 250) {
            int b1 = rd.readCFFCard8();
            return new Double((c - 247) * 256 + b1 + 108);
        } else if (c >= 251 && c <= 254) {
            int b1 = rd.readCFFCard8();
            return new Double(-(c - 251) * 256 - b1 - 108);
        } else {
            throw new CFFFormatException("unknown dict operator",
                    rd.getPosition() - 1);
        }
    }

    private Key readKey(int c, CFFRandomReader rd) throws IOException,
            CFFFormatException {
        switch (c) {
        case 0:
            return Key.KEY_VERSION;
        case 1:
            return Key.KEY_NOTICE;
        case 2:
            return Key.KEY_FULLNAME;
        case 3:
            return Key.KEY_FAMILYNAME;
        case 4:
            return Key.KEY_WEIGHT;
        case 5:
            return Key.KEY_FONTBBOX;
        case 6:
            return Key.KEY_BLUEVALUES;
        case 7:
            return Key.KEY_OTHERBLUES;
        case 8:
            return Key.KEY_FAMILYBLUES;
        case 9:
            return Key.KEY_FAMILYOTHERBLUES;
        case 10:
            return Key.KEY_STDHW;
        case 11:
            return Key.KEY_STDVW;
        case 12:
            return read2BytesKey(rd);
        case 13:
            return Key.KEY_UNIQUEID;
        case 14:
            return Key.KEY_XUID;
        case 15:
            return Key.KEY_CHARSET;
        case 16:
            return Key.KEY_ENCODING;
        case 17:
            return Key.KEY_CHARSTRINGS;
        case 18:
            return Key.KEY_PRIVATE;
        case 19:
            return Key.KEY_SUBRS;
        case 20:
            return Key.KEY_DEFAULTWIDTHX;
        case 21:
            return Key.KEY_NOMINALWIDTHX;
        default:
            assert (false) : "never goes here";
        }
        return null;
    }

    private Key read2BytesKey(CFFRandomReader rd) throws IOException,
            CFFFormatException {
        int c = rd.readCFFCard8();
        assert (c >= 0 && c < 256);
        switch (c) {
        case 0:
            return Key.KEY_COPYRIGHT;
        case 1:
            return Key.KEY_ISFIXEDPITCH;
        case 2:
            return Key.KEY_ITALICANGLE;
        case 3:
            return Key.KEY_UNDERLINEPOSITION;
        case 4:
            return Key.KEY_UNDERLINETHICKNESS;
        case 5:
            return Key.KEY_PAINTTYPE;
        case 6:
            return Key.KEY_CHARSTRINGTYPE;
        case 7:
            return Key.KEY_FONTMATRIX;
        case 8:
            return Key.KEY_STROKEWIDTH;
        case 9:
            return Key.KEY_BLUESCALE;
        case 10:
            return Key.KEY_BLUESHIFT;
        case 11:
            return Key.KEY_BLUEFUZZ;
        case 12:
            return Key.KEY_STEMSNAPH;
        case 13:
            return Key.KEY_STEMSNAPV;
        case 14:
            return Key.KEY_FORCEBOLD;

            // 15 ~ 16 is -Reserved-

        case 17:
            return Key.KEY_LANGUAGEGROUP;
        case 18:
            return Key.KEY_EXPANSIONFACTOR;
        case 19:
            return Key.KEY_INITIALRANDOMSEED;
        case 20:
            return Key.KEY_SYNTHETICBASE;
        case 21:
            return Key.KEY_POSTSCRIPT;
        case 22:
            return Key.KEY_BASEFONTNAME;
        case 23:
            return Key.KEY_BASEFONTBLEND;

            // 24 ~ 29 is -Reserved-

        case 30:
            return Key.KEY_ROS;
        case 31:
            return Key.KEY_CIDFONTVERSION;
        case 32:
            return Key.KEY_CIDFONTREVISION;
        case 33:
            return Key.KEY_CIDFONTTYPE;
        case 34:
            return Key.KEY_CIDCOUNT;
        case 35:
            return Key.KEY_UIDBASE;
        case 36:
            return Key.KEY_FDARRAY;
        case 37:
            return Key.KEY_FDSELECT;
        case 38:
            return Key.KEY_FONTNAME;
        default:
            throw new CFFFormatException("unknown dict operator", rd
                    .getPosition() - 2);
        }
    }

}
