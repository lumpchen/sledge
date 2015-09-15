package me.lumpchen.sledge.pdf.toolkit.editor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.lang.PObject;

public class XRefTable extends JTable {

	private static final long serialVersionUID = 1L;
	
	private PropertyTableModel propTableModel;
	private JTextArea textarea;
	
	private int selRow = -1;
	private List<Integer> historyList;
	private int historyCursor = -1;
	private JButton prevBtn;
	private JButton nextBtn;

	public XRefTable(XRefTableModel model, PropertyTableModel propTableModel, 
			JTextArea textarea) {
		super(model);
		
		this.propTableModel = propTableModel;
		this.textarea = textarea;
		
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		this.getSelectionModel().addListSelectionListener(new XTListSelectionListener());
//		this.addMouseListener(new XTMouseListener());
		
		this.historyList = new ArrayList<Integer>();
	}
	
	public void setSelectedRef(IndirectRef ref) {
		XRefTableModel model = (XRefTableModel) this.getModel();
		int row = model.getRowIndex(ref);
		this.updateRow(row, false);
	}
	
	public void clearAllRows() {
		XRefTableModel model = (XRefTableModel) this.getModel();
		int n = this.getRowCount();
		for (int i = n - 1; i >= 0; i--) {
			model.removeRow(i);
		}
		this.propTableModel.removeAllRows();
		this.prevBtn.setEnabled(false);
		this.nextBtn.setEnabled(false);
		this.historyList.clear();
	}
	
	private void updateRow(int row, boolean historyBack) {
		if (row >= 0) {
			if (this.selRow == row) {
				return;
			}
			
			if (!historyBack) {
				this.historyList.add(row);
				this.historyCursor = this.historyList.size() - 1;
				this.updateHistoryButton();
			}
			
			this.selRow = row;
			
			XRefTableModel model = (XRefTableModel) getModel();
			PObject selObj = model.getRowObject(row);
			this.updatePropTable(selObj);
			this.updateTextArea(row);
			
			this.setRowSelectionInterval(row, row);
		}
	}
	
	private void updateTextArea(final int row) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				XRefTableModel model = (XRefTableModel) getModel();
				String text = model.getRowString(row);
				if (text.length() > 20480) {
					text = text.substring(0, 1024);
					text += "\n......";
				}
				textarea.setText(text);
			}
		});
	}
	
	private void updatePropTable(final PObject selObj) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if (selObj == null) {
					propTableModel.removeAllRows();	
				} else {
					propTableModel.updateSelectedObject(selObj);					
				}
			}
		});
	}
	
	private void updateHistoryButton() {
		this.prevBtn.setEnabled(this.hasPrev());
		this.nextBtn.setEnabled(this.hasNext());
	}
	
	public void setPrevButton(JButton prevBtn) {
		this.prevBtn = prevBtn;
	}
	
	public void setNextBtn(JButton nextBtn) {
		this.nextBtn = nextBtn;
	}
	
	public boolean hasPrev() {
		return this.historyCursor > 0;
	}
	
	public boolean hasNext() {
		return this.historyCursor < this.historyList.size() - 1;
	}
	
	public void gotoPrev() {
		if (!this.hasPrev()) {
			return;
		}

		int row = this.historyList.get(--this.historyCursor);
		this.updateRow(row, true);
		this.updateHistoryButton();
	}
	
	public void gotoNext() {
		if (!this.hasNext()) {
			return;
		}
		
		int row = this.historyList.get(++this.historyCursor);
		this.updateRow(row, true);
		this.updateHistoryButton();
	}
	
	class XTListSelectionListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			int row = getSelectedRow();
			if (row < 0) {
				return;
			}
			updateRow(row, false);
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
				target.updateTextArea(row);
			}
		}
	}
}
