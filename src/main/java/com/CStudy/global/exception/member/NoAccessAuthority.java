package com.CStudy.global.exception.member;

import com.CStudy.global.exception.MemberAbstractException;

public class NoAccessAuthority extends MemberAbstractException {
    public NoAccessAuthority(String message) {
        super(message);
    }

    public NoAccessAuthority(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return 0;
    }
}
