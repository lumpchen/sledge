/**
 * 
 */
package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;

/**
 * TTC Header Version 1.0
 * 
 * <pre>
 * Type    Name                    Description
 * -----------------------------------------------------------------------------
 * TAG     TTCTag                  TrueType Collection ID string: 'ttcf'
 * ULONG   Version                 Version of the TTC Header (1.0), 0x00010000
 * ULONG   numFonts                Number of fonts in TTC
 * ULONG   OffsetTable[numFonts]   Array of offsets to the OffsetTable for each 
 *                                 font from the beginning of the file
 * </pre>
 * 
 * TTC Header Version 2.0
 * 
 * <pre>
 * Type    Name                    Description
 * -----------------------------------------------------------------------------
 * TAG     TTCTag                  TrueType Collection ID string: 'ttcf'
 * ULONG   Version                 Version of the TTC Header (2.0), 0x00020000
 * ULONG   numFonts                Number of fonts in TTC
 * ULONG   OffsetTable[numFonts]   Array of offsets to the OffsetTable for each 
 *                                 font from the beginning of the file
 * ULONG   ulDsigTag               Tag indicating that a DSIG table exists, 
 *                                 0x44534947 ('DSIG') (null if no signature)
 * ULONG   ulDsigLength            The length (in bytes) of the DSIG table (null
 *                                 if no signature)
 * ULONG   ulDsigOffset            The offset (in bytes) of the DSIG table from 
 *                                 the beginning of the TTC file (null if no 
 *                                 signature)
 * </pre>
 * 
 * @author wxin
 */
public final class TTCHeader {
    
    public static final int TAG_VERSION_ONE = 0x00010000;
    public static final int TAG_VERSION_TWO = 0x00020000;
    
    public static final int TAG_TTCF = TTFHelper.composeTag("ttcf");
    public static final int TAG_DSIG = TTFHelper.composeTag("DSIG");
    
    private int version;
    private long[] offsetTable;
    private boolean hasDsig;
    private long ulDsigLength;
    private long ulDsigOffset;
    
    public int getVersion() {
        return version;
    }
    
    public int getNumFonts() {
        return offsetTable.length;
    }
    
    public long getFontOffset(int i) {
        return offsetTable[i];
    }
    
    public boolean hasDsig() {
        return hasDsig;
    }
    
    public long getDsigLength() {
        return ulDsigLength;
    }
    
    public long getDsigOffset() {
        return ulDsigOffset;
    }
    
    public void read(TTFRandomReader rd) throws TTFFormatException, IOException {
        if (rd.readTTFTag() != TTFFile.FILE_TAG_TTC) {
            throw new TTFFormatException("bad ttc header tag", rd.getPosition() - 4);
        }
        version = rd.readTTFTag();
        if (version == TAG_VERSION_ONE) {
            long n = rd.readTTFULong();
            if (n <= 0) {
                throw new TTFFormatException("bad number of fonts in ttc header",
                        rd.getPosition() - 4);
            }
            else if (n >= Integer.MAX_VALUE) {
                throw new TTFFormatException("number of fonts larger than 2G, we can't handle it",
                        rd.getPosition() - 4);
            }
            offsetTable = new long[(int) n];
            for (int i=0; i<n; ++i) {
                offsetTable[i] = rd.readTTFULong();
            }
            hasDsig = false;
            ulDsigLength = 0;
            ulDsigOffset = 0;
        }
        else if (version == TAG_VERSION_TWO) {
            long n = rd.readTTFULong();
            if (n <= 0) {
                throw new TTFFormatException("bad number of fonts in ttc header",
                        rd.getPosition() - 4);
            }
            else if (n >= Integer.MAX_VALUE) {
                throw new TTFFormatException("number of fonts larger than 2G, we can't handle it",
                        rd.getPosition() - 4);
            }
            offsetTable = new long[(int) n];
            for (int i=0; i<n; ++i) {
                offsetTable[i] = rd.readTTFULong();
            }
            int tag = rd.readTTFTag();
            if (tag == TAG_DSIG) {
                hasDsig = true;
            }
            else if (tag == 0) {
                hasDsig = false;
            }
            else {
                throw new TTFFormatException("bad ulDsigTag in ttc header", rd.getPosition() - 4);
            }
            ulDsigLength = rd.readTTFULong();
            ulDsigOffset = rd.readTTFULong();
        }
        else {
            throw new TTFFormatException("bad ttc header version", rd.getPosition() - 4);
        }
    }
}