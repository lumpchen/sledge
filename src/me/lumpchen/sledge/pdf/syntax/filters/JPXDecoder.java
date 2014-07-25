package me.lumpchen.sledge.pdf.syntax.filters;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import me.lumpchen.sledge.pdf.syntax.lang.PName;

public class JPXDecoder extends Decode {

	protected JPXDecoder() {
		super(PName.JPXDecode);
	}

	@Override
	public ByteBuffer decode(ByteBuffer src) {
		ByteArrayInputStream bis = new ByteArrayInputStream(src.array());
		try {
			BufferedImage bi = ImageIO.read(bis);
	        if (bi != null) {
	            DataBuffer dBuf = bi.getData().getDataBuffer();
	            if (dBuf.getDataType() == DataBuffer.TYPE_BYTE) {
	                src.put(((DataBufferByte) dBuf).getData());
	                return src;
	            }
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
