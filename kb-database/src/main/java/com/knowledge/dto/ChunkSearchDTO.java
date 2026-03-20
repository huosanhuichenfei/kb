package com.knowledge.dto;

import lombok.Data;

/**
 * @author Dean
 * @date 2026/3/20
 * @description
 */
@Data
public class ChunkSearchDTO {

    private Long id;
    private String documentId;
    private String content;
    private String deptCode;
    private Integer securityLevel;
    private Float similarityScore; // 或者 Double
}
