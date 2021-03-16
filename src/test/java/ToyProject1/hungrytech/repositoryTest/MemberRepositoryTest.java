package ToyProject1.hungrytech.repositoryTest;

import ToyProject1.hungrytech.entity.member.Member;
import ToyProject1.hungrytech.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@SpringBootTest
@Rollback(value = false)
@Transactional
public class MemberRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("엔티티 저장이 잘 되는지 테스트")
    public void save() throws Exception {
        //given
        Member member = Member.createMember("first1",
                passwordEncoder.encode("ssdfds"),
                "first1@naver.com",
                "010-1100-0111");
        //when
        memberRepository.save(member);
        Optional<Member> findMember = memberRepository.findById(member.getId());
        Member result = null;
        if(findMember.isPresent()){
            result = findMember.get();
        }

        //then
        Assertions.assertThat(member).isSameAs(result);
    }
}
