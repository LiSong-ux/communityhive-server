package net.industryhive.controller;

import net.industryhive.entity.UnifiedResult;
import net.industryhive.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author 未央
 * @create 2020-01-19 16:02
 */
@Controller
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    public UnifiedResult getNoticeList(){
        return UnifiedResult.ok();
    }

}
