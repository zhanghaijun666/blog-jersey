package com.blog.store;

import com.tools.BasicConvertUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

/**
 * @author zhanghaijun
 */
public class FileUrl {

    public static final String DEFAULT_ROOT_HASH = "default";
    public static final String DEFAULT_BUCKET = "default";
    
    public static final int SPLITE_BATCH_SIZE = 32*1024*1024;
    public static Pattern standUrlPattern = Pattern.compile("\\/?(?<rootHash>[^/]+)\\/(?<gtype>[\\d]+)\\/(?<gpid>-?[\\d]+)\\/(?<bucket>[^/]+)(?<path>\\/?.*)");
    public static Pattern noRootHashPattern = Pattern.compile("(?<gtype>[^/]+)\\/(?<gpid>-?[\\d]+)\\/(?<bucket>[^/]+)(?<path>\\/?.*)");

    private int userId = 0;
    private String rootHash = FileUrl.DEFAULT_ROOT_HASH;
    private int gtype = 0;
    private int gpid = 0;
    private String bucket = FileUrl.DEFAULT_BUCKET;
    private String path = "";

    public FileUrl(String rootHash, int gtype, int gpid, String bucket, String path, int userId) {
        this.rootHash = rootHash;
        this.gtype = gtype;
        this.gpid = gpid;
        this.bucket = bucket;
        this.path = path;
        this.userId = userId;
    }

    public static FileUrl decodeFileUrlPath(String standUrlPath, int userId) {
        Matcher matcher = null;
        if (StringUtils.isNotEmpty(standUrlPath)) {
            matcher = standUrlPattern.matcher(standUrlPath);
            if (!matcher.matches()) {
                matcher = noRootHashPattern.matcher(standUrlPath);
            }
        }
        String rootHash = BasicConvertUtils.toString(matcher == null ? null : matcher.group("rootHash"), FileUrl.DEFAULT_ROOT_HASH);
        int gtype = BasicConvertUtils.toInteger(matcher == null ? null : matcher.group("gtype"), 0);
        int gpid = BasicConvertUtils.toInteger(matcher == null ? null : matcher.group("gpid"), 0);
        String bucket = BasicConvertUtils.toString(matcher == null ? null : matcher.group("bucket"), FileUrl.DEFAULT_BUCKET);
        String path = BasicConvertUtils.toString(matcher == null ? null : matcher.group("path"), "");
        return new FileUrl(rootHash, gtype, gpid, bucket, path, userId);
    }

}
