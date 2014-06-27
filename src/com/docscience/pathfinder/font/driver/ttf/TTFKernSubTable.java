package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;

public class TTFKernSubTable {
    
    // If table has horizontal data, or vertical data.
    public static final int HORIZONTAL_MASK = 1 << 0; 
    
    // If the table has minimum values, or kerning values.
    public static final int MINIMUM_VALUE_MASK = 1 << 1;  
    
    // If the kerning is perpendicular to the flow of the text. 
    public static final int CROSS_STREAM_MASK = 1 << 2; 

    // If the value in this table should replace the value currently being accumulated.
    public static final int OVERRIDE_MASK = 1 << 3;
    
    // Reserved. This should be set to zero.
    public static final int RESERVED_MASK = 0x000000f0;

    // Format of the subtable. Only formats 0 and 2 have been defined.
    public static final int FORMAT_MASK = 0x0000ff00;

    public static final int FORMAT_SHIFT = 8;

    public static class KerningPair {
    	public final int leftGlyphID;
    	public final int rightGlyphID;
    	public final int value;

    	public KerningPair(int leftGlyphID, int rightGlyphID, int value) {
    		this.leftGlyphID = leftGlyphID;
    		this.rightGlyphID = rightGlyphID;
    		this.value = value;
		}
    }
    
    private int version;
    private int length;
    private int coverage;
    private KerningPair[] kerningPairs;

	private int searchRange;
	private int entrySelector;
	private int rangeShift;
    
    public final int getVersion() {
        return version;
    }
    
    public final int getCoverage() {
        return coverage;
    }
      
    public final boolean isHorizontal() {
        return (coverage & HORIZONTAL_MASK) != 0;
    }
    
    public final boolean isVertical() {
        return !isHorizontal();
    }
    
    public final boolean isMinimumValue() {
        return (coverage & MINIMUM_VALUE_MASK) != 0;
    }
    
    public final boolean isKerningValue() {
        return !isMinimumValue();
    }
    
    public final boolean isCrossStream() {
        return (coverage & CROSS_STREAM_MASK) != 0;
    }

    public final int getFormat() {
        return (coverage & FORMAT_MASK) >> FORMAT_SHIFT;
    }
    
    public final void read(TTFRandomReader reader) 
            throws TTFFormatException, IOException {
        this.version  = reader.readTTFUShort();
        this.length   = reader.readTTFUShort();
        this.coverage = reader.readTTFUShort();
        if ((coverage & RESERVED_MASK) != 0) {
            throw new TTFFormatException("bad kerning subtable coverage value, reserved bits not zero", reader.getPosition() - 2);
        }
        if (getFormat() == 0) {
            readFormat0(reader);
        }
        else if (getFormat() == 2) {
            readFormat2(reader);
        }
        else {
            /*
             * XPR-65364
             * Accoding to TTF spec, Only formats 0 and 2 have been defined. 
             * Formats 1 and 3  through 255 are reserved for future use.
             * The font in this AR has a bad format value but a good format 0 data.
             * So try read as format 0 and 2, otherwise threw exception
             */
            boolean isReadable = false;
            if (!isReadable) {
                try {
                    readFormat0(reader);
                    isReadable = true;
                } catch (Exception e) {
                    // do nothing
                }
            }

            if (!isReadable) {
                try {
                    readFormat2(reader);
                    isReadable = true;
                } catch (Exception e) {
                    // do nothing
                }
            }
            
            if(!isReadable)
            throw new TTFFormatException("unknown kerning subtable format " + getFormat(), reader.getPosition() - 2);
        }
    }
    
    private void readFormat0(TTFRandomReader reader) 
            throws TTFFormatException, IOException {
        int numPairs = reader.readTTFUShort();
        this.searchRange = reader.readTTFUShort();
        this.entrySelector = reader.readTTFUShort();
        this.rangeShift = reader.readTTFUShort();
        
        kerningPairs = new KerningPair[numPairs];
        for (int i=0; i<numPairs; ++i) {
            int leftGlyphID = reader.readTTFUShort();
            int rightGlyphID = reader.readTTFUShort();
            int value = reader.readTTFFWord();
            kerningPairs[i] = new KerningPair(leftGlyphID, rightGlyphID, value);
        }
    }
    
    private void readFormat2(TTFRandomReader reader) 
            throws TTFFormatException, IOException {
        // TODO Auto-generated method stub
        throw new TTFFormatException("kerning subtable format 2 haven't implement yet", reader.getPosition());
    }
    
    public int getNumKerningPairs() {
        return kerningPairs.length;
    }
    
    public KerningPair getKerningPair(int index) {
        return kerningPairs[index];
    }
    
	public long getLength() {
		return length;
	}

	public int getSearchRange() {
		return searchRange;
	}

	public int getEntrySelector() {
		return entrySelector;
	}

	public int getRangeShift() {
		return rangeShift;
	}

}
