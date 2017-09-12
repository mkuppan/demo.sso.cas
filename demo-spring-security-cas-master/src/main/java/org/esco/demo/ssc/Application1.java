package org.esco.demo.ssc;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


@ComponentScan
@Configuration
@EnableAutoConfiguration
public class Application1 {

	public static void main(String[] args) {
		OverrideCertificateValidation();
		ApplicationContext ctx = SpringApplication.run(Application1.class, args);
		System.out.println("Let's inspect the beans provided by Spring Boot:");
		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		System.out.println(Arrays.toString(beanNames));
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
