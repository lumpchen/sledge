package com.docscience.pathfinder.font.driver.otf;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFFormatException;
import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;


public class OTFGPosTable extends OTFGCommonTable {

	public static final int SINGLE_ADJUSTMENT = 1; // Adjust position of a single glyph 
	public static final int PAIR_ADJUSTMENT = 2; // Adjust position of a pair of glyphs 
	public static final int CURSIVE_ATTACHMENT = 3; // Attach cursive glyphs 
	public static final int MARK_TO_BASE_ATTACHMENT = 4; // Attach a combining mark to a base glyph 
	public static final int MARK_TO_LIGATURE_ATTACHMENT = 5; // Attach a combining mark to a ligature 
	public static final int MARK_TO_MARK_ATTACHMENT = 6; // Attach a combining mark to another mark 
	public static final int CONTEXT_POSITIONING = 7; // Position one or more glyphs in context 
	public static final int CHAINED_CONTEXT = 8; // Position one or more glyphs in chained context 
	public static final int EXTENSION_POSITIONING = 9; // Extension mechanism for other positionings 

	public static class PairPosSubstFormat1 extends PairPosSubst {
        OTFCoverageTable coverageTable;
        GlyphPairValues[][] glyphPairValues;

        @Override
        public void read(TTFRandomReader rd) throws IOException,
                TTFFormatException {
            long offset = rd.getPosition() - 2;
            long coverageOffset = rd.readTTFOffset();
            int valueFormat1 = rd.readTTFUShort();
            int valueFormat2 = rd.readTTFUShort();
            int pairSetCount = rd.readTTFUShort();
            
            glyphPairValues = new GlyphPairValues[pairSetCount][];
            
            for (int i = 0; i < pairSetCount; i++) {
                long pairSetOffset = rd.readTTFOffset();
                
                long currentPos = rd.getPosition();
                rd.setPosition(offset + pairSetOffset);
                
                int pairValueCount = rd.readTTFUShort();
                GlyphPairValues[] glyphPairValue = new GlyphPairValues[pairValueCount];
                for (int j = 0; j < pairValueCount; j++) {
                    glyphPairValue[j] = readPairPosValue( rd, offset, valueFormat1, valueFormat2, true); 
                }
                glyphPairValues[i] = glyphPairValue;
                
                rd.setPosition(currentPos);//reset pos
            }
            
            rd.setPosition(offset + coverageOffset);
            this.coverageTable = OTFCoverageTable.readCoverageTable(rd);
        }

        @Override
        public int getKernByGlyphPair(int firstGlyph, int secondGlyph) {
            int coverageIndex = coverageTable.getCoverageIndex(firstGlyph);
            if(coverageIndex >= 0){
                for(GlyphPairValues values : glyphPairValues[coverageIndex]){
                    if(values.getGlyph() == secondGlyph){
                        return values.getValue1().getxAdvance();
                    }
                }
            }
            return Integer.MAX_VALUE;
        }
	}
	
    public static class PairPosSubstFormat2 extends PairPosSubst {
        
        OTFCoverageTable coverageTable;
        OTFClassDefTable classDefTable1;
        OTFClassDefTable classDefTable2;
        GlyphPairValues[][] glyphPairValues;

        @Override
        public void read(TTFRandomReader rd) throws IOException,
                TTFFormatException {
            long offset = rd.getPosition() - 2;
            long coverageOffset = rd.readTTFOffset();
            int valueFormat1 = rd.readTTFUShort();
            int valueFormat2 = rd.readTTFUShort();
            long classDef1Offset = rd.readTTFOffset();
            long classDef2Offset = rd.readTTFOffset();
            int class1Count = rd.readTTFUShort();
            int class2Count = rd.readTTFUShort();
            
            glyphPairValues = new GlyphPairValues[class1Count][class2Count];
            
            for ( int i = 0; i < class1Count; i++ ) {
                for (int j = 0; j < class2Count; j++) {
                    GlyphPairValues glyphPairValue = readPairPosValue(rd, offset, valueFormat1, valueFormat2, false);
                    glyphPairValues[i][j] = glyphPairValue;
                }
            }
            
            rd.setPosition(offset + coverageOffset);
            this.coverageTable = OTFCoverageTable.readCoverageTable(rd);
            
            rd.setPosition(offset + classDef1Offset);
            this.classDefTable1 = OTFClassDefTable.readClassDefTable(rd);
            
            rd.setPosition(offset + classDef2Offset);
            this.classDefTable2 = OTFClassDefTable.readClassDefTable(rd);
            
        }

        @Override
        public int getKernByGlyphPair(int firstGlyph, int secondGlyph) {
            if(coverageTable.getCoverageIndex(firstGlyph) >= 0){
                int firstGlyphClass = classDefTable1.getClassValue(firstGlyph);
                int secondGlyphClass = classDefTable2.getClassValue(secondGlyph);
                if(firstGlyphClass == OTFClassDefTable.INVALID_CLASS){
                	firstGlyphClass = 0;
                }
            	if(secondGlyphClass == OTFClassDefTable.INVALID_CLASS){
            		secondGlyphClass = 0;
            	}
            	return glyphPairValues[firstGlyphClass][secondGlyphClass].getValue1().getxAdvance();
            }
            return Integer.MAX_VALUE;
        }
    }
	
    public static abstract class PairPosSubst extends OTFLookupSubTable {

        public abstract void read(TTFRandomReader rd) throws IOException, TTFFormatException;

        public abstract int getKernByGlyphPair(int firstGlyph, int secondGlyph);
        
        public static PairPosSubst readPairPosSubstTable(TTFRandomReader rd) throws IOException, TTFFormatException {
            int format = rd.readTTFUShort();
            if (format == 1) {
                PairPosSubstFormat1 st = new PairPosSubstFormat1();
                st.read(rd);
                return st;
            } else if (format == 2) {
                PairPosSubstFormat2 st = new PairPosSubstFormat2();
                st.read(rd);
                return st;
            } else {
                throw new TTFFormatException(
                        "unsupported pair pos substitute table format [" + format
                                + "]");
            }
        }

        protected GlyphPairValues readPairPosValue(TTFRandomReader rd,
                long offset, int valueFormat1, int valueFormat2,
                boolean readGlyph) throws IOException {
            int glyph;

            if (readGlyph) {
                glyph = rd.readTTFGlyphID();
            } else {
                glyph = 0;
            }
            // first glyph
            Value v1;
            if (valueFormat1 != 0) {
                v1 = Value.readPosValue(rd, offset, valueFormat1);
            } else {
                v1 = null;
            }
            // second glyph
            Value v2;
            if (valueFormat2 != 0) {
                v2 = Value.readPosValue(rd, offset, valueFormat2);
            } else {
                v2 = null;
            }
            return new GlyphPairValues(glyph, v1, v2);
        }
    }

	private static class LookupSubTableReader implements OTFLookupSubTableReader {

		public static final LookupSubTableReader instance = new LookupSubTableReader();

		@Override
		public OTFLookupSubTable read(int lookupType, TTFRandomReader rd)
				throws IOException, TTFFormatException {
			switch (lookupType) {
			case SINGLE_ADJUSTMENT:
			    return null;
			case PAIR_ADJUSTMENT:
			    return PairPosSubst.readPairPosSubstTable(rd);
			case CURSIVE_ATTACHMENT:
			case MARK_TO_BASE_ATTACHMENT:
			case MARK_TO_LIGATURE_ATTACHMENT:
			case MARK_TO_MARK_ATTACHMENT:
			case CONTEXT_POSITIONING:
			case CHAINED_CONTEXT:
			case EXTENSION_POSITIONING:
				return null;
			default:
				throw new TTFFormatException("unsupported lookup type [" + lookupType + "]");
			}
		}		
		
	}
	
	public static class DeviceTable {
	    private final int startSize;
	    private final int endSize;
	    private final int[] deltas;
	
	    public DeviceTable (int startSize, int endSize, int[] deltas) {
	        this.startSize = startSize;
	        this.endSize = endSize;
	        this.deltas = deltas;
	    }
	
	    public int getStartSize() {
	        return startSize;
	    }
	
	    public int getEndSize() {
	        return endSize;
	    }
	
	    public int[] getDeltas() {
	        return deltas;
	    }
	}

	public static class GlyphPairValues {
	    private final int glyph;
	    private final Value value1;//first glyph
	    private final Value value2;//second glyph
	
	    public GlyphPairValues ( int glyph, Value value1, Value value2 ) {
	        this.glyph = glyph;
	        this.value1 = value1;
	        this.value2 = value2;
	    }
	
	    public int getGlyph() {
	        return glyph;
	    }
	
	    public Value getValue1() {
	        return value1;
	    }
	
	    public Value getValue2() {
	        return value2;
	    }
	}

	public static class Value {
	    public static final int X_PLACEMENT             = 0x0001;
	    public static final int Y_PLACEMENT             = 0x0002;
	    public static final int X_ADVANCE               = 0x0004;
	    public static final int Y_ADVANCE               = 0x0008;
	    public static final int X_PLACEMENT_DEVICE      = 0x0010;
	    public static final int Y_PLACEMENT_DEVICE      = 0x0020;
	    public static final int X_ADVANCE_DEVICE        = 0x0040;
	    public static final int Y_ADVANCE_DEVICE        = 0x0080;
	
	    private int xPlacement;// xPlacement
	    private int yPlacement;// yPlacement
	    private int xAdvance;// xAdvance
	    private int yAdvance;// yAdvance
	    private final DeviceTable xPlacementDevice;// xPlacement Device table
	    private final DeviceTable yPlacementDevice;// yPlacement Device table
	    private final DeviceTable xAdvanceDevice;// xAdvance Device table
	    private final DeviceTable yAdvanceDevice;// xAdvance Device table
	
	    public Value (int xPlacement, int yPlacement, int xAdvance, int yAdvance, DeviceTable xPlacementDevice, DeviceTable yPlacementDevice, DeviceTable xAdvanceDevice, DeviceTable yAdvanceDevice) {
	        this.xPlacement = xPlacement;
	        this.yPlacement = yPlacement;
	        this.xAdvance = xAdvance;
	        this.yAdvance = yAdvance;
	        this.xPlacementDevice = xPlacementDevice;
	        this.yPlacementDevice = yPlacementDevice;
	        this.xAdvanceDevice = xAdvanceDevice;
	        this.yAdvanceDevice = yAdvanceDevice;
	    }
	    
	    public int getxPlacement() {
	        return xPlacement;
	    }
	
	    public int getyPlacement() {
	        return yPlacement;
	    }
	
	    public int getxAdvance() {
	        return xAdvance;
	    }
	
	    public int getyAdvance() {
	        return yAdvance;
	    }
	
	    public DeviceTable getxPlacementDevice() {
	        return xPlacementDevice;
	    }
	
	    public DeviceTable getyPlacementDevice() {
	        return yPlacementDevice;
	    }
	
	    public DeviceTable getxAdvanceDevice() {
	        return xAdvanceDevice;
	    }
	
	    public DeviceTable getyAdvanceDevice() {
	        return yAdvanceDevice;
	    }
	
	    public static Value readPosValue(TTFRandomReader rd, long offset, int valueFormat) throws IOException {
	        int xPlacement = 0;
	        int yPlacement = 0;
	        int xAdvance = 0;
	        int yAdvance = 0;
	        DeviceTable xPlacementDevice = null;
	        DeviceTable yPlacementDevice = null;
	        DeviceTable xAdvanceDevice = null;
	        DeviceTable yAdvanceDevice = null;
	        
	        if ((valueFormat & Value.X_PLACEMENT) != 0) {
	            xPlacement = rd.readTTFShort();
	        }
	        if ((valueFormat & Value.Y_PLACEMENT) != 0) {
	            yPlacement = rd.readTTFShort();
	        }
	        if ((valueFormat & Value.X_ADVANCE) != 0) {
	            xAdvance = rd.readTTFShort();
	        }
	        if ((valueFormat & Value.Y_ADVANCE) != 0) {
	            yAdvance = rd.readTTFShort();
	        }
	        if ((valueFormat & Value.X_PLACEMENT_DEVICE) != 0) {
	            @SuppressWarnings("unused")
				int xPlacementDeviceOffset = rd.readTTFUShort();
	        }
	        if ((valueFormat & Value.Y_PLACEMENT_DEVICE) != 0) {
	            @SuppressWarnings("unused")
				int yPlacementDeviceOffset = rd.readTTFUShort();
	        }
	        if ((valueFormat & Value.X_ADVANCE_DEVICE) != 0) {
	            @SuppressWarnings("unused")
				int xAdvanceDeviceOffset = rd.readTTFUShort();
	        }
	        if ((valueFormat & Value.Y_ADVANCE_DEVICE) != 0) {
	            @SuppressWarnings("unused")
				int yAdvanceDeviceOffset = rd.readTTFUShort();
	        }
	        return new Value(xPlacement, yPlacement, xAdvance, yAdvance, xPlacementDevice, yPlacementDevice, xAdvanceDevice, yAdvanceDevice);
	    }
	    
	}

	@Override
	public int getTag() {
		return TAG_GPOS_TABLE;
	}

	@Override
	protected OTFLookupSubTableReader getLookupSubTableReader() {
		return LookupSubTableReader.instance;
	}
}
