package net.industryhive.service;

import net.industryhive.bean.User;
import net.industryhive.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 未央
 * @create 2019-10-30 10:01
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public String addUser(User newUser) {
        userMapper.insertSelective(newUser);
        return "success";
    }

}
