package com.docscience.pathfinder.font.driver.ps;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * @author wxin
 *
 */
public class PSWriter {
    
    public static final int LITERAL_STRING = 0;
    public static final int HEX_STRING = 1;
    public static final int ASCII85_STRING = 2;
    
    private static final String DEFAULT_ENCODING = "ASCII";
    private static final String HEX_NUMBER_CHARS = "0123456789ABCDEF";
    private static final char[] LITERAL_ENCODING = new char[256];
    
    private static final DecimalFormat realFormatter = new DecimalFormat("#########.#######", new DecimalFormatSymbols(Locale.US));
        
    static {
        for (int i=0; i<32; ++i) {
            LITERAL_ENCODING[i] = (char) ((4 << 8) + i);
        }
        for (int i=32; i<128; ++i) {
            LITERAL_ENCODING[i] = (char) ((1 << 8) + i);
        }
        for (int i=128; i<256; ++i) {
            LITERAL_ENCODING[i] = (char) ((4 << 8) + i);
        }
        LITERAL_ENCODING['\n'] = (char) ((2 << 8) + 'n');
        LITERAL_ENCODING['\r'] = (char) ((2 << 8) + 'r');
        LITERAL_ENCODING['\t'] = (char) ((2 << 8) + 't');
        LITERAL_ENCODING['\b'] = (char) ((2 << 8) + 'b');
        LITERAL_ENCODING['\f'] = (char) ((2 << 8) + 'f');
        LITERAL_ENCODING['\\'] = (char) ((2 << 8) + '\\');
        LITERAL_ENCODING['(']  = (char) ((2 << 8) + '(');
        LITERAL_ENCODING[')']  = (char) ((2 << 8) + ')');
    }

    private OutputStream ostream;
    private int charsInline;
    private int maxCharsInline = 80; // max characters in line, not include line terminate character.
    private boolean removeUnnecessarySpace = true;
    private boolean requireSpaceCharacter = false;
	private boolean inString = false;

    public PSWriter(OutputStream ostream) {
        this.ostream = ostream;
        this.charsInline = 0;
    }
    
    public void setMaxCharsInline(int n) {
        assert(n > 10);
        maxCharsInline = n;
    }
    
    public void setRemoveUnnecessarySpace(boolean b) {
        removeUnnecessarySpace = b;
    }
    
    public int getCharsInline() {
        return charsInline;
    }
    
    public boolean isLineEmpty() {
        return charsInline == 0;
    }
    
    public void bool(boolean b) throws IOException {
        if (b) {
            executeName("true");
        }
        else {
            executeName("false");
        }
    }
       
    public void integer(int a) throws IOException {
        byte[] temp = Integer.toString(a).getBytes(DEFAULT_ENCODING);
        doSpaceInCaseOfNeed(temp.length);
        write(temp);
        charsInline += temp.length;
        requireSpaceCharacter = true;
    }
    
    public void real(double d) throws IOException {
        byte[] temp = realFormatter.format(d).getBytes(DEFAULT_ENCODING);
        doSpaceInCaseOfNeed(temp.length);
        write(temp);
        charsInline += temp.length;
        requireSpaceCharacter = true;
    }
    
    public void radix(int radix, int value) throws IOException {
    	assert(value < 0) : "radix number must be positive";
        byte[] radixBytes = Integer.toString(radix).getBytes(DEFAULT_ENCODING);
        byte[] valueBytes = Integer.toString(value, radix).getBytes(DEFAULT_ENCODING);
        int length = radixBytes.length + 1 + valueBytes.length;
        doSpaceInCaseOfNeed(length);
        write(radixBytes);
        write('#');
        write(valueBytes);
        charsInline += length;
        requireSpaceCharacter = true;
    }
    
    public void dec(int h) throws IOException {
        integer(h);
    }

    public void hex(int h) throws IOException {
        radix(16, h);
    }
    
    public void oct(int h) throws IOException {
        radix(8, h);
    }
    
    public void string(String str, int format) throws IOException {
        string(str.getBytes(DEFAULT_ENCODING), format);
    }
    
    public void string(byte[] data, int format) throws IOException {
        string(data, 0, data.length, format);
    }
    
    public void string(byte[] data, int offset, int length, int format) throws IOException {
        switch(format) {
        case LITERAL_STRING:
            literalString(data, offset, length);
            break;
        case HEX_STRING:
            hexString(data, offset, length);
            break;
        case ASCII85_STRING:
            ascii85String(data, offset, length);
            break;
        default:
            assert(false) : "never goes here";
        }
    }
    
    public void literalString(String str) throws IOException {
        literalString(str.getBytes(DEFAULT_ENCODING));
    }
    
    public void literalString(byte[] data) throws IOException {
        literalString(data, 0, data.length);
    }
    
    public void literalString(byte[] data, int offset, int length) throws IOException {
        doLineBreakInCaseOfNeed(2);
        write('(');
        inString = true;
        charsInline += 1;
        for (int i=0; i<length; ++i) {     
            int c = data[i + offset] & 0xff;
            int e = LITERAL_ENCODING[c];
            int n = e >> 8;
            int m = e & 0xff;
            if (charsInline + n + 1 > maxCharsInline) {
                write('\\');
                lineBreak();
            }
            if (n == 1) {
                write(m);
            }
            else if (n == 2) {
                write('\\');
                write(m);
            }
            else if (n == 4) {
                write('\\');
                write('0' + ((m >> 6) & 7));
                write('0' + ((m >> 3) & 7));
                write('0' + (m & 7));
            }
            else {
                assert(false) : "never goes here";
            }
            charsInline += n;
        }
        doLineBreakInCaseOfNeed(1);
        inString = false;
        write(')');
        charsInline += 1;
        requireSpaceCharacter = false;
    }
    
    public void hexString(byte[] data) throws IOException {
        hexString(data, 0, data.length);
    }
    
    public void hexString(byte[] data, int offset, int length) throws IOException {
        doLineBreakInCaseOfNeed(3);
        write('<');
        inString = true;
        charsInline += 1;
        for (int i=0; i<length; ++i) {
            int c = data[i + offset] & 0xff;
            doLineBreakInCaseOfNeed(2);
            write(HEX_NUMBER_CHARS.charAt(c >> 4));
            write(HEX_NUMBER_CHARS.charAt(c & 0x0f));
            charsInline += 2;
        }
        doLineBreakInCaseOfNeed(1);
        inString = false;
        write('>');
        charsInline += 1;
        requireSpaceCharacter = false;
    }
    
    public void ascii85String(byte[] data) {
        ascii85String(data, 0, data.length);
    }
    
    public void ascii85String(byte[] data, int offset, int length) {
    	inString = true;
        assert(false) : "not implement yet!";
        inString = false;
        requireSpaceCharacter = false;
    }
                
    public void executeName(String n) throws IOException {
        byte[] temp = n.getBytes(DEFAULT_ENCODING);
        doSpaceInCaseOfNeed(temp.length);
        write(temp);
        charsInline += temp.length;
        requireSpaceCharacter = true;
    }
    
    public void literalName(String n) throws IOException {
        byte[] temp = n.getBytes(DEFAULT_ENCODING);
        doLineBreakInCaseOfNeed(temp.length + 1);
        write('/');
        write(temp);
        charsInline += temp.length + 1;
        requireSpaceCharacter = true;
    }
            
    public void beginArray() throws IOException {
        doLineBreakInCaseOfNeed(1);
        write('[');
        charsInline += 1;
        requireSpaceCharacter = false;
    }
    
    public void endArray() throws IOException {
        doLineBreakInCaseOfNeed(1);
        write(']');
        charsInline += 1;
        requireSpaceCharacter = false;
    }
    
    public void beginProcedure() throws IOException {
        doLineBreakInCaseOfNeed(1);
        write('{');
        charsInline += 1;
        requireSpaceCharacter = false;
    }
    
    public void endProcedure() throws IOException {
        doLineBreakInCaseOfNeed(1);
        write('}');
        charsInline += 1;
        requireSpaceCharacter = false;
    }
    
    public void beginDictionary() throws IOException {
        doLineBreakInCaseOfNeed(2);
        write('<');
        write('<');
        charsInline += 2;
        requireSpaceCharacter = false;
    }
    
    public void endDictionary() throws IOException {
        doLineBreakInCaseOfNeed(2);
        write('>');
        write('>');
        charsInline += 2;
        requireSpaceCharacter = false;
    }
       
    public void common(String message) throws IOException {
        if (charsInline != 0) {
            lineBreak();
        }
        tailCommon(message);
    }
        
    public void tailCommon(String message) throws IOException {
        doLineBreakInCaseOfNeed(2);
        byte[] messageBytes = message.getBytes(DEFAULT_ENCODING);
        int n = 0;
        int i = 0;
        do {
            write('%');
            charsInline++;
            n = Math.min(maxCharsInline - charsInline, messageBytes.length - i);
            write(messageBytes, i, n);
            lineBreak();
            i += n;
        }
        while(i < messageBytes.length);
    }
    
    public void space() throws IOException {
        if (charsInline + 1 > maxCharsInline) {
            lineBreak();
        }
        else {
            write(' ');
            charsInline++;
            requireSpaceCharacter = false;
        }
    }
    
    public void lineBreak() throws IOException {
        write('\n');
        charsInline = 0;
        requireSpaceCharacter = false;
    }
    
    public void newLine() throws IOException {
        if (charsInline != 0) {
            lineBreak();
        }
    }
        
    public void write(byte[] data) throws IOException {
        ostream.write(data);
    }
    
    public void write(byte[] data, int offset, int length) throws IOException {
        ostream.write(data, offset, length);
    }
    
    public void write(int c) throws IOException {
        ostream.write(c);
    }
    
    public void flush() {
    	// do nothing here currently
    }
        
    private void doLineBreakInCaseOfNeed(int n) throws IOException {
    	if (removeUnnecessarySpace || inString) {
	        if (charsInline + n > maxCharsInline) {
	            lineBreak();
	        }
    	}
    	else {
    		doSpaceInCaseOfNeed(n);
    	}
    }
    
    private void doSpaceInCaseOfNeed(int n) throws IOException {
    	if (charsInline == 0) {
    		return;
    	}
        if (removeUnnecessarySpace && !requireSpaceCharacter) {
            if (charsInline + n > maxCharsInline) {
                lineBreak();
            }
        }
        else {
            if (charsInline + n + 1 > maxCharsInline) {
                lineBreak();
            }
            else {
                write(' ');
                charsInline++;
                requireSpaceCharacter = false;
            }
        }
    }
}
