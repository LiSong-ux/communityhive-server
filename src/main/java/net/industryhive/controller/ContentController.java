package net.industryhive.controller;

import net.industryhive.bean.Reply;
import net.industryhive.bean.Topic;
import net.industryhive.bean.User;
import net.industryhive.been.wrap.WrapTopic;
import net.industryhive.service.ContentService;
import net.industryhive.service.UserService;
import net.industryhive.utils.UnifiedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内容处理Controller
 * @author 未央
 * @create 2019-11-01 14:29
 */
@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private UserService userService;

    @RequestMapping("/submitTopic")
    @ResponseBody
    public UnifiedResult submitTopic(HttpSession session, Topic newTopic){
        User user = (User) session.getAttribute("user");
        if (user==null){
            return UnifiedResult.build(400, "请登录后再发帖！", null);
        }

        newTopic.setUserId(user.getId());
        newTopic.setSubmittime(new Date());
        Topic topic = contentService.addTopic(newTopic);
        return UnifiedResult.ok(topic.getId());
    }

    @RequestMapping("/topic")
    @ResponseBody
    public UnifiedResult topic(int id){
        Map<String, Object> topicMap = new HashMap<>();

        WrapTopic wrapTopic = contentService.getWrapTopic(id);
        List<Reply> replyList = contentService.getReplyList(id);

        topicMap.put("topic", wrapTopic);
        topicMap.put("replyList", replyList);
        return UnifiedResult.ok(topicMap);
    }

}
