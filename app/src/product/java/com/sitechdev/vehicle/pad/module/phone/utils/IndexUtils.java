package com.sitechdev.vehicle.pad.module.phone.utils;

import android.text.TextUtils;

import com.sitechdev.vehicle.pad.model.phone.Contact;
import com.sitechdev.vehicle.pad.module.phone.BtGlobalRef;
import com.sitechdev.vehicle.pad.module.phone.adapter.ContactAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 通讯录index 工具类
 *
 * @author liuhe
 * @date 2019/04/04
 */
public class IndexUtils {

    public static List<String> getAllIndex() {
        ArrayList<String> characters = new ArrayList<>();
        for (char i = 'A'; i <= 'Z'; i++) {
            final String character = i + "";
            characters.add(character);
        }
        characters.add("#");
        return characters;
    }

    public static List<Contact> sortContacts(ArrayList<Contact> contactsList) {
        List<Contact> resultList = new ArrayList<>();
        List<String> sortContactList = new ArrayList<>();
        Map<String, Contact> map = new IdentityHashMap<>();

        for (int i = 0; i < contactsList.size(); i++) {
            Contact contact = contactsList.get(i);
            if (null != contact && !TextUtils.isEmpty(contact.getName())) {
                String abbr = contact.getAbbr();
                map.put(abbr, contact);
                sortContactList.add(abbr);
            }
        }
        Collections.sort(sortContactList, new ContactComparator());
        BtGlobalRef.characterSorts.clear();
        for (int i = 0; i < sortContactList.size(); i++) {
            String name = sortContactList.get(i);
            String character = (name.charAt(0) + "").toUpperCase(Locale.ENGLISH);
            if (!BtGlobalRef.characterSorts.contains(character)) {
                if (character.hashCode() >= "A".hashCode() && character.hashCode() <= "Z".hashCode()) {
                    // 是字母
                    BtGlobalRef.characterSorts.add(character);
                    resultList.add(new Contact(character, ContactAdapter.ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal()));
                } else {
                    if (!BtGlobalRef.characterSorts.contains("#")) {
                        BtGlobalRef.characterSorts.add("#");
                        resultList.add(new Contact("#", ContactAdapter.ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal()));
                    }
                }
            }
            resultList.add(new Contact(map.get(name).getAbbr(), map.get(name).getName(),
                    map.get(name).getPhoneNumber(), ContactAdapter.ITEM_TYPE.ITEM_TYPE_CONTACT.ordinal()));
        }
        return resultList;
    }
}
