FinServ__FinancialAccountTransaction__c[] trans =
    [select id, FinServ__TransactionDate__c, FinServ__SourceSystemId__c, FinServ__TransactionType__c
     from FinServ__FinancialAccountTransaction__c
     WHERE FinServ__FinancialAccount__r.FinServ__FinancialAccountNumber__c='222021250'
     and FinServ__TransactionDate__c < 2022-02-01T00:00:00.000Z];

System.debug('Trans: s=' + trans.size());

delete trans;
