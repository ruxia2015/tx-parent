/*
 * 描          述:  <描述>
 * 修  改   人:  brady
 * 修改时间:  2013-1-28
 * <修改描述:>
 */
package com.tx.component.rule.drools;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.reflection.MetaObject;
import org.drools.base.MapGlobalResolver;
import org.drools.runtime.Globals;
import org.drools.runtime.StatefulKnowledgeSession;

import com.tx.component.rule.exceptions.RuleAccessException;
import com.tx.component.rule.support.RuleSessionContext;
import com.tx.component.rule.support.impl.BaseRuleSession;

/**
 * drools规则会话实例<br/>
 * <功能详细描述>
 * 
 * @author  brady
 * @version  [版本号, 2013-1-28]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DroolsRuleSession extends BaseRuleSession<DroolsRule> {
    
    /**
     * drools规则<默认构造函数>
     */
    public DroolsRuleSession(DroolsRule rule) {
        super(rule);
    }
    
    /**
     * @param fact
     */
    @Override
    public void execute(Map<String, Object> fact) {
        StatefulKnowledgeSession session = this.rule.getKnowledgeBase()
                .newStatefulKnowledgeSession();
        try {
            //获取全局对象
            Map<String, Object> globas = RuleSessionContext.getGlobals();
            
            //设置全局对象
            for (Entry<String, Object> entryTemp : globas.entrySet()) {
                session.setGlobal(entryTemp.getKey(), entryTemp.getValue());
            }
            
            //插入事实
            session.insert(fact);
            
            //触发规则调用
            session.fireAllRules();
        }
        catch (Exception e) {
            
        }
        finally {
            Globals globals = session.getGlobals();
            if (globals instanceof MapGlobalResolver) {
                MapGlobalResolver globalInstance = (MapGlobalResolver)globals;
                MetaObject mo = MetaObject.forObject(globalInstance);
                @SuppressWarnings("unchecked")
                Map<String, Object> globalMap = (Map<String, Object>)mo.getValue("map");
                RuleSessionContext.setGlobals(globalMap);
            }
            else {
                throw new RuleAccessException(this.rule().rule(), this.rule(),
                        this,
                        "drools globals is not MapGlobalResolver instance");
            }
            if (session != null) {
                session.dispose();
            }
        }
    }
    
    /**
     * @param facts
     */
    @Override
    public void execute(List<Map<String, Object>> facts) {
        StatefulKnowledgeSession session = this.rule.getKnowledgeBase()
                .newStatefulKnowledgeSession();
        try {
            //获取全局对象
            Map<String, Object> globas = RuleSessionContext.getGlobals();
            
            //设置全局对象
            for (Entry<String, Object> entryTemp : globas.entrySet()) {
                session.setGlobal(entryTemp.getKey(), entryTemp.getValue());
            }
            
            //插入事实
            for(Map<String, Object> fact : facts){
                session.insert(fact);
            }
            
            //触发规则调用
            session.fireAllRules();
        }
        catch (Exception e) {
            
        }
        finally {
            Globals globals = session.getGlobals();
            if (globals instanceof MapGlobalResolver) {
                MapGlobalResolver globalInstance = (MapGlobalResolver)globals;
                MetaObject mo = MetaObject.forObject(globalInstance);
                @SuppressWarnings("unchecked")
                Map<String, Object> globalMap = (Map<String, Object>)mo.getValue("map");
                RuleSessionContext.setGlobals(globalMap);
            }
            else {
                throw new RuleAccessException(this.rule().rule(), this.rule(),
                        this,
                        "drools globals is not MapGlobalResolver instance");
            }
            if (session != null) {
                session.dispose();
            }
        }
    }
    
}
