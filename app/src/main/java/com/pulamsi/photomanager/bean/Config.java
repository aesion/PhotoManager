package com.pulamsi.photomanager.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-01-04
 * Time: 10:26
 * FIXME
 */
@Entity
public class Config {

    @Id
    private Long id;

    //图片上传须知
    private boolean isUploadShowDialog;

    //图片分享
    private boolean isImageDetailShareShowDialog;

    //图片分享
    private boolean isShowGuide;

    //详情分享引导
    private boolean isImageDetailShowGuide;

    @Generated(hash = 1787018170)
    public Config(Long id, boolean isUploadShowDialog,
            boolean isImageDetailShareShowDialog, boolean isShowGuide,
            boolean isImageDetailShowGuide) {
        this.id = id;
        this.isUploadShowDialog = isUploadShowDialog;
        this.isImageDetailShareShowDialog = isImageDetailShareShowDialog;
        this.isShowGuide = isShowGuide;
        this.isImageDetailShowGuide = isImageDetailShowGuide;
    }

    @Generated(hash = 589037648)
    public Config() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsUploadShowDialog() {
        return this.isUploadShowDialog;
    }

    public void setIsUploadShowDialog(boolean isUploadShowDialog) {
        this.isUploadShowDialog = isUploadShowDialog;
    }

    public boolean getIsImageDetailShareShowDialog() {
        return this.isImageDetailShareShowDialog;
    }

    public void setIsImageDetailShareShowDialog(
            boolean isImageDetailShareShowDialog) {
        this.isImageDetailShareShowDialog = isImageDetailShareShowDialog;
    }

    public boolean getIsShowGuide() {
        return this.isShowGuide;
    }

    public void setIsShowGuide(boolean isShowGuide) {
        this.isShowGuide = isShowGuide;
    }

    public boolean getIsImageDetailShowGuide() {
        return this.isImageDetailShowGuide;
    }

    public void setIsImageDetailShowGuide(boolean isImageDetailShowGuide) {
        this.isImageDetailShowGuide = isImageDetailShowGuide;
    }

}
