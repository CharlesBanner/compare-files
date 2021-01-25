package com.charles.util;

import com.charles.entity.CompareExcl;
import com.google.common.collect.Lists;

import java.util.List;

public class Constants {


    public final static String PROD_ROOT_PATH = "E:\\Compare\\Sc_Channel文件对比\\prod_channel\\classes";
    public final static String LOCAL_ROOT_PATH = "E:\\Compare\\Sc_Channel文件对比\\saved-channel\\classes本地";
    public final static String LOG_PREFIX = "D:\\log\\";
    public final static String RESULT_PREFIX = "D:\\result\\";
    public static List<CompareExcl> resultList = Lists.newArrayList();
    public static int DEAL_THREAD_NUM = 5;
    public static int MAX_THREAD_NUM = DEAL_THREAD_NUM + 1;

}
