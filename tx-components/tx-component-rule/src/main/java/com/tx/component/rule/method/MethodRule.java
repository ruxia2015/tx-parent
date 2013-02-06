/*
 * 描          述:  <描述>
 * 修  改   人:  pengqingyang
 * 修改时间:  2013-1-27
 * <修改描述:>
 */
package com.tx.component.rule.method;

import java.lang.reflect.Method;

import com.tx.component.rule.RuleConstants;
import com.tx.component.rule.model.Rule;

/**
 * 方法类型的规则<br/>
 * 
 * @author  pengqingyang
 * @version  [版本号, 2013-1-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MethodRule implements Rule {
    
    private Method method;
    
    private Object object;
    
    private com.tx.component.rule.method.annotation.RuleMethod ruleAnnotation;
    
    /**
     * @return
     */
    @Override
    public String rule() {
        return ruleAnnotation.rule();
    }
    
    /**
     * @return
     */
    @Override
    public String getServiceType() {
        return ruleAnnotation.serviceType();
    }

    /**
     * @return
     */
    @Override
    public String getRuleType() {
        return RuleConstants.RULE_TYPE_RULE_METHOD;
    }
    
    /**
     * @return 返回 method
     */
    public Method getMethod() {
        return method;
    }
    
    /**
     * @param 对method进行赋值
     */
    public void setMethod(Method method) {
        this.method = method;
    }
    
    /**
     * @return 返回 ruleAnnotation
     */
    public com.tx.component.rule.method.annotation.RuleMethod getRuleAnnotation() {
        return ruleAnnotation;
    }
    
    /**
     * @param 对ruleAnnotation进行赋值
     */
    public void setRuleAnnotation(
            com.tx.component.rule.method.annotation.RuleMethod ruleAnnotation) {
        this.ruleAnnotation = ruleAnnotation;
    }

    /**
     * @return 返回 object
     */
    public Object getObject() {
        return object;
    }

    /**
     * @param 对object进行赋值
     */
    public void setObject(Object object) {
        this.object = object;
    }
}