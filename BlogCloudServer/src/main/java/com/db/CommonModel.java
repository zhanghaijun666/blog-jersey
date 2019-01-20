package com.db;

import org.javalite.activejdbc.Model;

/**
 * @author zhanghaijun
 */
public class CommonModel extends Model {

    public static final String CREATED_BY = "created_by";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_BY = "updated_by";
    public static final String UPDATED_AT = "updated_at";

    public int getCreatedBy() {
        return getInteger(CREATED_BY);
    }

    public void setCreatedBy(Integer createdBy) {
        set(CREATED_BY, createdBy);
    }

    public int getUpdatedBy() {
        return getInteger(UPDATED_BY);
    }

    public void setUpdatedBy(Integer updatedBy) {
        set(UPDATED_BY, updatedBy);
    }

    public Long getCreatedAt() {
        return getLong(CREATED_AT);
    }

    public Long getUpdatedAt() {
        return getLong(UPDATED_AT);
    }

}
