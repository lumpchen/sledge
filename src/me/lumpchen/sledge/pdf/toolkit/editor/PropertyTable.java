package me.lumpchen.sledge.pdf.toolkit.editor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.toolkit.editor.PropertyTableModel.RowClass;

public class PropertyTable extends JTable {

	private static final long serialVersionUID = 1L;
	
	private XRefTable xRefTable;

	public PropertyTable(PropertyTableModel model, XRefTable xRefTable) {
		super(model);

		this.xRefTable = xRefTable;
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		this.getSelectionModel().addListSelectionListener(new PTListSelectionListener());

		this.addMouseListener(new PTMouseListener());
	}

	private void updateSelectedRow(final PObject selObj) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				PropertyTableModel model = (PropertyTableModel) getModel();
				model.updateSelectedObject(selObj);
			}
		});
	}
	
	private void updateXRefTable(final IndirectRef ref) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				xRefTable.setSelectedRef(ref);
			}
		});
	}
	
	class PTMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				JTable target = (JTable) e.getSource();
				int row = target.getSelectedRow();
				if (row < 0) {
					return;
				}
				PropertyTableModel model = (PropertyTableModel) target.getModel();
				RowClass rowClass = model.getRowClass(row);
				
				if (rowClass.type != null && rowClass.type == PObject.Type.IndirectRef) {
					updateXRefTable((IndirectRef) rowClass.value);
				} else {
					updateSelectedRow(rowClass.value);					
				}
			}
		}
	}

	class PTListSelectionListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {

		}
	}
}
