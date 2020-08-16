package com.llt.mybatis.pro.max.plug.view;

import com.alibaba.fastjson.JSON;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.llt.mybatishelper.core.model.BuildConfig;
import com.llt.mybatishelper.core.model.Config;
import com.llt.mybatishelper.core.model.EntityField;
import com.llt.mybatishelper.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * @author LILONGTAO
 */
public class ConfigDataHolder  {
    private static Logger log = Logger.getInstance(ConfigDataHolder.class);
    private static Config config ;
    private static Project project ;

    public static void updateBuildConfig(Vector<Vector> dataVector) {

        List<BuildConfig> buildConfigList = new ArrayList<>();
        for (Vector vector : dataVector) {
            BuildConfig buildConfig =
                    new BuildConfig(
                            (String) vector.get(1),
                            (String) vector.get(2),
                            (String) vector.get(3),
                            null,
                            (String) vector.get(5),
                            null,
                            null,
                            !(Boolean) vector.get(4),
                            !(Boolean) vector.get(0)
                    );

            buildConfigList.add(buildConfig);
        }
        config.setBuildConfigList(buildConfigList);
        save();
    }

    public static void updateModelConfig(Vector<Vector> dataVector) {

        List<EntityField> entityFieldList = new ArrayList<>();
        for (Vector vector : dataVector) {
            EntityField entityField =
                    new EntityField(
                            (String) vector.get(0),
                            (String) vector.get(1),
                            (String) vector.get(2),
                            null,
                            null,
                            null,
                            (String) vector.get(3),
                            (String) vector.get(4),
                            !(Boolean) vector.get(5),
                            (String) vector.get(6),
                            null
                    );

            entityFieldList.add(entityField);
        }
        config.setBaseEntityFieldList(entityFieldList);
        save();
    }

    public static void updateBaseConfig(String dbType, String baseDbUrl, String baseDbUsername, String baseDbPassword, Boolean useDb, boolean dropTable){
        config.setDbType(dbType);
        config.setBaseDbUrl(baseDbUrl);
        config.setBaseDbUsername(baseDbUsername);
        config.setBaseDbPassword(baseDbPassword);
        config.setUseDb(useDb);
        config.setDropTable(dropTable);
        save();
    }



    public static Config save() {

        String s = JSON.toJSONString(config);
        log.info("saveConfig:"+s);
        PropertiesComponent.getInstance().setValue(getConfigName(project),s);
        System.out.println(s);
        return config;
    }

    public static Config getData() {
        return config;
    }

    public static void setContext(Project project) {
        ConfigDataHolder.project = project;
        loadConfig(getConfigName(project) );
    }
    private static String getConfigName(Project project){
        if (project == null) {
            return "mybatis-pro-max-config";
        }
        return "mybatis-pro-max-config:"+project.getName();
    }

    private static void loadConfig(String name){
        try {
            String configStr = PropertiesComponent.getInstance().getValue(name, StringUtils.EMPTY);
            if (!StringUtils.isEmpty(configStr)) {
                ConfigDataHolder.config  = JSON.parseObject(configStr, Config.class);
            }
        } catch (Exception e) {
            log.warn("找不到配置文件", e);
        }

        if (config == null) {
            config = new Config();
            config.setUseDb(true);
            config.setBaseDbUsername("root");
            config.setDbType("mysql");
            config.setBaseDbUrl("127.0.0.1:3306");
            config.setBaseEntityFieldList(Collections.singletonList(new EntityField("id","id","Integer",null,null,null,null,null,false,"主键",null)));
            config.setBuildConfigList(Collections.singletonList(new BuildConfig(StringUtils.EMPTY,StringUtils.EMPTY,StringUtils.EMPTY,StringUtils.EMPTY,StringUtils.EMPTY,StringUtils.EMPTY,StringUtils.EMPTY,true,false)));
        }

        log.info("loadData:"+config );
    }
}
