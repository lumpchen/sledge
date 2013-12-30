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
import javax.swing.JTextArea;

import me.lumpchen.sledge.pdf.reader.PDFReader;
import me.lumpchen.sledge.pdf.syntax.XRef;
import me.lumpchen.sledge.pdf.syntax.document.PDFDocument;

public class EditorFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private int width;
	private int height;
	
	private JScrollPane leftScrollPane;
	private JScrollPane rightScrollPane;
	
	private JTextArea textarea;
	
	public EditorFrame() {
		super();
		
		this.width = 800;
		this.height = 600;
		this.setSize(this.width, this.height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.leftScrollPane = new JScrollPane();
		leftScrollPane.setMinimumSize(new Dimension(this.width / 3, this.height));
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		
		this.rightScrollPane = new JScrollPane();
		rightScrollPane.setPreferredSize(new Dimension(this.width / 3, this.height));
		rightPanel.add(this.rightScrollPane, BorderLayout.CENTER);
		
		JScrollPane rightBottomScrollPane = new JScrollPane();
		rightBottomScrollPane.setPreferredSize(new Dimension(this.width / 3, this.height / 2));
		rightPanel.add(rightBottomScrollPane, BorderLayout.SOUTH);
		this.textarea = new JTextArea();
		rightBottomScrollPane.setViewportView(this.textarea);
		
		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScrollPane, rightPanel);
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
//			this.createDocumentTree(doc);
			this.createXRefTable(doc);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void createXRefTable(PDFDocument doc) {
		XRefTableModel tableModel = new XRefTableModel(doc);
		XRefTable table = new XRefTable(tableModel, this.textarea);
		this.leftScrollPane.setViewportView(table);
	}
	
	private void createDocumentTree(PDFDocument doc) {
		PropertyTableModel tableModel = new PropertyTableModel(doc);
		DocumentTree dtree = new DocumentTree(doc, tableModel, this.textarea);
		this.leftScrollPane.setViewportView(dtree);
		
		PropertyTable table = new PropertyTable(tableModel);
		this.rightScrollPane.setViewportView(table);
	}
	
}
