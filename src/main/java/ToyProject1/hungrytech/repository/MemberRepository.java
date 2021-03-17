package ToyProject1.hungrytech.repository;

import ToyProject1.hungrytech.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {


    /**
     * 회원 아이디를 이용하여 회원정보 조회
     * 로그인 된 회원만 사용
     */
    Member findMemberByAccountId(@Param("accountId") String accountId);




    /**
     * 해당 회원이 등록한 게시물 조회
     * 로그인 된 회원만 사용
     */
    @Query("select distinct m from Member m join fetch m.boards where m.accountId = :accountId")
    Member findMemberAndBoard(@Param("accountId") String accountId);
}
