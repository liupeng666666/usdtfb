package com.whp.usdtfb.user.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/28 21:43
 * @descrpition :
 */
@Mapper
public interface FbOrderDao {

    /**
     * @param map
     */
    public void FbOrderInsert(Map<String, Object> map);

    /**
     * @param map
     */
    public void FbOrderLog(Map<String, Object> map);

    public Map<String, Object> FbOrderDan(Map<String, Object> map);

    /**
     * @param map
     */
    public void FbOrderUpdate(Map<String, Object> map);

    /**
     * @param userid
     * @param state
     * @param page
     * @param num
     * @return
     */
    public List<Map<String, Object>> FbOrderSelect(@Param("userid") String userid, @Param("state") int state, @Param("page") int page, @Param("num") int num);

    /**
     * @param userid
     * @param state
     * @return
     */
    public int FbOrderCount(@Param("userid") String userid, @Param("state") int state);

    /**
     * @param sellid
     * @return 查看进行中数量
     */
    public int FbOrderJx(@Param("sellid") String sellid);
}
