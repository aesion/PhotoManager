package com.pulamsi.photomanager.interfaces;

import com.pulamsi.photomanager.bean.Gallery;

import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-11-11
 * Time: 15:05
 * FIXME
 */
public interface IListFragment extends BaseInterface {

        void updateListData(List<Gallery> galleryList);
        void loadingListData(List<Gallery> galleryList);

        void loadingError();
}
