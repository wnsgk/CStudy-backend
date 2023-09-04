package com.CStudy.domain.question.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChoiceAnswerRequestDto {
    private int choiceNumber;
    private LocalDateTime time;
}
