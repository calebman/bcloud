package com.bcloud.common.file.endpoint;

import com.bcloud.common.file.FileSaveResult;
import com.bcloud.common.file.IFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author JianhuiChen
 * @description 文件服务端点
 * @date 2020-03-25
 */
@ResponseBody
@RequiredArgsConstructor
@RequestMapping("/service/file")
@Tag(name = "FileController", description = "提供文件的上传、下载、删除等接口")
public class FileEndpoint {

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return 上传文件结果
     */
    @PostMapping
    @Operation(summary = "上传文件")
    public ResponseEntity<FileSaveResult> uploadFile(@RequestParam("file") @Parameter(description = "上传的文件信息", required = true) MultipartFile file) {
        return ResponseEntity.ok(getFileService().save(file));
    }

    /**
     * 删除文件
     *
     * @param fileKey 文件唯一标志
     * @return 操作结果
     */
    @DeleteMapping("{key}")
    @Operation(summary = "删除文件")
    public ResponseEntity deleteFile(@PathVariable(name = "key")
                                     @Parameter(description = "文件唯一标志", required = true) String fileKey) {
        getFileService().remove(fileKey);
        return ResponseEntity.ok().build();
    }

    /**
     * 查询文件
     *
     * @param fileKey 文件唯一标志
     * @return 文件详细信息
     */
    @GetMapping("{key}")
    @Operation(summary = "获取文件详细信息")
    public ResponseEntity<FileSaveResult> getFileInfo(@PathVariable(name = "key")
                                                      @Parameter(description = "文件唯一标志", required = true) String fileKey) {
        return ResponseEntity.ok(getFileService().get(fileKey));
    }

    /**
     * 下载文件
     *
     * @param fileKey 文件唯一标志
     * @return 文件输出流
     */
    @GetMapping("/download/{key}")
    @Operation(summary = "下载文件")
    public ResponseEntity downloadFile(@PathVariable(name = "key")
                                       @Parameter(description = "文件唯一标志", required = true) String fileKey) {
        return getFileService().download(fileKey);
    }

    /**
     * 获取文件服务
     * 由于文件服务的动态插拔性 此次需要通过Lookup动态获取
     */
    @Lookup
    public IFileService getFileService() {
        return null;
    }
}
