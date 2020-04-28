package com.whp.usdtfb.user.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/18 16:49
 * @descrpition :
 */
@Mapper
public interface FbPlayDao {

    /**
     * @param map
     * @return
     */
    public List<Map<String, Object>> FbPlaySelect(Map<String, Object> map);

    /**
     * @param map
     */
    public void FbPlayInsert(Map<String, Object> map);

    /**
     * @param map
     */
    public void FbPlayUpdate(Map<String, Object> map);

    /**
     * @return
     */
    public List<Map<String, Object>> FbBankSelect();

    /**
     * @param map
     * @return
     */
    public int FbPlayCount(Map<String, Object> map);
}
