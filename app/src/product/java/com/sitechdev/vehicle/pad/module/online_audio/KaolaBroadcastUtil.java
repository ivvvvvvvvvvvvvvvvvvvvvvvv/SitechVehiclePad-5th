package com.sitechdev.vehicle.pad.module.online_audio;

import com.kaolafm.opensdk.api.operation.model.category.Category;
import com.kaolafm.opensdk.api.operation.model.category.LeafCategory;
import com.sitechdev.vehicle.pad.model.kaola.KaolaCategoryDataWarpper;
import com.sitechdev.vehicle.pad.view.Indexable;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/6/2
 * </pre>
 */
class KaolaBroadcastUtil {

    public static  List<KaolaCategoryDataWarpper> dealdata(List<Category> remoteData) {
        List<KaolaCategoryDataWarpper> level1Data = new ArrayList<>();
        List<KaolaCategoryDataWarpper> level2Data = new ArrayList<>();
        for (int i = 0; i < remoteData.size(); i++) {
            if (remoteData.get(i) instanceof LeafCategory) {
                KaolaCategoryDataWarpper d = new KaolaCategoryDataWarpper();//一级数据
                d.category = (LeafCategory) remoteData.get(i);
                d.code = remoteData.get(i).getCode();
                level1Data.add(d);
            } else {
                if (remoteData.get(i).getChildCategories() != null && remoteData.get(i).getChildCategories().size() > 0) {
                    KaolaCategoryDataWarpper d = new KaolaCategoryDataWarpper();//分组item
                    d.code = remoteData.get(i).getCode();
                    d.name = remoteData.get(i).getName();
                    level2Data.add(d);
                    for (int j = 0; j < remoteData.get(i).getChildCategories().size(); j++) {
                        if (remoteData.get(i).getChildCategories().get(j) instanceof LeafCategory) {
                            KaolaCategoryDataWarpper d2 = new KaolaCategoryDataWarpper();//二级数据
                            d2.code = remoteData.get(i).getCode();
                            d2.name = remoteData.get(i).getName();
                            d2.category = remoteData.get(i).getChildCategories().get(j);
                            level2Data.add(d2);
                        }
                    }
                }
            }
        }
        level1Data.addAll(level2Data);
        return level1Data;
    }
    public static List<Category> getList(List<Category> remoteData){
        List<KaolaCategoryDataWarpper> list = dealdata(remoteData);
        List<Category> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).category != null) {
                result.add(list.get(i).category);
            }
        }
        return result;
    }
    public static List<Indexable> getIndexableData(List<Category> remoteData, int size) {
        List<KaolaCategoryDataWarpper> list = dealdata(remoteData);
        List<Indexable> indexs = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).category != null) {
                int finalI = i;
                Indexable indexable = new Indexable() {
                    @Override
                    public String getIndex() {
                        return list.get(finalI).category.getName();
                    }
                };
                indexs.add(indexable);
                if (size > 0 && indexs.size() > size - 1) {
                    break;
                }
            }
        }
        return indexs;
    }
}
