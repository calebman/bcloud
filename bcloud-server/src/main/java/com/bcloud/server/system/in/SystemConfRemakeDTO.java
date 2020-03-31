package com.bcloud.server.system.in;

import com.bcloud.common.dao.IConverter;
import com.bcloud.common.util.ObjectUtils;
import com.bcloud.server.common.sys.SystemDaoConf;
import com.bcloud.server.common.sys.SystemFileConf;
import com.bcloud.server.common.sys.local.LocalSystemFileConf;
import com.bcloud.server.common.sys.mongo.MongoSystemDaoConf;
import com.bcloud.server.common.sys.mongo.MongoSystemFileConf;
import com.bcloud.server.common.valid.Options;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Map;
import java.util.Set;

/**
 * @author JianhuiChen
 * @description 系统配置重置传输层实体
 * @date 2020-03-27
 */
@Getter
@Setter
public class SystemConfRemakeDTO {

    /**
     * 数据持久层类型
     */
    @NotBlank(message = "数据持久层类型不可为空")
    @Options(message = "不合法的数据持久层类型", enumClass = SystemDaoConf.StorageType.class)
    private String daoStorageType;

    /**
     * 数据持久层配置
     */
    private Map<String, Object> daoStorageConf;

    /**
     * 文件持久层类型
     */
    @NotBlank(message = "文件持久层类型不可为空")
    @Options(message = "不合法的数据持久层类型", enumClass = SystemFileConf.StorageType.class)
    private String fileStorageType;

    /**
     * 文件持久层配置
     */
    private Map<String, Object> fileStorageConf;

    /**
     * 数据持久层配置转换工具
     */
    public static class DaoConfConverter implements IConverter<SystemDaoConf, SystemConfRemakeDTO> {

        @Override
        public SystemDaoConf doBackward(SystemConfRemakeDTO systemConfRemakeDTO) {
            // build
            SystemDaoConf systemDaoConf = null;
            if (SystemDaoConf.StorageType.MONGO.toString().equals(systemConfRemakeDTO.getDaoStorageType())) {
                systemDaoConf = new MongoSystemDaoConf();
            }
            ObjectUtils.populate(systemConfRemakeDTO.getDaoStorageConf(), systemDaoConf);
            // valid
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<SystemDaoConf>> violations = validator.validate(systemDaoConf);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
            return systemDaoConf;
        }
    }

    /**
     * 文件持久层配置转换工具
     */
    public static class FileConfConverter implements IConverter<SystemFileConf, SystemConfRemakeDTO> {

        @Override
        public SystemFileConf doBackward(SystemConfRemakeDTO systemConfRemakeDTO) {
            // build
            SystemFileConf systemFileConf = null;
            if (SystemFileConf.StorageType.LOCAL.toString().equals(systemConfRemakeDTO.getFileStorageType())) {
                systemFileConf = new LocalSystemFileConf();
            } else if(SystemFileConf.StorageType.MONGO.toString().equals(systemConfRemakeDTO.getFileStorageType())){
                systemFileConf =new MongoSystemFileConf();
            }
            ObjectUtils.populate(systemConfRemakeDTO.getFileStorageConf(), systemFileConf);
            // valid
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<SystemFileConf>> violations = validator.validate(systemFileConf);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
            return systemFileConf;
        }
    }
}
