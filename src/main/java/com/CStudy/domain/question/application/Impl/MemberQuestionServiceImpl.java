package com.CStudy.domain.question.application.Impl;

import com.CStudy.domain.member.entity.Member;
import com.CStudy.domain.member.repository.MemberRepository;
import com.CStudy.domain.question.application.MemberQuestionService;
import com.CStudy.domain.question.dto.request.ChoiceAnswerRequestDto;
import com.CStudy.domain.question.dto.response.QuestionAnswerDto;
import com.CStudy.domain.question.entity.MemberQuestion;
import com.CStudy.domain.question.entity.Question;
import com.CStudy.domain.question.repository.MemberQuestionRepository;
import com.CStudy.domain.question.repository.QuestionRepository;
import com.CStudy.global.exception.member.NotFoundMemberId;
import com.CStudy.global.exception.question.existByMemberQuestionDataException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class MemberQuestionServiceImpl implements MemberQuestionService {

    private final MemberQuestionRepository memberQuestionRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;

    public MemberQuestionServiceImpl(
            MemberQuestionRepository memberQuestionRepository,
            MemberRepository memberRepository,
            QuestionRepository questionRepository
    ) {
        this.memberQuestionRepository = memberQuestionRepository;
        this.memberRepository = memberRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    @Transactional
    public void validateAnswer(Long memberId, Long questionId, ChoiceAnswerRequestDto choiceDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberId(memberId));

        Question question = questionRepository.findById(questionId)
                .orElseThrow();
        MemberQuestion memberQuestion = memberQuestionRepository.findByMemberAndQuestion(member, question)
                .orElse(createMemberQuestion(member, question, choiceDto));

        if(memberQuestion.getSuccess()){
            return;
        }

        memberQuestion.changeAnswer(question.getAnswer() == choiceDto.getChoiceNumber(),
                choiceDto.getChoiceNumber(), choiceDto.getTime());

        if(question.getAnswer() == choiceDto.getChoiceNumber()){
            member.addSolve(1);
        }
    }

    @Transactional
    public MemberQuestion createMemberQuestion(Member member, Question question, ChoiceAnswerRequestDto requestDto){
        MemberQuestion memberQuestion = MemberQuestion.builder()
                .member(member)
                .question(question)
                .solveTime(requestDto.getTime())
                .choice(requestDto.getChoiceNumber())
                .success(false)
                .build();

        memberQuestionRepository.save(memberQuestion);
        return memberQuestion;
    }


    @Override
    public QuestionAnswerDto isCorrectAnswer(Long memberId, Long questionId, ChoiceAnswerRequestDto requestDto) {
        boolean answer = memberQuestionRepository.existsByMemberAndQuestionAndSuccess(memberId, questionId);
        return QuestionAnswerDto.builder()
                .answer(answer)
                .build();
    }
}
