package com.bcloud.common.file.impl.local;

import com.bcloud.common.file.IFileService;
import com.bcloud.common.file.FileSaveResult;
import com.bcloud.common.file.FileServiceException;
import lombok.ToString;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.net.URLEncoder;
import java.util.stream.Stream;

/**
 * @author JianhuiChen
 * @description 基于本地存储的文件服务
 * @date 2020-03-23
 */
@Slf4j
@ToString
public class LocalFileService implements IFileService {

    /**
     * 服务的实现类型
     */
    public static final String SERVICE_TYLE = "LocalFileService";

    /**
     * 服务配置信息
     */
    private final LocalFileServiceConfig fileServiceConfig;

    public LocalFileService(LocalFileServiceConfig fileServiceConfig) throws FileServiceException {
        this.fileServiceConfig = fileServiceConfig;
        // Init file saving folder
        String folderPath = fileServiceConfig.getFolder();
        File folder = new File(folderPath);
        if (!folder.exists() && !folder.mkdirs()) {
            log.error("Failed to create local file service storage folder {}", folderPath);
            throw new FileServiceException("Failed to create local file service storage folder " + folderPath);
        }
    }

    @Override
    public FileSaveResult save(String fileName, InputStream stream) throws FileServiceException {
        final String fileKey = generatorFileKey(fileName);
        this.writeToLocal(fileKey, fileName, stream);
        return this.get(fileKey);
    }

    @Override
    public FileSaveResult save(String fileKey, String fileName, InputStream stream) throws FileServiceException {
        this.writeToLocal(fileKey, fileName, stream);
        return this.get(fileKey);
    }

    @Override
    public void remove(String key) {
        String folder = fileServiceConfig.getFolder();
        File keyFolder = new File(folder + key);
        this.deleteDirectory(keyFolder);
        if (keyFolder.exists()) {
            log.warn("Failed to delete file key {}, path {}", key, keyFolder.getAbsolutePath());
        }
    }

    @Override
    public FileSaveResult get(String key) {
        FileSaveResult result = new FileSaveResult();
        // Find file name
        String folder = fileServiceConfig.getFolder();
        File keyFolder = new File(folder + key);
        if (keyFolder.exists() && keyFolder.isDirectory()) {
            File[] files = keyFolder.listFiles();
            if (null != files && files.length > 0) {
                Stream.of(files).min((o1, o2) -> (int) (o1.lastModified() - o2.lastModified()))
                        .ifPresent(file -> result.setName(file.getName()));
            }
        }
        if (result.getName() != null) {
            // Find file url with endpoint
            String endpoint = fileServiceConfig.getEndpoint();
            String url = endpoint + "/" + key;
            if (endpoint.endsWith("/")) {
                url = endpoint + key;
            }
            result.setUrl(url);
        }
        result.setKey(key);
        return result;
    }

    @Override
    public ResponseEntity download(String key) {
        FileSaveResult result = get(key);
        String relativePath = getRelativePath(result.getKey(), result.getName());
        String filePath = fileServiceConfig.getFolder() + relativePath;
        File file = new File(filePath);
        try {
            if (file.exists()) {
                byte[] buffer = FileCopyUtils.copyToByteArray(file);
                return ResponseEntity
                        .ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + URLEncoder.encode(file.getName(), "UTF-8") + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(buffer.length))
                        .header("Connection", "close")
                        .body(buffer);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File " + relativePath + " was not found ");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File " + relativePath + " failed to read ");
        }
    }

    @Override
    public String getStorageType() {
        return SERVICE_TYLE;
    }

    /**
     * 根据文件名称生成文件的唯一标志
     *
     * @param fileName 文件名称
     * @return 唯一标志
     */
    private String generatorFileKey(String fileName) {
        return System.currentTimeMillis() + "";
    }

    /**
     * 根据文件唯一标志与文件名称获得文件基于磁盘的相对路径
     *
     * @param fileKey  文件唯一标志
     * @param fileName 文件名称
     * @return 相对路径
     */
    private String getRelativePath(String fileKey, String fileName) {
        return fileKey + File.separator + fileName;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param file 被删除目录的文件
     */
    private void deleteDirectory(File file) {
        if (file.isFile()) {
            file.delete();
        } else {
            String[] childFilePath = file.list();
            if (childFilePath != null) {
                for (String path : childFilePath) {
                    deleteDirectory(new File(file.getAbsoluteFile() + File.separator + path));
                }
            }
            file.delete();
        }
    }

    /**
     * 写入文件到本地磁盘
     *
     * @param fileKey  文件唯一标志
     * @param fileName 文件名称
     * @param input    文件输入流
     * @throws FileServiceException 保存文件时的异常
     */
    private void writeToLocal(String fileKey, String fileName, InputStream input) throws FileServiceException {
        String folder = fileServiceConfig.getFolder();
        File targetfile = new File(folder + fileKey);
        if (targetfile.exists()) {
            targetfile.delete();
        } else {
            if (!targetfile.mkdirs()) {
                throw new FileServiceException("Failed to create file saving folder " + targetfile.getAbsolutePath());
            }
        }
        int index;
        byte[] bytes = new byte[1024];
        try {
            String relativePath = getRelativePath(fileKey, fileName);
            FileOutputStream output = new FileOutputStream(folder + relativePath);
            while ((index = input.read(bytes)) != -1) {
                output.write(bytes, 0, index);
                output.flush();
            }
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileServiceException("File failed write to local, " + e.getMessage());
        }
    }
}
