package pt.famility.backoffice.config;

import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.web.filter.CorsFilter;
import pt.famility.backoffice.config.firebase.FirebaseAuthenticationProvider;
import pt.famility.backoffice.config.firebase.FirebaseBasicAuthenticationFilter;
import pt.famility.backoffice.config.firebase.FirebaseFilter;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import pt.famility.backoffice.security.AuthoritiesConstants;
import pt.famility.backoffice.security.jwt.JWTConfigurer;
import pt.famility.backoffice.security.jwt.TokenProvider;
import tech.jhipster.config.JHipsterProperties;

import javax.annotation.PostConstruct;

// TODO: migrate this class to a new definition, without extending WebSecurityConfigurerAdapter.
// See the JHipster 7.9.3 template
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JHipsterProperties jHipsterProperties;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final FirebaseAuthenticationProvider authenticationProvider;

    private final TokenProvider tokenProvider;

    private final CorsFilter corsFilter;

    private final SecurityProblemSupport problemSupport;

    private final FirebaseFilter firebaseFilter;

    public SecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder,
                                 FirebaseAuthenticationProvider provider,
                                 TokenProvider tokenProvider,
                                 SecurityProblemSupport problemSupport,
                                 CorsFilter corsFilter,
                                 JHipsterProperties jHipsterProperties,
                                 FirebaseFilter firebaseFilter) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
        this.problemSupport = problemSupport;
        this.authenticationProvider = provider;
        this.corsFilter = corsFilter;
        this.jHipsterProperties = jHipsterProperties;
        this.firebaseFilter = firebaseFilter;
    }

    @PostConstruct
    public void init() {
        try {
            authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        } catch (Exception e) {
            throw new BeanInitializationException("Security configuration failed", e);
        }
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final FirebaseBasicAuthenticationFilter firebaseBasicAuthenticationFilter = new FirebaseBasicAuthenticationFilter(this.authenticationManager(),tokenProvider);
        // @formatter:off
        http
            .csrf()
            .disable()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(firebaseFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilter(firebaseBasicAuthenticationFilter)
            .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
        .and()
            .headers()
                .contentSecurityPolicy(jHipsterProperties.getSecurity().getContentSecurityPolicy())
            .and()
                .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
            .and()
                .permissionsPolicy().policy("camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()")
            .and()
                .frameOptions().sameOrigin()
        .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers("/app/**/*.{js,html}").permitAll()
            .antMatchers("/manifest.webapp").permitAll()
            .antMatchers("/i18n/**").permitAll()
            .antMatchers("/content/**").permitAll()
            .antMatchers("/swagger-ui/**").permitAll()
            .antMatchers("/test/**").permitAll()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/account/reset-password/init").permitAll()
            .antMatchers("/api/account/reset-password/finish").permitAll()
            .antMatchers("/api/profile-info").permitAll()
            .antMatchers("/api/login").permitAll()
            .antMatchers("/api/account/reset-password").permitAll()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/angular-env-configuration").permitAll()
            .antMatchers("/api/logger").permitAll()
            .antMatchers("/api/terms-and-conditions").permitAll()
            .antMatchers("/api/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/**").authenticated()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/health/**").permitAll()
            .antMatchers("/management/info").permitAll()
            .antMatchers("/management/prometheus").permitAll()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .and()
            .httpBasic()
        .and()
            .apply(securityConfigurerAdapter());
        // @formatter:on

    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }

}
