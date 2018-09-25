package com.aiwsport.core.mapper;


import com.aiwsport.core.entity.Activext;

import java.util.List;

public interface ActivextMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Activext record);

    Activext selectByPrimaryKey(Integer id);

    List<Activext> selectAll();

    int updateByPrimaryKey(Activext record);

    List<Activext> selectByUserAndZanUser(Integer userId, Integer zanUserId);

    List<Activext> selectByUserId(Integer userId);
}