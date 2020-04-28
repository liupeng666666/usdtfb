package com.whp.usdtfb.user.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/10 12:15
 * @descrpition :
 */
@Mapper
public interface SubUserDao {

    /**
     * 根据用户名查询用户信息 仅程序内部使用
     *
     * @param username
     * @return
     */
    Map<String, Object> getSubUserByUserName(@Param("username") String username);

    /**
     * @param map
     * @return
     */
    public int SubUserCount(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    public Map<String, Object> SubUserGoogle(Map<String, Object> map);
}
