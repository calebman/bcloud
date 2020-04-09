package com.bcloud.server.repo.out;

import com.bcloud.common.dao.IConverter;
import com.bcloud.common.dao.repo.IDataRepoColumnEntityDao;
import com.bcloud.common.dao.repo.IDataRepoDictEntityDao;
import com.bcloud.common.dao.system.IRoleEntityDao;
import com.bcloud.common.dao.system.IUserEntityDao;
import com.bcloud.common.entity.repo.DataRepoEntity;
import com.bcloud.common.entity.system.UserEntity;
import com.bcloud.common.file.IFileService;
import com.bcloud.server.system.out.UserVO;
import com.bcloud.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JianhuiChen
 * @description 数据仓库
 * @date 2020-03-26
 */
@Getter
@Setter
@Schema(title = "数据仓库")
public class DataRepoVO extends BaseEntity {

    @Schema(title = "仓库名称")
    private String name;

    @Schema(title = "仓库描述")
    private String desc;

    @Schema(title = "仓库包含的列信息")
    private List<DataRepoColumnVO> columns;

    @Schema(title = "仓库归属用户")
    private UserVO belongUser;

    /**
     * VO 实体转换类
     */
    public static class VOConverter implements IConverter<DataRepoVO, DataRepoEntity> {

        /**
         * 用户持久层
         */
        private final IUserEntityDao userEntityDao;

        /**
         * 数据仓库数据列持久层
         */
        private final IDataRepoColumnEntityDao dataRepoColumnEntityDao;

        /**
         * 数据仓库数据列视图层转换
         */
        private final DataRepoColumnVO.VOConverter dataRepoColumnVOConverter;

        private final UserVO.VOConverter userVOConverter;

        public VOConverter(IUserEntityDao userEntityDao, IFileService fileService, IRoleEntityDao roleEntityDao,
                           IDataRepoDictEntityDao dataRepoDictEntityDao, IDataRepoColumnEntityDao dataRepoColumnEntityDao) {
            this.userEntityDao = userEntityDao;
            this.dataRepoColumnEntityDao = dataRepoColumnEntityDao;
            this.dataRepoColumnVOConverter = new DataRepoColumnVO.VOConverter(dataRepoDictEntityDao);
            this.userVOConverter = new UserVO.VOConverter(fileService, roleEntityDao);
        }

        @Override
        public DataRepoVO doBackward(DataRepoEntity dataRepoEntity) {
            DataRepoVO vo = new DataRepoVO();
            BeanUtils.copyProperties(dataRepoEntity, vo);
            UserEntity userEntity = userEntityDao.findOne(dataRepoEntity.getBelongUser());
            vo.setBelongUser(userVOConverter.doBackward(userEntity));
            vo.setColumns(dataRepoColumnEntityDao.findByRepo(dataRepoEntity.getId())
                    .stream()
                    .map(dataRepoColumnVOConverter::doBackward)
                    .collect(Collectors.toList()));
            return vo;
        }
    }
}
