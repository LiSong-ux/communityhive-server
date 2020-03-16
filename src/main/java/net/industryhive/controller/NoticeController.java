package net.industryhive.controller;

import net.industryhive.bean.model.NoticeModel;
import net.industryhive.bean.wrap.WrapNotice;
import net.industryhive.entity.UnifiedResult;
import net.industryhive.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

        ArrayList<NoticeModel> noticeModelList = new ArrayList<>();
        wrapNoticeList.forEach(wrapNotice -> {
            NoticeModel noticeModel = getNoticeModel(wrapNotice);
            noticeModelList.add(noticeModel);
        });

        return UnifiedResult.ok(noticeModelList);
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

        NoticeModel noticeModel = getNoticeModel(wrapNotice);
        return UnifiedResult.ok(noticeModel);
    }

    public NoticeModel getNoticeModel(WrapNotice wrapNotice) {
        NoticeModel noticeModel = new NoticeModel();
        noticeModel.setId(wrapNotice.getId());
        noticeModel.setLabel(wrapNotice.getLabel());
        noticeModel.setTitle(wrapNotice.getTitle());
        if (wrapNotice.getContent() != null) {
            noticeModel.setContent(wrapNotice.getContent());
        }
        noticeModel.setUsername(wrapNotice.getUsername());
        noticeModel.setSubmitTime(wrapNotice.getSubmitTime());
        noticeModel.setLastEdit(wrapNotice.getLastEdit());
        noticeModel.setLastSubmitTime(wrapNotice.getLastSubmitTime());
        noticeModel.setViewCount(wrapNotice.getViewCount());
        return noticeModel;
    }

}
