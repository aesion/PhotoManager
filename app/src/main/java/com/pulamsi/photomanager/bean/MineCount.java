package com.pulamsi.photomanager.bean;

import java.io.Serializable;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-01-09
 * Time: 16:47
 * FIXME
 */
public class MineCount implements Serializable {
    private int pasterCount;// 个人发布数
    private int collectionCount;// 个人收藏数
    private int privateCount;// 私人发布数量
    private int openCouunt;// 公开发布数量


    public int getPrivateCount() {
        return privateCount;
    }

    public void setPrivateCount(int privateCount) {
        this.privateCount = privateCount;
    }

    public int getOpenCouunt() {
        return openCouunt;
    }

    public void setOpenCouunt(int openCouunt) {
        this.openCouunt = openCouunt;
    }

    public int getPasterCount() {
        return pasterCount;
    }

    public void setPasterCount(int pasterCount) {
        this.pasterCount = pasterCount;
    }

    public int getCollectionCount() {
        return collectionCount;
    }

    public void setCollectionCount(int collectionCount) {
        this.collectionCount = collectionCount;
    }


    @Override
    public String toString() {
        return "MineCount{" +
                "pasterCount=" + pasterCount +
                ", collectionCount=" + collectionCount +
                ", privateCount=" + privateCount +
                ", openCouunt=" + openCouunt +
                '}';
    }
}
