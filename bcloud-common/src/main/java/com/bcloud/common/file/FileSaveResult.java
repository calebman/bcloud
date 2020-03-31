package com.bcloud.common.file;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author JianhuiChen
 * @description 文件服务的返回结果
 * @date 2020-03-23
 */
@Getter
@Setter
@ToString
public class FileSaveResult {

    /**
     * 文件名称
     */
    private String name;

    /**
     * 下载链接
     */
    private String url;

    /**
     * 文件的唯一标志
     */
    private String key;
}
