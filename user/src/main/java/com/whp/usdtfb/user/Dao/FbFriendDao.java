package com.whp.usdtfb.user.Dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/3 13:43
 * @descrpition :
 */
@Mapper
public interface FbFriendDao {

    /**
     * @param map
     * @return
     */
    public int FbFriendCount(Map<String, Object> map);

    /**
     * @param map
     */
    public void FbFriendInsert(Map<String, Object> map);
}
