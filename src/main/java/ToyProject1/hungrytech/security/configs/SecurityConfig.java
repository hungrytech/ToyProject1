package ToyProject1.hungrytech.security.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationProvider authenticationProvider;

    private final AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    private final AuthenticationFailureHandler customAuthenticationFailureHandler;

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/img/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/login*", "/logout", "/main", "/member/new").permitAll()
                .antMatchers("/boards").permitAll()
                .antMatchers("/member/checkIdExist/**", "/loadImgFile/**").permitAll()
                .antMatchers("/member/**/edit", "/member/**/delete").hasRole("USER")
                .antMatchers("/board/new", "/board/**/edit", "/board/**/delete").hasRole("USER")
                .antMatchers("/boardComment/**").hasRole("USER")
                .antMatchers("/board/write").hasRole("USER")
                .antMatchers("/board/**", "/api/v1/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()    // 로그인
                .loginPage("/login")
                .loginProcessingUrl("/member/login")
                .defaultSuccessUrl("/")
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .userInfoEndpoint()
                .userService(customOAuth2UserService);



    }



}
