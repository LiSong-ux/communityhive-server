package net.industryhive.service;

import net.industryhive.bean.User;
import net.industryhive.bean.UserExample;
import net.industryhive.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 未央
 * @create 2019-10-30 10:01
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User addUser(User newUser) {
        userMapper.insertSelective(newUser);
        return getUser(newUser.getAccount());
    }

    public User getUser(String account) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andAccountEqualTo(account);
        List<User> listUser = userMapper.selectByExample(example);
        if (listUser.isEmpty()) {
            return null;
        }
        User user = listUser.get(0);
        return user;
    }

}
