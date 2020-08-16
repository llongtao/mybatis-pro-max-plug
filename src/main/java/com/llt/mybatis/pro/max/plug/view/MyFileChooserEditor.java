package com.llt.mybatis.pro.max.plug.view;


import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author LILONGTAO
 */
public class MyFileChooserEditor extends AbstractCellEditor implements TableCellEditor {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6546334664166791132L;


    Map<Integer, JPanel> jPanelMap = new HashMap<>();


    private String path;

    private Project project;

    private JPanel panel;

    private JButton button;

    private int row;

    private int column;

    private Vector<Vector> dataVector;

    public MyFileChooserEditor(Vector<Vector> dataVector, Project project, int row, int column) {
        this.row = row;
        this.column = column;
        this.dataVector = dataVector;
        this.path = dataVector.get(row).get(column).toString();
        this.project = project;
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        initButton();
        panel.add(button, BorderLayout.CENTER);
    }

    private void initButton() {

        button = new JButton();
        button.addActionListener(e -> {

            System.out.println(e);
            FileChooserDescriptor chooserDescriptor = new FileChooserDescriptor(false, true, false, false, false, false);
            String path = dataVector.get(row).get(column).toString();
            VirtualFile file = StringUtils.isEmpty(path) ? null : LocalFileSystem.getInstance().findFileByIoFile(new File(path));
            VirtualFile virtualFile = FileChooser.chooseFile(chooserDescriptor, project, file);
            if (virtualFile != null && virtualFile.isDirectory()) {
                setPath(virtualFile.getPath());
                System.out.println("文件夹:" + this.path);
            }
            //stopped!!!!
            fireEditingStopped();

        });


    }


    private void setPath(String path) {

        this.path = path;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return path;
    }


}
