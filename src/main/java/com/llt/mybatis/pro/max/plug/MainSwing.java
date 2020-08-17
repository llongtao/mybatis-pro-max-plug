package com.llt.mybatis.pro.max.plug;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import com.llt.mybatis.pro.max.plug.file.IntellijFileHandler;
import com.llt.mybatis.pro.max.plug.view.BaseConfigForm;
import com.llt.mybatis.pro.max.plug.view.BuildConfigTable;
import com.llt.mybatis.pro.max.plug.view.ConfigDataHolder;
import com.llt.mybatis.pro.max.plug.view.ModelConfigTable;
import com.llt.mybatishelper.core.data.DataSourceHolder;
import com.llt.mybatishelper.core.model.*;
import com.llt.mybatishelper.core.start.MyBatisHelperStarter;
import com.llt.mybatishelper.core.utils.CollectionUtils;
import com.llt.mybatishelper.core.utils.StringUtils;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author LILONGTAO
 */
public class MainSwing {

    private static Logger log = Logger.getInstance(ConfigDataHolder.class);

    private JPanel north = new JPanel();

    private JPanel center = new JPanel();

    private JPanel south = new JPanel();


    public JPanel initNorth() {

        //定义表单的标题部分，放置到IDEA会话框的顶部位置

        JLabel title = new JLabel("表单标题");
        //字体样式
        title.setFont(new Font("微软雅黑", Font.PLAIN, 26));
        //水平居中
        title.setHorizontalAlignment(SwingConstants.CENTER);
        //垂直居中
        title.setVerticalAlignment(SwingConstants.CENTER);
        north.add(title);

        return north;
    }

    public JPanel initCenter(Project project, AnActionEvent e) {

        //定义表单的主体部分，放置到IDEA会话框的中央位置

        //一个简单的3行2列的表格布局
        GridBagLayout gridLayout = new GridBagLayout();
        GridBagConstraints leftGrid = new GridBagConstraints();
        GridBagConstraints rightGrid = new GridBagConstraints();
        GridBagConstraints topGrid = new GridBagConstraints();
        GridBagConstraints midGrid = new GridBagConstraints();
        GridBagConstraints bottomGrid = new GridBagConstraints();
        leftGrid.gridx = 0;
        leftGrid.gridy = 0;
        leftGrid.weightx = 0.5;
        leftGrid.weighty = 0.5;
        leftGrid.gridwidth = 1;
        leftGrid.gridheight = 2;
        leftGrid.insets = JBUI.insets(0, 0, 5, 5);

        topGrid.gridx = 1;
        topGrid.gridy = 0;
        topGrid.weightx = 0.5;
        topGrid.weighty = 0.5;
        topGrid.gridwidth = 1;
        topGrid.gridheight = 1;
        topGrid.insets = JBUI.insets(0, 5, 0, 0);

        rightGrid.gridx = 1;
        rightGrid.gridy = 1;
        rightGrid.weightx = 0.5;
        rightGrid.weighty = 0.5;
        rightGrid.gridwidth = 1;
        rightGrid.gridheight = 1;
        rightGrid.insets = JBUI.insets(0, 5, 5, 0);

        midGrid.gridx = 0;
        midGrid.gridy = 3;
        midGrid.weightx = 0.5;
        midGrid.weighty = 0.5;
        midGrid.gridwidth = 2;
        midGrid.gridheight = 1;
        midGrid.insets = JBUI.insets(5, 0, 0, 0);


        bottomGrid.gridx = 0;
        bottomGrid.gridy = 4;
        bottomGrid.weightx = 1;
        bottomGrid.weighty = 2;
        bottomGrid.gridwidth = 2;
        bottomGrid.gridheight = 1;
        bottomGrid.insets = JBUI.insets(0, 0, 0, 0);
        center.setLayout(gridLayout);

        JPanel left = new BaseConfigForm().getForm();

        left.setPreferredSize(new Dimension(200, 150));
        center.add(left, leftGrid);



        JBScrollPane right = new JBScrollPane(new ModelConfigTable().getModelConfigTable());
        right.setPreferredSize(new Dimension(550, 150));
        center.add(new Label("公共属性配置"),topGrid);
        center.add(right, rightGrid);
        center.add(new Label("生成器配置"),midGrid);

        JBScrollPane jScrollPane = new JBScrollPane(new BuildConfigTable(project).getBuildConfigTable());
        jScrollPane.setPreferredSize(new Dimension(750, 200));

        center.add(jScrollPane, bottomGrid);

        return center;
    }

    public JPanel initSouth(Project project, AnActionEvent anActionEvent) {

        //定义表单的提交按钮，放置到IDEA会话框的底部位置

        JButton submit = new JButton("提交");
        //水平居中
        submit.setHorizontalAlignment(SwingConstants.CENTER);
        //垂直居中
        submit.setVerticalAlignment(SwingConstants.CENTER);
        south.add(submit);
        //按钮事件绑定
        submit.addActionListener(e -> {
            submit.setEnabled(false);
            try {
                Config config = ConfigDataHolder.getData();
                if (Objects.equals(config.getDropTable(),true)&& Objects.equals(config.getUseDb(),true)) {
                    int i = Messages.showYesNoDialog(project, "重建表会清除所有数据,确认执行?", "警告", "确认", "取消",Messages.getWarningIcon());
                    if (Messages.YES != i) {
                        submit.setEnabled(true);
                        return;
                    }
                }

                checkStartConfigAndConfigDataSource(config);
                BuildResult run = MyBatisHelperStarter.db(config.getDbType())
                        .withFileHandler(new IntellijFileHandler(project)).run(config);
                List<Log> logs = run.getLogs();
                StringBuilder stringBuilder = new StringBuilder();
                logs.forEach(item->stringBuilder.append(item).append("\r\n"));

                if (run.isSucceed()) {
                    if (run.getTotal() == 0) {
                        Messages.showMessageDialog("未找到可生成的实体类,请在实体类javaDoc注释增加.auto标识", "提示", Messages.getInformationIcon());
                    }else {
                        Messages.showMessageDialog("已生成"+run.getTotal()+"条记录,请查看指定目录下base文件夹\r\n\n"+stringBuilder, "成功", Messages.getInformationIcon());
                    }
                }else {
                    log.warn("生成异常", run.getE());
                    Messages.showMessageDialog(run.getE().getMessage()+"\r\n"+stringBuilder, "错误", Messages.getErrorIcon());
                }

            } catch (Exception ex) {
                log.warn("生成异常", ex);
                Messages.showMessageDialog(ex.getMessage(), "错误", Messages.getErrorIcon());

            }
            DataSourceHolder.clear();
            submit.setEnabled(true);

            log.info("SUCCESS");

        });
        return south;
    }

    private void checkStartConfigAndConfigDataSource(Config config) {
        boolean useDb = Objects.equals(config.getUseDb(), true);
        List<BuildConfig> buildConfigList = config.getBuildConfigList();
        List<EntityField> baseEntityFieldList = config.getBaseEntityFieldList();
        if (CollectionUtils.isEmpty(buildConfigList)) {
            throw new IllegalArgumentException("配置信息不能为空");
        }
        for (BuildConfig buildConfig1 : buildConfigList) {
            if (StringUtils.isEmpty(buildConfig1.getMapperFolder()) ||
                    StringUtils.isEmpty(buildConfig1.getEntityFolder()) ||
                    StringUtils.isEmpty(buildConfig1.getXmlFolder())
            ) {
                throw new IllegalArgumentException("配置文件夹信息不能为空");
            }
        }
        for (EntityField entityField : baseEntityFieldList) {
            if (StringUtils.isEmpty(entityField.getColumnName())) {
                throw new IllegalArgumentException("基类属性列名不能为空");
            }
            if (StringUtils.isEmpty(entityField.getType())) {
                throw new IllegalArgumentException("基类属性类型不能为空");
            }
        }



        if (useDb) {
            if (StringUtils.isEmpty(config.getBaseDbUrl()) ||
                    StringUtils.isEmpty(config.getBaseDbPassword()) ||
                    StringUtils.isEmpty(config.getBaseDbUsername()) ||
                    StringUtils.isEmpty(config.getDbType())
            ) {
                throw new IllegalArgumentException("若生成表结构则数据库信息必填");
            }
            boolean matches = Pattern.matches("^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5]):([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-5]{2}[0-3][0-5])$"
                    , config.getBaseDbUrl());
            if (!matches) {
                throw new RuntimeException("数据库地址应以 ip:port 的形式填写");
            }

        }
    }
}
