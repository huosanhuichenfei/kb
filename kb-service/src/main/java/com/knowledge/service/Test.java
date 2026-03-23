package com.knowledge.service;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Dean
 * @date 2026/3/23
 * @description
 */
@Slf4j
public class Test {

    public static void main(String[] args) {
        // 模拟一段文件内容
        String text = "HanLP是由何晗团队开发的开源中文自然语言处理工具包，特别适用于搜索引擎构建倒排索引。";

        log.info("原始文本：{}" , text);
        log.info("--------------------------------------------------");



        // 1. 标准分词 (Standard Segmentation)
        // 适用于一般文本分析、展示
        log.info("【标准分词结果】:");
        List<Term> standardList = HanLP.segment(text);
        for (Term term : standardList) {
            log.info(term.word + "/" + term.nature + "  ");
        }
        log.info("\n");

        // 2. 索引分词 (Index Segmentation) -> 【强烈推荐用于文件搜索】
        // 会将长词细粒度切分，例如将"自然语言处理"切分为"自然"、"语言"、"处理"、"自然语言"等
        // 这样用户搜索其中任意一个子词，都能匹配到该文档
        log.info("【索引分词结果 (用于建立搜索索引)】:");
        // 方法一：直接使用 IndexTokenizer
        List<Term> indexList = IndexTokenizer.segment(text);

        // 方法二：或者配置 Segment 对象
        // Segment indexSegment = HanLP.newSegment().enableIndexMode(true);
        // List<Term> indexList = indexSegment.seg(text);

        for (Term term : indexList) {
            log.info(term.word + "  ");
        }
        log.info("\n");

        // 3. 演示自定义词典 (可选)
        // 如果您的文件中有特定术语，比如公司名 "未来科技"，防止被切分成 "未来/科技"
        log.info("【添加自定义词典后】:");
        HanLP.Config.CustomDictionaryPath = new String[]{"data/dictionary/custom/MyCustomDict.txt"};
        // 注意：Portable 版本默认字典在 jar 包内，若要加载外部自定义字典，需确保文件路径存在
        // 这里仅做代码示意，实际运行时若没有该文件可能会报错或忽略，
        // 简单测试可以直接用代码动态添加：
        com.hankcs.hanlp.dictionary.CustomDictionary.add("未来科技");
        String text2 = "未来科技发布了新产品";
        log.info("原文：" + text2);
        log.info("分词：" + HanLP.segment(text2));
    }
}
