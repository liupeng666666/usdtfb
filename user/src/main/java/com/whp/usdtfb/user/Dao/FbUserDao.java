package com.whp.usdtfb.user.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/18 14:33
 * @descrpition :
 */
@Mapper
public interface FbUserDao {

    /**
     * @param pid
     * @return
     */
    public Map<String, Object> FbUserSelect(@Param("pid") String pid);

    /**
     * @param map
     */
    public void FbUserUpdate(Map<String, Object> map);

    /**
     * @param map
     */
    public void FbUserUpdateQ(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    public int FbUserReputation(Map<String, Object> map);
}
