package com.security.securitysimple.service;

import com.security.securitysimple.dao.ResourceDao;
import com.security.securitysimple.entity.Resource;
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
