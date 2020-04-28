package com.whp.usdtfb.Util.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * @author : 张吉伟
 * @data : 2018/7/14 18:17
 * @descrpition :
 */
public class ImgUtil {


    public static void Img(String url) {
        try {
            Thumbnails.of(url).scale(0.5f).toFile(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String url(MultipartFile file, int width, int height, int state) {
        String fileName = "";
        String originalFileName = "";
        String httpImgPath = "";
        try {
            if (!file.isEmpty()) {
                fileName = file.getOriginalFilename();
                originalFileName = fileName;

                String ext = (FilenameUtils.getExtension(file
                        .getOriginalFilename())).toLowerCase();
                String storePath = "";
                if ("jpg".equals(ext) || "png".equals(ext)
                        || "jpeg".equals(ext) || "bmp".equals(ext)) {
                    storePath = "/image/play/";
                } else {
                    storePath = "/video/play/";
                }
                if ("".equals(ext) || ext == null) {
                    ext = "jpg";
                }
                String newFileName = UUID.randomUUID() + "." + ext;
                String path = PropertiesUtils.KeyValue("file.location") + storePath;
                System.out.println("path:" + path);
                File dest = new File(path + newFileName);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                file.transferTo(dest);
                if (state == 7) {
                    ImgUtil.img(path + newFileName);
                } else if (state == 8) {
                    ImgUtil.Img(path + newFileName);
                } else if (state == 1) {
                    ImgUtil.img_size(path + newFileName, width, height);
                } else {
                    ImgUtil.keepAspectRatio(path + newFileName, width, height);
                }
                httpImgPath = PropertiesUtils.KeyValue("file.url") + storePath + newFileName;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpImgPath;
    }

    public static String url(MultipartFile file, int width, int height, int state, String userid) {
        String fileName = "";
        String originalFileName = "";
        String httpImgPath = "";
        try {
            if (!file.isEmpty()) {
                fileName = file.getOriginalFilename();
                originalFileName = fileName;

                String ext = (FilenameUtils.getExtension(file
                        .getOriginalFilename())).toLowerCase();
                String storePath = "";
                if ("jpg".equals(ext) || "png".equals(ext)
                        || "jpeg".equals(ext) || "bmp".equals(ext)) {
                    storePath = "/image/icon";
                } else {
                    storePath = "/video/icon";
                }
                String newFileName = "/img_" + userid + "." + "jpeg";
                String path = PropertiesUtils.KeyValue("file.location") + storePath;
                System.out.println("path:" + path);
                File dest = new File(path + newFileName);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                file.transferTo(dest);
                if (state == 1) {
                    ImgUtil.img_size(path + newFileName, width, height);
                } else {
                    ImgUtil.keepAspectRatio(path + newFileName, width, height);
                }
                httpImgPath = PropertiesUtils.KeyValue("file.url") + storePath + newFileName;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpImgPath;
    }

    public static void img_size(String url, int width, int height) {
        try {
            Thumbnails.of(url).size(width, height).toFile(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void keepAspectRatio(String url, int width, int height) {
        try {
            Thumbnails.of(url).size(width, height).keepAspectRatio(false).toFile(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void img(String url) {
        try {
            Thumbnails.of(url).size(720, 344).outputQuality(1f).toFile(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
