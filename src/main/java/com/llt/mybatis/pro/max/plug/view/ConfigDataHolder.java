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
import java.util.function.Consumer;

/**
 * @author LILONGTAO
 */
public class ConfigDataHolder  {
    private static Logger log = Logger.getInstance(ConfigDataHolder.class);
    private static Config config ;
    private static Project project ;
    private static List<Consumer<Config>> dataChangeConsumerList = new ArrayList<>();

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
                            (String) vector.get(2),
                            null,
                            null,
                            false,
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
        String configName = getConfigName(project);
        if (configName != null) {
            PropertiesComponent.getInstance().setValue(configName,s);
        }

        System.out.println(s);
        return config;
    }

    public static Config getData() {
        dataChangeConsumerList.forEach(consumer -> consumer.accept(config));
        return config;
    }

    public static void registerDataChangeConsumer(Consumer<Config> configConsumer) {
        dataChangeConsumerList.add(configConsumer);
    }

    public static void setContext(Project project) {
        ConfigDataHolder.project = project;
        String configName = getConfigName(project);
        loadConfig( configName);
    }
    private static String getConfigName(Project project){
        if (project == null) {
            return null;
        }
        return "mybatis-pro-max-config:"+project.getName();
    }

    private static void loadConfig(String name){
        if (name != null) {
            try {
                String configStr = PropertiesComponent.getInstance().getValue(name, StringUtils.EMPTY);
                if (!StringUtils.isEmpty(configStr)) {
                    ConfigDataHolder.config  = JSON.parseObject(configStr, Config.class);
                }
            } catch (Exception e) {
                log.warn("找不到配置文件", e);
            }
        }


        if (config == null) {
            config = new Config();
            config.setUseDb(true);
            config.setBaseDbUsername("root");
            config.setDbType("mysql");
            config.setBaseDbUrl("127.0.0.1:3306");
            config.setBaseEntityFieldList(Collections.singletonList(new EntityField("id","id","Integer",null,null,null,false,null,null,false,"主键",null)));
            config.setBuildConfigList(Collections.singletonList(new BuildConfig(StringUtils.EMPTY,StringUtils.EMPTY,StringUtils.EMPTY,StringUtils.EMPTY,StringUtils.EMPTY,StringUtils.EMPTY,StringUtils.EMPTY,true,false)));
        }

        log.info("loadData:"+config );
    }
}
