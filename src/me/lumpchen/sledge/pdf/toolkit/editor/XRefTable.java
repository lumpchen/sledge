package me.lumpchen.sledge.pdf.toolkit.editor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class XRefTable extends JTable {

	private static final long serialVersionUID = 1L;
	
	private JTextArea textarea;

	public XRefTable(XRefTableModel model, JTextArea textarea) {
		super(model);
		
		this.textarea = textarea;
		
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		this.addMouseListener(new XTMouseListener());
	}
	
	private void updateTextArea(final String text) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				textarea.setText(text);
			}});
	}
	
	private class XTMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
				XRefTable target = (XRefTable) e.getSource();
				int row = target.getSelectedRow();
				if (row < 0) {
					return;
				}
				
				XRefTableModel model = (XRefTableModel) target.getModel();
				String text = model.getRowString(row);
				target.updateTextArea(text);
			}
		}
	}
}
