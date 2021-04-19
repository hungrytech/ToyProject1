package ToyProject1.hungrytech.domain.member;

import ToyProject1.hungrytech.domain.BaseEntity;
import ToyProject1.hungrytech.domain.board.Board;
import ToyProject1.hungrytech.domain.boardcomment.BoardComment;
import ToyProject1.hungrytech.memberDto.MemberForm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SequenceGenerator(name = "USER_SEQ_MEMBER", sequenceName = "USER_SEQ_MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ_MEMBER")
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_name", nullable = false, length = 20)
    private String name;

    @Column(name = "account_id", nullable = false, unique = true, length = 15)
    private String accountId;

    @Column(name = "account_pw", nullable = false)
    private String accountPw;

    @Column(nullable = false, length = 25)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 13)
    private String phoneNumber;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Board> boards= new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<BoardComment> boardComments = new ArrayList<>();

    public Member(String name, String accountId, String accountPw, String email, String phoneNumber) {
        this.name = name;
        this.accountId = accountId;
        this.accountPw = accountPw;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }



    public static Member createMember(MemberForm memberForm) {
        return new Member(memberForm.getName(),
                memberForm.getAccountId(),
                memberForm.getAccountPw(),
                memberForm.getEmail(),
                memberForm.getPhoneNumber());
    }

    /**
     * Entity 정보변경
     */
    public void changePw(String changePw) {
        accountPw = changePw;
    }

    public void changeEmail(String changeEmail) {
        email = changeEmail;
    }

    public void changePhoneNumber(String changePhoneNumber) {
        phoneNumber = changePhoneNumber;
    }



}
