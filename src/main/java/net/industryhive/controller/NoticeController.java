package net.industryhive.controller;

import net.industryhive.bean.wrap.WrapNotice;
import net.industryhive.entity.UnifiedResult;
import net.industryhive.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 未央
 * @create 2020-01-19 16:02
 */
@RestController
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @RequestMapping("/noticeList")
    public UnifiedResult getNoticeList() {
        List<WrapNotice> wrapNoticeList = noticeService.getWrapNoticeList();
        return UnifiedResult.ok(wrapNoticeList);
    }

    @RequestMapping("/notice")
    public UnifiedResult getNotice(Integer id) {
        if (id == null) {
            return UnifiedResult.build(400, "参数错误", null);
        }
        WrapNotice wrapNotice = noticeService.getWrapNotice(id);
        if (wrapNotice == null) {
            return UnifiedResult.build(400, "公告不存在", null);
        }
        return UnifiedResult.ok(wrapNotice);
    }

}
