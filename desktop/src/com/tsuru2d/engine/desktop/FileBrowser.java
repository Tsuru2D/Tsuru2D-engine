package com.tsuru2d.engine.desktop;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FileBrowser extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTable table_1;
	private MTableModel mLocalModel;
	private MTableModel mCloudModel;
	private FileModel mFileModel;
	private ArrayList<FileModel.GameInfo> mLocalEntries;
	private ArrayList<FileModel.GameInfo> mCloudEntries;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileBrowser frame = new FileBrowser();
					frame.populateLocalTable();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FileBrowser() {
		mFileModel = FileModel.getInstance();
		mLocalModel = new MTableModel();
		mCloudModel = new MTableModel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 673, 501);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane);

		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				populateLocalTable();
				populateCloudTable();
			}
		});

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Local", null, panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.WEST);

		JButton btnNewButton_2 = new JButton("Open");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Open Pressed at " + getLocalSelection());
				if(getSelection(table_1) >= 0){
					mFileModel.openFile(mLocalEntries.get(getSelection(table_1)));
					hideWindow();
				}
			}
		});
		panel_3.add(btnNewButton_2);

		JPanel panel_5 = new JPanel();
		panel_1.add(panel_5, BorderLayout.CENTER);
		panel_5.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_5.add(scrollPane_1, BorderLayout.CENTER);

		table_1 = new JTable(mLocalModel){				//local
			public Class getColumnClass(int column) {
				return mCloudModel.getValueAt(0, column).getClass();
			}
		};
		table_1.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());
		table_1.setShowVerticalLines(true);
		table_1.setShowHorizontalLines(true);
		table_1.setRowHeight(50);
		table_1.setSelectionModel(new ForcedListSelectionModel());
		scrollPane_1.setViewportView(table_1);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Cloud", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.WEST);

		JButton btnNewButton_1 = new JButton("Download");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getSelection(table) >= 0)
					mFileModel.downloadFile(mCloudEntries.get(getSelection(table)));
			}
		});
		panel_2.add(btnNewButton_1);

		JButton btnNewButton = new JButton("Upload");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showDialog();
			}
		});
		panel.add(btnNewButton, BorderLayout.SOUTH);

		JPanel panel_4 = new JPanel();
		panel.add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel_4.add(scrollPane, BorderLayout.CENTER);

		table = new JTable(mCloudModel){							//cloud
			public Class getColumnClass(int column) {
				return mCloudModel.getValueAt(0, column).getClass();
			}
		};
		table.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(true);
		table.setSelectionModel(new ForcedListSelectionModel());
		table.setRowHeight(50);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		scrollPane.setViewportView(table);
	}

	public void showDialog() {
		try {
			UploadDialog dialog = new UploadDialog(this);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void upload(UploadDialog.Options options) {
		if(options != null) {
			mFileModel.uploadFile(options.mFile, options.mUrl, options.mAuthor, options.mGameName);
		}
	}

	public int getSelection(JTable table) {
		for(int i = 0; i < table.getRowCount(); i++) {
			if(table.getSelectionModel().isSelectedIndex(i))
				return i;
		}
		return -1;
	}

	public void hideWindow() {
		this.setVisible(false);
	}

	public void populateLocalTable() {
		mLocalEntries = mFileModel.downloadedList();
		mLocalModel.setRowContents(mLocalEntries);
		mLocalModel.fireTableDataChanged();
	}

	public void populateCloudTable() {
		mCloudEntries = mFileModel.cloudList();
		mCloudModel.setRowContents(mCloudEntries);
		mCloudModel.fireTableDataChanged();
	}

	private class MTableModel extends AbstractTableModel {
		private String[] clomunNames = {"image", "Name", "Author"};
		private ArrayList<FileModel.GameInfo> mGameInfos;

		@Override
		public String getColumnName(int index) {
			return clomunNames[index];
		}

		@Override
		public int getRowCount() {
			if(mGameInfos != null)
				return mGameInfos.size();
			return 1;
		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public Object getValueAt(int row, int column){
			try{
				if(mGameInfos == null) return "null";
				FileModel.GameInfo current = mGameInfos.get(row);
				switch(column) {
				case 0: return current.mImageIcon;
				case 1: return current.mName;
				case 2: return current.mAuthor;
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			return "null";
		}

		public void setRowContents(ArrayList<FileModel.GameInfo> gameInfos) {
			mGameInfos = gameInfos;
		}
	}

	private class ForcedListSelectionModel extends DefaultListSelectionModel {

		public ForcedListSelectionModel () {
			setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}

		@Override
		public void clearSelection() {
		}

		@Override
		public void removeSelectionInterval(int index0, int index1) {
		}

	}

	private class ImageRenderer extends DefaultTableCellRenderer {
		JLabel lbl = new JLabel();

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
													   boolean hasFocus, int row, int column) {
			lbl.setIcon((ImageIcon)value);
			return lbl;
		}
	}
}
