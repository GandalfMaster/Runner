package com.back.controller.user;


import com.back.constant.JwtClaimsConstant;
import com.back.dto.UserDTO;
import com.back.dto.UserLoginDTO;
import com.back.entity.Order;
import com.back.entity.User;
import com.back.properties.JwtProperties;
import com.back.result.Result;
import com.back.service.RiderService;
import com.back.service.UserService;
import com.back.utils.JwtUtil;
import com.back.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private RiderService riderService;

 /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/register")
    @ApiOperation("微信注册")
    public Result<UserLoginVO> register(@RequestBody UserLoginDTO userLoginDTO){

        log.info("微信用户注册：{}", userLoginDTO.getCode());

        //微信登录
        User user = userService.wxRegister(userLoginDTO);//后绪步骤实现

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

    @PostMapping("/login")
    @ApiOperation("微信登录")
    public Result<UserDTO> login(@RequestBody UserDTO userDTO){
        String userName = userDTO.getUsername();
        UserDTO user = userService.wxLogin(userName);
        log.info("用户信息：{}", user);
        if(user == null){
            return Result.error("不存在该用户");
        }

        return Result.success(user);
    }




    @PutMapping
    @ApiOperation("修改用户信息")
    public Result update(@RequestBody UserDTO userDTO){

        log.info("修改用户信息：{}", userDTO);

        List<Order> orderDoneInfo = riderService.getOrderDoneInfo(userDTO.getUserId());
        int userStar = 0;
        double sum = 0;

        // 计算所有订单的总评分
        for (Order order : orderDoneInfo) {
            sum += order.getOrderStar();
        }

        // 计算平均评分，假设订单数大于0
        if (orderDoneInfo.size() > 0) {
            userStar = (int) Math.round(sum / orderDoneInfo.size()); // 四舍五入
        }

        // 更新用户信息
        userDTO.setUserStar(userStar);
        if(userService.update(userDTO))return Result.success();
        else return Result.error("已存在该用户");


    }


    @GetMapping("/{id}")
    @ApiOperation("查询用户信息")
    public Result<UserDTO> getUserById(@PathVariable("id") Long userId) {
        UserDTO userDTO = userService.getUserInfo(userId);
        return Result.success(userDTO);
    }


}
