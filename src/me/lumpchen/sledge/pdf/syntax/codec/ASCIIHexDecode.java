package me.lumpchen.sledge.pdf.syntax.codec;

import java.io.ByteArrayOutputStream;

import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class ASCIIHexDecode extends Decoder {

	protected ASCIIHexDecode() {
		super(PName.ASCIIHexDecode);
	}

	@Override
	public byte[] decode(byte[] in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean first = true;
        int n1 = 0;
        for (int k = 0; k < in.length; ++k) {
            int ch = in[k] & 0xff;
            if (ch == '>') {
                break;
            }
            if (isWhitespace(ch)) {
                continue;
            }
            int n = toHex(ch);
            if (n == -1) {
            	throw new DecodeException("illegal.character.in.asciihexdecode");            	
            }
            if (first) {
            	n1 = n;            	
            } else {
                out.write((byte) ((n1 << 4) + n));
            }
            first = !first;
        }
        if (!first) {
            out.write((byte)(n1 << 4));
        }
        return out.toByteArray();
    }

}
