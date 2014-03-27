package me.lumpchen.sledge.pdf.text.font;

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import me.lumpchen.sledge.pdf.syntax.document.FontObject;
import me.lumpchen.sledge.pdf.text.font.ttf.CMap;
import me.lumpchen.sledge.pdf.text.font.ttf.CmapTable;
import me.lumpchen.sledge.pdf.text.font.ttf.Glyf;
import me.lumpchen.sledge.pdf.text.font.ttf.GlyfCompound;
import me.lumpchen.sledge.pdf.text.font.ttf.GlyfSimple;
import me.lumpchen.sledge.pdf.text.font.ttf.GlyfTable;
import me.lumpchen.sledge.pdf.text.font.ttf.HeadTable;
import me.lumpchen.sledge.pdf.text.font.ttf.HmtxTable;
import me.lumpchen.sledge.pdf.text.font.ttf.TrueTypeFont;

public class TTFFont extends PDFFont {

	private TrueTypeFont ttf;
	private double unitsPerEm;

	public TTFFont(FontObject fontObj) {
		super(fontObj);

		FontFile fontFile = this.fontDescriptor.getFontFile2();
		if (fontFile != null) {
			this.parse(fontFile.getBytes());
		}
	}

	private void parse(byte[] fontBytes) {
		this.ttf = TrueTypeFont.parseFont(fontBytes);

		// read the units per em from the head table
		HeadTable head = (HeadTable) ttf.getTable("head");
		this.unitsPerEm = head.getUnitsPerEm();
	}

	@Override
	public PDFGlyph getGlyph(char c) {
		PDFGlyph glyph = new PDFGlyph();
		this.getOutline(c, glyph);
		return glyph;
	}

	private void getOutline(char c, PDFGlyph glyph) {
		// find the cmaps
		CmapTable cmap = (CmapTable) this.ttf.getTable("cmap");

		// if there are no cmaps, this is (hopefully) a cid-mapped font,
		// so just trust the value we were given for src
		if (cmap == null) {
			this.getOutline((int) c, glyph);
			return;
		}

		CMap[] maps = cmap.getCMaps();
		// try the maps in order
		for (int i = 0; i < maps.length; i++) {
			int idx = maps[i].map(c);
			if (idx != 0) {
				this.getOutline(idx, glyph);
				return;
			}
		}

		// not found, return the empty glyph
		this.getOutline(0, glyph);
	}

	private void getOutline(int glyphID, PDFGlyph glyph) {
		// find the glyph itself
		GlyfTable glyf = (GlyfTable) this.ttf.getTable("glyf");
		Glyf g = glyf.getGlyph(glyphID);

		GeneralPath gp = null;
		if (g instanceof GlyfSimple) {
			gp = renderSimpleGlyph((GlyfSimple) g);
		} else if (g instanceof GlyfCompound) {
			gp = renderCompoundGlyph(glyf, (GlyfCompound) g);
		} else {
			gp = new GeneralPath();
		}

		// calculate the advance
		HmtxTable hmtx = (HmtxTable) this.ttf.getTable("hmtx");
		float advance = (float) hmtx.getAdvance(glyphID) / (float) unitsPerEm;
		glyph.setAdvance(advance);

		// the base transform scales the glyph to 1x1
		AffineTransform at = AffineTransform.getScaleInstance(1 / unitsPerEm, 1 / unitsPerEm);
		at.concatenate(AffineTransform.getScaleInstance(1, 1));

		gp.transform(at);
		glyph.setGlyph(gp);
	}

	protected GeneralPath renderSimpleGlyph(GlyfSimple g) {
		// the current contour
		int curContour = 0;

		// the render state
		RenderState rs = new RenderState();
		rs.gp = new GeneralPath();

		for (int i = 0; i < g.getNumPoints(); i++) {
			PointRec rec = new PointRec(g, i);

			if (rec.onCurve) {
				addOnCurvePoint(rec, rs);
			} else {
				addOffCurvePoint(rec, rs);
			}

			// see if we just ended a contour
			if (i == g.getContourEndPoint(curContour)) {
				curContour++;

				if (rs.firstOff != null) {
					addOffCurvePoint(rs.firstOff, rs);
				}

				if (rs.firstOn != null) {
					addOnCurvePoint(rs.firstOn, rs);
				}

				rs.firstOn = null;
				rs.firstOff = null;
				rs.prevOff = null;
			}
		}

		return rs.gp;
	}

	/**
	 * Render a compound glyf
	 */
	protected GeneralPath renderCompoundGlyph(GlyfTable glyf, GlyfCompound g) {
		GeneralPath gp = new GeneralPath();

		for (int i = 0; i < g.getNumComponents(); i++) {
			// find and render the component glyf
			Glyf gl = glyf.getGlyph(g.getGlyphIndex(i));
			GeneralPath path = null;
			if (gl instanceof GlyfSimple) {
				path = renderSimpleGlyph((GlyfSimple) gl);
			} else if (gl instanceof GlyfCompound) {
				path = renderCompoundGlyph(glyf, (GlyfCompound) gl);
			} else {
				throw new RuntimeException("Unsupported glyph type "
						+ gl.getClass().getCanonicalName());
			}

			// multiply the translations by units per em
			double[] matrix = g.getTransform(i);

			// transform the path
			path.transform(new AffineTransform(matrix));

			// add it to the global path
			gp.append(path, false);
		}

		return gp;
	}

	/** add a point on the curve */
	private void addOnCurvePoint(PointRec rec, RenderState rs) {
		// if the point is on the curve, either move to it,
		// or draw a line from the previous point
		if (rs.firstOn == null) {
			rs.firstOn = rec;
			rs.gp.moveTo(rec.x, rec.y);
		} else if (rs.prevOff != null) {
			rs.gp.quadTo(rs.prevOff.x, rs.prevOff.y, rec.x, rec.y);
			rs.prevOff = null;
		} else {
			rs.gp.lineTo(rec.x, rec.y);
		}
	}

	/** add a point off the curve */
	private void addOffCurvePoint(PointRec rec, RenderState rs) {
		if (rs.prevOff != null) {
			PointRec oc = new PointRec((rec.x + rs.prevOff.x) / 2,
					(rec.y + rs.prevOff.y) / 2, true);
			addOnCurvePoint(oc, rs);
		} else if (rs.firstOn == null) {
			rs.firstOff = rec;
		}
		rs.prevOff = rec;
	}

	class RenderState {
		// the shape itself

		GeneralPath gp;
		// the first off and on-curve points in the current segment

		PointRec firstOn;

		PointRec firstOff;
		// the previous off and on-curve points in the current segment

		PointRec prevOff;

	}

	/** a point on the stack of points */
	class PointRec {

		int x;

		int y;

		boolean onCurve;

		public PointRec(int x, int y, boolean onCurve) {
			this.x = x;
			this.y = y;
			this.onCurve = onCurve;
		}

		public PointRec(GlyfSimple g, int idx) {
			x = g.getXCoord(idx);
			y = g.getYCoord(idx);
			onCurve = g.onCurve(idx);
		}
	}
}
