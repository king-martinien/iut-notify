package com.kingmartinien.iutnotifyapi.enums;

import lombok.Getter;

@Getter
public enum EmailTemplateEnum {
    ACCOUNT_ACTIVATION("account_activation"),
    RESET_PASSWORD("reset_password");

    private final String value;

    EmailTemplateEnum(String value) {
        this.value = value;
    }

}
