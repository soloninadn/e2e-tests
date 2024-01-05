package cuboid_tests.salesforce;

import cuboid_tests.salesforce.dto.SalesforceAuthRequest;
import feign.*;
import feign.codec.ErrorDecoder;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

import java.util.Date;

public class SalesforceRestClient {

//    @Value("${sales.force.client.id}")
    private String clientId;
//    @Value("${sales.force.client.secret}")
    private String clientSecret;
//    @Value("${sales.force.login}")
    private String username;
//    @Value("${sales.force.password}")
    private String password;
//    @Value("${sales.force.token}")
    private String clientToken;

    private String token;

//    private final Supplier<String> tokenSupplier =
//            () -> token = token == null ? salesforceAuthAPI.getToken(buildAuthRequest()).getToken() : token;

    private SalesforceAuthRequest buildAuthRequest() {
        return SalesforceAuthRequest.builder()
                .grant_type("password")
                .client_id(clientId)
                .client_secret(clientSecret)
                .username(username)
                .password(password + clientToken)
                .build();
    }

    //@Value("${salesforce.api.base.url}") S
    public SalesforceApi salesforceApi(String serverUrl) {
        String tokenSupplier = "";
        return Feign.builder()
                .client(new OkHttpClient())
                .requestInterceptor(template -> {
                    template.removeHeader("Authorization");
                    template.header("Authorization", "Bearer " + tokenSupplier);
                })
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new AuthErrorDecoder())
                .retryer(new AuthRetryer())
                .logger(new Slf4jLogger(SalesforceApi.class))
                .logLevel(Logger.Level.FULL)
                .target(SalesforceApi.class, serverUrl);
    }

    //@Value("${salesforce.api.base.url}")
    public SalesforceAuthAPI salesforceAuthApi(String serverUrl) {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new FormEncoder())
                .decoder(new JacksonDecoder())
                .retryer(Retryer.NEVER_RETRY)
                .logger(new Slf4jLogger(SalesforceAuthAPI.class))
                .logLevel(Logger.Level.FULL)
                .target(SalesforceAuthAPI.class, serverUrl);
    }

    private class AuthErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            if (response.status() == 401) {
                token = null;
                return new RetryableException(401, response.reason(), response.request().httpMethod(), new Date(), response.request());
            }
            return new Default().decode(methodKey, response);
        }
    }

    private class AuthRetryer implements Retryer {

        private static final int MAX_ATTEMPTS = 1;
        private int attempt = 0;

        @Override
        public void continueOrPropagate(RetryableException e) {
            if (e.status() != 401 || attempt++ >= MAX_ATTEMPTS) {
                throw e;
            }
        }

        @Override
        public Retryer clone() {
            return new AuthRetryer();
        }

    }

}