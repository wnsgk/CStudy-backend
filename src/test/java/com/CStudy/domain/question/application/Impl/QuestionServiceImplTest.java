package com.CStudy.domain.question.application.Impl;

import com.CStudy.domain.choice.dto.CreateChoicesAboutQuestionDto;
import com.CStudy.domain.choice.entity.Choice;
import com.CStudy.domain.member.application.MemberService;
import com.CStudy.domain.member.dto.request.MemberLoginRequest;
import com.CStudy.domain.member.dto.request.MemberSignupRequest;
import com.CStudy.domain.member.dto.response.MemberLoginResponse;
import com.CStudy.domain.member.entity.Member;
import com.CStudy.domain.member.repository.MemberRepository;
import com.CStudy.domain.question.application.QuestionService;
import com.CStudy.domain.question.dto.request.*;
import com.CStudy.domain.question.dto.response.QuestionPageWithCategoryAndTitle;
import com.CStudy.domain.question.dto.response.QuestionResponseDto;
import com.CStudy.domain.question.entity.MemberQuestion;
import com.CStudy.domain.question.entity.Question;
import com.CStudy.domain.question.repository.MemberQuestionRepository;
import com.CStudy.domain.question.repository.QuestionRepository;
import com.CStudy.global.exception.member.NotFoundMemberEmail;
import com.CStudy.global.util.LoginUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
class QuestionServiceImplTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberQuestionRepository memberQuestionRepository;

    private Long questionId;

    @BeforeEach
    void beforeCreateSet() {
        CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                .category("네트워크")
                .build();

        CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                .questionTitle("문제 제목")
                .questionDesc("문제에 대한 설명")
                .questionExplain("문제에 대한 해답")
                .build();

        List<CreateChoicesAboutQuestionDto> createChoicesAboutQuestionDto = new ArrayList<>();

        for (int j = 1; j <= 5; j++) {
            CreateChoicesAboutQuestionDto request = CreateChoicesAboutQuestionDto.builder()
                    .number(j)
                    .content("선택 " + j)
                    .build();
            if (j == 3) {
                request.setAnswer("정답");
            }
            createChoicesAboutQuestionDto.add(request);
        }

        CreateQuestionAndCategoryRequestDto createQuestionAndCategoryRequestDto = CreateQuestionAndCategoryRequestDto.builder()
                .createQuestionRequestDto(createQuestionRequestDto)
                .categoryRequestDto(categoryRequestDto)
                .createChoicesAboutQuestionDto(createChoicesAboutQuestionDto)
                .build();

        questionId = questionService.createQuestionChoice(createQuestionAndCategoryRequestDto);

    }

    @Test
    @DisplayName("문제 생성")
    public void createQuestionWithChoice_Valid() throws Exception {
        //given

        Question question = questionRepository.findById(1L)
                .orElseThrow();
        //Then
        assertThat(questionRepository.count()).isNotNull();
        assertThat(question.getDescription()).isEqualTo("문제에 대한 설명");
        assertThat(question.getExplain()).isEqualTo("문제에 대한 해답");
        assertThat(question.getTitle()).isEqualTo("문제 제목");
    }

    @Test
    @DisplayName("문제 여러개 생성2")
    public void recursiveCreateQuestionChoice_Valid2() {
        // Given
        for(int i = 1; i < 100; i++) {
            CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                    .category("네트워크")
                    .build();

            CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                    .questionTitle("문제 제목 " + i)
                    .questionDesc("문제에 대한 설명 " + i)
                    .questionExplain("문제에 대한 해답 " + i)
                    .build();

            List<CreateChoicesAboutQuestionDto> createChoicesAboutQuestionDto = new ArrayList<>();

            for (int j = 1; j <= 5; j++) {
                CreateChoicesAboutQuestionDto request = CreateChoicesAboutQuestionDto.builder()
                        .number(j)
                        .content("선택 " + j)
                        .build();
                if (j == 3) {
                    request.setAnswer("정답");
                }
                createChoicesAboutQuestionDto.add(request);
            }

            CreateQuestionAndCategoryRequestDto createQuestionAndCategoryRequestDto = CreateQuestionAndCategoryRequestDto.builder()
                    .createQuestionRequestDto(createQuestionRequestDto)
                    .categoryRequestDto(categoryRequestDto)
                    .createChoicesAboutQuestionDto(createChoicesAboutQuestionDto)
                    .build();

            questionService.createQuestionChoice(createQuestionAndCategoryRequestDto);
        }

        // Then
        List<Question> questions = questionRepository.findAll();
        assertThat(questions).hasSize(100);
    }
    @Test
    @DisplayName("문제 여러개 생성")
    public void recursiveCreateQuestionChoice_Valid() {
        // Given
        List<CreateQuestionAndCategoryRequestDto> requestDtos = new ArrayList<>();

        for(int i = 1; i < 100; i++) {
            CategoryRequestDto categoryRequestDto = CategoryRequestDto.builder()
                    .category("네트워크")
                    .build();

            CreateQuestionRequestDto createQuestionRequestDto = CreateQuestionRequestDto.builder()
                    .questionTitle("문제 제목 " + i)
                    .questionDesc("문제에 대한 설명 " + i)
                    .questionExplain("문제에 대한 해답 " + i)
                    .build();

            List<CreateChoicesAboutQuestionDto> createChoicesAboutQuestionDto = new ArrayList<>();

            for (int j = 1; j <= 5; j++) {
                CreateChoicesAboutQuestionDto request = CreateChoicesAboutQuestionDto.builder()
                        .number(j)
                        .content("선택 " + j)
                        .build();
                if (j == 3) {
                    request.setAnswer("정답");
                }
                createChoicesAboutQuestionDto.add(request);
            }

            CreateQuestionAndCategoryRequestDto createQuestionAndCategoryRequestDto = CreateQuestionAndCategoryRequestDto.builder()
                    .createQuestionRequestDto(createQuestionRequestDto)
                    .categoryRequestDto(categoryRequestDto)
                    .createChoicesAboutQuestionDto(createChoicesAboutQuestionDto)
                    .build();

            requestDtos.add(createQuestionAndCategoryRequestDto);
        }
        // When
        questionService.recursiveCreateQuestionChoice(requestDtos);

        // Then
        List<Question> questions = questionRepository.findAll();
        assertThat(questions).hasSize(100);
    }

    @DisplayName("문제 검색")
    @Nested
    class findQuestionWithChoiceAndCategory {

        @Test
        @DisplayName("제목, 카테고리를 이용한 문제 검색")
        public void findPagingQuestionAndCategoryWithValid() throws Exception {
            //given
            LoginUserDto loginUserDto = LoginUserDto.builder()
                    .memberId(1L)
                    .build();
            //when
            QuestionSearchCondition questionSearchCondition = QuestionSearchCondition.builder()
                    .build();
            //Then
            Page<QuestionPageWithCategoryAndTitle> questionPageWithCategoryAndTitles = questionService.questionPageWithCategory(
                    questionSearchCondition, 0, 10,
                    loginUserDto.getMemberId());

            System.out.println("questionPageWithCategoryAndTitles = " + questionPageWithCategoryAndTitles);

            assertThat(questionPageWithCategoryAndTitles.stream().map(QuestionPageWithCategoryAndTitle::getQuestionTitle)
                    .findFirst().orElseThrow(RuntimeException::new)).isEqualTo("문제 제목");

            assertThat(questionPageWithCategoryAndTitles.stream().map(QuestionPageWithCategoryAndTitle::getCategoryTitle)
                    .findFirst().orElseThrow(RuntimeException::new)).isEqualTo("네트워크");
        }

        @Test
        @DisplayName("제목을 이용한 문제 검색")
        public void findPagingQuestionAndCategoryWithValidCondition() throws Exception {
            //given
            LoginUserDto loginUserDto = LoginUserDto.builder()
                    .memberId(1L)
                    .build();
            QuestionSearchCondition questionSearchCondition = QuestionSearchCondition.builder()
                    .questionTitle("문제 제목")
                    .build();
            //when
            Page<QuestionPageWithCategoryAndTitle> questionPageWithCategoryAndTitles = questionService.questionPageWithCategory(
                    questionSearchCondition, 0, 10,
                    loginUserDto.getMemberId());

            //Then
            assertThat(questionPageWithCategoryAndTitles.stream().map(QuestionPageWithCategoryAndTitle::getQuestionTitle)
                    .findFirst().orElseThrow(RuntimeException::new)).isEqualTo("문제 제목");
        }

        @Test
        @DisplayName("카테고리를 이용한 문제 검색")
        public void findQuestionWithCategory() throws Exception {
            //given
            LoginUserDto loginUserDto = LoginUserDto.builder()
                    .memberId(1L)
                    .build();
            QuestionSearchCondition questionSearchCondition = QuestionSearchCondition.builder()
                    .categoryTitle("네트워크")
                    .build();
            //when
            Page<QuestionPageWithCategoryAndTitle> questionPageWithCategoryAndTitles = questionService.questionPageWithCategory(
                    questionSearchCondition, 0, 10,
                    loginUserDto.getMemberId());

            //Then
            assertThat(questionPageWithCategoryAndTitles.stream().map(QuestionPageWithCategoryAndTitle::getCategoryTitle)
                    .findFirst().orElseThrow(RuntimeException::new)).isEqualTo("네트워크");
        }
    }

    @Nested
    @DisplayName("회원 문제")
    class test {

        @BeforeEach
        void setUp() {
            MemberSignupRequest memberSignupRequest = MemberSignupRequest.builder()
                    .email("test1234@gmail.com")
                    .password("1234")
                    .name("테스트")
                    .build();
            memberService.signUp(memberSignupRequest);
        }

        @Test
        @DisplayName("문제 선택 - 실패")
        public void choiceQuestionWithInValid() throws Exception {
            //given
            Member member = memberRepository.findByEmail("test1234@gmail.com")
                    .orElseThrow(() -> new NotFoundMemberEmail("test1234@gmail.com"));

            ChoiceAnswerRequestDto choiceAnswerRequestDto = ChoiceAnswerRequestDto.builder()
                    .choiceNumber(2)
                    .build();
            //when
            questionService.choiceQuestion(member.getId(), questionId, choiceAnswerRequestDto);

            MemberQuestion memberQuestion = memberQuestionRepository.findById(2L)
                    .orElseThrow(RuntimeException::new);
            //Then
            assertFalse(memberQuestion.getSuccess());
        }

        @Test
        @DisplayName("문제 선택 - 성공")
        public void choiceQuestionWithValid() throws Exception {
            //given
            Member member = memberRepository.findByEmail("test1234@gmail.com")
                    .orElseThrow(() -> new NotFoundMemberEmail("test1234@gmail.com"));

            ChoiceAnswerRequestDto choiceAnswerRequestDto = ChoiceAnswerRequestDto.builder()
                    .choiceNumber(3)
                    .build();
            //when
            System.out.println("member.getId() = " + member.getId());
            System.out.println("questionId = " + questionId);
            questionService.choiceQuestion(member.getId(), questionId, choiceAnswerRequestDto);

            MemberQuestion memberQuestion = memberQuestionRepository.findById(1L)
                    .orElseThrow(RuntimeException::new);
            //Then
            assertTrue(memberQuestion.getSuccess());
        }

    }
}