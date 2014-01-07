package me.lumpchen.sledge.pdf.toolkit.editor;


public class Main {

	public static void main(String args[]) {
		EditorFrame f = new EditorFrame();
		f.openDocument("/ff.pdf");
		f.setVisible(true);
	}
}
