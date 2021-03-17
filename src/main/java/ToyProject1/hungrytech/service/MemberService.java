package ToyProject1.hungrytech.service;

import ToyProject1.hungrytech.entity.board.Board;
import ToyProject1.hungrytech.entity.member.Member;
import ToyProject1.hungrytech.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    //회원가입
    @Transactional
    public void join(Member member) {
        memberRepository.save(member);

    }

    //회원 탈퇴
    @Transactional
    public void withDrawal(String accountId) {
        Member findMember = memberRepository.findMemberByAccountId(accountId);
        memberRepository.delete(findMember);
    }
    /**
     * MyPage
     */

    //회원 정보 조회
    public Member findInfo(String accountId) {
        return memberRepository.findMemberByAccountId(accountId);
    }

    //회원이 등록한 게시물 조회
    public List<Board> findWriteBoard(String accountId) {
        Member findMember = memberRepository.findMemberAndBoard(accountId);
        return findMember.getBoards();
    }



}
