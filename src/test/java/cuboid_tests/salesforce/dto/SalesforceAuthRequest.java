package cuboid_tests.salesforce.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalesforceAuthRequest {
    private String grant_type;
    private String username;
    private String password;
    private String client_id;
    private String client_secret;
}