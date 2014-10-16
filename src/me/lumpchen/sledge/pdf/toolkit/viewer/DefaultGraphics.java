package me.lumpchen.sledge.pdf.toolkit.viewer;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Stack;

import me.lumpchen.sledge.pdf.graphics.GraphicsState;
import me.lumpchen.sledge.pdf.graphics.Matrix;
import me.lumpchen.sledge.pdf.graphics.PDFColor;
import me.lumpchen.sledge.pdf.graphics.VirtualGraphics;
import me.lumpchen.sledge.pdf.text.font.PDFFont;

public class DefaultGraphics implements VirtualGraphics {

	private Stack<GraphicsState> gsStack = new Stack<GraphicsState>();

	private GraphicsState gstate;

	private Graphics2D g2;

	private GeneralPath currPath;

	private static int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();

	private float resolution = -1;

	private Font defaultFont;

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
		AffineTransform at = AffineTransform.getTranslateInstance(0, this.toPixel(height));
		this.gstate.ctm.concatenate(at);
	}

	@Override
	public void concatenate(Matrix matrix) {
		double[] m = this.toPixel(this.flateMatrix(matrix));
		m[5] = -m[5];
		AffineTransform at = new AffineTransform(m);
		this.gstate.ctm.concatenate(at);
	}

	private void setTransform(AffineTransform at) {
		double[] flatmatrix = new double[6];
		at.getMatrix(flatmatrix);
		flatmatrix[5] = -flatmatrix[5];
		at = new AffineTransform(this.toPixel(flatmatrix));
		
		AffineTransform curr = new AffineTransform(this.gstate.ctm);
		curr.concatenate(at);
		
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
		
		if (font.notEmbed()) {
			this.gstate.awtFont = font.peerAWTFont();
			if (null == this.gstate.awtFont) {
				this.gstate.awtFont = this.defaultFont;
			}
		}
	}

	@Override
	public void transformTextMatrix(Matrix matrix) {
		this.gstate.textState.setTextMatrix(matrix);
	}

	@Override
	public void transformTextPosition(double tx, double ty) {
		this.gstate.textState.moveTo(tx, ty);
	}

	@Override
	public void setTextLeading(double leading) {
		this.gstate.textState.setLeading(leading);
	}

	@Override
	public void moveToNextTextLine() {
		this.gstate.textState.moveToNextLine();
	}
	
	@Override
	public void setCharSpacing(double charSpace) {
		this.gstate.textState.setCharSpace(charSpace);
	}
	
	@Override
	public void setWordSpacing(double wordSpace) {
		this.gstate.textState.setWordSpace(wordSpace);
	}

	@Override
	public void setTextAdjustment(double adjustment) {
		this.gstate.textState.setAdjustment(adjustment);
		double ad = this.toPixel(-this.gstate.textState.getAdjustment());
		this.translate(ad, 0);
	}
	
	@Override
	public double getAdjustmentH(char c) {
		double adjustment = 0;
		adjustment += this.gstate.textState.getCharSpace();
		if (c == ' ') {
			adjustment += this.gstate.textState.getWordSpace();
		}
		
		return this.toPixel(adjustment);
	}
	
	@Override
	public void advanceH(double advance, char c) {
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
