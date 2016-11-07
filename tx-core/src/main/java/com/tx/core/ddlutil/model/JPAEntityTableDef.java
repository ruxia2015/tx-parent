/*
 * 描          述:  <描述>
 * 修  改   人:  Administrator
 * 修改时间:  2016年10月20日
 * <修改描述:>
 */
package com.tx.core.ddlutil.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tx.core.util.ObjectUtils;

/**
 * 表定义<br/>
 * <功能详细描述>
 * 
 * @author  Administrator
 * @version  [版本号, 2016年10月20日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class JPAEntityTableDef implements Serializable, TableDef {
    
    /** 注释内容 */
    private static final long serialVersionUID = 829927103002085200L;
    
    /** 表名 */
    private String tableName = null;
    
    /** 表内容备注 */
    private String comment = "";
    
    /** 表字段. */
    private List<? extends TableColumnDef> columns = new ArrayList<>();
    
    /** 索引集合. */
    private List<? extends TableIndexDef> indexes = new ArrayList<>();
    
    /**
     * @return 返回 tableName
     */
    public String getTableName() {
        return tableName;
    }
    
    /**
     * @param 对tableName进行赋值
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    /**
     * @return
     */
    @Override
    public String getComment() {
        return comment;
    }
    
    /**
     * @param 对comment进行赋值
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    /**
     * @return
     */
    @Override
    public List<? extends TableColumnDef> getColumns() {
        return columns;
    }
    
    /**
     * @param 对columns进行赋值
     */
    public void setColumns(List<? extends TableColumnDef> columns) {
        this.columns = columns;
    }
    
    /**
     * @return
     */
    @Override
    public List<? extends TableIndexDef> getIndexes() {
        return indexes;
    }
    
    /**
     * @param 对indexes进行赋值
     */
    public void setIndexes(List<? extends TableIndexDef> indexes) {
        this.indexes = indexes;
    }
    
    /**
     * @return
     */
    @Override
    public int hashCode() {
        int hashCode = ObjectUtils.generateHashCode(super.hashCode(),
                this,
                "name");
        return hashCode;
    }
    
    /**
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        boolean flag = ObjectUtils.equals(this, obj, "name");
        return flag;
    }
}
