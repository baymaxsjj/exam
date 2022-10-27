package com.baymax.exam.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author ：Baymax
 * @date ：Created in 2022/6/22 16:57
 * @description：快速生成代码
 * @modified By：
 * @version: 1.0
 */
public class FastAutoGeneratorTest {

    /**
     * 数据源配置
     */
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://localhost:3306/wk_exam?characterEncoding=utf-8&serverTimezone=Asia/Shanghai", "root", "123456");
    private static String author="baymax";
    private static String myPackage="com.baymax.exam.center";
    private static String path=System.getProperty("user.dir");;

    /**
     * 执行 run
     */
    public static void main(String[] args) throws SQLException, IOException {
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                // 全局配置
                .globalConfig((scanner, builder) -> {
                    builder
                            .author(author)
                            .enableSpringdoc()
                            .outputDir(path+"/fte-mbg/src/main/resources");
                })
                // 包配置
                .packageConfig((scanner, builder) -> builder.parent(myPackage).entity("model"))
//                .packageConfig((scanner, builder) -> builder.parent("com.baymax.mgo").entity("model").serviceImpl("service").controller("controller"))
                // 策略配置
                .strategyConfig((scanner, builder) -> builder.
                        addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
                        .addTablePrefix("es_", "ec_","eq_","ee_")
                        .controllerBuilder()
                        .enableFileOverride()
                        .enableRestStyle()
                        .enableHyphenStyle()
                        .entityBuilder().enableLombok().build())
                .execute();
    }
    // 处理 all 情况
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }
}
