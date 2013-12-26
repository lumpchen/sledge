package me.lumpchen.sledge.pdf.toolkit.editor;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

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

	public DocumentTree(PDFDocument doc, PropertyTableModel tableModel) {
		super();
		
		this.doc = doc;
		this.model = (DefaultTreeModel) this.getModel();
		this.treeSelectionListener = new DocObjectTreeSelectionListener(tableModel);
		this.addTreeSelectionListener(this.treeSelectionListener);
		
		this.initialize();
	}

	private void initialize() {
		this.root = new DefaultMutableTreeNode("Root");
		this.model.setRoot(this.root);

		this.showDocumentInfo();
		this.showPages();
	}

	private void showDocumentInfo() {
		DocumentInfo info = this.doc.getInfo();

		DefaultMutableTreeNode node = new DocObjectTreeNode(info);
		this.model.insertNodeInto(node, this.root, 0);
	}

	private void showPages() {
		Catalog catalog = this.doc.getCatalog();
		DefaultMutableTreeNode catalogNode = new DefaultMutableTreeNode("Catalog");
		
		this.model.insertNodeInto(catalogNode, this.root, this.root.getChildCount());
		
		DefaultMutableTreeNode node = new DocObjectTreeNode(catalog);
		this.model.insertNodeInto(node, catalogNode, 0);
		
		int count = this.doc.getRootPageTree().getCount();
		
		for (int i = 1; i <= count; i++) {
			Page page = this.doc.getPage(i);
			this.showPage(page, catalogNode);
		}
	}
	
	private void showPage(Page page, DefaultMutableTreeNode parent) {
		DefaultMutableTreeNode node = new DocObjectTreeNode(page);
		this.model.insertNodeInto(node, (MutableTreeNode) parent, parent.getChildCount());
	}
}
