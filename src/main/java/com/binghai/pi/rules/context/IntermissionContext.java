package com.binghai.pi.rules.context;

import lombok.Data;

/**
 * @author huaishuo
 * @date 2019/7/28 下午4:08
 **/
@Data
public class IntermissionContext extends RuleContext {
    private long startS;
    private long internalS;
    private long invalidTs;

    public IntermissionContext(String taskName, long startS, long internalS, long invalidTs) {
        super(taskName, false);
        this.startS = startS;
        this.internalS = internalS;
        this.invalidTs = invalidTs;
    }
}
