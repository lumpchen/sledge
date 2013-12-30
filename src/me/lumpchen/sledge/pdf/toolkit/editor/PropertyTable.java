package me.lumpchen.sledge.pdf.toolkit.editor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.toolkit.editor.PropertyTableModel.RowClass;

public class PropertyTable extends JTable {

	private static final long serialVersionUID = 1L;

	public PropertyTable(PropertyTableModel model) {
		super(model);

		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		this.getSelectionModel().addListSelectionListener(new PTListSelectionListener());

		this.addMouseListener(new PTMouseListener());
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
				
				if (rowClass.type != null) {
					if (rowClass.type == PObject.Type.Array) {
						 
					}
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
