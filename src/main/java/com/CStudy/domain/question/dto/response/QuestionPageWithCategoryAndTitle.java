package com.CStudy.domain.question.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class QuestionPageWithCategoryAndTitle {
    private Long questionId;
    private String questionTitle;
    private String categoryTitle;
    private Integer status;

    @QueryProjection
    public QuestionPageWithCategoryAndTitle(Long questionId, String questionTitle, String categoryTitle, Integer status) {
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.categoryTitle = categoryTitle;
        this.status = status;
    }
}
