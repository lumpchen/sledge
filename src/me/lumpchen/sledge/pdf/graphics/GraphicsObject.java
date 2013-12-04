package me.lumpchen.sledge.pdf.graphics;

public abstract class GraphicsObject {

	public static enum Type {
		path, text, XObject, inlineImage, shadding
	};
	
}

class TextGraphicsObject extends GraphicsObject {
	
}
