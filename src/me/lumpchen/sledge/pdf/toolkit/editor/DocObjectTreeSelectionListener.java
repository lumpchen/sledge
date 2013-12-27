package me.lumpchen.sledge.pdf.toolkit.editor;

import javax.swing.JTextArea;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

public class DocObjectTreeSelectionListener implements TreeSelectionListener {

	private PropertyTableModel tableModel;
	private JTextArea textarea;
	
	public DocObjectTreeSelectionListener(PropertyTableModel tableModel, JTextArea textarea) { 
		this.tableModel = tableModel;
		this.textarea = textarea;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		TreePath path = e.getPath();
		
		Object obj = path.getLastPathComponent();
		if (null != obj) {
			if (obj instanceof DocObjectTreeNode) {
				DocObjectTreeNode node = (DocObjectTreeNode) obj;
				this.tableModel.removeAllRows();
				this.tableModel.addDocObject(node.getDocObject());
				
				this.textarea.removeAll();
				this.textarea.setText(node.getDocObject().toString());
			} else {
				this.textarea.removeAll();
				this.textarea.setText(obj.toString());				
			}
		}
	}
}
