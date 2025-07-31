package com.turing.bigdata.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件工具类
 *
 * @author Tony
 * @date 2025/7/13 0013 11:57
 */
public class FileUtil {

    private FileUtil() {
    }

    public static final String USER_DIR = System.getProperty("user.dir");

    public static final String UPLOADS_PATH = StringUtils.join(USER_DIR, File.separator, "uploads", File.separator);

    private static final Integer BUFFER_SIZE = 8192;

    /**
     * 根据相对路径删除文件，示例地址加了多余的斜杠，实际使用时注意
     *
     * @param path 数据库保存的文件地址：时间文件夹（如：\\uploads\2023\03\30\） + 文件名.后缀
     *             文件上传得到的完整地址：D:\MyFile\GitHub\flood-server\\uploads\2023\03\30\269889b4-359d-48b5-93fb-049e621062d5.xmind
     *             UPLOADS_PATH：D:\MyFile\GitHub\flood-server\\uploads\
     *             数据库保存的文件地址：\\uploads\2023\03\30\269889b4-359d-48b5-93fb-049e621062d5.xmind
     * @return 是否删除成功
     */
    public static Boolean deleteFile(String path) {
        String originalPath = path;
        path = path.replace("\\uploads\\", "");
        // 验证路径的合法性：（策略）验证是否是以四位数年份开头即可
        String pattern = "^\\d{4}";
        if (Pattern.matches(pattern, path)) {
            throw new RuntimeException("无效的文件路径：" + originalPath);
        }
        File file = new File(StringUtils.join(UPLOADS_PATH, path));
        return file.delete();
    }

    /**
     * 从文件路径中获取文件名
     *
     * @param path 文件路径
     * @return 文件名
     */
    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf(File.separator) + 1);
    }

}