package com.caitiaobang.core.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.caitiaobang.core.app.bean.GreendaoLocationBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GREENDAO_LOCATION".
*/
public class GreendaoLocationDao extends AbstractDao<GreendaoLocationBean, Long> {

    public static final String TABLENAME = "GREENDAO_LOCATION";

    /**
     * Properties of entity GreendaoLocationBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Lat = new Property(1, String.class, "lat", false, "LAT");
        public final static Property Lng = new Property(2, String.class, "lng", false, "LNG");
        public final static Property Address = new Property(3, String.class, "address", false, "ADDRESS");
        public final static Property Time = new Property(4, String.class, "time", false, "TIME");
    }


    public GreendaoLocationDao(DaoConfig config) {
        super(config);
    }
    
    public GreendaoLocationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GREENDAO_LOCATION\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"LAT\" TEXT," + // 1: lat
                "\"LNG\" TEXT," + // 2: lng
                "\"ADDRESS\" TEXT," + // 3: address
                "\"TIME\" TEXT);"); // 4: time
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GREENDAO_LOCATION\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, GreendaoLocationBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String lat = entity.getLat();
        if (lat != null) {
            stmt.bindString(2, lat);
        }
 
        String lng = entity.getLng();
        if (lng != null) {
            stmt.bindString(3, lng);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(4, address);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(5, time);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, GreendaoLocationBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String lat = entity.getLat();
        if (lat != null) {
            stmt.bindString(2, lat);
        }
 
        String lng = entity.getLng();
        if (lng != null) {
            stmt.bindString(3, lng);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(4, address);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(5, time);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public GreendaoLocationBean readEntity(Cursor cursor, int offset) {
        GreendaoLocationBean entity = new GreendaoLocationBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // lat
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // lng
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // address
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // time
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, GreendaoLocationBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setLat(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setLng(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAddress(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTime(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(GreendaoLocationBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(GreendaoLocationBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(GreendaoLocationBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
