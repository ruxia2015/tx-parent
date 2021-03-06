/*
s * 描          述:  <描述>
 * 修  改   人:  Administrator
 * 修改时间:  2014年5月11日
 * <修改描述:>
 */
package com.tx.component.file.context;

import java.io.InputStream;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.tx.component.file.FileContextConstants;
import com.tx.component.file.model.FileDefinition;
import com.tx.component.file.resource.FileDefinitionResource;
import com.tx.core.exceptions.io.ResourceIsExistException;
import com.tx.core.exceptions.util.AssertUtils;

/**
 * 文件处理容器<br/>
 * 利用该容器能够实现存放文件<br/>
 *      获取文件<br/>
 *      存放临时文件<br/>
 *      自动清理临时文件<br/>
 *      获取临时文件流<br/>
 * ... ...<br/>
 * 不建议在该封装中对业务逻辑相关查询进行支撑<br/>
 * 文件容器对文件的获取最好都是find的逻辑关系<br/>
 * 文件容器，作为控件支撑系统中的文件存放逻辑<br/>
 * 
 * @author Rain.he
 * @version [版本号, 2015年3月12日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class FileContext extends FileContextBuilder implements InitializingBean {
    
    /** 文件容器自我引用 */
    public static FileContext context;
    
    /**
     * 返回自身唯一引用
     *
     * @return MRSContext 消息路由服务容器
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     * @version [版本号, 2015年11月12日]
     * @author rain
     */
    public static FileContext getContext() {
        if (FileContext.context != null) {
            return FileContext.context;
        }
        synchronized (FileContext.class) {
            FileContext.context = applicationContext.getBean(beanName,
                    FileContext.class);
        }
        AssertUtils.notNull(FileContext.context, "context is null.");
        
        return FileContext.context;
    }
    
    /**
     * 保存文件<br/>
     * 如果文件已经存在，则复写当前文件<br/>
     * 如果文件不存在，则创建文件后写入<br/>
     * 如果对应文件所在的文件夹不存在，对应文件夹会自动创建<br/>
     * 
     * @param relativePath 存储路径,此存储路径为文件全路径(包括扩展名)
     * @param input 文件流
     * 
     * @return FileDefinition 文件定义的实体
     * 
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    @Transactional
    public FileDefinition save(String relativePath, InputStream input) {
        AssertUtils.notEmpty(relativePath, "relativePath is null.");
        AssertUtils.notNull(input, "input is null.");
        
        FileDefinition fileDefinition = save(FileContextConstants.DEFAULT_MODULE,
                relativePath,
                input,
                StringUtils.getFilename(relativePath));
        
        return fileDefinition;
    }
    
    /**
     * 保存文件<br/>
     * 如果文件已经存在，则复写当前文件<br/>
     * 如果文件不存在，则创建文件后写入<br/>
     * 如果对应文件所在的文件夹不存在，对应文件夹会自动创建<br/>
     * 
     * @param relativePath 存储路径,此存储路径为文件全路径(包括扩展名)
     * @param input 文件流
     * @param filename 文件名,此文件的实际文件名称
     * 
     * @return FileDefinition 文件定义的实体
     * 
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    @Transactional
    public FileDefinition save(String module, String relativePath,
            InputStream input) {
        AssertUtils.notEmpty(relativePath, "relativePath is null.");
        AssertUtils.notNull(input, "input is null.");
        
        FileDefinition fileDefinition = save(module,
                relativePath,
                input,
                StringUtils.getFilename(relativePath));
        
        return fileDefinition;
    }
    
    /**
     * 保存文件<br/>
     * 如果文件已经存在，则复写当前文件<br/>
     * 如果文件不存在，则创建文件后写入<br/>
     * 如果对应文件所在的文件夹不存在，对应文件夹会自动创建<br/>
     * 
     * @param relativePath 存储路径,此存储路径为文件全路径(包括扩展名)
     * @param input 文件流
     * @param filename 文件名,此文件的实际文件名称
     * 
     * @return FileDefinition 文件定义的实体
     * 
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    @Transactional
    public FileDefinition save(String relativePath, InputStream input,
            String filename) {
        AssertUtils.notEmpty(relativePath, "relativePath is null.");
        AssertUtils.notNull(input, "input is null.");
        
        FileDefinition fileDefinition = save(FileContextConstants.DEFAULT_MODULE,
                relativePath,
                input,
                filename);
        
        return fileDefinition;
    }
    
    /**
     * 保存文件<br/>
     * 如果文件已经存在，则复写当前文件<br/>
     * 如果文件不存在，则创建文件后写入<br/>
     * 如果对应文件所在的文件夹不存在，对应文件夹会自动创建<br/>
     * 
     * @param module 归属模块
     * @param relativePath 存储路径,此存储路径为文件全路径(包括扩展名)
     * @param input 文件流
     * @param filename 文件名,此文件的实际文件名称
     * 
     * @return FileDefinition 文件定义的实体
     *         
     * @exception [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    @Transactional
    public FileDefinition save(String module, String relativePath,
            InputStream input, String filename) {
        AssertUtils.notEmpty(relativePath, "relativePath is null.");
        AssertUtils.notNull(input, "input is null.");
        
        module = StringUtils.isEmpty(module) ? FileContextConstants.DEFAULT_MODULE
                : module;
        filename = StringUtils.isEmpty(filename) ? StringUtils.getFilename(relativePath)
                : filename;
        
        FileDefinition fileDefinition = doSaveFile(module,
                relativePath,
                input,
                filename);
        
        return fileDefinition;
    }
    
    /**
     * 保存文件<br/>
     * 如果文件已经存在，则会抛出ResourceIsExistException<br/>
     * 如果文件不存在，则创建文件后写入<br/>
     * 如果对应文件所在的文件夹不存在，对应文件夹会自动创建<br/>
     * <功能详细描述>
     * 
     * @param relativePath
     * @param input
     * @param filename
     * 
     * @return FileDefinition
     * @throws ResourceIsExistException [参数说明]
     * 
     * @return FileDefinition [返回类型说明]
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    @Transactional
    public FileDefinition add(String relativePath, InputStream input,
            String filename) throws ResourceIsExistException {
        AssertUtils.notEmpty(relativePath, "relativePath is null.");
        AssertUtils.notNull(input, "input is null.");
        
        FileDefinition fileDefinition = add(FileContextConstants.DEFAULT_MODULE,
                relativePath,
                input,
                filename);
        return fileDefinition;
    }
    
    /**
     * 保存文件<br/>
     * 如果文件已经存在，则会抛出ResourceIsExistException<br/>
     * 如果文件不存在，则创建文件后写入<br/>
     * 如果对应文件所在的文件夹不存在，对应文件夹会自动创建<br/>
     * 
     * @param module 归属模块
     * @param relativePath 存储路径,此存储路径为文件全路径(包括扩展名)
     * @param input 文件流
     * @param filename 文件名,此文件的实际文件名称
     *            
     * @return FileDefinition 文件定义的实体
     * @exception ResourceIsExistException 如果存在同全路径文件,则抛出此异常
     * @see [类、类#方法、类#成员]
     */
    @Transactional
    public FileDefinition add(String module, String relativePath,
            InputStream input, String filename) throws ResourceIsExistException {
        AssertUtils.notEmpty(relativePath, "relativePath is null.");
        AssertUtils.notNull(input, "input is null.");
        
        module = StringUtils.isEmpty(module) ? FileContextConstants.DEFAULT_MODULE
                : module;
        filename = StringUtils.isEmpty(filename) ? StringUtils.getFilename(relativePath)
                : filename;
        
        FileDefinition fileDefinition = doAddFile(module,
                relativePath,
                input,
                filename);
        return fileDefinition;
    }
    
    /**
     * 根据文件定义id删除对应的文件定义及对应的资源<br/>
     * 删除对应数据库文件资源数据以及存储中对应的文件资源
     * 
     * @param fileDefinitionId 文件定义id
     *            
     * @return void
     * @exception [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    @Transactional
    public void deleteById(String fileDefinitionId) {
        AssertUtils.notEmpty(fileDefinitionId, "fileDefinitionId is empty.");
        
        doDeleteById(fileDefinitionId);
    }
    
    /**
     * 根据文件定义id获取对应的文件定义实例对象<br/>
     * <功能详细描述>
     * 
     * @param fileDefinitionId 文件定义id
     *            
     * @return FileDefinition 文件定义实体
     * @exception [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    public FileDefinition findById(String fileDefinitionId) {
        AssertUtils.notEmpty(fileDefinitionId, "fileDefinitionId is empty.");
        
        FileDefinition res = doFindById(fileDefinitionId);
        
        return res;
    }
    
    /**
     * 根据文件定义id获取文件定义对应的资源<br/>
     * <功能详细描述>
     * 
     * @param fileDefinitionId 文件定义id
     *            
     * @return Resource 文件资源
     * @exception [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    public FileDefinitionResource getResourceById(String fileDefinitionId) {
        AssertUtils.notEmpty(fileDefinitionId, "fileDefinitionId is empty.");
        
        FileDefinition res = doFindById(fileDefinitionId);
        if (res == null) {
            return null;
        }
        
        FileDefinitionResource resource = res.getResource();
        return resource;
    }
}
