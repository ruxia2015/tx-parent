/*
 * 描          述:  <描述>
 * 修  改   人:  Rain.he
 * 修改时间:  2015年3月30日
 * <修改描述:>
 */
package com.tx.component.messagerouter.exception;

import com.tx.core.exceptions.SILException;
import com.tx.core.util.MessageUtils;

/**
 * 手机应用异常
 * 
 * @author Rain.he
 * @version [版本号, 2015年3月30日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class MRSException extends SILException {
    private static final long serialVersionUID = -2323505371393070811L;
    
    @Override
    protected String doGetErrorCode() {
        return "MRS_ERROR";
    }
    
    @Override
    protected String doGetErrorMessage() {
        return "消息路由服务异常";
    }
    
    public MRSException(Throwable cause, String message, String... strings) {
        super(MessageUtils.createMessage(message, (Object[]) strings), cause);
    }
    
    public MRSException(String message, String... strings) {
        super(message, (Object[]) strings);
    }
    
    public MRSException(String message, Object[] parameters) {
        super(message, parameters);
    }
    
    public MRSException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public MRSException(String message) {
        super(message);
    }
}
