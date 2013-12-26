package me.lumpchen.sledge.pdf.toolkit.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import me.lumpchen.sledge.pdf.reader.PDFReader;
import me.lumpchen.sledge.pdf.syntax.document.PDFDocument;

public class EditorFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private int width;
	private int height;
	
	private JScrollPane leftScrollPane;
	private JScrollPane rightScrollPane;
	
	public EditorFrame() {
		super();
		
		this.width = 800;
		this.height = 600;
		this.setSize(this.width, this.height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.leftScrollPane = new JScrollPane();
		leftScrollPane.setMinimumSize(new Dimension(this.width / 3, this.height));
		this.rightScrollPane = new JScrollPane();
		rightScrollPane.setMinimumSize(new Dimension(this.width / 3, this.height));
		
		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScrollPane, rightScrollPane);
	    getContentPane().add(sp, BorderLayout.CENTER);
	}
	
	public void openDocument(String path) {
		PDFReader reader = new PDFReader();
		File pdf = new File(path);
		if (!pdf.exists()) {
			JOptionPane.showMessageDialog(this, "File not found.");
		}
		try {
			PDFDocument doc = reader.read(pdf);
			this.createDocumentTree(doc);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void createDocumentTree(PDFDocument doc) {
		PropertyTableModel tableModel = new PropertyTableModel();
		DocumentTree dtree = new DocumentTree(doc, tableModel);
		this.leftScrollPane.setViewportView(dtree);
		
		PropertyTable table = new PropertyTable(tableModel);
		this.rightScrollPane.setViewportView(table);
	}
	
}
