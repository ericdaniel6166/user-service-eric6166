//package com.eric6166.user.config.keycloak;
//
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.experimental.FieldDefaults;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@Getter
//@Setter
//@ConditionalOnProperty(name = "keycloak-service.enabled", havingValue = "true")
//@ConfigurationProperties(prefix = "keycloak-service")
//public class KeycloakProps {
//
//    String serverUrl;
//    String realm;
//    String clientId;
//    String clientSecret;
//    String issuerUri;
//
//}