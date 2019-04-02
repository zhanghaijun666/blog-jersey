package com.blog.file;

import com.tools.BasicConvertUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author haijun.zhang
 */
public class FileUrl {

    public static Pattern standUrlPattern = Pattern.compile("\\/?(?<rootHash>[^/]+)?\\/(?<gtype>[\\d]+)\\/(?<gpid>-?[\\d]+)\\/(?<bucket>[^/]+)(?<path>\\/?.*)");
    public static final String DEFAULT_ROOT_HASH = "default";
    public static final String DEFAULT_BUCKET = "directory";
    public static final int DEFAULT_GPTYPE = 0;
    public static final int DEFAULT_GPID = 0;

    String rootHash;
    int gpType;
    int gpId;
    String bucket;
    String path;    // 以/开头

    public FileUrl(String originPath, int userId) {
        Matcher matcher = standUrlPattern.matcher(originPath);
        if (matcher.matches()) {
            this.rootHash = BasicConvertUtils.toString(matcher.group("rootHash"), DEFAULT_ROOT_HASH);
            this.gpType = BasicConvertUtils.toInteger(matcher.group("gtype"), DEFAULT_GPTYPE);
            this.gpId = BasicConvertUtils.toInteger(matcher.group("gpid"), DEFAULT_GPID);
            this.bucket = BasicConvertUtils.toString(matcher.group("bucket"), DEFAULT_BUCKET);
            this.path = BasicConvertUtils.toString(matcher.group("path"), "");
        }
    }

    public String getRootHash() {
        return rootHash;
    }

    public int getGpType() {
        return gpType;
    }

    public int getGpId() {
        return gpId;
    }

    public String getBucket() {
        return bucket;
    }

    public String getPath() {
        return path;
    }

}
