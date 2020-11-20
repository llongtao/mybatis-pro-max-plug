package com.llt.mybatis.pro.max.plug;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.vfs.VcsFileSystem;
import com.intellij.openapi.vcs.vfs.VcsVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import com.llt.mybatis.pro.max.plug.view.ConfigDataHolder;

/**
 * @author LILONGTAO
 */
public class MybatisProMaxPlusAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            Messages.showMessageDialog("项目暂未加载请稍后再试", "提示", Messages.getInformationIcon());
            return;
        }
        ConfigDataHolder.setContext(e.getProject());
        MainDialog mainDialog = new MainDialog(e.getProject(),e);
        //是否允许用户通过拖拽的方式扩大或缩小你的表单框
        mainDialog.setResizable(true);
        mainDialog.show();

    }
}
