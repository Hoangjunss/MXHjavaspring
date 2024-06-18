package com.baconbao.mxh.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//tao cloud luu anh
@Service
public class CloudinaryService {
   public Cloudinary cloudinary;

   //contructor 
    public CloudinaryService() {
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("cloud_name", "dgts7tmnb");
        valuesMap.put("api_key", "397588242457691");
        valuesMap.put("api_secret", "oWxqnyZyL43qKZx93bYgQpGfNfI");
        cloudinary = new Cloudinary(valuesMap);
    }

    //tai hinh anh len cloud 
    public Map upload(MultipartFile multipartFile) throws IOException {
        File file = convert(multipartFile);
        Map result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        if (!Files.deleteIfExists(file.toPath())) {
            throw new IOException("Failed to delete temporary file: " + file.getAbsolutePath());
        }
        return result;
    }

    //xoa anh tren cloud
    public Map delete(String id) throws IOException {
        return cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
    }

    //chuyen anh thanh file
    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }
    public String getImageUrl(String publicId) {
        return cloudinary.url().generate(publicId);
    }

}
