package com.caitiaobang.core.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.caitiaobang.core.app.bean.GreendaoProvinceBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GREENDAO_PROVINCE_BEAN".
*/
public class GreendaoProvinceBeanDao extends AbstractDao<GreendaoProvinceBean, Long> {

    public static final String TABLENAME = "GREENDAO_PROVINCE_BEAN";

    /**
     * Properties of entity GreendaoProvinceBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property No = new Property(1, int.class, "No", false, "NO");
        public final static Property ProvinceName = new Property(2, String.class, "ProvinceName", false, "PROVINCE_NAME");
        public final static Property ProvinceNameId = new Property(3, String.class, "ProvinceNameId", false, "PROVINCE_NAME_ID");
    }


    public GreendaoProvinceBeanDao(DaoConfig config) {
        super(config);
    }
    
    public GreendaoProvinceBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GREENDAO_PROVINCE_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NO\" INTEGER NOT NULL UNIQUE ," + // 1: No
                "\"PROVINCE_NAME\" TEXT," + // 2: ProvinceName
                "\"PROVINCE_NAME_ID\" TEXT);"); // 3: ProvinceNameId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GREENDAO_PROVINCE_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, GreendaoProvinceBean entity) {
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
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, GreendaoProvinceBean entity) {
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
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public GreendaoProvinceBean readEntity(Cursor cursor, int offset) {
        GreendaoProvinceBean entity = new GreendaoProvinceBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // No
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // ProvinceName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // ProvinceNameId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, GreendaoProvinceBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNo(cursor.getInt(offset + 1));
        entity.setProvinceName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setProvinceNameId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(GreendaoProvinceBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(GreendaoProvinceBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(GreendaoProvinceBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}