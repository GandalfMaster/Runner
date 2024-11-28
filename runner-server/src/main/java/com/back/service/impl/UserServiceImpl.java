package com.back.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.back.constant.MessageConstant;
import com.back.dto.UserDTO;
import com.back.dto.UserLoginDTO;
import com.back.entity.Ticket;
import com.back.entity.User;
import com.back.exception.AccountNotFoundException;
import com.back.exception.LoginFailedException;
import com.back.mapper.TicketMapper;
import com.back.mapper.UserMapper;
import com.back.properties.WeChatProperties;
import com.back.service.UserService;
import com.back.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    //微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TicketMapper ticketMapper;




    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @Transactional
    public User wxRegister(UserLoginDTO userLoginDTO) {
        String openid = getOpenid(userLoginDTO.getCode());

        //判断openid是否为空，如果为空表示登录失败，抛出业务异常
        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //判断当前用户是否为新用户
        User user = userMapper.getByOpenid(openid);

        //如果是新用户，自动完成注册
        if(user == null){
            user = User.builder()
                    .openId(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            try{
                userMapper.insert(user);
            }catch (Exception e){
                throw new LoginFailedException(MessageConstant.USERNAME_EXIST);
            }

            //后绪步骤实现
        }else{
            throw new LoginFailedException(MessageConstant.LOGIN_EXIST);
        }

        //返回这个用户对象
        return user;
    }

    /**
     * 调用微信接口服务，获取微信用户的openid
     * @param code
     * @return
     */
    private String getOpenid(String code){
        //调用微信接口服务，获得当前微信用户的openid
        Map<String, String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);

        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }

    @Transactional
    public void update(UserDTO userDTO){
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        if(user.getUserId() == null){
            userMapper.insert(user);
            BeanUtils.copyProperties(user, userDTO);
        }else {
            userMapper.update(user);
        }


        Long userId = userDTO.getUserId();
        ticketMapper.deleteById(userId);
        List<Ticket> ticketList = userDTO.getTicketList();
        if(ticketList.size() != 0){
            ticketList.forEach(ticket -> ticket.setUserId(userId));
            ticketMapper.insertBatch(ticketList);
        }

    }

    /**
     * 根据用户 ID 查询用户信息
     *
     * @param userid 用户 ID
     * @return 返回用户信息或错误
     */
    public UserDTO getUserInfo(Long userid){
        User user = userMapper.getByUserid(userid);
        if(user == null){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_FAILED);
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setTicketList(ticketMapper.getByUserid(userid));


        return userDTO;
    }


    public List<UserDTO> getAllUsers(){
        List<UserDTO> userList = userMapper.getAllUsers();
        userList.forEach(userDTO -> userDTO.setTicketList(ticketMapper.getByUserid(userDTO.getUserId()) ));

        return userList;
    }


    public UserDTO getUserDTOById(Long userId){
        User user = userMapper.getByUserid(userId);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setTicketList(ticketMapper.getByUserid(userId));
        return userDTO;
    }


    public UserDTO wxLogin(String userName) {
        Long userId = userMapper.getUserIdByUserName(userName);
        UserDTO user = getUserDTOById(userId);
        if(user == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        return user;
    }
}
