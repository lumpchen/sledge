package me.lumpchen.sledge.pdf.viewer.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import me.lumpchen.sledge.pdf.syntax.Page;
import me.lumpchen.sledge.pdf.viewer.DefaultGraphics;

public class PageCanvas extends JPanel {

	private static final long serialVersionUID = -3805552464192825374L;

	private Page page;
	
	public PageCanvas() {
		super();
	}
	
	public void setPage(Page page) {
		this.page = page;
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		DefaultGraphics gd = new DefaultGraphics(g2);
		this.page.render(gd);
	}
}
