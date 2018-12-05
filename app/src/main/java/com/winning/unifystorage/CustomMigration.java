package com.winning.unifystorage;

import com.winning.unifystorage_core.BaseMigration;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * 2018/11/28
 * Created by SharkChao
 * 827623353@qq.com
 * https://github.com/sharkchao
 */
public class CustomMigration extends BaseMigration {

    @Override
    public void upgrade(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        if (oldVersion == 0 && newVersion == 1){
            RealmObjectSchema user = schema.get("User");
            user.addField("sex",String.class,FieldAttribute.REQUIRED)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.set("sex","男");
                        }
                    });
            oldVersion++;
        }else if (oldVersion == 1 && newVersion == 2){
            RealmObjectSchema user = schema.get("User");
            user.removeField("sex");
            oldVersion++;
        }
    }
}
