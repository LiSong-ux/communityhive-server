package net.industryhive.service;

import net.industryhive.bean.Reply;
import net.industryhive.bean.ReplyExample;
import net.industryhive.bean.Topic;
import net.industryhive.been.wrap.WrapTopic;
import net.industryhive.dao.ReplyMapper;
import net.industryhive.dao.TopicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public WrapTopic getWrapTopic(int id){
        WrapTopic wrapTopic = topicMapper.findWithUsername(id);
        return wrapTopic;
    }

    /**
     * 获取帖子回复列表
     * @param topicId
     * @return
     */
    public List<Reply> getReplyList(int topicId){
        ReplyExample example = new ReplyExample();
        ReplyExample.Criteria criteria = example.createCriteria();
        criteria.andTopicIdEqualTo(topicId);
        List<Reply> replyList = replyMapper.selectByExampleWithBLOBs(example);
        return replyList;
    }

    /**
     * 新增回复
     * @param newReply
     */
    public void addReply(Reply newReply) {
        Topic topic = topicMapper.selectByPrimaryKey(newReply.getTopicId());
        newReply.setFloor(topic.getReplycount()+1);
        replyMapper.insertSelective(newReply);
    }
}
