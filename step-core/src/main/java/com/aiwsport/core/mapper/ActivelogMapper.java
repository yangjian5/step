package com.aiwsport.core.mapper;


import com.aiwsport.core.entity.Activelog;

import java.util.List;

public interface ActivelogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Activelog record);

    Activelog selectByPrimaryKey(Integer id);

    List<Activelog> selectAll();

    int updateByPrimaryKey(Activelog record);
}