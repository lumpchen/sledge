package me.lumpchen.sledge.pdf.toolkit.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import me.lumpchen.sledge.pdf.reader.PDFReader;
import me.lumpchen.sledge.pdf.syntax.document.PDFDocument;

public class EditorFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private JPanel leftPanel;
	private JPanel rightPanel;
	private int width;
	private int height;
	
	public EditorFrame() {
		super();
		
		this.width = 800;
		this.height = 600;
		this.setSize(this.width, this.height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.leftPanel = new JPanel();
		this.leftPanel.setLayout(new BorderLayout());
		
		this.rightPanel = new JPanel();
		this.rightPanel.setLayout(new BorderLayout());
		
		JScrollPane leftScrollPane = new JScrollPane(this.leftPanel);
		leftScrollPane.setMinimumSize(new Dimension(this.width / 3, this.height));
		JScrollPane rightScrollPane = new JScrollPane(this.rightPanel);
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
		this.leftPanel.add(dtree, BorderLayout.CENTER);
		
		PropertyTable table = new PropertyTable(tableModel);
		this.rightPanel.add(table, BorderLayout.CENTER);
	}
	
}
