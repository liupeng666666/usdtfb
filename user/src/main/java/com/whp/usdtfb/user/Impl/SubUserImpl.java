package com.whp.usdtfb.user.Impl;

import com.whp.usdtfb.user.Dao.SubUserDao;
import com.whp.usdtfb.user.Interface.SubUserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SubUserImpl implements SubUserInterface {

    @Autowired
    private SubUserDao subUserDao;


    @Override
    public Map<String, Object> getSubUserByUserName(String username) {
        return subUserDao.getSubUserByUserName(username);
    }
}
