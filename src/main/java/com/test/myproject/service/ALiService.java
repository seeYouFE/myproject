package com.test.myproject.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import com.test.myproject.util.MyProjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class ALiService {
    private static final Logger logger = LoggerFactory.getLogger(ALiService.class);

    private static String endpoint = "oss-cn-beijing.aliyuncs.com";
    private static String accessKeyId = "LTAIseQmccXuuke1";
    private static String accessKeySecret = "JK7JG1XkiijXtmg2qwiR1rIrfmOfbq";

    private static String bucketName = "syroom";
    OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);

    public String saveImage(MultipartFile file)throws IOException {
        try{
            int doPos=file.getOriginalFilename().lastIndexOf(".");
            if(doPos<0){
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(doPos+1).toLowerCase();
            if(!MyProjectUtil.isFileAllowed(fileExt)){
                return null;
            }

            String fileName = UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;
            String key =fileName;
            PutObjectResult putObjectResult=client.putObject(bucketName, key, new ByteArrayInputStream(file.getBytes()));
            return MyProjectUtil.ALi_DOMAIN+key;

        }catch(Exception e){
            logger.error("阿里"+e.getMessage());
            return null;
        }

    }
}
