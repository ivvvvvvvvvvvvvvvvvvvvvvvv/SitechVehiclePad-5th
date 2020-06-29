package com.sitechdev.vehicle.pad.module.phone.utils;

import com.sitechdev.vehicle.pad.model.phone.Contact;

import java.text.Collator;
import java.util.Comparator;

public class PinyinComparator implements Comparator<Contact> {
    private static PinyinComparator comparator;
    private static Comparator<Object> com = null;

    public static synchronized PinyinComparator getDefault() {
        if (comparator == null) {
            comparator = new PinyinComparator();
            com = Collator.getInstance(java.util.Locale.CHINA);
        }
        return comparator;
    }

    @Override
    public int compare(Contact lhs, Contact rhs) {
//        String [] temp1  = lhs.getAbbr().split("_");
//        String [] temp2  = rhs.getAbbr().split("_");
//        int minL = Math.min(temp1.length,temp2.length);
//        int nameMinL = Math.min(lhs.getName().length(),rhs.getName().length());
//        for (int i = 0; i < minL; i++) {
//            int flag1 = temp1[i].compareTo(temp2[i]);
//            if(flag1 != 0){
//                return flag1;
//            }else {
//                if (nameMinL > i) {
//                    int flag2 = lhs.getName().substring(i,i+1).compareTo(rhs.getName().substring(i,i+1));
//                    if(flag2 != 0){
//                        return flag2;
//                    }
//                }
//            }
//        }
//        return lhs.getAbbr().compareTo(rhs.getAbbr());
        return com.compare(lhs.getName(), rhs.getName());
    }
}