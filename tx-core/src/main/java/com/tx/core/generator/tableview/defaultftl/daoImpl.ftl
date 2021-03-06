/*
 * 描          述:  <描述>
 * 修  改   人:  
 * 修改时间:  
 * <修改描述:>
 */
package ${dao.basePackage}.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import ${dao.basePackage}.dao.${dao.simpleEntityTypeName}Dao;
import ${dao.entityTypeName};
import com.tx.core.mybatis.model.Order;
import com.tx.core.mybatis.support.MyBatisDaoSupport;
import com.tx.core.paged.model.PagedList;

/**
 * ${dao.simpleEntityTypeName}持久层
 * <功能详细描述>
 * 
 * @author  
 * @version  [版本号, 2012-12-11]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Component("${dao.lowerCaseEntityTypeName}Dao")
public class ${dao.simpleEntityTypeName}DaoImpl implements ${dao.simpleEntityTypeName}Dao {
    
    @Resource(name = "myBatisDaoSupport")
    private MyBatisDaoSupport myBatisDaoSupport;
    
    /**
     * @param condition
     * @return
     */
    @Override
    public ${dao.simpleEntityTypeName} find(${dao.simpleEntityTypeName} condition) {
        return this.myBatisDaoSupport.<${dao.simpleEntityTypeName}> find("${dao.lowerCaseEntityTypeName}.find${dao.simpleEntityTypeName}", condition);
    }
    
    /**
     * @param params
     * @return
     */
    @Override
    public List<${dao.simpleEntityTypeName}> queryList(Map<String, Object> params) {
        return this.myBatisDaoSupport.<${dao.simpleEntityTypeName}> queryList("${dao.lowerCaseEntityTypeName}.query${dao.simpleEntityTypeName}",
                params);
    }
    
    /**
     * @param params
     * @param orderList
     * @return
     */
    @Override
    public List<${dao.simpleEntityTypeName}> queryList(Map<String, Object> params,
            List<Order> orderList) {
        return this.myBatisDaoSupport.<${dao.simpleEntityTypeName}> queryList("${dao.lowerCaseEntityTypeName}.query${dao.simpleEntityTypeName}",
                params,
                orderList);
    }
    
    /**
     * @param params
     * @return
     */
    @Override
    public int count(Map<String, Object> params) {
        return this.myBatisDaoSupport.<Integer> find("${dao.lowerCaseEntityTypeName}.query${dao.simpleEntityTypeName}Count",
                params);
    }
    
    /**
     * @param params
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public PagedList<${dao.simpleEntityTypeName}> queryPagedList(Map<String, Object> params,
            int pageIndex, int pageSize) {
        return this.myBatisDaoSupport.<${dao.simpleEntityTypeName}> queryPagedList("${dao.lowerCaseEntityTypeName}.query${dao.simpleEntityTypeName}",
                params,
                pageIndex,
                pageSize);
    }
    
    /**
     * @param params
     * @param pageIndex
     * @param pageSize
     * @param orderList
     * @return
     */
    @Override
    public PagedList<${dao.simpleEntityTypeName}> queryPagedList(Map<String, Object> params,
            int pageIndex, int pageSize, List<Order> orderList) {
        return this.myBatisDaoSupport.<${dao.simpleEntityTypeName}> queryPagedList("${dao.lowerCaseEntityTypeName}.query${dao.simpleEntityTypeName}",
                params,
                pageIndex,
                pageSize,
                orderList);
    }
}
