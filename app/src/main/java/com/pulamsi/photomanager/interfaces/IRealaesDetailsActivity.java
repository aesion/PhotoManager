package com.pulamsi.photomanager.interfaces;

import com.pulamsi.photomanager.bean.Gallery;

import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-02-14
 * Time: 10:55
 * FIXME
 */
public interface IRealaesDetailsActivity extends BaseInterface {


    void loadingListData(List<Gallery> galleryList);

    void updateListData(List<Gallery> galleryList);

    void deleteBack();

}
