package net.industryhive.service;

import net.industryhive.bean.*;
import net.industryhive.been.wrap.WrapReply;
import net.industryhive.been.wrap.WrapTopic;
import net.industryhive.dao.ReplyMapper;
import net.industryhive.dao.TopicMapper;
import net.industryhive.dao.UserMapper;
import net.industryhive.entity.UnifiedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 内容处理Service
 *
 * @author 未央
 * @create 2019-11-01 14:30
 */
@Service
public class ContentService {

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 发帖
     * @param newTopic
     * @return
     */
    @Transactional(timeout = 5)
    public Topic addTopic(Topic newTopic) {
        topicMapper.insertSelective(newTopic);
        //帖子作者的发帖量+1
        userMapper.updateTopicCountByPrimaryKey(newTopic.getUserId());
        return getTopic(newTopic.getId());
    }

    public Topic getTopic(int id) {
        Topic topic = topicMapper.selectByPrimaryKey(id);
        return topic;
    }

    /**
     * 获取帖子详情，携带帖子作者名
     * 同时将帖子的查看量+1
     *
     * @param id
     * @return
     */
    public WrapTopic getWrapTopic(int id) {
        WrapTopic wrapTopic = topicMapper.findWithUsername(id);
        //如果帖子不存在或已被删除，则返回空
        if (wrapTopic == null || wrapTopic.getDeleted()) {
            return null;
        }
        topicMapper.updateViewCountByPrimaryKey(wrapTopic.getId());
        return wrapTopic;
    }

    /**
     * 获取包装帖子列表
     *
     * @param page
     * @return
     */
    public List<WrapTopic> getWrapTopicList(Integer page) {
        int startRow = (page - 1) * 45;
        List<WrapTopic> wrapTopicList = topicMapper.findListWithUsername(startRow);
        return wrapTopicList;
    }

    /**
     * 获取帖子数量
     *
     * @return
     */
    public long getTopicCount() {
        TopicExample example = new TopicExample();
        long topicCount = topicMapper.countByExample(example);
        return topicCount;
    }

    public long getReplyCountByTopicId(int topicId) {
        ReplyExample example = new ReplyExample();
        ReplyExample.Criteria criteria = example.createCriteria();
        criteria.andTopicIdEqualTo(topicId);
        long replyCount = replyMapper.countByExample(example);
        return replyCount;
    }

    /**
     * 获取帖子回复列表
     *
     * @param topicId
     * @param page
     * @return
     */
    public List<WrapReply> getWrapReplyList(int topicId, int page) {
        int startRow = (page - 1) * 50;
        List<WrapReply> wrapReplyList = replyMapper.findWithUsername(topicId, startRow);
        //遍历回复列表，如果回复已被删除，则将内容替换为“该回复以被删除”
        for (WrapReply wrapReply : wrapReplyList) {
            if (wrapReply.getDeleted()) {
                wrapReply.setContent("<p style='font-style:oblique'>该回复已被删除</p>");
            }
        }
        return wrapReplyList;
    }

    /**
     * 获取帖子回复列表
     *
     * @param topicId
     * @return
     */
    public List<Reply> getReplyList(int topicId) {
        ReplyExample example = new ReplyExample();
        ReplyExample.Criteria criteria = example.createCriteria();
        criteria.andTopicIdEqualTo(topicId);
        List<Reply> replyList = replyMapper.selectByExampleWithBLOBs(example);
        return replyList;
    }

    /**
     * 新增回复
     *
     * @param newReply
     */
    @Transactional(timeout = 5)
    public UnifiedResult addReply(Reply newReply) {
        Topic topic = topicMapper.selectByPrimaryKeyForUpdate(newReply.getTopicId());
        //如果帖子不存在或已被删除，则禁止回复
        if (topic == null || topic.getDeleted()) {
            return UnifiedResult.build(400, "帖子不存在", null);
        }
        //如果帖子被锁定，则禁止回复
        if (topic.getLocked()) {
            return UnifiedResult.build(400, "帖子已被锁定，无法回复", null);
        }
        //如果引用的回复不存在，则禁止回复
        if (newReply.getQuote() != 0) {
            Reply quoteReply = getReplyByTopicIdAndFloor(newReply.getTopicId(), newReply.getQuote());
            if (quoteReply == null) {
                return UnifiedResult.build(400, "引用的回复不存在", null);
            }
        }

        int replyCount = topic.getReplycount();

        newReply.setFloor(replyCount + 1);
        replyMapper.insertSelective(newReply);

        topicMapper.updateReplyCountByPrimaryKey(topic.getId());
        return UnifiedResult.ok();
    }

    public Reply getReplyByTopicIdAndFloor(int topicId, int floor) {
        ReplyExample example = new ReplyExample();
        ReplyExample.Criteria criteria = example.createCriteria();
        criteria.andTopicIdEqualTo(topicId);
        criteria.andFloorEqualTo(floor);
        List<Reply> replyList = replyMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(replyList)) {
            return null;
        }
        return replyList.get(0);
    }
}
