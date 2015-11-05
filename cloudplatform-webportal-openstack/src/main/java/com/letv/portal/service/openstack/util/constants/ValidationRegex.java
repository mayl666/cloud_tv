package com.letv.portal.service.openstack.util.constants;

/**
 * Created by zhouxianguang on 2015/11/5.
 */
public class ValidationRegex {
    public static final String password = "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])[a-zA-Z0-9]{8,30}";
    public static final String passwordMessage = "8-30个字符，同时包含大小写字母和数字，不支持特殊符号";

    public static final String name="^[a-zA-Zu4e00-u9fa5][^@/:=\\\\\"<>\\{\\[\\]\\}\\s]{2,128}$";
    public static final String nameMessage="名称须为2-128个字符，以大小写字母或中文开头，不支持字符@/:=\\\"<>{[]}和空格";
}