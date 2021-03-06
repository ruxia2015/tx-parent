/*
 * 描          述:  <描述>
 * 修  改   人:  
 * 修改时间:  
 * <修改描述:>
 */
package ${dao.basePackage}.dao;

import java.util.List;
import java.util.Map;

import com.tx.core.mybatis.model.Order;
import com.tx.core.paged.model.PagedList;
import ${dao.entityTypeName};

/**
 * ${dao.simpleEntityTypeName}持久层
 * <功能详细描述>
 * 
 * @author  
 * @version  [版本号, ]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface ${dao.simpleEntityTypeName}Dao {
    
    /**
      * 查询${dao.simpleEntityTypeName}实体
      * auto generate
      * <功能详细描述>
      * @param condition
      * @return [参数说明]
      * 
      * @return ${dao.simpleEntityTypeName} [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    //auto generate
    public ${dao.simpleEntityTypeName} find(${dao.simpleEntityTypeName} condition);
    
    /**
      * 根据条件查询${dao.simpleEntityTypeName}列表
      * auto generate
      * <功能详细描述>
      * @param params
      * @return [参数说明]
      * 
      * @return List<${dao.simpleEntityTypeName}> [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    //auto generate
    public List<${dao.simpleEntityTypeName}> queryList(Map<String, Object> params);
    
    /**
      * 根据指定查询条件以及排序列查询${dao.simpleEntityTypeName}列表
      * auto generate
      * <功能详细描述>
      * @param params
      * @param orderList
      * @return [参数说明]
      * 
      * @return List<${dao.simpleEntityTypeName}> [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    //auto generate
    public List<${dao.simpleEntityTypeName}> queryList(Map<String, Object> params,
            List<Order> orderList);
    
    /**
      * 根据条件查询${dao.simpleEntityTypeName}列表总数
      * auto generated
      * <功能详细描述>
      * @param params
      * @return [参数说明]
      * 
      * @return int [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public int count(Map<String, Object> params);
    
    /**
      * 分页查询${dao.simpleEntityTypeName}列表
      * auto generate
      * <功能详细描述>
      * @param params
      * @param pageIndex
      * @param pageSize
      * @return [参数说明]
      * 
      * @return PagedList<${dao.simpleEntityTypeName}> [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    //auto generate
    public PagedList<${dao.simpleEntityTypeName}> queryPagedList(Map<String, Object> params,
            int pageIndex, int pageSize);
    
    /**
      * 分页查询${dao.simpleEntityTypeName}列表，传入排序字段
      * auto generate
      * <功能详细描述>
      * @param params
      * @param pageIndex
      * @param pageSize
      * @param orderList
      * @return [参数说明]
      * 
      * @return PagedList<${dao.simpleEntityTypeName}> [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    //auto generate
    public PagedList<${dao.simpleEntityTypeName}> queryPagedList(Map<String, Object> params,
            int pageIndex, int pageSize, List<Order> orderList);
}
