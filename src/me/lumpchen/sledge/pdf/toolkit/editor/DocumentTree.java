package me.lumpchen.sledge.pdf.toolkit.editor;

import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import me.lumpchen.sledge.pdf.syntax.document.Catalog;
import me.lumpchen.sledge.pdf.syntax.document.DocumentInfo;
import me.lumpchen.sledge.pdf.syntax.document.PDFDocument;
import me.lumpchen.sledge.pdf.syntax.document.Page;

public class DocumentTree extends JTree {

	private static final long serialVersionUID = 1L;
	private DefaultTreeModel model;
	private TreeSelectionListener treeSelectionListener;
	private DefaultMutableTreeNode root;

	private PDFDocument doc;

	public DocumentTree(PDFDocument doc, PropertyTableModel tableModel, JTextArea textarea) {
		super();
		
		this.doc = doc;
		this.model = (DefaultTreeModel) this.getModel();
		this.treeSelectionListener = new DocObjectTreeSelectionListener(tableModel, textarea);
		this.addTreeSelectionListener(this.treeSelectionListener);
		
		this.initialize();
	}

	private void initialize() {
		DocumentInfo info = this.doc.getInfo();
		this.root = new DocObjectTreeNode("Root", info);
		this.model.setRoot(this.root);

		this.showPages(this.root);
	}

	private void showPages(DefaultMutableTreeNode parent) {
		Catalog catalog = this.doc.getCatalog();
		DefaultMutableTreeNode catalogNode = new DocObjectTreeNode("Catalog", catalog);
		parent.add(catalogNode);
		
		int count = this.doc.getRootPageTree().getCount();
		
		for (int i = 1; i <= count; i++) {
			Page page = this.doc.getPage(i);
			this.showPage(page, catalogNode);
		}
	}
	
	private void showPage(Page page, DefaultMutableTreeNode parent) {
		DefaultMutableTreeNode pageNode = new DocObjectTreeNode("Page", page);
		parent.add(pageNode);
	}
}
