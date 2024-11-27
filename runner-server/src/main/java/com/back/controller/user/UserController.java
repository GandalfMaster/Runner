package com.back.controller.user;


import com.back.constant.JwtClaimsConstant;
import com.back.dto.UserDTO;
import com.back.dto.UserLoginDTO;
import com.back.entity.User;
import com.back.properties.JwtProperties;
import com.back.result.Result;
import com.back.service.UserService;
import com.back.utils.JwtUtil;
import com.back.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
@Api(tags = "用户端用户相关接口")
@Slf4j
public class UserController {
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserService userService;

 /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("微信登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){

        log.info("微信用户登录：{}", userLoginDTO.getCode());

        //微信登录
        User user = userService.wxLogin(userLoginDTO);//后绪步骤实现

        //为微信用户生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getUserId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getUserId())
                .openid(user.getOpenId())
                .token(token)
                .build();
        return Result.success(userLoginVO);

    }

    @PutMapping
    @ApiOperation("修改用户信息")
    public Result update(@RequestBody UserDTO userDTO){

        log.info("修改用户信息：{}", userDTO);
        userService.update(userDTO);
        return Result.success();
    }


    @GetMapping("/{id}")
    @ApiOperation("查询用户信息")
    public Result<UserDTO> getUserById(@PathVariable("id") Long userId) {
        UserDTO userDTO = userService.getUserInfo(userId);
        return Result.success(userDTO);
    }


}
