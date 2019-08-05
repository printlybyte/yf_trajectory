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
public class GreendaoAreaBean {
    @Id(autoincrement = true)
    Long id;//标签对应的值   作为主键
    @Unique
    int No;
    @Property
    private String ProvinceName;//对应的中文的值
    @Property
    private String ProvinceNameId;//对应的中文的值
    @Property
    private String CityName;//对应的中文的值 @Property
    @Property
    private String CityNameID;//对应的中文的值
    @Property
    private String AreaName;//对应的中文的值
    @Property
    private String AreaNameId;//对应的中文的值
    @Generated(hash = 1414777883)
    public GreendaoAreaBean(Long id, int No, String ProvinceName,
            String ProvinceNameId, String CityName, String CityNameID,
            String AreaName, String AreaNameId) {
        this.id = id;
        this.No = No;
        this.ProvinceName = ProvinceName;
        this.ProvinceNameId = ProvinceNameId;
        this.CityName = CityName;
        this.CityNameID = CityNameID;
        this.AreaName = AreaName;
        this.AreaNameId = AreaNameId;
    }
    @Generated(hash = 2071541266)
    public GreendaoAreaBean() {
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
    public String getCityName() {
        return this.CityName;
    }
    public void setCityName(String CityName) {
        this.CityName = CityName;
    }
    public String getAreaName() {
        return this.AreaName;
    }
    public void setAreaName(String AreaName) {
        this.AreaName = AreaName;
    }
    public int getNo() {
        return this.No;
    }
    public void setNo(int No) {
        this.No = No;
    }
    public String getCityNameID() {
        return this.CityNameID;
    }
    public void setCityNameID(String CityNameID) {
        this.CityNameID = CityNameID;
    }
    public String getProvinceNameId() {
        return this.ProvinceNameId;
    }
    public void setProvinceNameId(String ProvinceNameId) {
        this.ProvinceNameId = ProvinceNameId;
    }
    public String getAreaNameId() {
        return this.AreaNameId;
    }
    public void setAreaNameId(String AreaNameId) {
        this.AreaNameId = AreaNameId;
    }
}
