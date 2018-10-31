package com.aiwsport.core.mapper;


import com.aiwsport.core.entity.Activedata;

import java.util.List;

public interface ActivedataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Activedata record);

    Activedata selectByPrimaryKey(Integer id);

    List<Activedata> selectAll();

    int updateByPrimaryKey(Activedata record);

    Activedata selectByActiveStepId(Integer activeStepId);

    List<Activedata> selectTop(String type);

    int selectFinishCount(String type, Integer dayStep);

    int selectFinish5Count(String type);

    int selectCount(Integer sumStep);
}