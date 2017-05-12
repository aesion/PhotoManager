package com.pulamsi.photomanager.interfaces;

import com.pulamsi.photomanager.bean.Galleryclass;
import com.pulamsi.photomanager.bean.PasterLabel;

import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-10
 * Time: 14:36
 * FIXME
 */
public interface IUploadActivity extends BaseInterface {


    void initClassify(List<Galleryclass> GalleryList);

    void initHotTag(List<PasterLabel> pasterLabelList);

    void successBack();

    void showContent(boolean isHotTag,
                     boolean isClassify);

}
