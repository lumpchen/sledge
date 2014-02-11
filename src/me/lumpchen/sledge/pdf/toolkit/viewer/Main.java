package me.lumpchen.sledge.pdf.toolkit.viewer;

import me.lumpchen.sledge.pdf.toolkit.viewer.ui.FrameViewer;

public class Main {

	public static void main(String[] args) {
		testUI();
	}

	static void testUI() {
		FrameViewer viewer = new FrameViewer();
		viewer.setVisible(true);
	}
}
