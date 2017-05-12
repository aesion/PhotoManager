package com.pulamsi.photomanager.utils;

import com.pulamsi.photomanager.bean.FilterEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * 滤镜参数
 */
public class DataHandler {

    public static List<FilterEffect> filters = new ArrayList<FilterEffect>();

    static {
        filters.clear();
        filters.add(new FilterEffect("原图", GPUImageFilterTools.FilterType.NORMAL, 0));
        filters.add(new FilterEffect("绮丽", GPUImageFilterTools.FilterType.ACV_GAOLENG, 0));
        filters.add(new FilterEffect("浅滩", GPUImageFilterTools.FilterType.ACV_QINGXIN, 0));
        filters.add(new FilterEffect("和风", GPUImageFilterTools.FilterType.ACV_WENNUAN, 0));
        filters.add(new FilterEffect("可可", GPUImageFilterTools.FilterType.ACV_DANHUANG, 0));
        filters.add(new FilterEffect("午后", GPUImageFilterTools.FilterType.ACV_MORENJIAQIANG, 0));
        filters.add(new FilterEffect("留声", GPUImageFilterTools.FilterType.ACV_AIMEI, 0));
    }
}


