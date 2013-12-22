package me.lumpchen.sledge.pdf.toolkit.editor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import me.lumpchen.sledge.pdf.reader.PDFReader;
import me.lumpchen.sledge.pdf.syntax.document.Catalog;
import me.lumpchen.sledge.pdf.syntax.document.DocumentInfo;
import me.lumpchen.sledge.pdf.syntax.document.PDFDocument;

public class DocumentTree {

	private JTree tree;
	private DefaultTreeModel model;
	private DefaultMutableTreeNode root;

	private PDFDocument doc;

	public DocumentTree(PDFDocument doc) {
		this.doc = doc;

		tree = new JTree();
		model = (DefaultTreeModel) tree.getModel();

		this.initialize();
	}

	public JTree getJTree() {
		return this.tree;
	}

	private void initialize() {
		this.root = new DefaultMutableTreeNode("root");
		this.model.setRoot(this.root);

		this.showDocumentInfo();
		this.showPages();
	}

	private void showDocumentInfo() {
		DocumentInfo info = this.doc.getInfo();

		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Info");
		this.model.insertNodeInto(node, this.root, 0);
	}

	private void showPages() {
		Catalog catalog = this.doc.getCatalog();
	}

	public static void main(String args[]) {
		try {
			JFrame f = new JFrame("123");
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Container content = f.getContentPane();

			PDFReader reader = new PDFReader();
			File pdf = new File("/simple.pdf");
			PDFDocument doc = reader.read(pdf);

			DocumentTree dtree = new DocumentTree(doc);
			JTree tree = dtree.getJTree();
			JScrollPane scrollPane = new JScrollPane(tree);
			content.add(scrollPane, BorderLayout.CENTER);
			f.setSize(300, 200);
			f.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
