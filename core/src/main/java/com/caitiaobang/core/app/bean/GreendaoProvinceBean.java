package com.caitiaobang.core.app.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * ============================================
 * 描  述：
 * 包  名：com.caitiaobang.core.app.bean
 * 类  名：GreendaoProvinceBean
 * 创建人：lgd
 * 创建时间：2019/6/28 15:19
 * ============================================
 **/
@Entity
public class GreendaoProvinceBean {
    @Id(autoincrement = true)
    Long id;
    @Unique
    int No;

    @Property
    private String ProvinceName;//对应的中文的值
    @Property
    private String ProvinceNameId;//对应的中文的值
    @Generated(hash = 1277339395)
    public GreendaoProvinceBean(Long id, int No, String ProvinceName,
            String ProvinceNameId) {
        this.id = id;
        this.No = No;
        this.ProvinceName = ProvinceName;
        this.ProvinceNameId = ProvinceNameId;
    }
    @Generated(hash = 199976768)
    public GreendaoProvinceBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getProvinceName() {
        return this.ProvinceName;
    }
    public void setProvinceName(String ProvinceName) {
        this.ProvinceName = ProvinceName;
    }
    public String getProvinceNameId() {
        return this.ProvinceNameId;
    }
    public void setProvinceNameId(String ProvinceNameId) {
        this.ProvinceNameId = ProvinceNameId;
    }
    public int getNo() {
        return this.No;
    }
    public void setNo(int No) {
        this.No = No;
    }
}
