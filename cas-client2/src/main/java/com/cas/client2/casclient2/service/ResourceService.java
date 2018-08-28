package com.cas.client2.casclient2.service;

import com.cas.client2.casclient2.dao.ResourceDao;
import com.cas.client2.casclient2.entity.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {
    @Autowired
    private ResourceDao resourceDao;

    public List<Resource> getAll(){
        return resourceDao.getAllResource();
    }
}
