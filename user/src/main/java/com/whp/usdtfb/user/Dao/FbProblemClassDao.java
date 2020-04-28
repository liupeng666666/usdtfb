package com.whp.usdtfb.user.Dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2019/1/4 15:15
 * @descrpition :
 */
@Mapper
public interface FbProblemClassDao {

    public List<Map<String, Object>> FbProblemClassSelect();
}
