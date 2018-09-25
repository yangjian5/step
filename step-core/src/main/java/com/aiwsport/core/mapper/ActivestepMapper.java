package com.aiwsport.core.mapper;



import com.aiwsport.core.entity.Activestep;

import java.util.List;

public interface ActivestepMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Activestep record);

    Activestep selectByPrimaryKey(Integer id);

    List<Activestep> selectAll();

    int updateByPrimaryKey(Activestep record);

    Activestep selectByUserId(Integer userId);

    List<Activestep> selectTop();
}