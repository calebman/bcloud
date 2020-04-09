package com.bcloud.server.system.out;

import com.bcloud.common.dao.FilterLogic;
import com.bcloud.common.dao.FilterOperator;
import com.bcloud.common.dao.IConverter;
import com.bcloud.common.dao.system.IRoleEntityDao;
import com.bcloud.common.entity.BaseEntity;
import com.bcloud.common.file.FileSaveResult;
import com.bcloud.common.file.IFileService;
import com.bcloud.common.entity.system.UserEntity;
import com.bcloud.server.system.service.IRoleService;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.data.util.Pair;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author JianhuiChen
 * @description 系统用户视图层实体
 * @date 2020-03-25
 */
@Getter
@Setter
@Schema(title = "系统用户信息")
public class UserVO extends BaseEntity {

    @Schema(title = "账号")
    private String account;

    @Schema(title = "邮箱")
    private String email;

    @Schema(title = "姓名")
    private String name;

    @Schema(title = "用户当前的状态")
    private UserEntity.Status status;

    @Schema(title = "用户头像信息 外链地址")
    private String avatar;

    @Schema(title = "用户的角色列表")
    private List<RoleVO> roles;

    @Schema(title = "额外信息 如系统采用主题、样式等等")
    private Map<String, String> extraInfo;

    /**
     * VO 实体转换层
     */
    @RequiredArgsConstructor
    public static class VOConverter implements IConverter<UserVO, UserEntity> {

        /**
         * 文件服务 用于头像外链转换
         */
        private final IFileService fileService;

        /**
         * 角色持久层
         */
        private final IRoleEntityDao roleEntityDao;

        /**
         * 角色视图层转换
         */
        private final RoleVO.VOConverter roleVOConverter = new RoleVO.VOConverter();

        @Override
        public UserVO doBackward(UserEntity userEntity) {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(userEntity, vo);
            // 补充头像信息
            if (!StringUtils.isEmpty(userEntity.getAvatarKey())) {
                FileSaveResult result = fileService.get(userEntity.getAvatarKey());
                if (result != null) {
                    vo.setAvatar(result.getUrl());
                }
            }
            // 补充角色信息
            Table<String, Pair<FilterLogic, FilterOperator>, Object> filters = HashBasedTable.create();
            filters.put("_id", Pair.of(FilterLogic.AND, FilterOperator.IN), userEntity.getRoleIds());
            vo.setRoles(roleEntityDao.findAll(filters).stream().map(roleVOConverter::doBackward).collect(Collectors.toList()));
            return vo;
        }
    }
}
