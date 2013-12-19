package me.lumpchen.sledge.pdf.viewer.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JPanel;

import me.lumpchen.sledge.pdf.syntax.document.Page;
import me.lumpchen.sledge.pdf.viewer.DefaultGraphics;

public class PageCanvas extends JPanel {

	private static final long serialVersionUID = -3805552464192825374L;

	private Page page;
	
	private Color background = Color.darkGray;

	private static int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
	private double resolution = screenRes;
	
	public PageCanvas() {
		super();
	}
	
	public void setPage(Page page) {
		this.page = page;
		this.repaint();
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(background);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		if (this.page != null) {
			double pw = this.toPixel(this.page.getWidth());
			double ph = this.toPixel(this.page.getHeight());
			
			int w = this.getWidth();
			double offx = 0;
			if (w > pw) {
				offx = (w - pw) / 2;
			}
			g2.translate(offx, 0);
			
			DefaultGraphics gd = new DefaultGraphics(g2);
			gd.setResolutoin(this.resolution);
			this.page.render(gd);
		}
	}
	
	private double toPixel(double d) {
		if (this.resolution <= 0) {
			this.resolution = screenRes;
		}
		return (d / 72) * resolution;
	}
}
