package net.industryhive.controller;

import net.industryhive.bean.Topic;
import net.industryhive.service.ContentService;
import net.industryhive.utils.UnifiedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 内容处理Controller
 * @author 未央
 * @create 2019-11-01 14:29
 */
@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping("/submitTopic")
    @ResponseBody
    public UnifiedResult submitTopic(Topic newTopic){
//        contentService.addTopic
        return UnifiedResult.ok();
    }

}
