/*
 * 描          述:  <描述>
 * 修  改   人:  Administrator
 * 修改时间:  2014年2月26日
 * <修改描述:>
 */
package com.tx.component.auth.context;

import javax.sql.DataSource;

import net.sf.ehcache.Ehcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.tx.component.auth.context.authchecker.AuthChecker;
import com.tx.component.auth.context.loader.impl.XmlAuthLoader;
import com.tx.component.auth.context.loaderprocessor.ChildAuthRegisterSupportLoaderProcessor;
import com.tx.component.auth.context.loaderprocessor.ControllerAuthRegisterSupportLoaderProcessor;
import com.tx.component.auth.persister.AuthItemPersister;
import com.tx.component.auth.persister.dao.AuthItemImplDao;
import com.tx.component.auth.persister.dao.AuthItemRefImplDao;
import com.tx.component.auth.persister.dao.impl.AuthItemImplDaoImpl;
import com.tx.component.auth.persister.dao.impl.AuthItemRefImplDaoImpl;
import com.tx.component.auth.persister.service.AuthItemImplService;
import com.tx.component.auth.persister.service.AuthItemRefImplService;
import com.tx.component.auth.persister.service.NotTempAuthItemRefImplService;
import com.tx.core.dbscript.context.DBScriptExecutorContext;
import com.tx.core.exceptions.util.AssertUtils;

/**
 * 权限容器配置器<br/>
 * <功能详细描述>
 * 
 * @author  Administrator
 * @version  [版本号, 2014年2月26日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Configuration
public class AuthContextConfigurator implements InitializingBean,
        ApplicationContextAware {
    
    /** 增加对Controller权限加载的支持 */
    @Bean(name = "auth.controllerAuthRegisterSupportLoaderProcessor")
    public AuthItemLoaderProcessor controllerAuthRegisterSupportLoaderProcessor(){
        ControllerAuthRegisterSupportLoaderProcessor processor = new ControllerAuthRegisterSupportLoaderProcessor();
        processor.setBasePackages(scanControllerAuthBasePackages);
        return processor;
    }
    
    /** 增加对子权限加载的支撑 */
    @Bean(name = "auth.childAuthRegisterSupportLoaderProcessor")
    public AuthItemLoaderProcessor childAuthRegisterSupportLoaderProcessor() {
        ChildAuthRegisterSupportLoaderProcessor processor = new ChildAuthRegisterSupportLoaderProcessor();
        return processor;
    }
    
    @Bean(name = "authContext")
    public AuthContextFactory authContext() {
        AuthContextFactory authContextFactory = new AuthContextFactory();
        authContextFactory.setEhcache(this.ehcache);
        authContextFactory.setDatabaseSchemaUpdate(databaseSchemaUpdate);
        authContextFactory.setDataSource(dataSource);
        authContextFactory.setDbScriptExecutorContext(dbScriptExecutorContext);
        authContextFactory.setDefaultAuthChecker(defaultAuthChecker);
        authContextFactory.setJdbcTemplate(jdbcTemplate);
        authContextFactory.setPlatformTransactionManager(platformTransactionManager);
        authContextFactory.setSystemId(systemId);
        authContextFactory.setTableSuffix(tableSuffix);
        return authContextFactory;
    }
    
    @Bean(name = "authItemRefImplDao")
    public AuthItemRefImplDao authItemRefImplDao() {
        AuthItemRefImplDao authItemRefImplDao = new AuthItemRefImplDaoImpl(
                this.jdbcTemplate, this.dataSource);
        return authItemRefImplDao;
    }
    
    @Bean(name = "authItemImplDao")
    public AuthItemImplDao authItemImplDao() {
        AuthItemImplDao authItemImplDao = new AuthItemImplDaoImpl(
                this.jdbcTemplate, this.dataSource);
        return authItemImplDao;
    }
    
    @Bean(name = "authItemRefImplService")
    public AuthItemRefImplService authItemRefImplService() {
        AuthItemRefImplService authItemRefImplService = new AuthItemRefImplService(
                this.platformTransactionManager);
        return authItemRefImplService;
    }
    
    @Bean(name = "authItemImplService")
    public AuthItemImplService authItemImplService() {
        AuthItemImplService authItemImplService = new AuthItemImplService(
                this.platformTransactionManager);
        return authItemImplService;
    }
    
    @Bean(name = "authItemPersister")
    public AuthItemPersister authItemPersister() {
        AuthItemPersister authItemPersister = new AuthItemPersister(
                this.tableSuffix, this.systemId);
        return authItemPersister;
    }
    
    @Bean(name = "xmlAuthLoader")
    public XmlAuthLoader xmlAuthLoader() {
        XmlAuthLoader xmlAuthLoader = new XmlAuthLoader(this.authConfigLocaions);
        return xmlAuthLoader;
    }
    
    @Bean(name = "notTempAuthItemRefImplService")
    public NotTempAuthItemRefImplService notTempAuthItemRefImplService() {
        NotTempAuthItemRefImplService notTempAuthItemRefImplService = new NotTempAuthItemRefImplService(
                this.platformTransactionManager);
        return notTempAuthItemRefImplService;
    }
    
    /** 日志记录器 */
    protected static final Logger logger = LoggerFactory.getLogger(AuthContextConfigurator.class);
    
    /** 默认的权限检查器 */
    protected AuthChecker defaultAuthChecker;
    
    /** 权限项缓存对应的缓存生成器 */
    protected Ehcache ehcache;
    
    /** 系统id 64，用以与其他系统区分 */
    protected String systemId;
    
    /** 表后缀名 */
    protected String tableSuffix;
    
    /** 数据库脚本是否自动执行 */
    protected boolean databaseSchemaUpdate = false;
    
    /** 数据脚本自动执行器 */
    protected DBScriptExecutorContext dbScriptExecutorContext;
    
    /** 当前spring容器 */
    protected ApplicationContext applicationContext;
    
    /** 事务处理器:如果dataSource为空则该值不能为空 */
    protected PlatformTransactionManager platformTransactionManager;
    
    /** 数据源 */
    protected DataSource dataSource;
    
    /** jdbc句柄:如果dataSource为空则该值不能为空 */
    protected JdbcTemplate jdbcTemplate;
    
    /** 权限配置地址 */
    protected String[] authConfigLocaions;
    
    private String scanControllerAuthBasePackages = "com.tx";
    
    /**
     * @param arg0
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    /**
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.dataSource == null) {
            AssertUtils.notNull(this.jdbcTemplate,
                    "dataSource or jdbcTemplate is null");
            AssertUtils.notNull(this.platformTransactionManager,
                    "platformTransactionManager or jdbcTemplate is null");
        } else {
            if (this.jdbcTemplate == null) {
                this.jdbcTemplate = new JdbcTemplate(this.dataSource);
            }
            if (this.platformTransactionManager == null) {
                this.platformTransactionManager = new DataSourceTransactionManager(
                        this.dataSource);
            }
        }
    }
    
    /**
     * @param 对defaultAuthChecker进行赋值
     */
    public void setDefaultAuthChecker(AuthChecker defaultAuthChecker) {
        this.defaultAuthChecker = defaultAuthChecker;
    }
    
    /**
     * @param 对cache进行赋值
     */
    public void setEhcache(Ehcache ehcache) {
        this.ehcache = ehcache;
    }
    
    /**
     * @param 对systemId进行赋值
     */
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }
    
    /**
     * @param 对tableSuffix进行赋值
     */
    public void setTableSuffix(String tableSuffix) {
        this.tableSuffix = tableSuffix;
    }
    
    /**
     * @param 对databaseSchemaUpdate进行赋值
     */
    public void setDatabaseSchemaUpdate(boolean databaseSchemaUpdate) {
        this.databaseSchemaUpdate = databaseSchemaUpdate;
    }
    
    /**
     * @return 返回 dbScriptExecutorContext
     */
    public DBScriptExecutorContext getDbScriptExecutorContext() {
        return dbScriptExecutorContext;
    }
    
    /**
     * @param 对dbScriptExecutorContext进行赋值
     */
    public void setDbScriptExecutorContext(
            DBScriptExecutorContext dbScriptExecutorContext) {
        this.dbScriptExecutorContext = dbScriptExecutorContext;
    }
    
    /**
     * @param 对platformTransactionManager进行赋值
     */
    public void setPlatformTransactionManager(
            PlatformTransactionManager platformTransactionManager) {
        this.platformTransactionManager = platformTransactionManager;
    }
    
    /**
     * @param 对dataSource进行赋值
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    /**
     * @param 对jdbcTemplate进行赋值
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * @param 对scanControllerAuthBasePackages进行赋值
     */
    public void setScanControllerAuthBasePackages(
            String scanControllerAuthBasePackages) {
        this.scanControllerAuthBasePackages = scanControllerAuthBasePackages;
    }
}
