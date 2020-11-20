package com.llt.mybatis.pro.max.plug;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;

/**
 * @author LILONGTAO
 */
public class MainDialog extends DialogWrapper {

    private Project project;
    private AnActionEvent e;

    private MainSwing mainSwing = new MainSwing();

    public MainDialog(Project project, AnActionEvent e) {
        super(true);

        //设置会话框标题
        setTitle("Mybatis生成器("+project.getName()+")");
        setAutoAdjustable(true);

        //获取到当前项目
        this.project = project;
        this.e = e;

        //触发一下init方法，否则swing样式将无法展示在会话框
        init();
    }


    /**
     * 特别说明：不需要展示SouthPanel要重写返回null，否则IDEA将展示默认的"Cancel"和"OK"按钮
     *
     */
    @Override
    protected JComponent createSouthPanel() {

        return mainSwing.initSouth(project,e);
    }

    @Override
    protected JComponent createCenterPanel() {
        //定义表单的主题，放置到IDEA会话框的中央位置
        return mainSwing.initCenter(project,e);
    }
}
