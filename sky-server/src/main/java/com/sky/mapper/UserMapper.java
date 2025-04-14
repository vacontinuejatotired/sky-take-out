package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper  {
    void insert(User user);

    @Select("select * from sky_take_out.user where openid=#{openid}")
    User selectByOpenid(String openid);
}
