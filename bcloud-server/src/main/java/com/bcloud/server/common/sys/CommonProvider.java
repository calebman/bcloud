package com.bcloud.server.common.sys;

import com.bcloud.common.dao.repo.IDataRepoColumnEntityDao;
import com.bcloud.common.dao.repo.IDataRepoDictEntityDao;
import com.bcloud.common.dao.repo.IDataRepoDocDaoFactory;
import com.bcloud.common.dao.repo.IDataRepoEntityDao;
import com.bcloud.common.dao.system.IRoleEntityDao;
import com.bcloud.common.dao.system.IUserEntityDao;
import com.bcloud.common.file.IFileService;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @author JianhuiChen
 * @description 基础组件提供者 面向service层提供服务
 * 此类提供了基础组件（数据持久化、文件持久化）等服务的获取
 * 由于基础组件具备可切换的动态特性，所以在获取组件时必须从 beanFactory 中提取
 * 如果直接使用 构造或者设值注入 将导致基础组件切换后其服务层与控制层无法得到基础组件的更新
 * @date 2020-03-30
 */
@Getter
public abstract class CommonProvider implements BeanFactoryAware {

    /**
     * Spring 实体仓库
     */
    private BeanFactory beanFactory;

    protected IFileService getFileService() {
        return loadComponent(IFileService.class);
    }

    protected IDataRepoColumnEntityDao getDataRepoColumnEntityDao() {
        return loadComponent(IDataRepoColumnEntityDao.class);
    }

    protected IDataRepoDictEntityDao getDataRepoDictEntityDao() {
        return loadComponent(IDataRepoDictEntityDao.class);
    }

    protected IDataRepoDocDaoFactory getDataRepoDocDaoFactory() {
        return loadComponent(IDataRepoDocDaoFactory.class);
    }

    protected IDataRepoEntityDao getDataRepoEntityDao() {
        return loadComponent(IDataRepoEntityDao.class);
    }

    protected IRoleEntityDao getRoleEntityDao() {
        return loadComponent(IRoleEntityDao.class);
    }

    protected IUserEntityDao getUserEntityDao() {
        return loadComponent(IUserEntityDao.class);
    }

    /**
     * 根据类型从Spring实体工厂中加载最新的组件
     *
     * @param requiredType 组件类对象
     * @param <T>          具体类型
     * @return 组件实体
     * @throws BeansException 加载时可能出现的异常
     */
    private <T> T loadComponent(Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(requiredType);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
