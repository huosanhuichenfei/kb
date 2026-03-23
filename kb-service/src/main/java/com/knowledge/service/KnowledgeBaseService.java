package com.knowledge.service;

import com.knowledge.common.CommonResult;
import com.knowledge.dto.KnowledgeBaseUploadDTO;
import org.springframework.stereotype.Service;

/**
 * @author Dean
 * @date 2026/3/23
 * @description
 */
@Service
public class KnowledgeBaseService {


    public CommonResult<String> upload(KnowledgeBaseUploadDTO uploadDTO) {
        //这个是文章上传的标题和内容， 现在需要做三件事，1. 和标题和内容进行分词操作，2. 获取分词结果，3. 获取分词结果对应的向量
        return null;
    }
}
