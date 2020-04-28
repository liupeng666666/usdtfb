package com.whp.usdtfb.user.Dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/12/30 16:22
 * @descrpition :
 */
@Mapper
public interface FbScoreDao {
    public Map<String, Object> FbScoreSelect();

    public List<Map<String, Object>> FbScoreSelectQ();
}
