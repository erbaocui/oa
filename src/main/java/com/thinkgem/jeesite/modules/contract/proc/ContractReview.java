package com.thinkgem.jeesite.modules.contract.proc;

import com.sun.xml.internal.rngom.parse.host.Base;
import com.thinkgem.jeesite.modules.act.entity.BaseReview;

/**
 * Created by USER on 2018/7/2.
 */
public class ContractReview  extends BaseReview{

    private String specificItem; //特殊条款
    private String legalAdvise; //法律意见

    public String getSpecificItem() {
        return specificItem;
    }

    public void setSpecificItem(String specificItem) {
        this.specificItem = specificItem;
    }

    public String getLegalAdvise() {
        return legalAdvise;
    }

    public void setLegalAdvise(String legalAdvise) {
        this.legalAdvise = legalAdvise;
    }
}
