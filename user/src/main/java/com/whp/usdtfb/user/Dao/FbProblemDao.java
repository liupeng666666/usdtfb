package com.whp.usdtfb.user.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/4 15:48
 * @descrpition :
 */
@Mapper
public interface FbProblemDao {

    /**
     * @param map
     * @return
     */
    public List<Map<String, Object>> FbProblemSelect(Map<String, Object> map);

    /**
     * @param map
     */
    public void FbProblemInsert(Map<String, Object> map);

    /**
     * @param pid
     * @return
     */
    public Map<String, Object> FbProblemDan(@Param("pid") String pid);
}
