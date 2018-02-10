package com.example.nttr.panobumapp.util;

import io.realm.Realm;
import io.realm.RealmModel;

public class RealmUtils {
    /**
     * idというlong型のprimary keyがある場合に、新しいPrimary Keyとなる値を返します。
     * @return 新しいPrimary Key
     */
    public static <T extends RealmModel> long getNewPrimaryId(Realm realm, Class<T> modelClass) {
        Number id = realm.where(modelClass).max("id");
        if (id != null) {
            return id.longValue() + 1;
        }
        return 1;
    }
}
