package com.letv.portal.vo.cloudvm.form.keypair;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * Created by zhouxianguang on 2015/11/4.
 */
public class CreateKeyPairForm {
    private String region;
    private String name;

    @NotBlank
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z][^[@/:=\\\\\"<>\\{\\[\\]\\}\\su4e00-u9fa5]]{2,128}$", message = "密钥名须为2-128个字符，以大小写字母开头，不支持中文、字符@/:=\\\"<>{[]}和空格")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
