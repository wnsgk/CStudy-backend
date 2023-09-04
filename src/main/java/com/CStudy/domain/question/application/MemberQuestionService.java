package com.CStudy.domain.question.application;

import com.CStudy.domain.question.dto.request.ChoiceAnswerRequestDto;
import com.CStudy.domain.question.dto.response.QuestionAnswerDto;
import com.CStudy.global.util.LoginUserDto;
import org.springframework.transaction.annotation.Transactional;

public interface MemberQuestionService {

    void validateAnswer(Long memberId, Long questionId, ChoiceAnswerRequestDto choiceAnswerRequestDto);

    QuestionAnswerDto isCorrectAnswer(Long memberId, Long questionId, ChoiceAnswerRequestDto requestDto);
}
