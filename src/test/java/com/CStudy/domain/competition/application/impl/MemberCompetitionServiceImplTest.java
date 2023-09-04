package com.CStudy.domain.competition.application.impl;

import com.CStudy.domain.competition.application.CompetitionService;
import com.CStudy.domain.competition.application.MemberCompetitionService;
import com.CStudy.domain.competition.dto.request.CreateCompetitionRequestDto;
import com.CStudy.domain.competition.entity.Competition;
import com.CStudy.domain.competition.repository.CompetitionRepository;
import com.CStudy.domain.member.application.MemberService;
import com.CStudy.domain.member.dto.request.MemberSignupRequest;
import com.CStudy.domain.member.dto.response.MemberSignupResponse;
import com.CStudy.domain.member.entity.Member;
import com.CStudy.domain.member.repository.MemberRepository;
import com.CStudy.domain.workbook.repository.WorkbookRepository;
import com.CStudy.global.exception.competition.NotFoundCompetitionId;
import com.CStudy.global.exception.member.NotFoundMemberEmail;
import com.CStudy.global.util.LoginUserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@ActiveProfiles("local")
class MemberCompetitionServiceImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CompetitionService competitionService;

    @Autowired
    private MemberCompetitionService memberCompetitionService;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private OptimisticFacade optimisticFacade;
    @Autowired
    private WorkbookRepository workbookRepository;

    private static final String EMAIL = "test1213@email.com";
    private static final String PASSWORD = "test1234!";
    private static final String NAME = "테스트유저";
    private Long competitionId;

    @BeforeEach
    void setUp() {
        CreateCompetitionRequestDto requestDto = CreateCompetitionRequestDto.builder()
                .competitionTitle("CS 대회")
                .participants(50)
                .competitionStart(LocalDateTime.now())
                .competitionEnd(LocalDateTime.now().plusHours(1))
                .build();

        competitionId = competitionService.createCompetition(requestDto);
    }

    @Test
    @DisplayName("싱글 스레드 대회 참가")
    @Transactional
    public void findCompetitionAboutJoinCompetitionWithSingleThread() throws Exception {
        //given
        MemberSignupRequest memberSignupRequest = MemberSignupRequest.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .name(NAME)
                .build();
        memberService.signUp(memberSignupRequest);

        Member member = memberRepository.findByEmail(EMAIL).orElseThrow(() -> new NotFoundMemberEmail(EMAIL));

        LoginUserDto loginUserDto = LoginUserDto.builder()
                .memberId(member.getId())
                .build();
        //when
        memberCompetitionService.joinCompetition(loginUserDto, competitionId);

        //Then
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new NotFoundCompetitionId(competitionId));

        assertThat(competition.getParticipants()).isEqualTo(49);
    }

    @Test
    @DisplayName("싱글 스레드 대회 인원 초과")
    @Transactional
    public void findCompetitionAboutInvalidJoinCompetitionWithSingleThread() throws Exception {
        //given
        for(int i = 0; i < 51; i++){
            MemberSignupRequest r = MemberSignupRequest.builder()
                    .name("테스트 유저_" + i)
                    .email("test123@gmail.com_" + i)
                    .password(PASSWORD)
                    .build();
            MemberSignupResponse response = memberService.signUp(r);
            Member member = memberRepository.findByEmail("test123@gmail.com_" + i)
                    .orElseThrow(() -> new NotFoundMemberEmail("test123@gmail.com"));
            LoginUserDto loginUserDto = LoginUserDto.builder()
                    .memberId(member.getId())
                    .build();
            if(i < 50) {
                memberCompetitionService.joinCompetition(loginUserDto, competitionId);
            }
        }

        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new NotFoundCompetitionId(competitionId));

        Member member = memberRepository.findByEmail("test123@gmail.com_50").get();

        LoginUserDto loginUserDto = LoginUserDto.builder()
                .memberId(member.getId())
                .build();

        assertThatThrownBy(() -> memberCompetitionService.joinCompetition(loginUserDto, competitionId))
                .isInstanceOf(RuntimeException.class);
        assertThat(competition.getParticipants()).isEqualTo(0);
    }

    @Test
    @DisplayName("멀티쓰레드 대회 참가")
    public void studyMemberCreateJoinMemberWithMultiThread() throws Exception {

        ArrayList<Long> ids = new ArrayList<>();

        int count = 50;

        ExecutorService executorService = Executors.newFixedThreadPool(8);

        CountDownLatch latch = new CountDownLatch(count);

        for (int i = 0; i < count; i++) {
            int a = i;
            executorService.submit(() -> {
                try {
                    MemberSignupRequest r = MemberSignupRequest.builder()
                            .name("테스트 유_" + a)
                            .email("test12345@gmail.com_" + a)
                            .password(PASSWORD)
                            .build();
                    memberService.signUp(r);
                    Member member = memberRepository.findByEmail("test12345@gmail.com_" + a)
                            .orElseThrow(() -> new NotFoundMemberEmail("test12345@gmail.com"));
                    LoginUserDto loginUserDto = LoginUserDto.builder()
                            .memberId(member.getId())
                            .build();
                    optimisticFacade.joinCompetition(loginUserDto, competitionId);
                } catch (Exception e) {
                    System.out.println("e = " + e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow();

        assertThat(competition.getParticipants()).isEqualTo(0);
        competitionRepository.deleteAll();
        workbookRepository.deleteAll();
    }
}