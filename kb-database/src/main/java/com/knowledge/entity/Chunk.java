package com.knowledge.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Dean
 * @date 2026/3/20
 * @description
 */
@Data
@TableName ("chunks")
public class Chunk {

    @TableId(type = IdType.AUTO) // 对应 BIGSERIAL
    private Long id;

    @TableField("document_id")
    private String documentId;

    @TableField("chunk_index")
    private Integer chunkIndex;

    @TableField("content")
    private String content;

    // 核心：向量字段
    // 注意：MP 默认会将 List 转为 JSON 或数组，我们需要在 Mapper 中确保它被当作 vector 处理
    @TableField("embedding")
    private List<Float> embedding;

    @TableField("dept_code")
    private String deptCode;

    @TableField("security_level")
    private Integer securityLevel;

    @TableField("created_at")
    private LocalDateTime createdAt;
}
