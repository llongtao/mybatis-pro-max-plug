package com.llt.mybatis.pro.max.plug.view;

import com.intellij.openapi.project.Project;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.AbstractTableCellEditor;
import com.llt.mybatishelper.core.model.BuildConfig;
import com.llt.mybatishelper.core.model.Config;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.Vector;

/**
 * @author LILONGTAO
 */
public class BuildConfigTable  {

    private JBTable buildConfigTable;

    private DefaultTableModel defaultTableModel;

    public BuildConfigTable(Project project) {
        Vector<String> columnNamesVector = new Vector<>();
        columnNamesVector.add("");
        columnNamesVector.add("实体类文件夹");
        columnNamesVector.add("mapper映射文件夹");
        columnNamesVector.add("xml文件夹");
        columnNamesVector.add("继承公共");
        columnNamesVector.add("数据库");
        columnNamesVector.add("添加");
        columnNamesVector.add("删除");
        Vector<Vector> dataVector = new Vector<>();

        Config config = ConfigDataHolder.getData();
        java.util.List<BuildConfig> buildConfigList = config.getBuildConfigList();


        if (buildConfigList != null) {
            buildConfigList.forEach(item->{
                Vector<Object> vector = new Vector<>();
                vector.add(!item.getDisable());
                vector.add(item.getEntityFolder());
                vector.add(item.getMapperFolder());
                vector.add(item.getXmlFolder());
                vector.add(!item.getIgnoreBaseField());
                vector.add(item.getDb());
                vector.add("");
                vector.add("");
                dataVector.add(vector);
            });
        }else {
            dataVector.add(getNewRowData());
        }


        defaultTableModel = new DefaultTableModel(dataVector, columnNamesVector){
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4 || columnIndex == 0) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }


        };


        defaultTableModel.addTableModelListener(e -> {
            if (e.getSource() instanceof DefaultTableModel) {
                ConfigDataHolder.updateBuildConfig(dataVector);

            }

        });


        buildConfigTable = new JBTable(defaultTableModel){
            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                if(column == 1 || column == 2||column == 3){
                    return new MyFileChooserEditor(dataVector,project,row,column);
                }
                return super.getCellEditor(row, column);
            }
        };
        //表格
        buildConfigTable.setEnableAntialiasing(true);
        buildConfigTable.setExpandableItemsEnabled(true);
        buildConfigTable.setStriped(true);




        buildConfigTable.getColumnModel().getColumn(7).setCellEditor(new AbstractTableCellEditor() {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

                JButton del = new JButton("删除");
                del.addActionListener(e -> {
                    System.out.println("del"+table.getSelectedRow());
                    System.out.println(dataVector);
                    if (defaultTableModel.getRowCount() > 1) {
                        //stopped!!!!
                        fireEditingStopped();
                        defaultTableModel.removeRow(table.getSelectedRow());

                    }
                    System.out.println(dataVector);
                });

                return del;
            }

            @Override
            public Object getCellEditorValue() {
                return "";
            }
        });
        buildConfigTable.getColumnModel().getColumn(6).setCellEditor(new AbstractTableCellEditor() {

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

                JButton add = new JButton("添加");
                add.addActionListener(e -> {

                    System.out.println("add"+dataVector.size());
                    System.out.println(dataVector);

                    defaultTableModel.insertRow(defaultTableModel.getRowCount(),getNewRowData());
                    System.out.println(dataVector);
                    //stopped!!!!
                    fireEditingStopped();
                });
                return add;
            }

            @Override
            public Object getCellEditorValue() {
                return "";
            }
        });
        buildConfigTable.getColumnModel().getColumn(7).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton del = new JButton("删除");
            del.addActionListener(e -> {
                System.out.println("del"+table.getSelectedRow());
                System.out.println(dataVector);
                if (defaultTableModel.getRowCount() > 1) {
                    defaultTableModel.removeRow(table.getSelectedRow());
                }
                System.out.println(dataVector);
            });

            return del;
        });

        buildConfigTable.getColumnModel().getColumn(6).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> new JButton("添加"));

        buildConfigTable.getColumnModel().getColumn(7).setPreferredWidth(30);
        buildConfigTable.getColumnModel().getColumn(6).setPreferredWidth(30);
        buildConfigTable.getColumnModel().getColumn(4).setPreferredWidth(40);
        buildConfigTable.getColumnModel().getColumn(3).setCellRenderer(new MyFileChooserRenderer());
        buildConfigTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        buildConfigTable.getColumnModel().getColumn(2).setCellRenderer(new MyFileChooserRenderer());
        buildConfigTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        buildConfigTable.getColumnModel().getColumn(1).setCellRenderer(new MyFileChooserRenderer());
        buildConfigTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        buildConfigTable.getColumnModel().getColumn(0).setPreferredWidth(30);
    }

    @NotNull
    private Vector<Object> getNewRowData() {
        Vector<Object> vector = new Vector<>();
        vector.add(true);
        vector.add("");
        vector.add("");
        vector.add("");
        vector.add(true);
        vector.add("");
        vector.add("");
        vector.add("");

        return vector;
    }

    public JBTable getBuildConfigTable() {
        return buildConfigTable;
    }
}
