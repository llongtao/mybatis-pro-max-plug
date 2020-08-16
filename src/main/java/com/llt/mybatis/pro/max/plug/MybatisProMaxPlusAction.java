package com.llt.mybatis.pro.max.plug;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
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
        ConfigDataHolder.setContext(e.getProject());
        MainDialog mainDialog = new MainDialog(e.getProject(),e);
        //是否允许用户通过拖拽的方式扩大或缩小你的表单框，我这里定义为true，表示允许
        mainDialog.setResizable(false);
        mainDialog.show();

    }
}
