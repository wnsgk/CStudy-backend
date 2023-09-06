package com.CStudy.domain.member.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailVerifyResponse {

    public Boolean success;

    public static EmailVerifyResponse of(Boolean success){
        return EmailVerifyResponse.builder()
                .success(success)
                .build();
    }
}
