package cuboid_tests.salesforce;

import cuboid_tests.salesforce.dto.SalesforceFinAccDto;
import feign.CollectionFormat;
import feign.Param;
import feign.RequestLine;

import java.util.Collections;
import java.util.List;

public interface SalesforceApi {

    @RequestLine(value = "GET /services/data/v51.0/sobjects/FinServ__FinancialAccount__c/FinServ__FinancialAccountNumber__c/{accountNumber}?fields={fields}" , collectionFormat = CollectionFormat.CSV)
    SalesforceFinAccDto findFinAccountByAccountNumber(@Param("accountNumber") String accountNumber, @Param("fields") List<String> fields);

    default SalesforceFinAccDto findFinAccountByAccountNumber(String accountNumber){
        return findFinAccountByAccountNumber(accountNumber, Collections.singletonList("id"));
    }
}