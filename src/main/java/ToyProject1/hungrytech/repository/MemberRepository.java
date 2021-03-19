package ToyProject1.hungrytech.repository;

import ToyProject1.hungrytech.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 로그인
     */
    //로그인 Id로 해당 회원 조회
    Optional<Member> findLoginCheckByAccountId(@Param("accountId") String accountId);





    /**
     * 회원 아이디를 이용하여 회원정보 조회
     * 로그인 된 회원만 사용
     */
    Member findMemberByAccountId(@Param("accountId") String accountId);





}
