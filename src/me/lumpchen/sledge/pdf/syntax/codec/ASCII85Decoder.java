package me.lumpchen.sledge.pdf.syntax.codec;

import java.io.ByteArrayOutputStream;

import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class ASCII85Decoder extends Decoder {

	public ASCII85Decoder() {
		super(PName.ASCII85Decode);
	}
	
	@Override
	public byte[] decode(byte[] src) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
        int state = 0;
        int chn[] = new int[5];
        for (int k = 0; k < src.length; ++k) {
        	int ch = src[k] & 0xff;
        	if (ch == '~') {
                break;
        	}
        	if (isWhitespace(ch)) {
        		continue;
        	}
        	if (ch == 'z' && state == 0) {
                out.write(0);
                out.write(0);
                out.write(0);
                out.write(0);
                continue;
            }
            if (ch < '!' || ch > 'u') {
                throw new DecodeException("illegal character in ascii85decode: " + ch);
            }
            chn[state] = ch - '!';
            ++state;
            if (state == 5) {
                state = 0;
                int r = 0;
                for (int j = 0; j < 5; ++j) {
                    r = r * 85 + chn[j];
                }
                out.write((byte)(r >> 24));
                out.write((byte)(r >> 16));
                out.write((byte)(r >> 8));
                out.write((byte)r);
            }
        }
        int r = 0;
        if (state == 2) {
        	r = chn[0] * 85 * 85 * 85 * 85 + chn[1] * 85 * 85 * 85 + 85 * 85 * 85  + 85 * 85 + 85;
        	out.write((byte)(r >> 24));
        } else if (state == 3) {
            r = chn[0] * 85 * 85 * 85 * 85 + chn[1] * 85 * 85 * 85  + chn[2] * 85 * 85 + 85 * 85 + 85;
            out.write((byte)(r >> 24));
            out.write((byte)(r >> 16));
        } else if (state == 4) {
            r = chn[0] * 85 * 85 * 85 * 85 + chn[1] * 85 * 85 * 85  + chn[2] * 85 * 85  + chn[3] * 85 + 85;
            out.write((byte)(r >> 24));
            out.write((byte)(r >> 16));
            out.write((byte)(r >> 8));
        }
        return out.toByteArray();
    }

	@Override
	public void setDecodeParms(PDictionary decodeParms) {
		// TODO Auto-generated method stub
		
	}

	
}
