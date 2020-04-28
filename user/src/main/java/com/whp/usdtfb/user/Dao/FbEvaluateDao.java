package com.whp.usdtfb.user.Dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/30 19:49
 * @descrpition :
 */
@Mapper
public interface FbEvaluateDao {

    /**
     * @param map
     */
    public void FbEvakyateInsert(Map<String, Object> map);
}
