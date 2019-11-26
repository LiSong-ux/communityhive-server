package net.industryhive.service;

import net.industryhive.bean.Reply;
import net.industryhive.bean.ReplyExample;
import net.industryhive.bean.Topic;
import net.industryhive.bean.TopicExample;
import net.industryhive.been.wrap.WrapReply;
import net.industryhive.been.wrap.WrapTopic;
import net.industryhive.dao.ReplyMapper;
import net.industryhive.dao.TopicMapper;
import net.industryhive.utils.UnifiedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Topic addTopic(Topic newTopic) {
        topicMapper.insertSelective(newTopic);
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
        //如果帖子已被删除，则返回空
        if (wrapTopic.getDeleted()) {
            return null;
        }
        topicMapper.updateViewCountByPrimaryKey(wrapTopic.getId());
        return wrapTopic;
    }


    public List<WrapTopic> getWrapTopicList(Integer page) {
        int startRow = (page - 1) * 45;
        List<WrapTopic> wrapTopicList = topicMapper.findListWithUsername(startRow);
        return wrapTopicList;
    }


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


    public List<WrapReply> getWrapReplyList(int topicId, int page) {
        int startRow = (page - 1) * 50;
        List<WrapReply> wrapReplyList = replyMapper.findWithUsername(topicId, startRow);
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
        //如果帖子被锁定，则禁止回复
        if (topic.getDeleted()) {
            return UnifiedResult.build(400, "帖子不存在", null);
        }
        //如果帖子被删除，则禁止回复
        if (topic.getLocked()) {
            return UnifiedResult.build(400, "帖子已被锁定，无法回复", null);
        }

        int replyCount = topic.getReplycount();

        newReply.setFloor(replyCount + 1);
        replyMapper.insertSelective(newReply);

        topicMapper.updateReplyCountByPrimaryKey(topic.getId());
        return UnifiedResult.ok();
    }
}
