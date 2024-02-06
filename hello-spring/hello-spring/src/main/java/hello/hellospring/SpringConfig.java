package hello.hellospring;

import hello.hellospring.repository.*;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    private final MemberRepository memberRepository;

    // 스프링 데이터 JPA가 만든 인터페이스 구현체(memberRepository) 자동 주입
    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // memberService 스프링 빈 등록
    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);   // memberService가 memberRepository 의존
    }

}