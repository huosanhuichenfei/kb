package com.knowledge.controller;

import com.knowledge.dto.ChunkSearchDTO;
import com.knowledge.entity.Chunk;
import com.knowledge.service.ChunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Dean
 * @date 2026/3/20
 * @description
 */
@RestController
@RequestMapping("/api/mp-vector")
public class VectorController {

    @Autowired
    private ChunkService chunkService;

    @PostMapping("/insert")
    public String insert() {
        Chunk chunk = new Chunk();
        chunk.setDocumentId("doc-mp-001");
        chunk.setChunkIndex(0);
        chunk.setContent("MyBatis-Plus 集成 pgvector 非常丝滑，性能很好。");
        chunk.setDeptCode("DEV");
        chunk.setSecurityLevel(1);

        // 生成随机向量
        List<Float> vec = new ArrayList<>();
        Random r = new Random();
        for(int i=0; i<1536; i++) vec.add(r.nextFloat());
        chunk.setEmbedding(vec);

        chunkService.saveChunk(chunk);
        return "Insert OK";
    }

    @GetMapping("/search")
    public List<ChunkSearchDTO> search(@RequestParam(defaultValue = "DEV") String dept) {
        // 模拟查询向量
        List<Float> queryVec = new ArrayList<>();
        Random r = new Random();
        for(int i=0; i<1536; i++) queryVec.add(r.nextFloat());

        return chunkService.searchSimilarSafe(queryVec, dept, 5, 5);
    }
}
