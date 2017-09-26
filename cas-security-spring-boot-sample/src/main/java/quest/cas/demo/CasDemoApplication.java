package quest.cas.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.UriComponentsBuilder;

import com.kakawait.spring.boot.security.cas.CasHttpSecurityConfigurer;
import com.kakawait.spring.boot.security.cas.CasSecurityConfigurerAdapter;
import com.kakawait.spring.boot.security.cas.CasSecurityProperties;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;



@SpringBootApplication
public class CasDemoApplication {

    public static void main(String[] args) {
    	
    	OverrideCertificateValidation();
    	SpringApplication.run(CasDemoApplication.class, args);
    }

    @Bean
    FilterRegistrationBean forwardedHeaderFilter() {
        FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
        filterRegBean.setFilter(new ForwardedHeaderFilter());
        filterRegBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegBean;
    }

    @Profile("!custom-logout")
    @Configuration
    static class LogoutConfiguration extends CasSecurityConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        }
    }

    @Configuration
    static class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/api/**").authorizeRequests().anyRequest().authenticated();           
            CasHttpSecurityConfigurer.cas().configure(http);
            http.exceptionHandling().authenticationEntryPoint(new Http401AuthenticationEntryPoint("CAS"));
        }
    }

    @Profile("custom-logout")
    @Configuration
    static class CustomLogoutConfiguration extends CasSecurityConfigurerAdapter {
        private final CasSecurityProperties casSecurityProperties;

        public CustomLogoutConfiguration(CasSecurityProperties casSecurityProperties) {
            this.casSecurityProperties = casSecurityProperties;
        }

       
    }

    @Profile("custom-logout")
    @Configuration
    static class WebMvcConfiguration extends WebMvcConfigurerAdapter {
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addViewController("/logout.html").setViewName("logout");
            registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        }
    }

    @Controller
    @RequestMapping(value = "/")
    static class IndexController {

        @RequestMapping
        public String hello(Principal principal, Model model) {
            if (StringUtils.hasText(principal.getName())) {
                model.addAttribute("username", principal.getName());
            }
            return "index";
        }

        @RequestMapping(path = "/ignored")
        public String ignored() {
            return "index";
        }
    }


    @RestController
    @RequestMapping(value = "/api")
    static class HelloWorldController {

        @GetMapping
        public @ResponseBody String hello(Principal principal) {
            return principal == null ? "Hello anonymous" : "Welcome to the api landing page " + principal.getName();
        }
    }
    
    static void OverrideCertificateValidation()
    {
    	TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	            public void checkClientTrusted(X509Certificate[] certs, String authType) {
	            }
	            public void checkServerTrusted(X509Certificate[] certs, String authType) {
	            }
        	}
    	};

	    // Install the all-trusting trust manager
	    SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("SSL");
		} catch (NoSuchAlgorithmException e) {	
			e.printStackTrace();
		}
	    try {
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
		} catch (KeyManagementException e) {		
			e.printStackTrace();
		}
	    
	    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	    // Create a trusting (all) host name verifier
	    HostnameVerifier allHostsValid = new HostnameVerifier() {
	        public boolean verify(String hostname, SSLSession session) {
	            return true;
	        }
	    };

	    // Install the trusting (all) host verifier
	    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

}
