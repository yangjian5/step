package com.aiwsport.core.mapper;

import com.aiwsport.core.entity.GoodChangeLog;
import java.util.List;

public interface GoodChangeLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodChangeLog record);

    GoodChangeLog selectByPrimaryKey(Integer id);

    List<GoodChangeLog> selectByGoodId(Integer goodId);

    List<GoodChangeLog> selectByUserId(Integer UserId);

    List<GoodChangeLog> selectAll();

    int updateByPrimaryKey(GoodChangeLog record);
}