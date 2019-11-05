package net.industryhive.service;

import net.industryhive.bean.Access;
import net.industryhive.dao.AccessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 访问统计Service
 * @author 未央
 * @create 2019-11-04 20:35
 */
@Service
public class AccessService {

    @Autowired
    private AccessMapper accessMapper;

    public void addAccess(Access newAccess){
        accessMapper.insert(newAccess);
    }

}
