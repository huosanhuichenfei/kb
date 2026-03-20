package com.knowledge.service;

import com.knowledge.dto.ChunkSearchDTO;
import com.knowledge.entity.Chunk;
import com.knowledge.mapper.ChunkMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dean
 * @date 2026/3/20
 * @description
 */
@Service
public class ChunkService {

    @Resource
    private ChunkMapper chunkMapper;

    /**
     * 保存数据
     */
    @Transactional
    public boolean saveChunk(Chunk chunk) {
        // MP 标准插入，只要驱动够新，List<Float> 会自动处理
        // 如果报错，可能需要自定义 TypeHandler 或在插入前转为 String
        return chunkMapper.insert(chunk) > 0;
    }

    /**
     * 向量搜索
     */
    public List<ChunkSearchDTO> searchSimilar(List<Float> queryVector, String dept, int maxLevel, int limit) {
        // 关键步骤：将 List<Float> 转换为 PostgreSQL vector 字面量格式 '[0.1, 0.2, ...]'
        // PG 的 vector 输入通常接受方括号格式
        String vectorLiteral = queryVector.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));

        return chunkMapper.searchSimilar(queryVector, dept, maxLevel, limit);
        // 等等，XML 里的 #{queryVector} 如果是 List，PG 驱动可能会转成 {0.1, 0.2}
        // 让我们修改 XML 策略：直接传 String 进去最稳
    }

    // 修正后的搜索调用
    public List<ChunkSearchDTO> searchSimilarSafe(List<Float> queryVector, String dept, int maxLevel, int limit) {
        String vectorStr = queryVector.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));

        return chunkMapper.searchSimilarByString(vectorStr, dept, maxLevel, limit);
    }
}
