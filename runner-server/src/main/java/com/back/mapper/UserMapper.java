package com.back.mapper;

import com.back.annotation.AutoFill;
import com.back.dto.UserDTO;
import com.back.entity.User;
import com.back.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
     * 根据openid查询用户
     * @param user_id
     * @return
     */
    @Select("select * from user where user_id = #{userid}")
    User getByUserid(Long user_id);

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

    @Select("select * from user")
    List<UserDTO> getAllUsers();

    @Select("select user_id from user where user_name = #{userName}")
    Long getUserIdByUserName(String userName);
}
