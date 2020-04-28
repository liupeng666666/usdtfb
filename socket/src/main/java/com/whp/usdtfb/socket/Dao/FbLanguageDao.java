package com.whp.usdtfb.socket.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/3 4:35
 * @descrpition :
 */
@Mapper
public interface FbLanguageDao {

    /**
     * @param userid
     * @return
     */
    public List<Map<String, Object>> FbLanguageSelect(@Param("userid") String userid);

    /**
     * @param map
     */
    public void FbLanguageInsert(Map<String, Object> map);
}

