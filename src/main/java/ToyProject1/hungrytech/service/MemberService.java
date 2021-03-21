package ToyProject1.hungrytech.service;

import ToyProject1.hungrytech.entity.member.Member;
import ToyProject1.hungrytech.memberDto.MemberForm;
import ToyProject1.hungrytech.memberDto.MemberInfo;
import ToyProject1.hungrytech.memberDto.MemberLoginForm;
import ToyProject1.hungrytech.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입, 탈퇴
     * 아이디 증복 조회
     */
    //회원가입
    @Transactional
    public void join(MemberForm memberForm) {
        memberRepository.save(Member
                .createMember(memberForm));

    }
    //아이디 증복 조회
    public Optional<Member> checkId(String accountId) {
        return memberRepository.findCheckByAccountId(accountId);
    }
    

    //회원 탈퇴
    @Transactional
    public void withDrawal(String accountId) {
        Member findMember = memberRepository.findMemberByAccountId(accountId);
        memberRepository.delete(findMember);
    }

    /**
     * 로그인
     */
    //로그인
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
    public Member findInfo(String accountId) {
        return memberRepository.findMemberByAccountId(accountId);
    }


    //회원 정보 변경
    @Transactional
    public void changeInfo(MemberInfo memberInfo) {
        Member findMember = memberRepository.findMemberByAccountId(memberInfo.getAccountId());

        findMember.changePw(passwordEncoder
                .encode(memberInfo.getAccountPw()));

        findMember.changeEmail(memberInfo.getEmail());

        findMember.changePhoneNumber(memberInfo.getPhoneNumber());

    }


}
