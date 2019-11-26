package net.industryhive.controller;

import net.industryhive.bean.Reply;
import net.industryhive.bean.Topic;
import net.industryhive.bean.User;
import net.industryhive.been.wrap.WrapReply;
import net.industryhive.been.wrap.WrapTopic;
import net.industryhive.service.ContentService;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 内容处理Controller
 *
 * @author 未央
 * @create 2019-11-01 14:29
 */
@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;

    /**
     * 发帖
     *
     * @param session
     * @param newTopic
     * @return
     */
    @RequestMapping("/submitTopic")
    @ResponseBody
    public UnifiedResult submitTopic(HttpSession session, Topic newTopic) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return UnifiedResult.build(400, "请登录后再发帖！", null);
        }

        String regLabel = "^[\\u4e00-\\u9fa5]{2,4}$";
        Pattern pattern = Pattern.compile(regLabel);
        Matcher matcher = pattern.matcher(newTopic.getLabel());
        if (!matcher.matches()) {
            return UnifiedResult.build(400, "帖子标签为2至4位汉字", null);
        }

        if (newTopic.getTitle().length() < 4 || newTopic.getTitle().length() > 35) {
            return UnifiedResult.build(400, "帖子标题的长度为4至35个字符", null);
        }

        String validate = newTopic.getContent();
        String validateA = validate.replaceAll(" ", "");
        String validateB = validateA.replaceAll("<p>", "");
        String validateC = validateB.replaceAll("</p>", "");
        String validateD = validateC.replaceAll("&nbsp;", "");
        String validateE = validateD.replaceAll("<br>", "");
        if (validateE.length() == 0) {
            return UnifiedResult.build(400, "帖子内容不能为空！", null);
        }

        if (newTopic.getContent().length() > 16384) {
            return UnifiedResult.build(400, "帖子内容的长度不得超过16000个字符", null);
        }

        newTopic.setUserId(user.getId());
        newTopic.setSubmittime(new Date());
        Topic topic = contentService.addTopic(newTopic);
        return UnifiedResult.ok(topic.getId());
    }

    /**
     * 回复
     *
     * @param session
     * @param newReply
     * @return
     */
    @RequestMapping("/submitReply")
    @ResponseBody
    public UnifiedResult submitReply(HttpSession session, Reply newReply) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return UnifiedResult.build(400, "请登录后再回复！", null);
        }

        String validate = newReply.getContent();
        String validateA = validate.replaceAll(" ", "");
        String validateB = validateA.replaceAll("<p>", "");
        String validateC = validateB.replaceAll("</p>", "");
        String validateD = validateC.replaceAll("&nbsp;", "");
        String validateE = validateD.replaceAll("<br>", "");
        if (validateE.length() == 0) {
            return UnifiedResult.build(400, "回复内容不能为空！", null);
        }


        if (newReply.getContent().length() > 11264) {
            return UnifiedResult.build(400, "回复的长度不得超过11000个字符", null);
        }

        newReply.setUserId(user.getId());
        newReply.setSubmittime(new Date());
        contentService.addReply(newReply);
        return UnifiedResult.ok();
    }

    /**
     * 获取帖子详情
     *
     * @param id
     * @return
     */
    @RequestMapping("/topic")
    @ResponseBody
    public UnifiedResult getTopic(Integer id, Integer page) {
        if (id == null || page == null) {
            return UnifiedResult.build(400, "参数错误", null);
        }
        Map<String, Object> topicMap = new HashMap<>();

        WrapTopic wrapTopic = contentService.getWrapTopic(id);
        if (wrapTopic == null) {
            return UnifiedResult.build(400, "帖子不存在", null);
        }
        List<WrapReply> wrapReplyList = contentService.getWrapReplyList(id, page);
        long replyCount = contentService.getReplyCountByTopicId(id);
        for (WrapReply wrapReply : wrapReplyList) {
            if (wrapReply.getQuote() != 0) {
                wrapReply.setQuoteIndex(wrapReply.getQuote() - 1);
            }
        }

        topicMap.put("topic", wrapTopic);
        topicMap.put("replyList", wrapReplyList);
        topicMap.put("replyCount", replyCount);
        return UnifiedResult.ok(topicMap);
    }

    /**
     * 获取帖子列表
     *
     * @return
     */
    @RequestMapping("/topicList")
    @ResponseBody
    public UnifiedResult getTopicList(Integer page) {
        if (page == null) {
            page = 1;
        }
        Map<String, Object> map = new HashMap<>();
        List<WrapTopic> wrapTopicList = contentService.getWrapTopicList(page);
        long topicCount = contentService.getTopicCount();
        map.put("topicList", wrapTopicList);
        map.put("topicCount", topicCount);
        return UnifiedResult.ok(map);
    }

}
