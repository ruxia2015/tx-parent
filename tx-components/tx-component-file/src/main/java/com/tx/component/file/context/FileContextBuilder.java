/*
 * 描          述:  <描述>
 * 修  改   人:  Administrator
 * 修改时间:  2014年5月11日
 * <修改描述:>
 */
package com.tx.component.file.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import com.thoughtworks.xstream.XStream;
import com.tx.component.file.config.FileContextConfig;
import com.tx.component.file.config.FileModuleCfg;
import com.tx.component.file.driver.FileDefinitionResourceDriver;
import com.tx.component.file.driver.FileDefinitionResourceDriverRegistry;
import com.tx.component.file.driver.impl.SystemFileDefinitionResourceDriver;
import com.tx.component.file.model.FileDefinition;
import com.tx.component.file.model.FileModule;
import com.tx.component.file.model.ReadWritePermissionEnum;
import com.tx.component.file.resource.FileDefinitionResource;
import com.tx.core.ddlutil.builder.DDLBuilder;
import com.tx.core.ddlutil.builder.alter.AlterTableDDLBuilder;
import com.tx.core.ddlutil.builder.create.CreateTableDDLBuilder;
import com.tx.core.ddlutil.executor.TableDDLExecutorFactory;
import com.tx.core.exceptions.util.AssertUtils;
import com.tx.core.util.UUIDUtils;
import com.tx.core.util.XstreamUtils;

/**
 * 文件容器构建器<br/>
 * 
 * @author Administrator
 * @version [版本号, 2014年5月11日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class FileContextBuilder extends FileContextConfigurator implements
        BeanNameAware, ResourceLoaderAware {
    
    /** beanName */
    protected static String beanName;
    
    /** 默认文件模块 */
    protected FileModule defaultFileModule;
    
    /** 模块映射 */
    protected final Map<String, FileModule> moduleMap = new HashMap<String, FileModule>();
    
    /** 文件容器配置 */
    protected final XStream fileModuleCfgXstream = XstreamUtils.getXstream(FileContextConfig.class);
    
    /** 资源加载器 */
    protected ResourceLoader resourceLoader;
    
    /**
     * @param resourceLoader
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    
    /** @param name */
    @Override
    public void setBeanName(String name) {
        FileContextBuilder.beanName = name;
    }
    
    /**
     * @throws Exception
     */
    @Override
    protected final void doBuild() throws Exception {
        this.tableDDLExecutor = TableDDLExecutorFactory.buildTableDDLExecutor(dataSourceType,
                dataSource);
        initTables();//初始化表定义
        
        parseFileModuleConfig();
        
        AssertUtils.notEmpty(this.location, "location is empty.");
        this.defaultFileModule = new FileModule("default",
                ReadWritePermissionEnum.PUBLIC_READ,
                new SystemFileDefinitionResourceDriver(this.location));
    }
    
    /** 
     * 解析文件模块配置<br/>
     * <功能详细描述>
     * @throws IOException [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    private void parseFileModuleConfig() throws IOException {
        Resource configResource = this.resourceLoader.getResource(this.location);
        InputStream inputStream = configResource.getInputStream();
        FileContextConfig fcc = (FileContextConfig) fileModuleCfgXstream.fromXML(inputStream);
        if (fcc.getFileModuleConfig() == null) {
            return;
        }
        if (CollectionUtils.isEmpty(fcc.getFileModuleConfig()
                .getFileModuleCfg())) {
            return;
        }
        for (FileModuleCfg fmcfg : fcc.getFileModuleConfig().getFileModuleCfg()) {
            if (StringUtils.isEmpty(fmcfg.getModule())) {
                continue;
            }
            FileModule fm = new FileModule();
            fm.setDriver(FileDefinitionResourceDriverRegistry.parseDriver(fmcfg.getDriver()));
            fm.setModule(fmcfg.getModule());
            fm.setPermission(fmcfg.getPermission());
            moduleMap.put(fmcfg.getModule(), fm);
        }
    }
    
    /**
     * 整理传入的relativePath的值<br/>
     *     relativePath不能为空
     *     relativePath不应以"/"开始
     * <功能详细描述>
     * @param relativePath
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
    */
    private String cleanRelativePath(String relativePath) {
        AssertUtils.notEmpty(relativePath, "relativePath is empty.");
        relativePath = StringUtils.cleanPath(relativePath);//整理path中"\\"为"/"
        while (relativePath.startsWith("/")) {
            //去除path中尾部存在的"/"
            relativePath = relativePath.substring(1, relativePath.length());
        }
        AssertUtils.notEmpty(relativePath, "relativePath is empty.");
        return relativePath;
    }
    
    /**
      * 获取模块对应的文件文件模块对象<br/>
      * <功能详细描述>
      * @param module
      * @return [参数说明]
      * 
      * @return FileModule [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    private FileModule getFileModule(String module) {
        AssertUtils.notEmpty(module, "module is empty.");
        
        if (!moduleMap.containsKey(module)) {
            return defaultFileModule;
        }
        
        FileModule fm = moduleMap.get(module);
        return fm;
    }
    
    /**
     * 返回文件定义对象<br/>
     * 
     * @param fileId 文件定义id
     * 
     * @return FileDefinition 文件定义
     * @exception [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    protected FileDefinition doFindById(String fileId) {
        AssertUtils.notEmpty(fileId, "fileId is empty.");
        
        FileDefinition fileDefinition = getFileDefinitionService().findById(fileId);
        if (fileDefinition == null) {
            return null;
        }
        
        String module = fileDefinition.getModule();
        FileModule fm = getFileModule(module);
        FileDefinitionResourceDriver driver = fm.getDriver();
        FileDefinitionResource resource = driver.getResource(fileDefinition);
        
        fileDefinition.setResource(resource);
        
        return fileDefinition;
    }
    
    /**
     * 删除对应的文件，并将对应文件的相关记录移除到历史表中<br/>
     * 
     * @param fileId 文件定义id
     * 
     * @return void
     * @exception [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    protected void doDeleteById(String fileId) {
        AssertUtils.notEmpty(fileId, "fileId is empty.");
        
        FileDefinition fileDefinition = getFileDefinitionService().findById(fileId);
        if (fileDefinition == null) {
            return;
        }
        
        getFileDefinitionService().moveToHisById(fileId);
        
        String module = fileDefinition.getModule();
        FileModule fm = getFileModule(module);
        FileDefinitionResourceDriver driver = fm.getDriver();
        FileDefinitionResource resource = driver.getResource(fileDefinition);
        
        resource.delete();
    }
    
    /**
     * 
     * 保存文件，允许存在的文件写入<br/>
     * 
     * @param relativePath 存储路径
     * @param filename 文件名
     * @param input 文件流
     * 
     * @return FileDefinition 文件定义的实体
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    protected FileDefinition doSaveFile(String module, String relativePath,
            InputStream input, String filename) {
        AssertUtils.notEmpty(module, "module is empty.");
        AssertUtils.notEmpty(relativePath, "relativePath is empty.");
        AssertUtils.notEmpty(filename, "filename is empty.");
        AssertUtils.notNull(input, "input is empty.");
        
        relativePath = cleanRelativePath(relativePath);//相对路径
        //持久化对应的文件对象
        FileDefinition fileDefinition = fileDefinitionService().findByRelativePath(relativePath);
        if (fileDefinition != null) {
            FileModule fm = getFileModule(module);
            FileDefinitionResourceDriver driver = fm.getDriver();
            FileDefinitionResource resource = driver.getResource(fileDefinition);
            resource.save(input);
            
            fileDefinition.setViewUrl(resource.getViewUrl());
            fileDefinition.setResource(resource);
            
            getFileDefinitionService().updateById(fileDefinition);
        } else {
            fileDefinition = buildFileDefinition(module, relativePath, filename);
            FileModule fm = getFileModule(module);
            FileDefinitionResourceDriver driver = fm.getDriver();
            FileDefinitionResource resource = driver.getResource(fileDefinition);
            resource.add(input);
            
            fileDefinition.setViewUrl(resource.getViewUrl());
            fileDefinition.setResource(resource);
            
            getFileDefinitionService().insert(fileDefinition);
        }
        
        return fileDefinition;
    }
    
    /**
     * 新增文件，如果文件已经存在，将会抛出异常<br/>
     * 
     * @param relativePath 存储路径
     * @param filename 文件名
     * @param input 文件流
     * 
     * @return FileDefinition 文件定义
     * @exception [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    protected FileDefinition doAddFile(String module, String relativePath,
            InputStream input, String filename) {
        AssertUtils.notEmpty(module, "module is empty.");
        AssertUtils.notEmpty(relativePath, "relativePath is empty.");
        AssertUtils.notNull(input, "input is empty.");
        AssertUtils.notEmpty(filename, "filename is empty.");
        
        relativePath = cleanRelativePath(relativePath);//相对路径
        //持久化对应的文件对象
        FileDefinition fileDefinition = buildFileDefinition(module,
                relativePath,
                filename);
        
        FileModule fm = getFileModule(module);
        FileDefinitionResourceDriver driver = fm.getDriver();
        FileDefinitionResource resource = driver.getResource(fileDefinition);
        resource.add(input);
        
        fileDefinition.setViewUrl(resource.getViewUrl());
        fileDefinition.setResource(resource);
        
        return fileDefinition;
    }
    
    /**
     * 创建fileDefinition并持久化对应的文件定义<br/>
     * 
     * @param relativePath 存储路径
     * @param filename 文件名
     * 
     * @return FileDefinition 文件定义
     * @exception [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    private FileDefinition buildFileDefinition(String module,
            String relativePath, String filename) {
        AssertUtils.notEmpty(module, "module is empty.");
        AssertUtils.notEmpty(relativePath, "relativePath is empty.");
        AssertUtils.notEmpty(filename, "filename is empty.");
        
        relativePath = cleanRelativePath(relativePath);//相对路径
        Date now = new Date();
        
        FileDefinition fileDefinition = new FileDefinition();
        fileDefinition.setId(UUIDUtils.generateUUID());
        fileDefinition.setCreateDate(now);
        fileDefinition.setLastUpdateDate(now);
        
        fileDefinition.setModule(module);
        fileDefinition.setRelativePath(relativePath);
        fileDefinition.setFilename(filename);
        fileDefinition.setFilenameExtension(StringUtils.getFilenameExtension(relativePath));
        
        fileDefinition.setSystem(this.system);
        
        return fileDefinition;
    }
    
    /**
     * 为容器创建或升级表
     * <功能详细描述> [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
    */
    private void initTables() {
        table_core_file_definition();
    }
    
    /**
      * 核心文件定义表<br/>
      * <功能详细描述> [参数说明]
      * 
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    private void table_core_file_definition() {
        String tableName = "core_file_definition";
        String hisTableName = "core_file_definition_his";
        
        CreateTableDDLBuilder createDDLBuilder = null;
        AlterTableDDLBuilder alterDDLBuilder = null;
        DDLBuilder<?> ddlBuilder = null;
        CreateTableDDLBuilder hisCreateDDLBuilder = null;
        AlterTableDDLBuilder hisAlterDDLBuilder = null;
        DDLBuilder<?> hisDDLBuilder = null;
        
        if (this.tableDDLExecutor.exists(tableName)) {
            alterDDLBuilder = this.tableDDLExecutor.generateAlterTableDDLBuilder(tableName);
            ddlBuilder = alterDDLBuilder;
        } else {
            createDDLBuilder = this.tableDDLExecutor.generateCreateTableDDLBuilder(tableName);
            ddlBuilder = createDDLBuilder;
        }
        
        if (this.tableDDLExecutor.exists(hisTableName)) {
            hisAlterDDLBuilder = this.tableDDLExecutor.generateAlterTableDDLBuilder(hisTableName);
            hisDDLBuilder = hisAlterDDLBuilder;
        } else {
            hisCreateDDLBuilder = this.tableDDLExecutor.generateCreateTableDDLBuilder(hisTableName);
            hisDDLBuilder = hisCreateDDLBuilder;
        }
        
        core_file_definition(ddlBuilder);//写入表结构
        ddlBuilder.newIndex(true,
                "idx_file_definition_00",
                "relativePath",
                "module");
        
        if (alterDDLBuilder != null
                && alterDDLBuilder.isNeedAlter(false, false)) {
            this.tableDDLExecutor.alter(alterDDLBuilder, false, false);
        } else if (createDDLBuilder != null) {
            this.tableDDLExecutor.create(createDDLBuilder);
        }
        
        core_file_definition(hisDDLBuilder);//写入表结构
        if (hisAlterDDLBuilder != null
                && hisAlterDDLBuilder.isNeedAlter(false, false)) {
            this.tableDDLExecutor.alter(hisAlterDDLBuilder, false, false);
        } else if (hisCreateDDLBuilder != null) {
            this.tableDDLExecutor.create(hisCreateDDLBuilder);
        }
    }
    
    /**
     * dt_table的构建器<br/>
     * <功能详细描述>
     * @param ddlBuilder [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
    */
    private void core_file_definition(DDLBuilder<?> ddlBuilder) {
        ddlBuilder.newColumnOfVarchar(true, "id", 64, true, null)
                .newColumnOfVarchar("system", 64, true, null)
                .newColumnOfVarchar("module", 64, true, null)
                .newColumnOfVarchar("relativePath", 256, true, null)
                .newColumnOfVarchar("filename", 64, true, null)
                .newColumnOfVarchar("filenameExtension", 64, false, null)
                .newColumnOfVarchar("viewUrl", 256, false, null)
                .newColumnOfDate("deleteDate", false, false)
                .newColumnOfDate("lastUpdateDate", true, true)
                .newColumnOfDate("createDate", true, true);
    }
    
    //  public static void main(String[] args) {
    //        String relativePath = "/\\sdfads\\sdfadsd\\test.txt";
    //        AssertUtils.notEmpty(relativePath, "relativePath is empty.");
    //        relativePath = StringUtils.cleanPath(relativePath);//整理path中"\\"为"/"
    //        while (relativePath.startsWith("/")) {
    //            //去除path中尾部存在的"/"
    //            relativePath = relativePath.substring(1, relativePath.length());
    //        }
    //        AssertUtils.notEmpty(relativePath, "relativePath is empty.");
    //        
    //        System.out.println(relativePath);
    //    }
}
