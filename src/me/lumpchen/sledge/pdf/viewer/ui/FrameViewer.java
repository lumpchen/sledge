package me.lumpchen.sledge.pdf.viewer.ui;

import javax.swing.JFrame;

public class FrameViewer extends JFrame {

	private static final long serialVersionUID = -7681692658609592158L;

	public FrameViewer() {
		super();
		
		this.setSize(800, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void setPageCanvas(PageCanvas pageCanvas) {
		this.add(pageCanvas);
	}
}
