package com.bcloud.common.file;

import com.bcloud.common.util.DelayedTaskScheduled;
import com.bcloud.common.util.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author JianhuiChen
 * @description 文件存储/删除相关操作接口
 * @date 2020-03-23
 */
public interface IFileService {

    /**
     * 获取当前文件服务的类型
     *
     * @return 存储类型
     */
    String getStorageType();

    default List<FileSaveResult> saveAsRequest(HttpServletRequest request) throws FileServiceException {
        return this.saveAsRequest(request, "file");
    }

    /**
     * 保存一个HTTP请求中包含的文件信息
     *
     * @param request         请求对象
     * @param requestFileProp 请求体中文件的键值
     * @return 文件存储的结果
     * @throws FileServiceException 文件服务异常
     */
    default List<FileSaveResult> saveAsRequest(HttpServletRequest request, String requestFileProp) throws FileServiceException {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(requestFileProp);
        if (files.size() == 0) {
            throw new FileServiceException("Current http request has not any files");
        }
        return this.save(files);
    }

    default List<FileSaveResult> save(List<MultipartFile> files) throws FileServiceException {
        return files.stream().map(this::save).collect(Collectors.toList());
    }

    /**
     * 保存Spring提供的文件对象
     *
     * @param file 文件信息对象
     * @return 文件存储的结果
     * @throws FileServiceException 文件服务异常
     */
    default FileSaveResult save(MultipartFile file) throws FileServiceException {
        if (file.isEmpty()) {
            throw new FileServiceException("File " + file.getOriginalFilename() + " is empty, can not save it");
        }
        try {
            return this.save(file.getOriginalFilename(), file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileServiceException(e.getMessage());
        }
    }

    /**
     * 保存本地磁盘的文件
     *
     * @param file 文件信息对象
     * @return 文件存储的结果
     * @throws FileServiceException 文件服务异常
     */
    default FileSaveResult save(File file) throws FileServiceException {
        if (!file.exists()) {
            throw new FileServiceException("File path " + file.getAbsolutePath() + " not exists, can not save it");
        }
        if (file.isDirectory()) {
            throw new FileServiceException("File path " + file.getAbsolutePath() + " is a directory, can not save it");
        }
        try {
            return this.save(file.getName(), new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new FileServiceException(e.getMessage());
        }
    }

    /**
     * 保存网络上的文件
     *
     * @param url 文件的网络地址
     * @return 文件存储的结果
     * @throws FileServiceException 文件服务异常
     */
    default FileSaveResult saveAsUrl(String url) throws FileServiceException {
        try {
            String fileName = FileUtils.getFileNameAsUrl(url);
            return this.save(fileName, FileUtils.downloadFileAsUrl(url));
        } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
            e.printStackTrace();
            throw new FileServiceException(e.getMessage());
        }
    }

    /**
     * 保存文件
     *
     * @param fileName 文件名称
     * @param stream   文件输入流
     * @return 文件存储的结果
     * @throws FileServiceException 文件服务异常
     */
    FileSaveResult save(String fileName, InputStream stream) throws FileServiceException;

    /**
     * 保存文件
     *
     * @param fileKey  文件唯一标志
     * @param fileName 文件名称
     * @param stream   文件输入流
     * @return 文件存储的结果
     * @throws FileServiceException 文件服务异常
     */
    FileSaveResult save(String fileKey, String fileName, InputStream stream) throws FileServiceException;

    /**
     * 移除文件
     *
     * @param key 文件的唯一标识
     */
    void remove(String key);

    /**
     * 获取文件信息
     *
     * @param key 文件的唯一标识
     * @return 文件详细信息
     */
    FileSaveResult get(String key);


    default FileSaveResult tempSave(String fileName, InputStream stream) throws FileServiceException {
        return tempSave(fileName, stream, 60L, TimeUnit.SECONDS);
    }

    /**
     * 临时保存文件
     * 应用场景示例：表格导出的excel文件所需的临时下载链接
     *
     * @param fileName    文件名称
     * @param stream      输入流
     * @param deleteDelay 文件存储的有效期
     * @param timeUnit    时间单位
     * @return 操作结果
     * @throws FileServiceException 文件服务异常
     */
    default FileSaveResult tempSave(String fileName, InputStream stream, long deleteDelay, TimeUnit timeUnit) throws FileServiceException {
        FileSaveResult result = this.save(fileName, stream);
        DelayedTaskScheduled.schedule(() -> this.remove(result.getKey()), deleteDelay, timeUnit);
        return result;
    }

    /**
     * 下载文件
     *
     * @param key 文件唯一标志
     */
    ResponseEntity download(String key);
}
