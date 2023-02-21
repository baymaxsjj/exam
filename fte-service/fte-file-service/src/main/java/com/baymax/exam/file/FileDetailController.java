package com.baymax.exam.file;

import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.FileStorageService;
import cn.xuyanwu.spring.file.storage.UploadPretreatment;
import com.baymax.exam.common.core.result.Result;
import com.baymax.exam.web.annotation.Inner;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ：Baymax
 * @date ：Created in 2023/2/5 8:36
 * @description：
 * @modified By：
 * @version:
 */

@Slf4j
@RestController
@RequestMapping("/files")
public class FileDetailController {

    @Autowired
    private FileStorageService fileStorageService;//注入实列

    /**
     * 上传文件，成功返回文件 url
     */
    @Inner
    @PostMapping("/upload")
    public String upload(MultipartFile file) {
        FileInfo fileInfo = fileStorageService.of(file)
                .setPath("upload/") //保存到相对路径下，为了方便管理，不需要可以不写
                .setObjectId("0")   //关联对象id，为了方便管理，不需要可以不写
                .setObjectType("0") //关联对象类型，为了方便管理，不需要可以不写
                .putAttr("role","user") //保存一些属性，可以在切面、保存上传记录、自定义存储平台等地方获取使用，不需要可以不写
                .upload();  //将文件上传到对应地方
        return fileInfo == null ? "上传失败！" : fileInfo.getUrl();
    }

    /**
     * 上传图片，成功返回文件信息
     * 图片处理使用的是 https://github.com/coobird/thumbnailator
     */
    @Inner
    @PostMapping("/upload-image")
    public Result uploadImage(@RequestPart("file") MultipartFile file,
                              @RequestParam(required = false) String path,
                              @RequestParam(required = false) String id,
                              @RequestParam(required = false) String type
                              ) {
         UploadPretreatment fileStorage = fileStorageService.of(file);
         if(path!=null){
             fileStorage.setPath(path);
         }
         if(id!=null){
             fileStorage.setObjectId("0") ;  //关联对象id，为了方便管理，不需要可以不写
         }
         if(type!=null){
             fileStorage.setObjectType("0"); //关联对象类型，为了方便管理，不需要可以不写
         }
        String url = fileStorage.upload().getUrl();
        return Result.success(url);
    }
    public void saveImage( MultipartFile file,String path,String id,String type){

    }
}
