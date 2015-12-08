package com.tsuru2d.engine.desktop;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class UploadDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private File mChosen;
    private String mUrl;
    private String mName;
    private String mAuthor;
    private FileBrowser mParent;

    /**
     * Create the dialog.
     */
    public UploadDialog(FileBrowser parent) {
        mParent = parent;
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        textField = new JTextField();
        textField.setBounds(48, 57, 179, 19);
        contentPanel.add(textField);
        textField.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setBounds(48, 115, 179, 19);
        contentPanel.add(textField_1);
        textField_1.setColumns(10);

        textField_2 = new JTextField();
        textField_2.setBounds(48, 150, 179, 19);
        textField_2.setText("Game Name");
        contentPanel.add(textField_2);
        textField_2.setColumns(10);

        textField_3 = new JTextField();
        textField_3.setBounds(48, 180, 179, 19);
        textField_3.setText("Author Name");
        contentPanel.add(textField_3);
        textField_3.setColumns(10);

        JLabel lblFile = new JLabel("File");
        lblFile.setBounds(56, 30, 70, 15);
        contentPanel.add(lblFile);

        JButton btnChooseFile = new JButton("Choose File");
        btnChooseFile.setBounds(267, 54, 117, 25);
        btnChooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File chosen = showFileChooser();
                if(chosen != null) {
                    mChosen = chosen;
                    textField.setText(mChosen.getAbsolutePath());
                }
            }
        });
        contentPanel.add(btnChooseFile);

        JLabel lblUrl = new JLabel("Image URL");
        lblUrl.setBounds(58, 88, 128, 15);
        contentPanel.add(lblUrl);
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mUrl = textField_1.getText();
                        mName = textField_2.getText();
                        mAuthor = textField_3.getText();
                        mParent.upload(getOption());
                        hideWindow();
                    }
                });
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mParent.upload(null);
                        hideWindow();
                    }
                });
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }

    public void hideWindow() {
        this.setVisible(false);
    }

    public File showFileChooser() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("game files", "vngame");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    public Options getOption() {
        return new Options(mChosen, mUrl, mName, mAuthor);
    }

    public class Options {
        public File mFile;
        public String mUrl;
        public String mGameName;
        public String mAuthor;
        public Options(File file, String url, String gameName, String author) {
            mFile = file;
            mUrl = url;
            mGameName = gameName;
            mAuthor = author;
        }
    }
}


