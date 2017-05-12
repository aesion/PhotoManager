package com.pulamsi.photomanager.interfaces;

import com.pulamsi.photomanager.bean.Galleryclass;

import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-01-12
 * Time: 10:55
 * FIXME
 */
public interface IClassificationManagementActivity extends BaseInterface {
    void dataBack(List<Galleryclass> body);
}
