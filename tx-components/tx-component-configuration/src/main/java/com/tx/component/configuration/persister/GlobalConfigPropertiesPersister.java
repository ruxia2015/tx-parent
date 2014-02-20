/*
 * 描          述:  <描述>
 * 修  改   人:  brady
 * 修改时间:  2014-2-17
 * <修改描述:>
 */
package com.tx.component.configuration.persister;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.sf.ehcache.Element;

import com.tx.component.configuration.config.ConfigGroupParse;
import com.tx.component.configuration.config.ConfigPropertyParse;
import com.tx.component.configuration.context.ConfigContext;
import com.tx.component.configuration.exception.NotExistException;
import com.tx.component.configuration.model.ConfigProperty;
import com.tx.component.configuration.model.ConfigPropertyItem;
import com.tx.component.configuration.model.ConfigPropertyProxy;
import com.tx.component.configuration.persister.dao.ConfigPropertyItemDao;
import com.tx.component.configuration.persister.dao.impl.ConfigPropertyItemDaoImpl;
import com.tx.core.dbscript.TableDefinition;
import com.tx.core.dbscript.XMLTableDefinition;
import com.tx.core.dbscript.context.DBScriptExecutorContext;
import com.tx.core.exceptions.util.AssertUtils;

/**
 * 全局配置属性持久器<br/>
 * <功能详细描述>
 * 
 * @author  brady
 * @version  [版本号, 2014-2-17]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class GlobalConfigPropertiesPersister extends
        BaseConfigPropertiesPersister {
    
    /** 配置属性项持久层 */
    private ConfigPropertyItemDao configPropertyItemDao;
    
    /** 数据源实例 */
    private DataSource dataSource;
    
    /** 系统id */
    private String systemId = "";
    
    /** 脚本执行器容器 */
    private DBScriptExecutorContext dbScriptExecutorContext;
    
    /** 配置属性项表定义 */
    private TableDefinition configPropertyItemTableDefinition = new XMLTableDefinition(
            "classpath:com/tx/component/configuration/script/config_context_table.xml");
    
    /** 配置属性项映射实例 */
    private Map<String, ConfigPropertyItem> configPropertyItemMapping = new HashMap<String, ConfigPropertyItem>();
    
    /** 是否自动执行创建表脚本 */
    private boolean databaseSchemaUpdate = true;
    
    /**
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        AssertUtils.notNull(this.dataSource, "dataSource is null.");
        AssertUtils.notEmpty(this.systemId, "systemId is empty.");
        
        if (this.databaseSchemaUpdate && dbScriptExecutorContext != null) {
            dbScriptExecutorContext.createOrUpdateTable(configPropertyItemTableDefinition);
        }
        /** 配置属性项持久层 */
        this.configPropertyItemDao = new ConfigPropertyItemDaoImpl(dataSource,
                systemId);
        super.afterPropertiesSet();
    }
    
    /**
     * @param configContext
     * @param configPropertyParse
     * @param configGroupParse
     * @return
     */
    @Override
    protected ConfigProperty buildConfigProperty(ConfigContext configContext,
            ConfigPropertyParse configPropertyParse,
            ConfigGroupParse configGroupParse) {
        ConfigProperty configProperty = new ConfigPropertyProxy(configContext,
                this, configGroupParse, configPropertyParse);
        
        //如果在持久表中不存在，则自动创建
        if (!this.configPropertyItemMapping.containsKey(configPropertyParse.getKey())) {
            ConfigPropertyItem newConfigPropertyItem = insertConfigPropertyItem(configPropertyParse);
            this.configPropertyItemMapping.put(newConfigPropertyItem.getKey(),
                    newConfigPropertyItem);
            
            //压栈入缓存中
            this.cache.put(new Element(configPropertyParse.getKey(),
                    newConfigPropertyItem.getValue()));
        } else {
            ConfigPropertyItem nowConfigPropertyItem = this.configPropertyItemMapping.get(configPropertyParse.getKey());
            //判断是否需要更新配置属性项
            if (isNeedUpdate(nowConfigPropertyItem, configPropertyParse)) {
                ConfigPropertyItem newConfigPropertyItem = updateConfigPropertyItem(nowConfigPropertyItem,
                        configPropertyParse);
                this.configPropertyItemMapping.put(newConfigPropertyItem.getKey(),
                        newConfigPropertyItem);
                //压栈入缓存中
                this.cache.put(new Element(configPropertyParse.getKey(),
                        newConfigPropertyItem.getValue()));
            } else {
                //压栈入缓存中
                this.cache.put(new Element(configPropertyParse.getKey(),
                        nowConfigPropertyItem.getValue()));
            }
        }
        
        return configProperty;
    }
    
    /**
      * 更新配置属性项<br/>
      *<功能详细描述>
      * @param configPropertyParse
      * @return [参数说明]
      * 
      * @return ConfigPropertyItem [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    private ConfigPropertyItem updateConfigPropertyItem(
            ConfigPropertyItem nowConfigPropertyItem,
            ConfigPropertyParse configPropertyParse) {
        Date now = new Date();
        ConfigPropertyItem configPropertyItem = new ConfigPropertyItem();
        configPropertyItem.setLastUpdateDate(now);
        configPropertyItem.setDescription(configPropertyParse.getDescription());
        configPropertyItem.setKey(configPropertyParse.getKey());
        configPropertyItem.setName(configPropertyParse.getName());
        configPropertyItem.setValid(true);
        configPropertyItem.setValidateExpression(configPropertyParse.getValidateExpression());
        //使用书库表中的值
        configPropertyItem.setCreateDate(nowConfigPropertyItem.getCreateDate());
        configPropertyItem.setValue(nowConfigPropertyItem.getValue());
        
        this.configPropertyItemDao.update(configPropertyItem);
        return configPropertyItem;
    }
    
    /**
     * @return
     */
    @Override
    public boolean isSupportModifyAble() {
        return true;
    }
    
    /**
     * @param configProperty
     */
    @Override
    public synchronized void updateConfigProperty(ConfigProperty configProperty) {
        ConfigPropertyItem configPropertyItem = this.configPropertyItemMapping.get(configProperty.getKey());
        AssertUtils.notNull(configPropertyItem, "configPropertyItem is null.");
        AssertUtils.notEmpty(configPropertyItem.getId(),
                "configPropertyItem.id is empty.");
        configPropertyItem.setValue(configProperty.getValue());
        this.configPropertyItemDao.update(configPropertyItem);
        this.cache.removeAll();
    }
    
    /**
      * 重新构建配置属性值缓存
      *<功能详细描述> [参数说明]
      * 
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    private synchronized void reBuildConfigPropertyValueCache() {
        logger.info("重新构建配置属性值缓存.");
        
        configPropertyItemMapping.clear();
        List<ConfigPropertyItem> configPropertyItems = this.configPropertyItemDao.queryConfigPropertyItemList();
        for (ConfigPropertyItem configPropertyTemp : configPropertyItems) {
            configPropertyItemMapping.put(configPropertyTemp.getKey(),
                    configPropertyTemp);
            
            //压栈入缓存中
            this.cache.put(new Element(configPropertyTemp.getKey(),
                    configPropertyTemp.getValue()));
        }
    }
    
    /**
     * @param key
     * @return
     */
    @Override
    public String getConfigPropertyValueByKey(String key) {
        String resValue = null;
        if (this.cache.get(key) != null) {
            resValue = (String) this.cache.get(key).getValue();
        } else {
            reBuildConfigPropertyValueCache();
            
            resValue = (String) this.cache.get(key).getValue();
        }
        //如果重建缓存后依然无法寻找到对应值，抛出异常
        if (resValue == null) {
            throw new NotExistException("配置属性不存在。key:{}", new Object[] { key });
        }
        return resValue;
    }
    
    /**
      * 判断是否需要更新配置属性项<br/>
      *<功能详细描述>
      * @param nowConfigPropertyItem
      * @param configPropertyParse
      * @return [参数说明]
      * 
      * @return boolean [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    private boolean isNeedUpdate(ConfigPropertyItem nowConfigPropertyItem,
            ConfigPropertyParse configPropertyParse) {
        AssertUtils.notNull(nowConfigPropertyItem,
                "nowConfigPropertyItem is null");
        
        if (!nowConfigPropertyItem.getDescription()
                .equals(configPropertyParse.getDescription())) {
            return true;
        } else if (nowConfigPropertyItem.getName()
                .equals(configPropertyParse.getName())) {
            return true;
        } else if (nowConfigPropertyItem.getValidateExpression()
                .equals(configPropertyParse.getValidateExpression())) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
      * 根据配置属性项目配置文件将配置属性项插入数据库中<br/>
      *<功能详细描述>
      * @param configPropertyParse
      * @return [参数说明]
      * 
      * @return ConfigPropertyItem [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    private ConfigPropertyItem insertConfigPropertyItem(
            ConfigPropertyParse configPropertyParse) {
        Date now = new Date();
        ConfigPropertyItem configPropertyItem = new ConfigPropertyItem();
        configPropertyItem.setCreateDate(now);
        configPropertyItem.setLastUpdateDate(now);
        configPropertyItem.setDescription(configPropertyParse.getDescription());
        configPropertyItem.setKey(configPropertyParse.getKey());
        configPropertyItem.setName(configPropertyParse.getName());
        configPropertyItem.setValid(true);
        configPropertyItem.setValidateExpression(configPropertyParse.getValidateExpression());
        configPropertyItem.setValue(configPropertyParse.getValue());
        
        this.configPropertyItemDao.insert(configPropertyItem);
        return configPropertyItem;
    }
    
    /**
     * 
     */
    @Override
    protected void afterPropertyConfigsLoaded() {
        @SuppressWarnings("unchecked")
        List<String> keys = (List<String>) this.cache.getKeys();
        for (String key : keys) {
            logger.info("   加载全局系统参数:  {}={}", new Object[] { key,
                    this.cache.get(key).getValue() });
        }
    }
    
    /**
     * 
     */
    @Override
    protected void postPropertyConfigsLoaded() {
        List<ConfigPropertyItem> configPropertyItems = this.configPropertyItemDao.queryConfigPropertyItemList();
        
        for (ConfigPropertyItem configPropertyTemp : configPropertyItems) {
            configPropertyItemMapping.put(configPropertyTemp.getKey(),
                    configPropertyTemp);
        }
    }
    
    /**
     * @param 对dbScriptExecutorContext进行赋值
     */
    public void setDbScriptExecutorContext(
            DBScriptExecutorContext dbScriptExecutorContext) {
        this.dbScriptExecutorContext = dbScriptExecutorContext;
    }
    
    /**
     * @param 对dataSource进行赋值
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    /**
     * @param 对systemId进行赋值
     */
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }
    
    /**
     * @param 对databaseSchemaUpdate进行赋值
     */
    public void setDatabaseSchemaUpdate(boolean databaseSchemaUpdate) {
        this.databaseSchemaUpdate = databaseSchemaUpdate;
    }
}