package net.industryhive.service;

import net.industryhive.bean.wrap.WrapNotice;
import net.industryhive.dao.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 未央
 * @create 2020-01-19 16:03
 */
@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    public List<WrapNotice> getWrapNoticeList() {
        List<WrapNotice> wrapNoticeList = noticeMapper.findListWithUsername();
        return wrapNoticeList;
    }

    public WrapNotice getWrapNotice(int id) {
        WrapNotice wrapNotice = noticeMapper.findWithUsername(id);
        if (wrapNotice == null || wrapNotice.getDeleted()) {
            return null;
        }
        noticeMapper.updateViewCountByPrimaryKey(id);
        return wrapNotice;
    }

}
