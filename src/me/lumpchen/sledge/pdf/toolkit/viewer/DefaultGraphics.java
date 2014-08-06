package me.lumpchen.sledge.pdf.toolkit.viewer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import me.lumpchen.sledge.pdf.graphics.GraphicsState;
import me.lumpchen.sledge.pdf.graphics.Matrix;
import me.lumpchen.sledge.pdf.graphics.PDFColor;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.graphics.GraphicsState.TextState;
import me.lumpchen.sledge.pdf.text.font.PDFFont;

public class DefaultGraphics implements VirtualGraphics {

	private Stack<GraphicsState> gsStack = new Stack<GraphicsState>();

	private GraphicsState gstate;

	private Graphics2D g2;

	private GeneralPath currPath;

	private static int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();

	private float resolution = -1;

	private Font defaultFont;

	private AffineTransform textCTM;

	public DefaultGraphics(Graphics2D g2) {
		this.g2 = g2;

		this.g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		this.g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		this.defaultFont = new Font("Arial", Font.PLAIN, 12);

		this.gstate = new GraphicsState();
		this.gstate.ctm = this.g2.getTransform();
	}

	public void setResolutoin(float deviceRes) {
		this.resolution = deviceRes;
	}

	private double toPixel(double d) {
		if (this.resolution <= 0) {
			this.resolution = screenRes;
		}
		return (d / 72) * resolution;
	}

	@Override
	public void saveGraphicsState() {
		if (this.gstate != null) {
			this.gsStack.push(gstate);
		}

		this.gstate = new GraphicsState(this.gstate);
	}

	@Override
	public void restoreGraphicsState() {
		if (this.gsStack.isEmpty()) {
			throw new RuntimeException("unmatched graphics state.");
		}
		this.gstate = this.gsStack.pop();
	}

	@Override
	public void beginCanvas(double width, double height) {
		this.gstate.ctm = AffineTransform.getTranslateInstance(0, height);
//		this.g2.setTransform(this.gstate.ctm);
		
		try {
			Font f = Font.createFont(Font.TRUETYPE_FONT, new File("c:/temp/msyh.ttf"));
			f = f.deriveFont(36.0f);
			this.g2.setFont(f);
			this.g2.setColor(Color.black);
			this.g2.drawString("AAA", 300, -300);
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void concatenate(Matrix matrix) {
		double[] m = this.flateMatrix(matrix);
		m[5] = -m[5];
		AffineTransform at = new AffineTransform(m);
		this.gstate.ctm.concatenate(at);
	}

	private void setTransform(AffineTransform at) {
		double[] flatmatrix = new double[6];
		at.getMatrix(flatmatrix);
		flatmatrix[5] = -flatmatrix[5];
		at = new AffineTransform(flatmatrix);
		
		AffineTransform curr = new AffineTransform(this.gstate.ctm);
		curr.concatenate(at);
		
		double[] m = new double[6];
		curr.getMatrix(m);
		curr = new AffineTransform(this.toPixel(m));
		
		this.g2.setTransform(curr);
	}
	
	private double[] toPixel(double[] m) {
		m[4] = this.toPixel(m[4]);
		m[5] = this.toPixel(m[5]);
		return m;
	}
	
	@Override
	public void setColor(me.lumpchen.sledge.pdf.graphics.PDFColor color) {
		this.gstate.color = color.toJavaColor();
		this.g2.setColor(this.gstate.color);
	}

	@Override
	public void beginText() {
		this.gstate.textState.reset();
		this.saveGraphicsState();
	}

	@Override
	public void setFont(PDFFont font, float size) {
		if (font == null) {
			return;
		}

		this.gstate.textState.font = font;
		this.gstate.textState.setFontSize(size);
		
//		this.gstate.font = font;
//		this.gstate.baseFontSize = size;

//		if (this.textCTM != null) {
//			double sx = textCTM.getScaleX();
//			this.gstate.textState.fontSize = (float) sx * size;
//		} else {
//			this.gstate.textState.fontSize = (int) (this.toPixel(size) + 0.5);
//		}

		if (font.notEmbed()) {
			this.gstate.awtFont = font.peerAWTFont();
			if (null == this.gstate.awtFont) {
				this.gstate.awtFont = this.defaultFont;
//				this.gstate.awtFont = this.gstate.awtFont
//						.deriveFont((float) this.gstate.textState.fontSize);
			}
		}
	}

	@Override
	public void transformTextMatrix(Matrix matrix) {
		this.gstate.textState.setTextMatrix(matrix);
//		this.textCTM = new AffineTransform(this.flateMatrix(matrix));
	}

	@Override
	public void transformTextPosition(double tx, double ty) {
//		double sx = textCTM.getScaleX();
//		double sy = textCTM.getScaleY();
//		AffineTransform at = new AffineTransform(1, 0, 0, 1, tx * sx, ty * sy);
//		this.textCTM.preConcatenate(at);
	}

	@Override
	public void setTextLeading(double leading) {
//		this.gstate.textState.leading = leading;
		this.gstate.textState.setLeading(leading);
	}

	@Override
	public void moveToNextTextLine() {
//		if (!(this.gstate.textState.leading != 0)) {
//			return;
//		}
//		this.transformTextPosition(0, this.gstate.textState.leading);
		this.gstate.textState.moveToNextLine();
	}
	
	@Override
	public void setCharSpacing(double charSpace) {
//		this.gstate.textState.charSpace = charSpace;
		this.gstate.textState.setCharSpace(charSpace);
	}
	
	@Override
	public void setWordSpacing(double wordSpace) {
//		this.gstate.textState.wordSpace = wordSpace;
		this.gstate.textState.setWordSpace(wordSpace);
	}

	@Override
	public void setTextAdjustment(double adjustment) {
//		this.gstate.textState.adjustment = adjustment;
	}
	
	@Override
	public double getAdjustmentH(char c) {
//		double adjustment = 0;
//		if (c == ' ') {
//			adjustment += tstate.wordSpace;
//		} else {
//			adjustment += tstate.charSpace;
//		}
//		return this.toPixel(adjustment * sx);
		return 0;
	}
	
	@Override
	public void advanceH(double advance, char c) {
		this.gstate.textState.advance(advance, c);
	}
	
	@Override
	public void showText(String text) {
		if (this.gstate.textState.font == null || this.gstate.textState.font.notEmbed()) {
			this.gstate.awtFont = this.gstate.awtFont
					.deriveFont((float) this.gstate.textState.getFontSize());
			this.g2.setFont(this.gstate.awtFont);
			this.g2.drawString(text, 0, 0);
			return;
		}


		
		
		try {
			if (this.textCTM != null) {
				double sx = textCTM.getScaleX();
				double sy = textCTM.getScaleY();
		
				double rx = textCTM.getShearX();
				double ry = textCTM.getShearY();

//				this.gstate.textState.fontSize = (float) sx * this.gstate.baseFontSize;
		
				double tx = textCTM.getTranslateX();
				double ty = textCTM.getTranslateY();
				tx = this.toPixel(tx);
				ty = this.toPixel(ty);
		
				AffineTransform at = new AffineTransform(1, 0, 0, 1, tx, -ty);
				AffineTransform m = new AffineTransform(this.gstate.ctm);
				m.preConcatenate(at);
				this.g2.setTransform(m);
				
				TextState tstate = this.gstate.textState;
//				double adjustment = (tstate.adjustment / 1000) * tstate.fontSize;
//				if (adjustment != 0) {
//					this.translate(-this.toPixel(adjustment), 0);		
//				}
			}
			
//			double tx = this.gstate.textState.textMatrix.getTranslateX();
//			double ty = this.gstate.textState.textMatrix.getTranslateY();
//			AffineTransform at = new AffineTransform(1, 0, 0, 1, this.toPixel(tx), this.toPixel(-ty));
//			this.gstate.ctm.concatenate(at);
			
//			this.g2.setTransform(this.gstate.ctm);
			
			this.setTransform(this.gstate.textState.textMatrix);
			
			this.gstate.textState.font.renderText(text, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	AffineTransform restCTM;
	@Override
	public void beginTextLine() {
		this.restCTM = new AffineTransform(this.gstate.ctm);
	}
	
	@Override
	public void endTextLine() {
		this.gstate.ctm = restCTM;
//		this.gstate.textState.adjustment = 0;
	}
	
	@Override
	public void endText() {
		this.gstate.textState.reset();
		this.textCTM = null;
		this.restCTM = null;
		this.restoreGraphicsState();
	}

	@Override
	public void translate(double tx, double ty) {
		this.gstate.ctm.translate(tx, ty);
		this.g2.translate(tx, ty);
	}

	private double[] flateMatrix(Matrix matrix) {
		double[] m = matrix.flate();
		return m;
	}

	@Override
	public GraphicsState currentGState() {
		return this.gstate;
	}

	@Override
	public float[] getResolution() {
		if (this.resolution <= 0) {
			this.resolution = screenRes;
		}
		return new float[] { this.resolution, this.resolution };
	}

	@Override
	public boolean drawImage(Image img) {
		return this.g2.drawImage(img, null, null);
	}

	@Override
	public void newPath() {
		this.currPath = new GeneralPath();
	}

	@Override
	public void closePath() {
		this.currPath.closePath();
	}

	@Override
	public void moveTo(double x, double y) {
		this.currPath.moveTo(this.toPixel(x), this.toPixel(y));
	}

	@Override
	public void lineTo(double x, double y) {
		this.currPath.lineTo(this.toPixel(x), this.toPixel(y));
	}

	@Override
	public void quadTo(double x1, double y1, double x2, double y2) {
		this.currPath
				.quadTo(this.toPixel(x1), this.toPixel(y1), this.toPixel(x2), this.toPixel(y2));
	}

	@Override
	public void curveTo(double x1, double y1, double x2, double y2, double x3, double y3) {
		this.currPath.curveTo(this.toPixel(x1), this.toPixel(y1), this.toPixel(x2),
				this.toPixel(y2), this.toPixel(x3), this.toPixel(y3));
	}

	@Override
	public void stroke() {
		if (this.currPath != null) {
			this.g2.draw(this.currPath);
		}
	}

	@Override
	public void fill() {
		if (this.currPath != null) {
			this.g2.fill(this.currPath);
		}
	}

	@Override
	public void clip() {
		if (null != this.currPath) {
			this.g2.clip(this.currPath);
		}
	}

	@Override
	public void strokeRect(double x, double y, double width, double height) {
		x = this.toPixel(x);
		y = this.toPixel(y);
		width = this.toPixel(width);
		height = this.toPixel(height);
		Rectangle2D.Double rect = new Rectangle2D.Double(x, y, width, height);
		this.g2.draw(rect);
	}

	@Override
	public void fillRect(double x, double y, double width, double height) {
		x = this.toPixel(x);
		y = this.toPixel(y);
		width = this.toPixel(width);
		height = this.toPixel(height);
		Rectangle2D.Double rect = new Rectangle2D.Double(x, y, width, height);
		this.g2.fill(rect);
	}

	@Override
	public void setStrokeColor(PDFColor color) {
		this.gstate.color = color.toJavaColor();
		this.g2.setColor(this.gstate.color);
	}

	@Override
	public void setFillColor(PDFColor color) {
		this.gstate.color = color.toJavaColor();
		this.g2.setColor(this.gstate.color);
	}

	@Override
	public void strokeShape(Shape shape) {
		this.g2.draw(shape);
	}

	@Override
	public void fillShape(Shape shape) {
		this.g2.fill(shape);
	}
}
