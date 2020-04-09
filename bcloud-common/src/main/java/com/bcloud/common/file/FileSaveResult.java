package com.bcloud.common.file;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "文件操作结果")
public class FileSaveResult {

    @Schema(title = "文件名称")
    private String name;

    @Schema(title = "下载链接")
    private String url;

    @Schema(title = "文件的唯一标志")
    private String key;
}
