package net.industryhive.utils;

/**
 * 文件类型及安全检查
 *
 * @Author 未央
 * @Create 2020-12-17 17:49
 */
public class FileInspection {
    /**
     * 合法的文件扩展名白名单
     */
    private static final String[] validExtensions = {
            "jpg", "jpeg", "png", "bmp", "gif"
    };

    /**
     * 校验文件名是否合法
     *
     * @param filename 文件名
     * @return boolean值【true：校验通过；false:校验不通过】
     */
    public static boolean inspect(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.'));
        for (String validExtension : validExtensions) {
            if (extension.equalsIgnoreCase(validExtension)) {
                return true;
            }
        }
        return false;
    }
}
