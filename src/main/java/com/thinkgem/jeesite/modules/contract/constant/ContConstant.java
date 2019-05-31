package com.thinkgem.jeesite.modules.contract.constant;

/**
 * Created by USER on 2018/4/25.
 */
public class ContConstant {
    public static final String PROCESS_KEY_CONTRACT_SPLIT_DETAIL ="contractSplitDetailProcess";
    public static final String PROCESS_KEY_CONTRACT_SPLIT ="contractSplitProcess";
    public static final String PROCESS_KEY_CONTRACT_APPLY_PAY="contractApplyPayProcess";

    public static final String PROCESS_TITLE_CONTRACT_SPLIT_DETAIL="合同拆解细化流程";
    public static final String PROCESS_TITLE_CONTRACT_SPLIT="合同拆解流程";
    public static final String PROCESS_TITLE_CONTRACT_APPLY_PAY="合同请款";


    public static final String CONTRACT_TABLE_NAME= "bm_contract";
    public static final String TABLE_NAME_CONT_APPYLY="bm_cont_apply";
    public static final String TABLE_NAME_CONT_SPLIT_DETAIL="bm_cont_split_detail";
    public static final String TABLE_NAME_CONT_SPLIT="bm_cont_split";
            //
    public static final String APPLY_PAY_FILE_PATH= "applypay";
    public static final String APPLY_PAY_FILE_PREFIX= "applypay";
    public static final String CONTRACT_FILE_PATH= "contract";
    public static final String CONTRACT_FILE_PREFIX= "contract";



    /**
     * 拆分状态
     */
    public enum ApplyPayStatus {
        /**
         * 唯独
         */
        UNSTART(0),
        /**
         * 进程中
         */
        INPROCESS(1),
        /**
         * 完成
         */
        FINISH(2);

        private int value;

        ApplyPayStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    /**
     * 拆分状态
     */
    public enum SplitStatus {
        /**
         * 唯独
         */
        UNSTART(0),
        /**
         * 进程中
         */
        INPROCESS(1),
        /**
         * 完成
         */
        FINISH(2);

        private int value;

        SplitStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 拆分状态
     */
    public enum SplitDetailStatus {
        /**
         * 唯独
         */
        UNSTART(0),
        /**
         * 进程中
         */
        INPROCESS(1),
        /**
         * 完成
         */
        FINISH(2);

        private int value;

        SplitDetailStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    /**
     * 拆分分项类型
     */
    public enum SplitType {
        /**
         *
         */
        DRAW(0),
        /**
         * 进程中
         */
        PLAN(1),
        /**
         * 完成
         */
        OTHER(2);

        private int value;

        SplitType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
