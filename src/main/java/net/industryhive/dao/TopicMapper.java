package net.industryhive.dao;

import net.industryhive.bean.Topic;
import net.industryhive.bean.TopicExample;
import net.industryhive.bean.wrap.WrapTopic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TopicMapper {
    long countByExample(TopicExample example);

    int deleteByExample(TopicExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Topic record);

    int insertSelective(Topic record);

    List<Topic> selectByExampleWithBLOBs(TopicExample example);

    List<Topic> selectByExample(TopicExample example);

    Topic selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Topic record, @Param("example") TopicExample example);

    int updateByExampleWithBLOBs(@Param("record") Topic record, @Param("example") TopicExample example);

    int updateByExample(@Param("record") Topic record, @Param("example") TopicExample example);

    int updateByPrimaryKeySelective(Topic record);

    int updateByPrimaryKeyWithBLOBs(Topic record);

    int updateByPrimaryKey(Topic record);

    /**********************************************************************************/

    List<WrapTopic> findListWithUsername(int startRow);

    WrapTopic findWithUsername(int id);

    Topic selectByPrimaryKeyForUpdate(int id);

    int updateViewCountByPrimaryKey(int id);

    int updateReplyCountByPrimaryKey(int id);

}
