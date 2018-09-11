package com.aiwsport.core.mapper;

import com.aiwsport.core.entity.Address;
import java.util.List;

public interface AddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Address record);

    Address selectByPrimaryKey(Integer id);

    List<Address> selectAll();

    int updateByPrimaryKey(Address record);
}