/*
 * 描          述:  <描述>
 * 修  改   人:  Rain.he
 * 修改时间:  2015年4月28日
 * <修改描述:>
 */
package com.tx.core.exceptions.rpc;

import com.tx.core.exceptions.SILException;

/**
 * 远程接口调用异常
 * 
 * @author Rain.he
 * @version [版本号, 2015年4月28日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RPCAccessException extends SILException {
    private static final long serialVersionUID = -5085684691190099695L;
    
    /**
     * <默认构造函数><br/>
     * <构造功能简述>
     * 
     * @param message
     * @param parameters
     * 
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    public RPCAccessException(String message, Object[] parameters) {
        super(message, parameters);
    }
    
    /**
     * <默认构造函数><br/>
     * <构造功能简述>
     * 
     * @param message
     * @param cause
     * 
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    public RPCAccessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * <默认构造函数><br/>
     * <构造功能简述>
     * 
     * @param message
     * 
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
     */
    public RPCAccessException(String message) {
        super(message);
    }
    
    /**
     * @return
     */
    @Override
    protected String doGetErrorCode() {
        return "RPC_ACCESS_ERROR";
    }
    
    /**
     * @return
     */
    @Override
    protected String doGetErrorMessage() {
        return "远程接口调用异常";
    }
}
