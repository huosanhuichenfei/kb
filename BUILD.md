# 知识库 Starter 项目 - 构建与使用说明

## 📁 项目结构

```
knowledge-base-parent/
├── README.md                          # 项目说明文档
├── pom.xml                            # 父 POM（版本管理、依赖管理）
│
├── kb-core/                           # 核心模块
│   ├── pom.xml
│   └── src/main/java/com/knowledge/base/core/
│       ├── exception/                 # 异常定义
│       │   ├── KnowledgeException.java
│       │   ├── EsOperationException.java
│       │   └── DatabaseOperationException.java
│       ├── model/                     # 数据模型
│       │   ├── KnowledgeEntity.java
│       │   └── SearchResult.java
│       └── repository/                # 基础接口
│           └── KnowledgeRepository.java
│
├── kb-es/                             # ES 模块 (Elasticsearch 7.17.14)
│   ├── pom.xml
│   └── src/main/java/com/knowledge/base/es/
│       ├── config/                    # ES 配置
│       │   ├── EsProperties.java
│       │   └── EsAutoConfiguration.java
│       ├── model/                     # ES 文档模型
│       │   └── KnowledgeDocument.java
│       ├── repository/                # ES 数据访问层
│       │   └── KnowledgeEsRepository.java
│       └── service/                   # ES 业务服务
│           └── KnowledgeEsService.java
│
├── kb-database/                       # 数据库模块 (MyBatis Plus)
│   ├── pom.xml
│   └── src/main/java/com/knowledge/base/db/
│       ├── config/                    # 数据库配置
│       │   ├── DatabaseProperties.java
│       │   └── DatabaseAutoConfiguration.java
│       ├── entity/                    # 数据库实体
│       │   └── KnowledgeDbEntity.java
│       ├── repository/                # 数据库 Mapper
│       │   └── KnowledgeDbMapper.java
│       └── service/                   # 数据库业务服务
│           └── KnowledgeDbService.java
│
├── kb-service/                        # 统一服务层
│   ├── pom.xml
│   └── src/main/java/com/knowledge/base/service/
│       └── UnifiedKnowledgeService.java
│
└── kb-starter/                        # Starter 聚合模块
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── java/com/knowledge/base/config/
        │   │   ├── KnowledgeBaseProperties.java
        │   │   ├── KnowledgeBaseAutoConfiguration.java
        │   │   └── KnowledgeRepositoryImpl.java
        │   └── resources/
        │       ├── META-INF/
        │       │   └── spring.factories          # SPI 自动配置注册
        │       └── application-example.yml       # 配置示例
        └── test/java/com/example/
            └── KnowledgeBaseDemoApplication.java # 使用示例

```

## 🔨 构建步骤

### 前置要求

- JDK 1.8+
- Maven 3.6+
- Elasticsearch 7.17.14（可选，如果只使用数据库）
- MySQL 8.0+（可选，如果只使用 ES）

### 1. 编译项目

```bash
cd D:\project\flowable
mvn clean install -DskipTests
```

### 2. 安装到本地 Maven 仓库

```bash
mvn clean install
```

构建成功后，会在各模块的 `target` 目录下生成 jar 文件：
- `kb-core-1.0.0-SNAPSHOT.jar`
- `kb-es-1.0.0-SNAPSHOT.jar`
- `kb-database-1.0.0-SNAPSHOT.jar`
- `kb-service-1.0.0-SNAPSHOT.jar`
- `kb-starter-1.0.0-SNAPSHOT.jar`

## 🚀 在 Spring Boot 项目中使用

### 步骤 1: 添加依赖

在你的项目 `pom.xml` 中添加：

```xml
<dependency>
    <groupId>com.knowledge</groupId>
    <artifactId>kb-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 步骤 2: 配置连接信息

在 `application.yml` 中配置：

```yaml
knowledge:
  base:
    enabled: true
    default-storage-type: ES
    
    elasticsearch:
      enabled: true
      hosts: 192.168.1.100:9200
      username: elastic
      password: your_password
      
    database:
      enabled: true
      url: jdbc:mysql://localhost:3306/knowledge_db
      username: root
      password: your_password
```

### 步骤 3: 初始化数据库

执行 SQL 脚本创建表结构：

```bash
mysql -u root -p < kb-database/src/main/resources/schema.sql
```

### 步骤 4: 安装 ES IK 分词器插件

在 ES 服务器上安装 IK Analyzer：

```bash
cd $ES_HOME/plugins
./elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.17.14/elasticsearch-analysis-ik-7.17.14.zip
```

重启 ES 后生效。

### 步骤 5: 编写代码

```java
@Service
public class MyService {
    
    @Autowired
    private UnifiedKnowledgeService knowledgeService;
    
    public void demo() {
        // 创建并保存
        KnowledgeEntity entity = KnowledgeEntity.builder()
            .title("测试标题")
            .content("测试内容")
            .category("分类")
            .storageType(KnowledgeEntity.StorageType.ES)
            .build();
        
        knowledgeService.save(entity);
        
        // 搜索
        SearchResult<KnowledgeEntity> result = knowledgeService.search(
            "关键词", 1, 10, KnowledgeEntity.class, 
            KnowledgeEntity.StorageType.ES
        );
        
        // 处理结果
        result.getResults().forEach(System.out::println);
    }
}
```

## ⚙️ 高级配置

### 仅使用 ES（不使用数据库）

```yaml
knowledge:
  base:
    default-storage-type: ES
    elasticsearch:
      enabled: true
      hosts: localhost:9200
    database:
      enabled: false
```

### 仅使用数据库（不使用 ES）

```yaml
knowledge:
  base:
    default-storage-type: DATABASE
    elasticsearch:
      enabled: false
    database:
      enabled: true
      url: jdbc:mysql://localhost:3306/knowledge_db
      username: root
      password: password
```

### 同时使用 ES 和数据库（双写）

```yaml
knowledge:
  base:
    default-storage-type: BOTH
    elasticsearch:
      enabled: true
    database:
      enabled: true
```

## 🧪 测试

运行示例应用进行测试：

```bash
cd kb-starter
mvn spring-boot:run
```

或者在 IDE 中运行 `KnowledgeBaseDemoApplication` 类的 main 方法。

## 📝 注意事项

1. **ES 版本兼容性**: 本项目专为 Elasticsearch 7.17.14 设计，使用其他版本可能需要调整依赖
2. **IK 分词器**: 必须确保 ES 服务器安装了相应版本的 IK Analyzer 插件
3. **数据库驱动**: 如果使用 PostgreSQL 或其他数据库，需要修改 `kb-database` 模块的依赖
4. **事务支持**: 数据库操作已添加 `@Transactional`，ES 操作不支持事务
5. **索引管理**: ES 索引会在首次启动时自动创建，也可以手动通过 API 管理

## 🔧 故障排查

### 问题 1: 自动配置未生效

检查是否添加了配置：
```yaml
knowledge:
  base:
    enabled: true
```

### 问题 2: ES 连接失败

- 检查 ES 服务是否启动
- 检查防火墙设置
- 验证用户名密码是否正确
- 查看 ES 日志

### 问题 3: 中文分词不生效

- 确认 ES 已安装 IK 插件
- 重启 ES 服务
- 检查索引 mapping 中的 analyzer 配置
- 使用 `_analyze` API 测试分词效果

## 📦 发布到私有 Maven 仓库

如果有私有 Nexus/Artifactory 仓库：

```bash
mvn clean deploy
```

需要在 `pom.xml` 或 `settings.xml` 中配置仓库信息。

## 🎯 下一步扩展

1. 添加更多 ES 查询功能（聚合、高亮、建议等）
2. 支持更多数据库类型（PostgreSQL、Oracle 等）
3. 添加缓存支持（Redis）
4. 实现知识图谱功能
5. 添加权限控制和审计日志

## 📄 License

MIT License
