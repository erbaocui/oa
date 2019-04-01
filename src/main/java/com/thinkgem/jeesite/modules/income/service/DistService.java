package com.thinkgem.jeesite.modules.income.service;

import com.thinkgem.jeesite.common.utils.NumberOperateUtils;
import com.thinkgem.jeesite.modules.contract.entity.ContApply;
import com.thinkgem.jeesite.modules.income.constant.IncomeConstant;
import com.thinkgem.jeesite.modules.income.entity.*;
import com.thinkgem.jeesite.modules.income.vo.*;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly = true)
public class DistService {

    @Autowired
    AccountFlowService accountFlowService;
    @Autowired
    AccountService accountService;
    @Autowired
    ApplyService applyService;
    @Autowired
    DistOfficeService  distOfficeService;
    @Autowired
    DistributeService distributeService;
    @Autowired
    DistTypeService distTypeService;
    @Autowired
    IncomeService incomeService;
    @Autowired
    RuleGroupService ruleGroupService;
    @Autowired
    RuleItemService ruleItemService;
    @Autowired
    RuleService ruleService;

    @Transactional(readOnly = false)
    public void  distTypeAdd(DistType distType){
        distTypeService.save(distType);
        Income income= incomeService.get(distType.getIncomeId());
        if(distType.getType().equals("1")){
            income.setDraw(1);
            incomeService.save(income);

        }
        if(distType.getType().equals("2")) {
            income.setPlan(1);
            incomeService.save(income);
        }
    }

    @Transactional(readOnly = false)
    public void  distTypeDelete(String ids){
        String[] idArray=ids.split(",");
        for(String id:idArray) {
            if(id!=null &&!id.equals("")) {
                DistType distType =new DistType();
                distType.setId(id);
                distType =distTypeService.get(distType);
                distTypeService.delete(distType);
//                DistOffice distOffice=new DistOffice();
//                distOffice.setTypeId(distType.getId());
//                distOfficeService.delete(distOffice);
//                Distribute distribute=new Distribute();
//                distribute.setIncomeId(incomeId);
//                distribute.setTypeId(distType.getId());
//                distributeService.delete(distribute);
                if(distType.getType().equals("1")){
                    Income income=incomeService.get(distType.getIncomeId());
                    income.setDraw(0);
                    incomeService.save(income);
                }
                if(distType.getType().equals("2")){
                    Income income=incomeService.get(distType.getIncomeId());
                    income.setPlan(0);
                    incomeService.save(income);
                }
            }
        }
    }

    @Transactional(readOnly = false)
    public void  distOfficeAdd(DistOffice distOffice, String  officeId) {
        Office office = new Office();
        office.setId(officeId);
        distOffice.setOffice(office);
        distOfficeService.save(distOffice);
    }

    @Transactional(readOnly = false)
    public void  distOfficeDelete(String ids,String type,String incomeId) {
        String[] idArray=ids.split(",");
        for(String id:idArray){
            DistOffice distOffice=new DistOffice();
            distOffice.setId(id);
            distOfficeService.delete(distOffice);
//            Distribute distribute=new Distribute();
//            distribute.setIncomeId( incomeId);
//            distribute.setTypeId(typeId);
//            distribute.setOfficeId(id);
//            distributeService.delete(distribute);
        }
        if(type.equals("1")){
            Income income=incomeService.get(incomeId);
            income.setDraw(1);
            incomeService.save(income);

        }
        if(type.equals("2")){
            Income income=incomeService.get(incomeId);
            income.setPlan(1);
            incomeService.save(income);


        };
    }

    @Transactional(readOnly = false)
    public void ruleSave(DistOffice distOffice, String[] groups)throws Exception{
        Distribute dbDelete =new Distribute();
        dbDelete.setTypeId(distOffice.getTypeId());
        dbDelete.setIncomeId(distOffice.getIncomeId());
        distributeService.delete(dbDelete);
        List<DistOffice> distOfficeList= distOfficeService.findList(distOffice);
        List<Distribute> distributeList = new ArrayList<Distribute>();
        for(int k=0;k<distOfficeList.size();k++){
            DistOffice item=distOfficeList.get(k);

            if(groups[k]!=null) {
                item.setGroupId(groups[k]);
                distOfficeService.save(item);
                Rule r=new Rule();
                r.setGroupId(groups[k]);
                r.setOfficeId(item.getOffice().getId());
                List<Rule> ruleList = ruleService.findListByOfficeId(r);
                if (ruleList != null && !ruleList.isEmpty()) {
                    for (Rule rule : ruleList) {

                        Double ruleValue=0d;
                        if (ruleList.size() == 1) {
                            ruleValue=item.getValue().doubleValue();
                        } else {
                            //多条规则
                            String sql=rule.getBaseSql();
                            String conditon=rule.getCondition();
                            Double baseValue=ruleService.runSql(sql).doubleValue();

                            ScriptEngineManager manager = new ScriptEngineManager();
                            ScriptEngine engine = manager.getEngineByName("js");
                            engine.put("value", baseValue);
                            engine.put("threshold",rule.getThreshold());
                            Boolean before= (Boolean)engine.eval(conditon);
                            engine.put("value", NumberOperateUtils.add(baseValue,item.getValue().doubleValue()));
                            engine.put("threshold", rule.getThreshold());
                            Boolean after= (Boolean)engine.eval(conditon);
                            //触发条件
                            if(before==false||after==true){
                                ruleValue=Math.abs(NumberOperateUtils.sub(rule.getThreshold().doubleValue(),NumberOperateUtils.add(baseValue,item.getValue().doubleValue())));
                            }
                            if(before==true||after==false){
                                ruleValue=Math.abs(NumberOperateUtils.sub(rule.getThreshold().doubleValue(),baseValue));
                            }
                            if(before==true&&after==true){
                                ruleValue=item.getValue().doubleValue();

                            }
                            if(before==false&&after==false){
                                continue;
                            }

                        }
                        RuleItem ruleItem = new RuleItem();
                        ruleItem.setOfficeId(item.getOffice().getId());
                        ruleItem.setRuleId(rule.getId());
                        List<RuleItem> ruleItemList = ruleItemService.findList(ruleItem);
                        if (ruleItemList != null && !ruleItemList.isEmpty()) {
                            for (RuleItem ri : ruleItemList) {
                                Double itemValue = 0.0;
                                Double tax = 0.0;
                                Double filingFee = 0.0;
                                itemValue = NumberOperateUtils.mul(ruleValue, ri.getPercent().doubleValue());
                                RuleItemVo riv = new RuleItemVo();
                                riv.setName(ri.getName());
                                riv.setValue(itemValue.toString());

                                Distribute  distribute;
                                if (ri.getIsTax().equals(0)) {
                                    distribute = new Distribute();
                                    distribute.preInsert();
                                    distribute.setRuleItemId(ri.getId());
                                    distribute.setStatus(1);
                                    distribute.setAccount(IncomeConstant.ACCOUNT_TAX);
                                    distribute.setDes("税");
                                    distribute.setTypeId(item.getTypeId());
                                    tax = NumberOperateUtils.mul(itemValue, 0.067);
                                    distribute.setValue(new BigDecimal(tax));
                                    distribute.setOfficeId(item.getOffice().getId());
                                    distribute.setIncomeId(item.getIncomeId());
                                    distribute.setDistOfficeId(item.getId());
                                    distributeList.add(distribute);
                                }
                                if (ri.getIsFilingFee().equals(0)) {
                                    distribute = new Distribute();
                                    distribute.preInsert();
                                    distribute.setRuleItemId(ri.getId());
                                    distribute.setStatus(1);
                                    distribute.setAccount(ri.getAccount()+IncomeConstant.ACCOUNT_FILING_FEE_POSTFIX);
                                    distribute.setDes("归档费");
                                    distribute.setTypeId(item.getTypeId());
                                    filingFee = NumberOperateUtils.sub(itemValue, tax);
                                    filingFee = NumberOperateUtils.mul(filingFee, 0.02);
                                    distribute.setValue(new BigDecimal(filingFee));
                                    distribute.setOfficeId(item.getOffice().getId());
                                    distribute.setIncomeId(item.getIncomeId());
                                    distribute.setDistOfficeId(item.getId());
                                    distributeList.add(distribute);
                                }
                                itemValue = NumberOperateUtils.sub(itemValue, tax);
                                itemValue = NumberOperateUtils.sub(itemValue, filingFee);
                                distribute = new Distribute();
                                distribute.preInsert();
                                distribute.setRuleItemId(ri.getId());
                                distribute.setStatus(1);
                                distribute.setAccount(ri.getAccount());
                                distribute.setDes("净值");
                                distribute.setTypeId(item.getTypeId());
                                distribute.setValue(new BigDecimal(itemValue));
                                distribute.setOfficeId(item.getOffice().getId());
                                distribute.setIncomeId(item.getIncomeId());
                                distribute.setDistOfficeId(item.getId());
                                distributeList.add(distribute);

                            }
                        }

                    }
                }

            }

        }
        distributeService.addBatch( distributeList);
    }


    public String  rule(DistOffice distOffice, String[] groups, List<DistOfficeVo> distOffices,String type)throws Exception{
        List<DistOffice> distOfficeList= distOfficeService.findList(distOffice);
        String typeId="";
        for(int k=0;k<distOfficeList.size();k++){
            DistOffice item=distOfficeList.get(k);
            typeId=item.getTypeId();
            DistOfficeVo dov=new DistOfficeVo();
            dov.setId(item.getId());
            dov.setOfficeId(item.getOffice().getId());
            dov.setOfficeName(item.getOffice().getName());
            dov.setType(item.getType().getType());
            if(item.getValue()!=null) {
                dov.setValue(item.getValue().toString());
            }
            List<RuleGroup> list=ruleGroupService.findListByOfficeId(item.getOffice().getId(),type);
            List<RuleGroupVo> ruleGroups=new ArrayList<RuleGroupVo>();
            RuleGroupVo rgv=new RuleGroupVo();
            if(groups!=null&&(!groups[k].equals("-1"))){
                dov.setRuleGroupId(groups[k]);
            }else{
                Map<String,String> paramMap=new HashMap<String, String>();
                paramMap.put("typeId",item.getTypeId());
                paramMap.put("officeId",item.getOffice().getId());
                String groupId=distOfficeService.findGroupId(paramMap);
                if(groupId!=null&&!groupId.equals("")){
                    dov.setRuleGroupId(groupId);
                }
            }

            rgv.setId("-1");
            rgv.setName("请选择......");
            ruleGroups.add(rgv);
            for(RuleGroup rg:list){
                rgv=new RuleGroupVo();
                rgv.setId(rg.getId());
                rgv.setName(rg.getName());
                ruleGroups.add(rgv);
            }

            dov.setRuleGroups(ruleGroups);
            if(dov.getRuleGroupId()!=null) {
                Rule r=new Rule();
                r.setGroupId(dov.getRuleGroupId());
                r.setOfficeId(item.getOffice().getId());
                List<Rule> ruleList = ruleService.findListByOfficeId(r);
                //List<Rule> ruleList=ruleService.findList(item.getOffice().getId());
                Integer rowspan = 0;
                List<RuleVo> ruleVoList = new ArrayList<RuleVo>();
                if (ruleList != null && !ruleList.isEmpty()) {
                    Integer ruleRowspan = 0;
                    for (Rule rule : ruleList) {
                        RuleVo rv = new RuleVo();
                        Double ruleValue=0d;
                        if (ruleList.size() == 1) {
                            rv.setValue(item.getValue().toString());
                            ruleValue=item.getValue().doubleValue();
                        } else {
                            //多条规则
                            String sql=rule.getBaseSql();
                            String conditon=rule.getCondition();
                            Double baseValue=ruleService.runSql(sql).doubleValue();

                            ScriptEngineManager manager = new ScriptEngineManager();
                            ScriptEngine engine = manager.getEngineByName("js");
                            engine.put("value", baseValue);
                            engine.put("threshold",rule.getThreshold());
                            Boolean before= (Boolean)engine.eval(conditon);
                            engine.put("value", NumberOperateUtils.add(baseValue,item.getValue().doubleValue()));
                            engine.put("threshold", rule.getThreshold());
                            Boolean after= (Boolean)engine.eval(conditon);
                            //触发条件
                            if(before==false||after==true){
                                ruleValue=Math.abs(NumberOperateUtils.sub(rule.getThreshold().doubleValue(),NumberOperateUtils.add(baseValue,item.getValue().doubleValue())));
                            }
                            if(before==true||after==false){
                                ruleValue=Math.abs(NumberOperateUtils.sub(rule.getThreshold().doubleValue(),baseValue));
                            }
                            if(before==true&&after==true){
                                ruleValue=item.getValue().doubleValue();

                            }
                            rv.setValue(ruleValue.toString());
                            if(before==false&&after==false){
                                continue;
                            }


                        }
                        RuleItem ruleItem = new RuleItem();
                        ruleItem.setOfficeId(item.getOffice().getId());
                        ruleItem.setRuleId(rule.getId());
                        List<RuleItem> ruleItemList = ruleItemService.findList(ruleItem);
                        List<RuleItemVo> ruleItemVoList = new ArrayList<RuleItemVo>();

                        if (ruleItemList != null && !ruleItemList.isEmpty()) {
                            for (RuleItem ri : ruleItemList) {
                                Double itemValue = 0.0;
                                Double tax = 0.0;
                                Double filingFee = 0.0;
                                itemValue = NumberOperateUtils.mul(ruleValue, ri.getPercent().doubleValue());
                                RuleItemVo riv = new RuleItemVo();
                                riv.setName(ri.getName());
                                riv.setValue(itemValue.toString());
                                List<DistributeVo> distributeVoList = new ArrayList<DistributeVo>();
                                DistributeVo dbv;
                                if (ri.getIsTax().equals(0)) {
                                    dbv = new DistributeVo();
                                    dbv.setName("税");
                                    tax = NumberOperateUtils.mul(itemValue, 0.067);
                                    dbv.setValue(tax.toString());
                                    distributeVoList.add(dbv);
                                }
                                if (ri.getIsFilingFee().equals(0)) {
                                    dbv = new DistributeVo();
                                    dbv.setName("归档费");
                                    filingFee = NumberOperateUtils.sub(itemValue, tax);
                                    filingFee = NumberOperateUtils.mul(filingFee, 0.02);
                                    dbv.setValue(filingFee.toString());
                                    distributeVoList.add(dbv);
                                }
                                itemValue = NumberOperateUtils.sub(itemValue, tax);
                                itemValue = NumberOperateUtils.sub(itemValue, filingFee);
                                dbv = new DistributeVo();
                                dbv.setName("净值");
                                dbv.setValue(itemValue.toString());
                                distributeVoList.add(dbv);
                                riv.setRowspan(distributeVoList.size());
                                rowspan += distributeVoList.size();
                                ruleRowspan += distributeVoList.size();
                                riv.setDistributes(distributeVoList);
                                ruleItemVoList.add(riv);
                            }
                        }
                        rv.setRowspan(ruleRowspan);
                        rv.setRoleItems(ruleItemVoList);
                        ruleVoList.add(rv);
                    }
                }
                dov.setRowspan(rowspan);
                dov.setRules(ruleVoList);
                distOffices.add(dov);
            }else{
                dov.setRowspan(1);
                dov.setRules(null);
                distOffices.add(dov);
            }

        }
        return typeId;
    }

//    @Transactional(readOnly = false)
//    public void saveAcount(String incomeId,String typeId) {
//        Distribute distribute=new Distribute();
//        distribute.setIncomeId(incomeId);
//        List<Distribute>  distributes= distributeService.findDistAccountSum(distribute);
//        List<AccountFlow> accountFlows=new ArrayList<AccountFlow>();
//        List<Account> accounts=new ArrayList<Account>();
//        for(Distribute d:distributes){
//            AccountFlow accountFlow=new AccountFlow();
//            accountFlow.preInsert();
//            accountFlow.setValue(d.getValue());
//            accountFlow.setType(1);
//            Account a=new Account();
//            a.setAccount(d.getAccount());
//            accountFlow.setAccount(a);
//            accountFlow.setIncomeId(incomeId);
//            distribute.setTypeId(typeId);
//            accountFlows.add(accountFlow);
//
//            Account account=new Account();
//            account.setAccount(d.getAccount());
//            account.setValue(d.getValue());
//            accounts.add(account);
//        }
//
//        accountFlowService.saveBatch(accountFlows);
//        for(Account a:accounts){
//            accountService.updateAdd( a);
//        }
//        distribute.setStatus(2);
//        distributeService.(distribute);
//
//        Income income=incomeService.get(incomeId);
//        income.setStatus(3);
//        incomeService.save(income);
//        ContApply contApply=contApplyService.get(income.getApplyId());
//        Double incomeValue=0.0;
//        Double applyIncomeValue=0.0;
//        if(income.getValue()!=null){
//            incomeValue=income.getValue().doubleValue();
//        }
//        if(contApply.getIncome()!=null){
//            applyIncomeValue=contApply.getIncome().doubleValue();
//        }
//        Double incomeSum= NumberOperateUtils.add(incomeValue,applyIncomeValue);
//        contApply.setIncome(new BigDecimal(incomeSum));
//        if(incomeSum>=contApply.getReceiptValue().doubleValue()){
//            contApply.setStatus(4);
//        }
//        contApplyService.save(contApply);
//    }


//
//    public String  rule(DistOffice distOffice, String[] groups,List<DistOfficeVo> distOffices)throws Exception{
//        List<DistOffice> distOfficeList= distOfficeService.findList(distOffice);
//        String typeId="";
//        for(int k=0;k<distOfficeList.size();k++){
//            DistOffice item=distOfficeList.get(k);
//            typeId=item.getTypeId();
//            DistOfficeVo dov=new DistOfficeVo();
//            dov.setId(item.getId());
//            dov.setOfficeId(item.getOffice().getId());
//            dov.setOfficeName(item.getOffice().getName());
//            dov.setType(item.getType().getType());
//            if(item.getValue()!=null) {
//                dov.setValue(item.getValue().toString());
//            }
//            List<RuleGroup> list=ruleGroupService.findListByOfficeId(item.getOffice().getId());
//            List<RuleGroupVo> ruleGroups=new ArrayList<RuleGroupVo>();
//            RuleGroupVo rgv=new RuleGroupVo();
//            if(groups!=null&&(!groups[k].equals("-1"))){
//                dov.setRuleGroupId(groups[k]);
//            }else{
//                Map<String,String> paramMap=new HashMap<String, String>();
//                paramMap.put("typeId",item.getTypeId());
//                paramMap.put("officeId",item.getOffice().getId());
//                String groupId=distOfficeService.findGroupId(paramMap);
//                if(groupId!=null&&!groupId.equals("")){
//                    dov.setRuleGroupId(groupId);
//                }
//            }
//
//            rgv.setId("-1");
//            rgv.setName("请选择......");
//            ruleGroups.add(rgv);
//            for(RuleGroup rg:list){
//                rgv=new RuleGroupVo();
//                rgv.setId(rg.getId());
//                rgv.setName(rg.getName());
//                ruleGroups.add(rgv);
//            }
//
//            dov.setRuleGroups(ruleGroups);
//            if(dov.getRuleGroupId()!=null) {
//                Rule r=new Rule();
//                r.setGroupId(dov.getRuleGroupId());
//                r.setOfficeId(item.getOffice().getId());
//                List<Rule> ruleList = ruleService.findListByOfficeId(r);
//                //List<Rule> ruleList=ruleService.findList(item.getOffice().getId());
//                Integer rowspan = 0;
//                List<RuleVo> ruleVoList = new ArrayList<RuleVo>();
//                if (ruleList != null && !ruleList.isEmpty()) {
//                    Integer ruleRowspan = 0;
//                    for (Rule rule : ruleList) {
//                        RuleVo rv = new RuleVo();
//                        Double ruleValue=0d;
//                        if (ruleList.size() == 1) {
//                            rv.setValue(item.getValue().toString());
//                            ruleValue=item.getValue().doubleValue();
//                        } else {
//                            //多条规则
//                            String sql=rule.getBaseSql();
//                            String conditon=rule.getCondition();
//                            Double baseValue=ruleService.runSql(sql).doubleValue();
//
//                            ScriptEngineManager manager = new ScriptEngineManager();
//                            ScriptEngine engine = manager.getEngineByName("js");
//                            engine.put("value", baseValue);
//                            engine.put("threshold",rule.getThreshold());
//                            Boolean before= (Boolean)engine.eval(conditon);
//                            engine.put("value", NumberOperateUtils.add(baseValue,item.getValue().doubleValue()));
//                            engine.put("threshold", rule.getThreshold());
//                            Boolean after= (Boolean)engine.eval(conditon);
//                            //触发条件
//                            if(before==false||after==true){
//                                ruleValue=Math.abs(NumberOperateUtils.sub(rule.getThreshold().doubleValue(),NumberOperateUtils.add(baseValue,item.getValue().doubleValue())));
//                            }
//                            if(before==true||after==false){
//                                ruleValue=Math.abs(NumberOperateUtils.sub(rule.getThreshold().doubleValue(),baseValue));
//                            }
//                            if(before==true&&after==true){
//                                ruleValue=item.getValue().doubleValue();
//
//                            }
//                            rv.setValue(ruleValue.toString());
//                            if(before==false&&after==false){
//                                continue;
//                            }
//
//
//                        }
//                        RuleItem ruleItem = new RuleItem();
//                        ruleItem.setOfficeId(item.getOffice().getId());
//                        ruleItem.setRuleId(rule.getId());
//                        List<RuleItem> ruleItemList = ruleItemService.findList(ruleItem);
//                        List<RuleItemVo> ruleItemVoList = new ArrayList<RuleItemVo>();
//
//                        if (ruleItemList != null && !ruleItemList.isEmpty()) {
//                            for (RuleItem ri : ruleItemList) {
//                                Double itemValue = 0.0;
//                                Double tax = 0.0;
//                                Double filingFee = 0.0;
//                                itemValue = NumberOperateUtils.mul(ruleValue, ri.getPercent().doubleValue());
//                                RuleItemVo riv = new RuleItemVo();
//                                riv.setName(ri.getName());
//                                riv.setValue(itemValue.toString());
//                                List<DistributeVo> distributeVoList = new ArrayList<DistributeVo>();
//                                DistributeVo dbv;
//                                if (ri.getIsTax().equals(0)) {
//                                    dbv = new DistributeVo();
//                                    dbv.setName("税");
//                                    tax = NumberOperateUtils.mul(itemValue, 0.067);
//                                    dbv.setValue(tax.toString());
//                                    distributeVoList.add(dbv);
//                                }
//                                if (ri.getIsFilingFee().equals(0)) {
//                                    dbv = new DistributeVo();
//                                    dbv.setName("归档费");
//                                    filingFee = NumberOperateUtils.sub(itemValue, tax);
//                                    filingFee = NumberOperateUtils.mul(filingFee, 0.02);
//                                    dbv.setValue(filingFee.toString());
//                                    distributeVoList.add(dbv);
//                                }
//                                itemValue = NumberOperateUtils.sub(itemValue, tax);
//                                itemValue = NumberOperateUtils.sub(itemValue, filingFee);
//                                dbv = new DistributeVo();
//                                dbv.setName("净值");
//                                dbv.setValue(itemValue.toString());
//                                distributeVoList.add(dbv);
//                                riv.setRowspan(distributeVoList.size());
//                                rowspan += distributeVoList.size();
//                                ruleRowspan += distributeVoList.size();
//                                riv.setDistributes(distributeVoList);
//                                ruleItemVoList.add(riv);
//                            }
//                        }
//                        rv.setRowspan(ruleRowspan);
//                        rv.setRoleItems(ruleItemVoList);
//                        ruleVoList.add(rv);
//                    }
//                }
//                dov.setRowspan(rowspan);
//                dov.setRules(ruleVoList);
//                distOffices.add(dov);
//            }else{
//                dov.setRowspan(1);
//                dov.setRules(null);
//                distOffices.add(dov);
//            }
//
//        }
//        return typeId;
//    }
//


}
