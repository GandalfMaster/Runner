package com.back.mapper;

import com.back.annotation.AutoFill;
import com.back.entity.User;
import com.back.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from user where open_id = #{openid}")
    User getByOpenid(String openid);

    /**
     * 插入数据
     * @param user
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(User user);

    /**
     * 更新用户数据
     * @param user
     */
    //@AutoFill(value = OperationType.UPDATE)
    void update(User user);
}
