package com.knowledge.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Dean
 * @date 2026/3/23
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // 如果字段为null，则不返回该字段，保持响应简洁
public class CommonResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码：200表示成功，其他表示失败或特定业务状态
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private String timestamp;

    // 构造方法：方便链式调用或快速构建
    public CommonResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // ================= 静态工厂方法 (推荐用法) =================

    /**
     * 成功返回 (无数据)
     */
    public static <T> CommonResult<T> success() {
        return new CommonResult<>(200, "操作成功", null);
    }

    /**
     * 成功返回 (带数据)
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(200, "操作成功", data);
    }

    /**
     * 成功返回 (自定义消息和数据)
     */
    public static <T> CommonResult<T> success(String message, T data) {
        return new CommonResult<>(200, message, data);
    }

    /**
     * 失败返回 (默认500)
     */
    public static <T> CommonResult<T> error(String message) {
        return new CommonResult<>(500, message, null);
    }

    /**
     * 失败返回 (指定状态码和消息)
     */
    public static <T> CommonResult<T> error(Integer code, String message) {
        return new CommonResult<>(code, message, null);
    }

    /**
     * 失败返回 (指定状态码、消息和数据-有时错误也需要返回部分数据)
     */
    public static <T> CommonResult<T> error(Integer code, String message, T data) {
        return new CommonResult<>(code, message, data);
    }
}
