package cuboid_tests.salesforce;

import cuboid_tests.salesforce.dto.SalesforceAuthRequest;
import cuboid_tests.salesforce.dto.SalesforceAuthResponse;
import feign.Headers;
import feign.RequestLine;

public interface SalesforceAuthAPI {

    @RequestLine("POST /services/oauth2/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    SalesforceAuthResponse getToken(SalesforceAuthRequest request);
}
