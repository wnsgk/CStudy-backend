package com.CStudy.domain.notice.application;

import com.CStudy.domain.choice.dto.CreateChoicesAboutQuestionDto;
import com.CStudy.domain.member.application.MemberService;
import com.CStudy.domain.member.dto.request.MemberSignupRequest;
import com.CStudy.domain.member.dto.response.MemberSignupResponse;
import com.CStudy.domain.member.repository.MemberRepository;
import com.CStudy.domain.notice.dto.request.NoticeRequest;
import com.CStudy.domain.notice.dto.response.NoticeResponseDto;
import com.CStudy.domain.notice.entitiy.Notice;
import com.CStudy.domain.notice.repository.NoticeRepository;
import com.CStudy.domain.question.application.QuestionService;
import com.CStudy.domain.question.dto.request.CategoryRequestDto;
import com.CStudy.domain.question.dto.request.CreateQuestionAndCategoryRequestDto;
import com.CStudy.domain.question.dto.request.CreateQuestionRequestDto;
import com.CStudy.global.exception.notice.NotFoundNoticeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
class NoticeServiceTest {

    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NoticeService noticeService;

    private Long memberId;
    private Long questionId;

    @BeforeEach
    void setup(){
        MemberSignupRequest request = MemberSignupRequest.builder()
                .name("테스트 유저")
                .email("test1234@gmail.com")
                .password("test1234!")
                .build();

        memberService.signUp(request);
        memberId = memberRepository.findByEmail("test1234@gmail.com").get().getId();

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
            CreateChoicesAboutQuestionDto choice = CreateChoicesAboutQuestionDto.builder()
                    .number(j)
                    .content("선택 " + j)
                    .build();
            if (j == 3) {
                choice.setAnswer("정답");
            }
            createChoicesAboutQuestionDto.add(choice);
        }

        CreateQuestionAndCategoryRequestDto createQuestionAndCategoryRequestDto = CreateQuestionAndCategoryRequestDto.builder()
                .createQuestionRequestDto(createQuestionRequestDto)
                .categoryRequestDto(categoryRequestDto)
                .createChoicesAboutQuestionDto(createChoicesAboutQuestionDto)
                .build();

        questionId = questionService.createQuestionChoice(createQuestionAndCategoryRequestDto);
    }

    @Test
    @DisplayName("Notice 저장")
    void saveNotice() {
        LocalDateTime time = LocalDateTime.now().plusHours(1);
        NoticeRequest request = NoticeRequest.builder()
                .noticeTime(time)
                .content("테스트 notice")
                .questionId(questionId)
                .build();
        Long noticeId = noticeService.saveNotice(request, memberId);
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new NotFoundNoticeId(noticeId));
        assertEquals(notice.getContent(),  "테스트 notice");
        assertEquals(notice.getQuestion().getId(),  questionId);
        assertEquals(notice.getMember().getId(),  memberId);
        assertEquals(notice.getNoticeTime(), time);
    }

    @Test
    @DisplayName("Notice 삭제")
    void deleteNotice() {
        List<Long> notice = createNotice();
        noticeService.deleteNotice(notice.get(0), memberId);
        List<Notice> all = noticeRepository.findAll();
        assertEquals(all.size(), 19);
    }

    @Test
    @DisplayName("Notice 조회")
    void getNotices() {
        createNotice();
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<NoticeResponseDto> notices = noticeService.getNotices(memberId, pageRequest);
        assertEquals(notices.getTotalElements(), 10);
    }

    @Test
    @DisplayName("Notice 개수 조회")
    void getNewNoticesCount() {
        createNotice();
        Integer count = noticeService.getNewNoticesCount(memberId);
        assertEquals(count, 10);
    }

    List<Long> createNotice(){
        List<Long> list = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            LocalDateTime time = i <= 10 ? LocalDateTime.now().minusMinutes(i) : LocalDateTime.now().plusMinutes(i-10);
            NoticeRequest request = NoticeRequest.builder()
                    .noticeTime(time)
                    .content("테스트 notice")
                    .questionId(questionId)
                    .build();
            Long noticeId = noticeService.saveNotice(request, memberId);
            list.add(noticeId);
        }
        return list;
    }
}