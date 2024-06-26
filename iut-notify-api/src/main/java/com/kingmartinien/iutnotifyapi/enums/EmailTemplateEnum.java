package com.kingmartinien.iutnotifyapi.enums;

import lombok.Getter;

@Getter
public enum EmailTemplateEnum {
    ACCOUNT_ACTIVATION("account_activation");
    private final String value;

    EmailTemplateEnum(String value) {
        this.value = value;
    }
}
