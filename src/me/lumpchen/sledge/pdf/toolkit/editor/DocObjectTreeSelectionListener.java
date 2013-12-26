package me.lumpchen.sledge.pdf.toolkit.editor;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

public class DocObjectTreeSelectionListener implements TreeSelectionListener {

	private PropertyTableModel tableModel;
	
	public DocObjectTreeSelectionListener(PropertyTableModel tableModel) { 
		this.tableModel = tableModel;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		TreePath path = e.getPath();
		
		Object obj = path.getLastPathComponent();
		if (null != obj) {
			if (obj instanceof DocObjectTreeNode) {
				DocObjectTreeNode node = (DocObjectTreeNode) obj;
				this.tableModel.addDocObject(node.getDocObject());
			}
			System.out.println(obj.getClass().getName());
		}
	}

}
