package com.pulamsi.photomanager.interfaces;

import com.pulamsi.photomanager.bean.Gallery;

import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-03-10
 * Time: 17:01
 * FIXME
 */
public interface IReleaseDetailsFragment  extends BaseInterface {


    void loadingListData(List<Gallery> galleryList);

    void updateListData(List<Gallery> galleryList);

    void deleteBack();

}
