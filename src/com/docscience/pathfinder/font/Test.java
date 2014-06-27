package com.docscience.pathfinder.font;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.docscience.pathfinder.font.driver.FontReader;
import com.docscience.pathfinder.font.driver.FontReaderFactory;
import com.docscience.pathfinder.font.shared.Font;
import com.docscience.pathfinder.font.shared.FontLoadOptions;
import com.docscience.pathfinder.font.shared.GlyphDescription;
import com.docscience.pathfinder.font.shared.GlyphPath;
import com.docscience.pathfinder.font.shared.GlyphPathIterator;

public class Test {

	static int unitsPerEM;

	public static void main(String[] args) throws IOException {

		File file = new File("C:/temp/simhei.ttf");

		FontReader reader = FontReaderFactory.getInstance().createFontReader(file);

		System.out.println(reader.getNumFonts());

		reader.selectFont(0);
		Font f = reader.getFont(new FontLoadOptions(true, true, true));
		System.out.println(f.getFontFamily());

		System.out.println(reader.getGlyphCount());

		int gid = reader.getEncoding().getGlyphId('文');
		System.out.println(gid);

		GlyphDescription glyph = reader.getGlyph(gid);
		System.out.println(glyph);

		unitsPerEM = f.getMetrics().getUnitsPerEm();
		System.out.println(unitsPerEM);

		showGlyph(glyph);
	}

	static void showGlyph(GlyphDescription glyph) {
		GlyphPath path = glyph.getPath();

		GeneralPath gpath = new GeneralPath();

		float pt = 96;
		float[] res = new float[] { 96, 96 };
		AffineTransform at = new AffineTransform(1, 0, 0, -1, 0, 0);
		at.scale((pt / 72) * res[0] / unitsPerEM, (pt / 72) * res[1] / unitsPerEM);
		double[] m = new double[6];
		at.getMatrix(m);

		GlyphPathIterator iter = path.getGlyphPathIterator(m);

		double[] coords = new double[6];
		for (; !iter.isDone(); iter.next()) {
			int type = iter.currentSegment(coords);
			switch (type) {
			case GlyphPathIterator.SEG_MOVETO:
				gpath.moveTo(coords[0], coords[1]);
				break;
			case GlyphPathIterator.SEG_LINETO:
				gpath.lineTo(coords[0], coords[1]);
				break;
			case GlyphPathIterator.SEG_QUADTO:
				gpath.quadTo(coords[0], coords[1], coords[2], coords[3]);
				break;
			case GlyphPathIterator.SEG_CURVETO:
				gpath.curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
				break;
			case GlyphPathIterator.SEG_CLOSE:
				gpath.closePath();
				break;
			}
		}

		JFrame frame = new JFrame();
		frame.setSize(600, 600);

		PageCanvas panel = new PageCanvas();
		panel.setPage(gpath);

		frame.add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
	}

}

class PageCanvas extends JPanel {

	private static final long serialVersionUID = -3805552464192825374L;

	private Color background = Color.darkGray;

	private static int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
	private float resolution = screenRes;

	GeneralPath gpath;

	public PageCanvas() {
		super();
	}

	public void setPage(GeneralPath gpath) {
		this.gpath = gpath;
		this.repaint();
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		int w = this.getWidth();
		int h = this.getHeight();

		g2.translate(w / 2f, h / 2f);

		g2.setColor(Color.black);
		g2.fill(gpath);
	}

	private double toPixel(double d) {
		if (this.resolution <= 0) {
			this.resolution = screenRes;
		}
		return (d / 72) * resolution;
	}
}
