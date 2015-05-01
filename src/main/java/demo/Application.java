package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;


public class Application {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(WebApplicationConfiguration.class);
        app.setShowBanner(false);
        app.run(args);
    }

    /**
     * Base context
     */

    @Configuration
    @ComponentScan(includeFilters = @ComponentScan.Filter(Service.class), useDefaultFilters = false)
    @EnableAutoConfiguration
    static class ApplicationConfiguration {}

    /**
     * Web Appplication context
     */
    @Configuration
    @Import({ApplicationConfiguration.class, WebSecurityConfiguration.class, WebSocketConfiguration.class})
    @ComponentScan(excludeFilters = @ComponentScan.Filter({ Service.class, Configuration.class }))
    static class WebApplicationConfiguration {

    }

//    @Configuration
//    static class MvcConfig extends WebMvcConfigurerAdapter {
//        @Override
//        public void addViewControllers(ViewControllerRegistry registry) {
//            registry.addViewController("/login*").setViewName("login");
//        }
//    }
    /**
     * WebSocket configuration
     */
    @Configuration
    @EnableWebSocketMessageBroker
    static class WebSocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer {

        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint("/socket").withSockJS();
        }

        @Override
        public void configureMessageBroker(org.springframework.messaging.simp.config.MessageBrokerRegistry registry) {
            registry.enableSimpleBroker("/queue", "/widget");
            registry.setApplicationDestinationPrefixes("/app");
        }
    }



     /**
     * Web security configuration. Allow only webjars and assets unless logged in.
     * Defines what the login/logout urls are.
     */
    @Configuration
    static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                        .antMatchers("/assets/**").permitAll()
                        .antMatchers("/webjars/**").permitAll()
                        .anyRequest().authenticated()
                        .and()
                    .logout()
                        .logoutSuccessUrl("/login.html?logout")
                        .logoutUrl("/logout.html")
                        .permitAll()
                        .and()
                    .formLogin()
                        .defaultSuccessUrl("/index.html")
                        .loginPage("/login.html")
                        .failureUrl("/login.html?error")
                        .permitAll();


        }

        /**
         * Configure user details service and password encoder that takes any user and any password.
         * @param auth
         * @throws Exception
         */
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            //authenticate everything
            auth.userDetailsService(new UserDetailsService() {

                @Override
                public UserDetails loadUserByUsername(String username)
                        throws UsernameNotFoundException {
                    return new User(username, "", true, true,
                            true, true, AuthorityUtils.createAuthorityList("USER"));
                }
            }).passwordEncoder(new PasswordEncoder() {

                /**
                 * Allows any passwords
                 * @param rawPassword
                 * @param encodedPassword
                 * @return
                 */
                @Override
                public boolean matches(CharSequence rawPassword, String encodedPassword) {
                    return true;
                }

                @Override
                public String encode(CharSequence rawPassword) {
                    return null;
                }
            });
        }
    }
}
