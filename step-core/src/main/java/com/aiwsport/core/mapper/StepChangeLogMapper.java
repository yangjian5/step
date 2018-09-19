package com.aiwsport.core.mapper;

import com.aiwsport.core.entity.StepChangeLog;
import java.util.List;

public interface StepChangeLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StepChangeLog record);

    StepChangeLog selectByPrimaryKey(Integer id);

    List<StepChangeLog> selectAll();

    int updateByPrimaryKey(StepChangeLog record);

    List<StepChangeLog> selectAByUserIdToday(Integer userId, String sTime, String eTime);
}