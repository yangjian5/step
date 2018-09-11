package com.aiwsport.core.mapper;

import com.aiwsport.core.entity.User;
import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    User getByOpenId(String openId);

    int insert(User record);

    User selectByPrimaryKey(Integer id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);
}