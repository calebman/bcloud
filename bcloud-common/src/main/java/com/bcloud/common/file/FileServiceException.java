package com.bcloud.common.file;

/**
 * @author JianhuiChen
 * @description 文件服务异常
 * @date 2020-03-23
 */
public class FileServiceException extends RuntimeException {

    public FileServiceException(String errMsg) {
        super(errMsg);
    }
}
