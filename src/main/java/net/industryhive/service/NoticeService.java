package net.industryhive.service;

import net.industryhive.dao.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 未央
 * @create 2020-01-19 16:03
 */
@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

}
