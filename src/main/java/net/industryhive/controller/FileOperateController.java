package net.industryhive.controller;

import net.industryhive.bean.User;
import net.industryhive.client.service.FileOperator;
import net.industryhive.entity.UploadResult;
import net.industryhive.utils.FileInspection;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 图片上传Controller
 *
 * @Author 未央
 * @Create 2020-11-01 18:10
 */
@RestController
public class FileOperateController {

    @PostMapping("/upload/img")
    public UploadResult uploadImg(HttpSession session, @RequestParam("file") MultipartFile[] uploadImages) {
        if (session.getAttribute("user") == null) {
            return new UploadResult(1, null);
        }
        if (uploadImages == null || uploadImages.length == 0) {
            return new UploadResult(2, null);
        }
        ArrayList<String> data = new ArrayList<>();
        try {
            for (MultipartFile uploadImg : uploadImages) {
                FileInputStream fis = (FileInputStream) uploadImg.getInputStream();

                String filename = uploadImg.getOriginalFilename();

                if (!FileInspection.inspect(filename)) {
                    return new UploadResult(3, null);
                }

                String filePath = FileOperator.upload(uploadImg.getOriginalFilename(), fis);
                data.add("http://inhive.net/" + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new UploadResult(0, data);
    }
}
