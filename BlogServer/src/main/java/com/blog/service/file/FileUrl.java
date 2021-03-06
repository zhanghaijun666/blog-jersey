package com.blog.service.file;

import com.blog.db.Repository;
import com.blog.service.RepositoryService;
import com.blog.utils.FileUtils;
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

    private String rootHash;
    private int gpType;
    private int gpId;
    private String bucket;
    private String path;
    private final String originPath;
    private final int userId;

    public FileUrl(String originPath, int userId) {
        this.originPath = originPath;
        this.userId = userId;
        Matcher matcher = standUrlPattern.matcher(originPath);
        if (matcher.matches()) {
            this.rootHash = BasicConvertUtils.toString(matcher.group("rootHash"), DEFAULT_ROOT_HASH);
            this.gpType = BasicConvertUtils.toInteger(matcher.group("gtype"), DEFAULT_GPTYPE);
            this.gpId = BasicConvertUtils.toInteger(matcher.group("gpid"), DEFAULT_GPID);
            this.bucket = BasicConvertUtils.toString(matcher.group("bucket"), DEFAULT_BUCKET);
            this.path = BasicConvertUtils.toString(matcher.group("path"), "");
            Repository repository = RepositoryService.getRepository(this.gpType, this.gpId);
            if (null != repository) {
                this.rootHash = repository.getRootHash();
            }
        }
    }

    public FileUrl getParent() {
        if (this.getPath().length() < 2) {
            return null;
        }
        String fileOriginPath = this.getOriginPath();
        return new FileUrl(fileOriginPath.substring(0, fileOriginPath.lastIndexOf("/") + 1), this.getUserId());
    }

    public FileUrl getChild(String childName) {
        return new FileUrl(this.getOriginPath() + (this.getOriginPath().endsWith("/") ? "" : "/") + childName, this.getUserId());
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

    public String getOriginPath() {
        return originPath;
    }

    public int getUserId() {
        return userId;
    }

    public String getFileName() {
        return FileUtils.getFileName(this.getPath());
    }

}
