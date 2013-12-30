package me.lumpchen.sledge.pdf.toolkit.editor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;

public class XRefTable extends JTable {

	private static final long serialVersionUID = 1L;
	
	private PropertyTableModel propTableModel;
	private JTextArea textarea;

	public XRefTable(XRefTableModel model, PropertyTableModel propTableModel, 
			JTextArea textarea) {
		super(model);
		
		this.propTableModel = propTableModel;
		this.textarea = textarea;
		
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		this.getSelectionModel().addListSelectionListener(new XTListSelectionListener());
		this.addMouseListener(new XTMouseListener());
	}
	
	private void updateTextArea(final String text) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				textarea.setText(text);
			}
		});
	}
	
	private void updatePropTable(final IndirectObject selObj) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				propTableModel.updateIndirectObject(selObj);
			}
		});
	}
	
	class XTListSelectionListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			int row = getSelectedRow();
			if (row < 0) {
				return;
			}
			XRefTableModel model = (XRefTableModel) getModel();
			IndirectObject selObj = model.getRowObject(row);
			if (selObj != null) {
				updatePropTable(selObj);
			}
		}
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
