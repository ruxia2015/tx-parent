/*
 * 描          述:  <描述>
 * 修  改   人:  brady
 * 修改时间:  2013-3-14
 * <修改描述:>
 */
package com.tx.component.rule.drools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.springframework.stereotype.Component;

import com.tx.component.rule.context.RuleLoader;
import com.tx.component.rule.model.Rule;
import com.tx.component.rule.model.RuleStateEnum;
import com.tx.component.rule.model.RuleTypeEnum;
import com.tx.component.rule.model.SimplePersistenceRule;
import com.tx.component.rule.model.SimpleRuleParamEnum;
import com.tx.component.rule.model.SimpleRulePropertyByte;
import com.tx.component.rule.service.SimplePersistenceRuleService;

/**
 * drools drl 类型的加载
 *      1、从数据库中读取drl资源加载
 * <功能详细描述>
 * 
 * @author  brady
 * @version  [版本号, 2013-3-14]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Component("drlByteDroolsRuleLoader")
public class DrlByteDroolsRuleLoader implements RuleLoader {
    
    /** 负责对规则进行持久  */
    @Resource(name = "simplePersistenceRuleService")
    private SimplePersistenceRuleService simplePersistenceRuleService;
    
    /** 规则加载器排序值 */
    private int order = 0;
    
    /**
     * @return
     */
    @Override
    public List<Rule> load() {
        List<Rule> resList = new ArrayList<Rule>();
        
        //获取到drools_drl_byte的资源列表
        List<SimplePersistenceRule> dbRuleList = this.simplePersistenceRuleService.querySimplePersistenceRuleListByRuleType(RuleTypeEnum.DROOLS_DRL_BYTE);
        //如果查询出的资源列表为空
        if (dbRuleList == null) {
            return resList;
        }
        
        //加载资源类规则
        for (SimplePersistenceRule spRuleTemp : dbRuleList) {
            if (!RuleStateEnum.OPERATION.equals(spRuleTemp.getState())) {
                //如果非运营态的规则，不进行加载
                continue;
            }
            //获取属性
            SimpleRulePropertyByte srpb = spRuleTemp.getByteProperty(SimpleRuleParamEnum.DROOLS_DRL_RESOURCE_BYTE);
            if (srpb == null || srpb.getParamValue() == null) {
                spRuleTemp.setState(RuleStateEnum.ERROR);
                this.simplePersistenceRuleService.changeRuleStateById(spRuleTemp.getId(),
                        RuleStateEnum.ERROR);
                continue;
            } else {
                KnowledgeBuilder kBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
                kBuilder.add(ResourceFactory.newByteArrayResource(srpb.getParamValue()),
                        ResourceType.DRL);
                
                if (kBuilder.hasErrors()) {
                    spRuleTemp.setState(RuleStateEnum.ERROR);
                    this.simplePersistenceRuleService.changeRuleStateById(spRuleTemp.getId(),
                            RuleStateEnum.ERROR);
                    continue;
                }
                
                Collection<KnowledgePackage> kpCollection = kBuilder.getKnowledgePackages();
                KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
                knowledgeBase.addKnowledgePackages(kpCollection);
                
                resList.add(new DroolsRule(spRuleTemp, knowledgeBase));
            }
        }
        
        return resList;
    }
    
    /**
     * @return
     */
    @Override
    public String ruleLoaderKey() {
        return String.valueOf(this.getClass().hashCode()
                + RuleTypeEnum.DROOLS_DRL_BYTE.hashCode()
                + "BaseDroolsRuleLoader".hashCode());
    }
    
    /**
     * @return
     */
    @Override
    public int getOrder() {
        return this.order;
    }
    
    /**
     * @return 返回 simplePersistenceRuleService
     */
    public SimplePersistenceRuleService getSimplePersistenceRuleService() {
        return simplePersistenceRuleService;
    }
    
    /**
     * @param 对simplePersistenceRuleService进行赋值
     */
    public void setSimplePersistenceRuleService(
            SimplePersistenceRuleService simplePersistenceRuleService) {
        this.simplePersistenceRuleService = simplePersistenceRuleService;
    }
    
    /**
     * @param 对order进行赋值
     */
    public void setOrder(int order) {
        this.order = order;
    }
}
