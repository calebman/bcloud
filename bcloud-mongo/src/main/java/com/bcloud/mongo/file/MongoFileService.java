package com.bcloud.mongo.file;

import com.bcloud.common.file.IFileService;
import com.bcloud.common.file.FileSaveResult;
import com.bcloud.common.file.FileServiceException;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * @author JianhuiChen
 * @description 基于mongodb的文件访问服务
 * @date 2020-03-25
 */
@Slf4j
@ToString
@RequiredArgsConstructor
public class MongoFileService implements IFileService {

    /**
     * 服务的实现类型
     */
    public static final String SERVICE_TYLE = "MongoFileService";

    /**
     * mongo db 文件操作类
     */
    private final GridFsTemplate gridFsTemplate;

    /**
     * 服务配置信息
     */
    private final MongoFileServiceConfig fileServiceConfig;


    @Override
    public String getStorageType() {
        return SERVICE_TYLE;
    }

    @Override
    public FileSaveResult save(String fileName, InputStream stream) throws FileServiceException {
        ObjectId objectId = gridFsTemplate.store(stream, fileName);
        return this.get(String.valueOf(objectId.getTimestamp()));
    }

    @Override
    public FileSaveResult save(String fileKey, String fileName, InputStream stream) throws FileServiceException {
        ObjectId objectId = gridFsTemplate.store(stream, fileName);
        return this.get(String.valueOf(objectId.getTimestamp()));
    }

    @Override
    public void remove(String key) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(key));
        gridFsTemplate.delete(query);
    }

    @Override
    public FileSaveResult get(String key) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(key));
        GridFSFile fsFile = gridFsTemplate.findOne(query);
        if (fsFile != null) {
            FileSaveResult result = new FileSaveResult();
            result.setKey(key);
            result.setName(fsFile.getFilename());
            String endpoint = fileServiceConfig.getEndpoint();
            String url = endpoint + "/" + key;
            if (endpoint.endsWith("/")) {
                url = endpoint + key;
            }
            result.setUrl(url);
            return result;
        }
        throw new FileServiceException("Failed to find file by key " + key);
    }

    @Override
    public ResponseEntity download(String key) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(key));
        GridFSFile fsFile = gridFsTemplate.findOne(query);
        if (fsFile != null) {
            try {
                InputStream in = gridFsTemplate.getResource(fsFile.getObjectId().toHexString()).getInputStream();
                byte[] buffer = FileCopyUtils.copyToByteArray(in);
                return ResponseEntity
                        .ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + URLEncoder.encode(fsFile.getFilename(), "UTF-8") + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(buffer.length))
                        .header("Connection", "close")
                        .body(buffer);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File " + key + " failed to read ");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File " + key + " was not found ");
        }
    }
}
