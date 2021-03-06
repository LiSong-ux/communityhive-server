package net.industryhive.controller;

import net.industryhive.bean.Reply;
import net.industryhive.bean.Topic;
import net.industryhive.bean.User;
import net.industryhive.bean.model.ReplyModel;
import net.industryhive.bean.model.TopicModel;
import net.industryhive.bean.wrap.WrapReply;
import net.industryhive.bean.wrap.WrapTopic;
import net.industryhive.entity.UnifiedResult;
import net.industryhive.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;
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
        if (user.getLocked()) {
            return UnifiedResult.build(400, "您已被封禁，无法发帖", null);
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

        //设置帖子作者id
        newTopic.setUser_id(user.getId());
        //设置发布时间
        newTopic.setSubmitTime(new Date());
        //将帖子写入数据库
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
        if (user.getLocked()) {
            return UnifiedResult.build(400, "您已被封禁，无法回复", null);
        }

        //对回复内容进行校验，替换富文本编辑器自动添加的html字符为空字符串
        //如果替换后的内容长度为0，则返回错误信息
        String validate = newReply.getContent();
        String validateA = validate.replaceAll(" ", "");
        String validateB = validateA.replaceAll("<p>", "");
        String validateC = validateB.replaceAll("</p>", "");
        String validateD = validateC.replaceAll("&nbsp;", "");
        String validateE = validateD.replaceAll("<br>", "");
        if (validateE.length() == 0) {
            return UnifiedResult.build(400, "回复内容不能为空！", null);
        }

        //对回复内容长度进行校验，如果内容长度超过11264，则返回错误信息
        if (newReply.getContent().length() > 11264) {
            return UnifiedResult.build(400, "回复的长度不得超过11000个字符", null);
        }

        //设置回复作者id
        newReply.setUser_id(user.getId());
        //设置发布时间
        newReply.setSubmitTime(new Date());
        //将回复写入数据库
        UnifiedResult result = contentService.addReply(newReply);
        return result;
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

        TopicModel topicModel = getTopicModel(wrapTopic);

        List<WrapReply> wrapReplyList = contentService.getWrapReplyList(id, page);

        ArrayList<ReplyModel> replyModelList = new ArrayList<>();
        wrapReplyList.forEach(wrapReply -> {
            ReplyModel replyModel = getReplyModel(wrapReply);
            replyModelList.add(replyModel);
        });

        long replyCount = contentService.getReplyCountByTopicId(id);

        topicMap.put("topic", topicModel);
        topicMap.put("replyList", replyModelList);
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

        /*
            将包装类中的一部分属性封装到响应实体模型中返回
         */
        ArrayList<TopicModel> topicModelList = new ArrayList<>();
        wrapTopicList.forEach(wrapTopic -> {
            TopicModel topicModel = getTopicModel(wrapTopic);
            topicModelList.add(topicModel);
        });

        long topicCount = contentService.getTopicCount();
        map.put("topicList", topicModelList);
        map.put("topicCount", topicCount);
        return UnifiedResult.ok(map);
    }

    /**
     * 将WrapTopic封装到TopicModel
     *
     * @param wrapTopic
     * @return
     */
    public TopicModel getTopicModel(WrapTopic wrapTopic) {
        TopicModel topicModel = new TopicModel();
        topicModel.setId(wrapTopic.getId());
        topicModel.setLabel(wrapTopic.getLabel());
        topicModel.setTitle(wrapTopic.getTitle());
        if (wrapTopic.getContent() != null) {
            topicModel.setContent(wrapTopic.getContent());
        }
        topicModel.setUsername(wrapTopic.getUsername());
        topicModel.setSubmitTime(wrapTopic.getSubmitTime());
        if (wrapTopic.getLastReply() != null) {
            topicModel.setLastReply(wrapTopic.getLastReply());
        }
        if (wrapTopic.getLastSubmit() != null) {
            topicModel.setLastSubmit(wrapTopic.getLastSubmit());
        }
        topicModel.setReplyCount(wrapTopic.getReplyCount());
        topicModel.setViewCount(wrapTopic.getViewCount());
        return topicModel;
    }

    /**
     * 将WrapReply封装到ReplyModel
     *
     * @param wrapReply
     * @return
     */
    public ReplyModel getReplyModel(WrapReply wrapReply) {
        ReplyModel replyModel = new ReplyModel();
        replyModel.setUsername(wrapReply.getUsername());
        replyModel.setSubmitTime(wrapReply.getSubmitTime());
        replyModel.setFloor(wrapReply.getFloor());
        replyModel.setQuote(wrapReply.getQuote());
        replyModel.setContent(wrapReply.getContent());
        return replyModel;
    }

}
