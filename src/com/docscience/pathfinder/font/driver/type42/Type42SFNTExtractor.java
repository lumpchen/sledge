package com.docscience.pathfinder.font.driver.type42;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.docscience.pathfinder.font.driver.ttf.TTFHelper;
import com.docscience.pathfinder.font.driver.ttf.TTFTable;
import com.docscience.pathfinder.font.driver.ttf.TTFTableDirectory;
import com.docscience.pathfinder.font.driver.ttf.TTFWrapper;

/**
 * extract a type42 sfnt data from ttf file.
 * 
 * @author wxin
 *
 */
public class Type42SFNTExtractor {
	
	public static final int MAX_BYTES_IN_STRING = 65535;
	
    private static class OutputDirectory {
        int tag;
        int length;
        byte[] data;
    }
    
	private TTFWrapper ttfWrapper;
	private boolean includeGlyphs;
	private int maxBytesInString = MAX_BYTES_IN_STRING;
	
	public Type42SFNTExtractor(TTFWrapper ttfWrapper) {
		this.ttfWrapper = ttfWrapper;
	}
	
	public void setMaxArrayLength(int maxArrayLength) {
		this.maxBytesInString = maxArrayLength;
	}
	
	public void setIncludeGlyphs(boolean b) {
		if (b == true) {
			throw new IllegalArgumentException("include glyphs not implemented yet");
		}
		includeGlyphs = b;
	}
	
	public boolean isIncludeGlyphs() {
		return includeGlyphs;
	}
	
	/**
	 * Generate sfnts bytes array defined by Type42 Specification. The bytes array contains the tail byte zero.
	 * 
	 * @return
	 * @throws IOException
	 */
	public byte[][] getSFNTBytesArray() throws IOException {
        List<OutputDirectory> outDirs = new ArrayList<OutputDirectory>(10);
        
        TTFTableDirectory headDir = ttfWrapper.getTableDirectory(TTFTable.TAG_HEAD_TABLE);
        TTFTableDirectory hheaDir = ttfWrapper.getTableDirectory(TTFTable.TAG_HHEA_TABLE);
        TTFTableDirectory hmtxDir = ttfWrapper.getTableDirectory(TTFTable.TAG_HMTX_TABLE);
        TTFTableDirectory fpgmDir = ttfWrapper.getTableDirectory(TTFTable.TAG_FPGM_TABLE);
        TTFTableDirectory cvt_Dir = ttfWrapper.getTableDirectory(TTFTable.TAG_CVT__TABLE);
        TTFTableDirectory maxpDir = ttfWrapper.getTableDirectory(TTFTable.TAG_MAXP_TABLE);
        TTFTableDirectory prepDir = ttfWrapper.getTableDirectory(TTFTable.TAG_PREP_TABLE);
        
		if (headDir != null) {
            outDirs.add(generateOutputDirectory(headDir));
        }
		if (hheaDir != null) {
            outDirs.add(generateOutputDirectory(hheaDir));
        }
        if (hmtxDir != null) {
            outDirs.add(generateOutputDirectory(hmtxDir));
        }
        if (fpgmDir != null) {
            outDirs.add(generateOutputDirectory(fpgmDir));
        }
        if (cvt_Dir != null) {
            outDirs.add(generateOutputDirectory(cvt_Dir));
        }
        if (maxpDir != null) {
            outDirs.add(generateOutputDirectory(maxpDir));
        }
        if (prepDir != null) {
            outDirs.add(generateOutputDirectory(prepDir));
        }
        
        List<byte[]> sfntBytesArray = new ArrayList<byte[]>(10);

        // --- generate head ---
        int numTable = outDirs.size();
        byte[] head = new byte[12 + 16 * (numTable + 1)];
        
        int pos = 0;
        pos = putInt32(0x00010000, head, pos);
        pos = putInt16(numTable+1, head, pos); // number table
        pos = putInt16(maxPowerOf2Mul(numTable+1), head, pos);
        pos = putInt16(maxPowerOf2Log(numTable+1), head, pos);
        pos = putInt16((numTable+1) * 16 - maxPowerOf2Mul(numTable+1), head, pos);
        assert(pos == 12);
        
        int offset = head.length;
        for (int i=0; i<numTable; i++) {
        	OutputDirectory outDir = outDirs.get(i);
            int length = outDir.length;
            int checks = calcCheckSum(offset, length, outDir.data);
            pos = putInt32(outDir.tag, head, pos);
            pos = putInt32(checks, head, pos);
            pos = putInt32(offset, head, pos);
            pos = putInt32(length, head, pos);
            offset += outDir.data.length;
        }
        assert(pos == (numTable * 16) + 12);
        
        pos = putInt32(TTFHelper.composeTag("gdir"), head, pos);
        pos = putInt32(calcCheckSum(0, 0, head), head, pos);
        pos = putInt32(0, head, pos);
        pos = putInt32(0, head, pos);
        assert(pos == head.length);
        
        // add last byte for sfnts
        byte[] head2 = new byte[head.length + 1];
        System.arraycopy(head, 0, head2, 0, head.length);
        head2[head2.length-1] = 0;

        sfntBytesArray.add(head);
        
        // --- generate body ---
        for (int i=0; i<numTable; i++) {
        	OutputDirectory outDir = outDirs.get(i);
        	int len = outDir.data.length;
        	int off = 0;
        	do {
        		byte[] temp = new byte[Math.min(maxBytesInString, len + 1)];
        		System.arraycopy(outDir.data, off, temp, 0, temp.length - 1);
        		temp[temp.length - 1] = 0;
        		len -= (temp.length - 1);
        		off += (temp.length - 1);
        		sfntBytesArray.add(temp);
        	} while (len > 0);
        }

        return sfntBytesArray.toArray(new byte[sfntBytesArray.size()][]);
	}

    private int bytesToInt(byte[] b, int pos) {
        return (b[pos] << 24)
                + (b[pos + 1] << 16)
                + (b[pos + 2] << 8)
                + (b[pos + 3]);
    }
    
    private int calcCheckSum(int offset, int length, byte[] data) {
        int sum = 0;
        int endpos = offset+((length+3) & ~3) / 4;

        int n = 0;
        while (offset < endpos) {
            sum += bytesToInt(data, n * 4);
            n++;
            offset += 4;
        }
        return sum;
    }

    private int maxPowerOf2Log(int i) {
        int p = 0;
        while ((2 << p) <= i) {
            p++;
        }
        return p-1;
    }

    private int maxPowerOf2Mul(int i) {
        int p = 0;
        while ((2 << p) <= i) {
            p++;
        }
        return (2 << (p - 1)) * 16;
    }

    private int putInt32(int value, byte[] buffer, int offset) {
        buffer[offset]   = (byte) ((value >> 24) & 0xff);
        buffer[offset+1] = (byte) ((value >> 16) & 0xff);
        buffer[offset+2] = (byte) ((value >> 8) & 0xff);
        buffer[offset+3] = (byte) (value & 0xff);
        return offset + 4;
    }
    
    private int putInt16(int value, byte[] buffer, int offset) {
        buffer[offset]   = (byte) ((value >> 8) & 0xff);
        buffer[offset+1] = (byte) (value & 0xff);
        return offset + 2;
    }

    private OutputDirectory generateOutputDirectory(TTFTableDirectory tableDir) throws IOException {
        OutputDirectory outDir = new OutputDirectory();
        outDir.tag  = tableDir.getTag();
        outDir.length = (int) tableDir.getLength();
        byte[] data = new byte[(int) ((tableDir.getLength() + 3) / 4 * 4)];
        outDir.data = ttfWrapper.getTableRawData(tableDir.getTag(), data, 0);
        return outDir;
    }
}
