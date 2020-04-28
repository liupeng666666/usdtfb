package com.whp.usdtfb.socket.Dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/3 3:19
 * @descrpition :
 */
@Mapper
public interface FbSocketDao {

    /**
     * 查询
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> FbSocketSelect(Map<String, Object> map);

    /**
     * 插入
     *
     * @param map
     */
    public void FbSocketInsert(Map<String, Object> map);

    /**
     * 修改已读
     *
     * @param map
     */
    public void FbSocketUpdate(Map<String, Object> map);

    /**
     * 查询未读人员
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> FbSocketWeiDu(Map<String, Object> map);
}
