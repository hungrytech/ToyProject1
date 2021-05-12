package ToyProject1.hungrytech.security.handler;

import ToyProject1.hungrytech.domain.member.Member;
import ToyProject1.hungrytech.memberDto.MemberLoginInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("customAuthenticationSuccessHandler")
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        setDefaultTargetUrl("/");

        Member authenticationMember = (Member) authentication.getPrincipal();

        request.getSession()
                .setAttribute("memberInfo", new MemberLoginInfo(
                        authenticationMember.getAccountId(),
                        authenticationMember.getName())
                );

        if(savedRequest != null) { //인증이 되기전 요청 url이 있을때
            String target = savedRequest.getRedirectUrl(); //인증 성공후 해당 요청 url로 이동
            redirectStrategy.sendRedirect(request, response, target);
        }else {
            redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
        }
    }
}
