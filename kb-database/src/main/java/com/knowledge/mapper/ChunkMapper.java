package com.knowledge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knowledge.dto.ChunkSearchDTO;
import com.knowledge.entity.Chunk;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Dean
 * @date 2026/3/20
 * @description
 */
@Mapper
public interface ChunkMapper extends BaseMapper<Chunk> {

    /**
     * 自定义向量相似度搜索
     * 使用 XML 配置更灵活，也可以在这里用 @Select 注解
     */
    List<ChunkSearchDTO> searchSimilar(
            @Param("queryVector") List<Float> queryVector,
            @Param("deptCode") String deptCode,
            @Param("maxSecurityLevel") Integer maxSecurityLevel,
            @Param("limit") Integer limit
    );

    List<ChunkSearchDTO> searchSimilarByString(
            @Param("queryVectorStr") String queryVectorStr, // 传入 "[0.1, 0.2...]"
            @Param("deptCode") String deptCode,
            @Param("maxSecurityLevel") Integer maxSecurityLevel,
            @Param("limit") Integer limit
    );
}
