package ToyProject1.hungrytech.entity.member;

import ToyProject1.hungrytech.entity.BaseEntity;
import ToyProject1.hungrytech.entity.board.Board;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "account_id", nullable = false, unique = true)
    private String accountId;

    @Column(name = "account_pw", nullable = false)
    private String accountPw;

    @Column(nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "member")
    private List<Board> boards= new ArrayList<>();

    protected Member() {

    }

    public Member(String accountId, String accountPw, String email, String phoneNumber) {
        this.accountId = accountId;
        this.accountPw = accountPw;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }



    public static Member createMember(String accountId, String accountPw, String email, String phoneNumber) {
        return new Member(accountId, accountPw, email, phoneNumber);
    }


}
