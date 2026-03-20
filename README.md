# Knowledge Base Starter - 知识库组件

一个可复用的知识库 jar 包，支持 Elasticsearch 和数据库双存储，提供完整的分词搜索功能。

## 📦 模块结构

```
knowledge-base-parent/
├── kb-core         # 核心模块 - 基础接口和模型定义
├── kb-es           # ES 模块 - Elasticsearch 7.17.14 操作
├── kb-database     # 数据库模块 - MyBatis Plus 支持
├── kb-service      # 服务层 - 统一业务逻辑
└── kb-starter      # Starter 模块 - 自动配置聚合
```

## 🚀 快速开始

### 1. 引入依赖

在你的 Spring Boot 项目的 `pom.xml` 中添加：

```xml
<dependency>
    <groupId>com.knowledge</groupId>
    <artifactId>kb-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 2. 配置连接信息

在 `application.yml` 中配置：

```yaml
knowledge:
  base:
    enabled: true
    default-storage-type: ES  # ES, DATABASE, BOTH
    
    elasticsearch:
      enabled: true
      hosts: 127.0.0.1:9200
      username: elastic
      password: your_password
      index-prefix: kb_
      
    database:
      enabled: true
      type: mysql
      url: jdbc:mysql://localhost:3306/knowledge_db
      username: root
      password: your_password
```

### 3. 使用示例

```java
@Service
public class MyKnowledgeService {
    
    @Autowired
    private UnifiedKnowledgeService knowledgeService;
    
    public void saveKnowledge() {
        // 创建知识实体
        KnowledgeEntity entity = KnowledgeEntity.builder()
                .title("Spring Boot 教程")
                .content("Spring Boot 是一个快速开发框架...")
                .category("技术文档")
                .tags(new String[]{"Spring", "Boot"})
                .storageType(KnowledgeEntity.StorageType.ES)
                .build();
        
        // 保存
        knowledgeService.save(entity);
    }
    
    public void searchKnowledge() {
        // 搜索（支持分词）
        SearchResult<KnowledgeEntity> result = knowledgeService.search(
            "Spring Boot", 
            1, 
            10, 
            KnowledgeEntity.class, 
            KnowledgeEntity.StorageType.ES
        );
        
        result.getResults().forEach(item -> {
            System.out.println(item.getTitle());
        });
    }
}
```

## ✨ 核心特性

- ✅ **即插即用**: 基于 Spring Boot 自动配置，零手动配置
- ✅ **双存储支持**: ES 和数据库可同时或独立使用
- ✅ **中文分词**: 集成 IK Analyzer，支持细粒度和粗粒度分词
- ✅ **灵活搜索**: 支持关键词搜索、分页查询、高级查询
- ✅ **类型安全**: 统一的实体模型和结果封装
- ✅ **异常处理**: 完善的异常体系和错误码

## 🔧 配置说明

### 完整配置项

```yaml
knowledge:
  base:
    enabled: true                          # 是否启用
    default-storage-type: ES               # 默认存储类型
    
    # ES 配置
    elasticsearch:
      enabled: true
      hosts: 127.0.0.1:9200               # ES 地址列表
      username: elastic
      password: password
      index-prefix: kb_                   # 索引前缀
      connection-timeout: 5000            # 连接超时 (ms)
      socket-timeout: 60000               # Socket 超时 (ms)
      max-conn-total: 100                 # 最大连接数
      max-conn-per-route: 20              # 每路由最大连接数
      analyzer: IK_MAX_WORD               # 分词器类型
      
    # 数据库配置
    database:
      enabled: true
      type: mysql                         # 数据库类型
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/knowledge_db
      username: root
      password: password
      minimum-idle: 5                     # 最小空闲连接
      maximum-pool-size: 20               # 最大连接池大小
      connection-timeout: 30000           # 连接超时 (ms)
      table-prefix: kb_                   # 表名前缀
      show-sql: false                     # 是否显示 SQL
```

## 📊 API 接口

### UnifiedKnowledgeService

| 方法 | 描述 | 参数 |
|------|------|------|
| `save(T entity)` | 保存知识实体 | entity: 知识对象 |
| `findById(String id, Class<T>, StorageType)` | 根据 ID 查询 | id, 类型，存储类型 |
| `search(String keyword, int page, int size, Class<T>, StorageType)` | 搜索 | 关键词，页码，大小，类型，存储类型 |
| `deleteById(String id, StorageType)` | 删除 | id, 存储类型 |

### KnowledgeEntity

```java
KnowledgeEntity.builder()
    .id("唯一标识")
    .title("标题")
    .content("内容")
    .category("分类")
    .tags(new String[]{"标签 1", "标签 2"})
    .metadata(Map.of("key", "value"))
    .storageType(StorageType.ES)
    .build()
```

## 🛠️ 扩展开发

### 自定义 ES 映射

```java
@Document(indexName = "#{@esProperties.indexPrefix}custom")
@Setting(settingPath = "/custom-settings.json")
public class CustomDocument {
    @Id
    private String id;
    
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String content;
    
    // ... getters/setters
}
```

### 自定义 Repository

```java
@Repository
public interface CustomRepository extends ElasticsearchRepository<CustomDocument, String> {
    Page<CustomDocument> findByCategory(String category, Pageable pageable);
}
```

## 📝 注意事项

1. **ES 版本**: 专为 Elasticsearch 7.17.14 优化，其他版本可能需要调整
2. **IK 分词器**: 需要在 ES 服务器安装 IK Analyzer 插件
3. **数据库**: 首次使用需要手动创建表结构
4. **事务**: 数据库操作已添加事务支持，ES 操作不支持事务

## 🔍 常见问题

**Q: 如何只使用 ES 或数据库？**  
A: 通过配置 `enabled` 参数控制：
```yaml
knowledge.base.elasticsearch.enabled=true
knowledge.base.database.enabled=false
```

**Q: 如何切换分词器？**  
A: 修改配置 `knowledge.base.elasticsearch.analyzer`，可选值：IK_MAX_WORD, IK_SMART, STANDARD

**Q: 支持哪些数据库？**  
A: 理论上支持所有 MyBatis Plus 支持的数据库，修改 `database.type` 和驱动即可

## 📄 License

MIT License
