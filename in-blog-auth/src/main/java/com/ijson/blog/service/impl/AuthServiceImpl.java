package com.ijson.blog.service.impl;

import com.google.common.base.Strings;
import com.ijson.blog.dao.AuthDao;
import com.ijson.blog.dao.entity.AuthEntity;
import com.ijson.blog.dao.query.AuthQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.AuthService;
import com.ijson.mongo.generator.util.ObjectId;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/14 9:21 AM
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthDao authDao;

    @Override
    public AuthEntity findInternalById(String id) {
        return authDao.findInternalById(id);
    }

    @Override
    public AuthEntity edit(AuthContext context, AuthEntity entity) {
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setLastModifiedBy(context.getId());
        return authDao.update(entity);
    }

    @Override
    public AuthEntity create(AuthContext context, AuthEntity entity) {
        entity.setId(ObjectId.getId());
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setEnable(true);
        entity.setCreatedBy(context.getId());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setLastModifiedBy(context.getId());
        if(Strings.isNullOrEmpty(entity.getFatherId())){
            entity.setFatherId("0");
        }
        return authDao.create(entity);
    }

    @Override
    public void enable(String id, boolean enable, AuthContext context) {
        authDao.enable(id, enable, context.getId());
    }

    @Override
    public void delete(String id) {
        authDao.delete(id);
    }

    @Override
    public PageResult<AuthEntity> find(AuthQuery query, Page pageEntity) {
        return authDao.find(query, pageEntity);
    }

    @Override
    public List<AuthEntity> findByIds(List<String> ids) {
        return authDao.findByIds(ids);
    }

    @Override
    public List<AuthEntity> findFathers(String fatherId) {
        return authDao.findFathers(fatherId);
    }
}
