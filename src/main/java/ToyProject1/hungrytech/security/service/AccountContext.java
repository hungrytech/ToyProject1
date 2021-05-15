package ToyProject1.hungrytech.security.service;

import ToyProject1.hungrytech.domain.member.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AccountContext extends User {

    private final Member member;

    public AccountContext(Member member, Collection<? extends GrantedAuthority> authorities) {
        super(member.getAccountId(), member.getAccountPw(), authorities);
        this.member = member;
    }

    public Member getMember() {
        return member;
    }
}
