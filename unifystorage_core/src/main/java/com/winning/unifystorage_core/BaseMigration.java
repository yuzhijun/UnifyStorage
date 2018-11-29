package com.winning.unifystorage_core;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;

public abstract class BaseMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        upgrade(realm, oldVersion, newVersion);
    }

    public abstract void  upgrade(DynamicRealm realm, long oldVersion, long newVersion);
}
