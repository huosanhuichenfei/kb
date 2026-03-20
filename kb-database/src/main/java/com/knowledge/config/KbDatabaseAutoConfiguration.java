package com.knowledge.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * KB Database 自动配置
 * <p>
 * 使用说明:
 * 1. 在 application.yml 中配置数据源
 * 2. 确保 kb-database 依赖已引入
 * 3. 本配置类会自动扫描 mapper 包下的所有 Mapper 接口
 */
@Configuration
//@MapperScan(basePackages = "com.knowledge.mapper")
public class KbDatabaseAutoConfiguration {

}

