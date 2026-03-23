package com.knowledge.controller;

import com.knowledge.common.CommonResult;
import com.knowledge.dto.KnowledgeBaseUploadDTO;
import com.knowledge.service.KnowledgeBaseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Dean
 * @date 2026/3/23
 * @description
 */
@RestController
@RequestMapping("/knowledge-base/")
public class KnowledgeBaseController {

    @Resource
    private KnowledgeBaseService knowledgeBaseService;


    @PostMapping("upload")
    public CommonResult<String> upload(@RequestBody KnowledgeBaseUploadDTO uploadDTO) {
        return knowledgeBaseService.upload(uploadDTO);
    }
}
