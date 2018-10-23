package com.aiwsport.core.mapper;



import com.aiwsport.core.entity.Reply;

import java.util.List;

public interface ReplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Reply record);

    Reply selectByPrimaryKey(Integer id);

    List<Reply> selectAll();

    int updateByPrimaryKey(Reply record);
}