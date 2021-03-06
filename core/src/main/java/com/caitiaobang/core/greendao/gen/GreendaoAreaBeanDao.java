package com.caitiaobang.core.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.caitiaobang.core.app.bean.GreendaoAreaBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GREENDAO_AREA_BEAN".
*/
public class GreendaoAreaBeanDao extends AbstractDao<GreendaoAreaBean, Long> {

    public static final String TABLENAME = "GREENDAO_AREA_BEAN";

    /**
     * Properties of entity GreendaoAreaBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property No = new Property(1, int.class, "No", false, "NO");
        public final static Property ProvinceName = new Property(2, String.class, "ProvinceName", false, "PROVINCE_NAME");
        public final static Property ProvinceNameId = new Property(3, String.class, "ProvinceNameId", false, "PROVINCE_NAME_ID");
        public final static Property CityName = new Property(4, String.class, "CityName", false, "CITY_NAME");
        public final static Property CityNameID = new Property(5, String.class, "CityNameID", false, "CITY_NAME_ID");
        public final static Property AreaName = new Property(6, String.class, "AreaName", false, "AREA_NAME");
        public final static Property AreaNameId = new Property(7, String.class, "AreaNameId", false, "AREA_NAME_ID");
    }


    public GreendaoAreaBeanDao(DaoConfig config) {
        super(config);
    }
    
    public GreendaoAreaBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GREENDAO_AREA_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NO\" INTEGER NOT NULL UNIQUE ," + // 1: No
                "\"PROVINCE_NAME\" TEXT," + // 2: ProvinceName
                "\"PROVINCE_NAME_ID\" TEXT," + // 3: ProvinceNameId
                "\"CITY_NAME\" TEXT," + // 4: CityName
                "\"CITY_NAME_ID\" TEXT," + // 5: CityNameID
                "\"AREA_NAME\" TEXT," + // 6: AreaName
                "\"AREA_NAME_ID\" TEXT);"); // 7: AreaNameId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GREENDAO_AREA_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, GreendaoAreaBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getNo());
 
        String ProvinceName = entity.getProvinceName();
        if (ProvinceName != null) {
            stmt.bindString(3, ProvinceName);
        }
 
        String ProvinceNameId = entity.getProvinceNameId();
        if (ProvinceNameId != null) {
            stmt.bindString(4, ProvinceNameId);
        }
 
        String CityName = entity.getCityName();
        if (CityName != null) {
            stmt.bindString(5, CityName);
        }
 
        String CityNameID = entity.getCityNameID();
        if (CityNameID != null) {
            stmt.bindString(6, CityNameID);
        }
 
        String AreaName = entity.getAreaName();
        if (AreaName != null) {
            stmt.bindString(7, AreaName);
        }
 
        String AreaNameId = entity.getAreaNameId();
        if (AreaNameId != null) {
            stmt.bindString(8, AreaNameId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, GreendaoAreaBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getNo());
 
        String ProvinceName = entity.getProvinceName();
        if (ProvinceName != null) {
            stmt.bindString(3, ProvinceName);
        }
 
        String ProvinceNameId = entity.getProvinceNameId();
        if (ProvinceNameId != null) {
            stmt.bindString(4, ProvinceNameId);
        }
 
        String CityName = entity.getCityName();
        if (CityName != null) {
            stmt.bindString(5, CityName);
        }
 
        String CityNameID = entity.getCityNameID();
        if (CityNameID != null) {
            stmt.bindString(6, CityNameID);
        }
 
        String AreaName = entity.getAreaName();
        if (AreaName != null) {
            stmt.bindString(7, AreaName);
        }
 
        String AreaNameId = entity.getAreaNameId();
        if (AreaNameId != null) {
            stmt.bindString(8, AreaNameId);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public GreendaoAreaBean readEntity(Cursor cursor, int offset) {
        GreendaoAreaBean entity = new GreendaoAreaBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // No
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // ProvinceName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // ProvinceNameId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // CityName
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // CityNameID
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // AreaName
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // AreaNameId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, GreendaoAreaBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNo(cursor.getInt(offset + 1));
        entity.setProvinceName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setProvinceNameId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCityName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCityNameID(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setAreaName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAreaNameId(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(GreendaoAreaBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(GreendaoAreaBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(GreendaoAreaBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
