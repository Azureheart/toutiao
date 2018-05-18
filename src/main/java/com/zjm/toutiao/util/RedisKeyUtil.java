package com.zjm.toutiao.util;

//对redis的key定义的规范
public class RedisKeyUtil {
    private static final String SPLIT=":";
    private static final String BIZ_LIKE="LIKE";
    private static final String BIZ_DISLIKE="DISLIKE";
    private static final String BIZ_EVENT="EVENT";

    public static String getLikeKey(int entityId,int entityType){
        return BIZ_LIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityId,int entityType){
        return BIZ_DISLIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
    }

    public static String getEventQueueKey(){
        return BIZ_EVENT;
    }
}
