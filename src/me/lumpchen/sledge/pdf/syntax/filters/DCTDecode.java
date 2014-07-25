package me.lumpchen.sledge.pdf.syntax.filters;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.nio.ByteBuffer;

import me.lumpchen.sledge.pdf.syntax.lang.PName;

public class DCTDecode extends Decode {

	protected DCTDecode() {
		super(PName.DCTDecode);
	}

	@Override
	public ByteBuffer decode(ByteBuffer src) {

		// System.out.println("DCTDecode image info: "+params);
		src.rewind();

		// copy the data into a byte array required by createimage
		byte[] ary = new byte[src.remaining()];
		src.get(ary);

		// wait for the image to get drawn
		Image img = Toolkit.getDefaultToolkit().createImage(ary);
		MyTracker mt = new MyTracker(img);
		mt.waitForAll();

		// the default components per pixel is 3
		int numComponents = this.getNumComponents();

		// figure out the type
		int type = BufferedImage.TYPE_INT_RGB;
		if (numComponents == 1) {
			type = BufferedImage.TYPE_BYTE_GRAY;
		} else if (numComponents == 4) {
			type = BufferedImage.TYPE_INT_ARGB;
		}

		// create a buffered image
		BufferedImage bimg = new BufferedImage(img.getWidth(null),
				img.getHeight(null), type);
		Graphics bg = bimg.getGraphics();

		// draw the image onto it
		bg.drawImage(img, 0, 0, null);

		byte[] output = null;

		// incidentally, there's a bit of an optimisation we could apply here,
		// if we weren't pretty confident that this isn't actually going to
		// be called, anyway. Namely, if we just use JAI to read in the data
		// the underlying data buffer seems to typically be byte[] based,
		// and probably already in the desired arrangement (and if not, that
		// could be engineered by supplying our own sample model). As it is,
		// we won't bother, since this code is most likely not going
		// to be used.

		if (type == BufferedImage.TYPE_INT_RGB) {
			// read back the data
			DataBufferInt db = (DataBufferInt) bimg.getData().getDataBuffer();
			int[] data = db.getData();

			output = new byte[data.length * 3];
			for (int i = 0; i < data.length; i++) {
				output[i * 3] = (byte) (data[i] >> 16);
				output[i * 3 + 1] = (byte) (data[i] >> 8);
				output[i * 3 + 2] = (byte) (data[i]);
			}
		} else if (type == BufferedImage.TYPE_BYTE_GRAY) {
			DataBufferByte db = (DataBufferByte) bimg.getData().getDataBuffer();
			output = db.getData();
		} else if (type == BufferedImage.TYPE_INT_ARGB) {
			// read back the data
			DataBufferInt db = (DataBufferInt) bimg.getData().getDataBuffer();
			int[] data = db.getData();

			output = new byte[data.length * 4];
			for (int i = 0; i < data.length; i++) {
				output[i * 4] = (byte) (data[i] >> 24);
				output[i * 4 + 1] = (byte) (data[i] >> 16);
				output[i * 4 + 2] = (byte) (data[i] >> 8);
				output[i * 4 + 3] = (byte) (data[i]);
			}
		}

		// System.out.println("Translated data");
		return ByteBuffer.wrap(output);
	}

	private int getNumComponents() {
		// // see if we have a colorspace
		// try {
		// PDFObject csObj = dict.getDictRef("ColorSpace");
		// if (csObj != null) {
		// // we do, so get the number of components
		// PDFColorSpace cs = PDFColorSpace.getColorSpace(csObj, null);
		// numComponents = cs.getNumComponents();
		// }
		// } catch (IOException ioe) {
		// // oh well
		// }

		// to get the number of component, not implemented now
		return 3;
	}

	/**
	 * Image tracker. I'm not sure why I'm not using the default Java image
	 * tracker for this one.
	 */
	class MyTracker implements ImageObserver {
		boolean done = false;

		/**
		 * create a new MyTracker that watches this image. The image will start
		 * loading immediately.
		 */
		public MyTracker(Image img) {
			img.getWidth(this);
		}

		/**
		 * More information has come in about the image.
		 */
		public boolean imageUpdate(Image img, int infoflags, int x, int y,
				int width, int height) {
			if ((infoflags & (ALLBITS | ERROR | ABORT)) != 0) {
				synchronized (this) {
					done = true;
					notifyAll();
				}
				return false;
			}
			return true;
		}

		/**
		 * Wait until the image is done, then return.
		 */
		public synchronized void waitForAll() {
			if (!done) {
				try {
					wait();
				} catch (InterruptedException ie) {
				}
			}
		}
	}
}
