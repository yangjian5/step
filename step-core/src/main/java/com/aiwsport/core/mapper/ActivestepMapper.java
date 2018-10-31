package com.aiwsport.core.mapper;


import com.aiwsport.core.entity.Activestep;

import java.util.List;

public interface ActivestepMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Activestep record);

    Activestep selectByPrimaryKey(Integer id);

    List<Activestep> selectAll();

    int updateByPrimaryKey(Activestep record);

    Activestep selectByUserIdAndType(Integer UserId, String type);

    List<Activestep> selectByUserId(Integer UserId);

    List<Activestep> selectByType(String type);

    int selectByTypeCount(String type);
}