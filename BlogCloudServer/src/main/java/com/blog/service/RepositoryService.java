package com.blog.service;

import com.blog.db.Repository;
import static com.blog.proto.BlogStore.GtypeEnum;

/**
 * @author zhanghaijun
 */
public class RepositoryService {

    public static Repository getRepository(int gptype, int gpid) {
        switch (gptype) {
            case GtypeEnum.User_VALUE:
                return com.blog.db.User.findById(gpid);
            case GtypeEnum.Group_VALUE:
                return com.blog.db.Group.findById(gpid);
        }
        return null;
    }

    public static void updateCommitHash(int gptype, int gpid, String newCommitHash) {
        Repository repository = RepositoryService.getRepository(gptype, gpid);
        repository.setRootHash(newCommitHash);
        repository.saveIt();
    }
}
