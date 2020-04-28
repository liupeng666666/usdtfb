package com.whp.usdtfb.CNY.Impl;

import com.whp.usdtfb.CNY.Dao.FbCNYDao;
import com.whp.usdtfb.CNY.Interface.FbCNYInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author : 张吉伟
 * @data : 2018/12/19 9:56
 * @descrpition :
 */
@Service
public class FbCNYImpl implements FbCNYInterface {

    @Autowired
    private FbCNYDao fbCNYDao;

    @Override
    public void FbCNYInsert(BigDecimal money) {
        fbCNYDao.FbCnyInsert(money);
    }
}
