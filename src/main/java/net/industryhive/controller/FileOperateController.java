package net.industryhive.controller;

import net.industryhive.client.service.FileOperator;
import net.industryhive.entity.UploadResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public UploadResult uploadImg(@RequestParam("file") MultipartFile[] uploadImgs) {
        if (uploadImgs == null || uploadImgs.length == 0) {
            return null;
        }
        ArrayList<String> data = new ArrayList<>();
        try {
            for (MultipartFile uploadImg : uploadImgs) {
                FileInputStream fis = (FileInputStream) uploadImg.getInputStream();
                String filePath = FileOperator.upload(uploadImg.getOriginalFilename(), fis);
                data.add("http://inhive.net/" + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new UploadResult(0, data);
    }
}
