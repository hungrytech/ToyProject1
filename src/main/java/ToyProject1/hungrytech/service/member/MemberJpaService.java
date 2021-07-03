package ToyProject1.hungrytech.service.member;

import ToyProject1.hungrytech.domain.member.Member;
import ToyProject1.hungrytech.domain.member.Oauth;
import ToyProject1.hungrytech.memberDto.MemberForm;
import ToyProject1.hungrytech.memberDto.MemberInfo;
import ToyProject1.hungrytech.memberDto.MemberLoginForm;
import ToyProject1.hungrytech.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberJpaService implements MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입, 탈퇴
     * 아이디 증복 조회
     */
    //회원가입
    @Transactional
    @Override
    public void join(MemberForm memberForm) {
        memberForm.setAccountPw(passwordEncoder.encode(memberForm.getAccountPw()));

        //Oauth none 처리
        memberForm.setOauth(Oauth.NONE);

        memberRepository.save(Member
                .createMember(memberForm));

    }
    //아이디 증복 조회
    @Override
    public Optional<Member> checkId(String accountId) {
        return memberRepository.findCheckByAccountId(accountId);
    }
    

    //회원 탈퇴
    @Transactional
    @Override
    public void withDrawal(String accountId) {
        Member findMember = memberRepository.findMemberByAccountId(accountId);
        memberRepository.delete(findMember);
    }

    /**
     * 로그인
     */
    //로그인
    @Override
    public Member login(MemberLoginForm loginForm){
        Optional<Member> checkMember = memberRepository
                .findLoginCheckByAccountId(loginForm.getAccountId());

        if(checkMember.isPresent()) {
            Member member = checkMember.get();

            if(passwordEncoder.matches(loginForm.getAccountPw(),
                    member.getAccountPw())) { return member; }

            return null;
        }

        return null;
    }


    /**
     * MyPage
     */
    //회원 정보 조회
    @Override
    public Member findInfo(String accountId) {
        return memberRepository.findMemberByAccountId(accountId);
    }


    //회원 정보 변경
    @Transactional
    @Override
    public void changeInfo(MemberInfo memberInfo) {
        Member findMember = memberRepository.findMemberByAccountId(memberInfo.getAccountId());

        if(StringUtils.hasText(memberInfo.getAccountPw())) {
            findMember.changePw(passwordEncoder
                    .encode(memberInfo.getAccountPw()));
        }

        findMember.changeEmail(memberInfo.getEmail());

        findMember.changePhoneNumber(memberInfo.getPhoneNumber());

    }


    /**
     * 아이디 찾기
     * 비밀번호 찾기
     */
    @Override
    public String findMemberAccountId(String name, String email) {
        Optional<Member> optionalMember = memberRepository.findMemberAccountId(name, email);

        if(optionalMember.isPresent()) {
            Member member = optionalMember.get();
            return member.getAccountId();
        }

        return null;

    }

    @Override
    public Member findMemberAccountPw(String accountId, String name, String email) {

        Optional<Member> optionalMember = memberRepository.findMemberAccountPw(accountId, name, email);

        if(optionalMember.isPresent()) {
            return optionalMember.get();
        }

        return null;

    }
}
