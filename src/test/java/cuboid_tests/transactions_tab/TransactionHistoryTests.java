package cuboid_tests.transactions_tab;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import cuboid_tests.EnvConfig;
import cuboid_tests.PayapiSteps;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static cuboid_tests.EnvConfig.MAM;
import static cuboid_tests.EnvConfig.TIMEOUT_MSEC;
import static cuboid_tests.PayapiSteps.*;

public class TransactionHistoryTests {

    @Test
    void searchByAccountNo() {
        String dateFrom = "05/07/2021";
        String accountNumber = MAM;

        // log in admin
        login();

        // try to search transaction by order id
        PayapiSteps.setDateFrom(dateFrom);
        element(byId("accountNo")).setValue(accountNumber);
        clickUpdateButton();

        // check that displayed 1 transaction in list
        $(byId("transactionAccountNumber")).should(exist);
        element(byId("transactionAccountNumber")).shouldHave(text(accountNumber));
    }

    @Test
    void searchByAccType() {
        String dateFrom = "06/07/2021";
        String dateTo = "23/07/2021";

        // log in and set needed date range
        login();
        setDateRange(dateFrom, dateTo);

        // choose Regular account type
        $(byCssSelector("div.account-type button")).shouldBe(visible).click();
        $(byCssSelector("div.account-type li:nth-child(2)")).shouldBe(visible).click();
        clickUpdateButton();
        $$(byCssSelector("td.account-type")).filterBy(text("Regular")).shouldHave(CollectionCondition.size(100));

        // choose Brokeree account type
        $(byCssSelector("div.account-type button")).shouldBe(visible).click();
        $(byCssSelector("div.account-type li:nth-child(3)")).shouldBe(visible).click();
        clickUpdateButton();
        $$(byCssSelector("td.account-type")).filterBy(text("Brokeree")).shouldHave(CollectionCondition.size(3));

        // choose Wallet account type
        $(byCssSelector("div.account-type button")).shouldBe(visible).click();
        $(byCssSelector("div.account-type li:nth-child(5)")).shouldBe(visible).click();
        clickUpdateButton();
        $$(byCssSelector("td.account-type")).filterBy(text("Wallet")).shouldHave(CollectionCondition.size(78));
    }

    @Test
    @Disabled
    void searchByBroker() {
        String dateFrom = "15/07/2021";
        String dateTo = "22/07/2021";

        // log in cuboid and set needed date range
        login();
        setDateRange(dateFrom, dateTo);

        // choose FSC Broker option
        $(byCssSelector("div.broker button")).shouldBe(visible).click();
        $(byCssSelector("div.broker li:nth-child(2)")).shouldBe(visible).click();
        clickUpdateButton();
        $$(byCssSelector("td.broker")).filterBy(text("FSC")).shouldHave(CollectionCondition.size(45));

        // choose SCB Broker option
        $(byCssSelector("div.broker button")).shouldBe(visible).click();
        $(byCssSelector("div.broker li:nth-child(3)")).shouldBe(visible).click();
        clickUpdateButton();
        $$(byCssSelector("td.broker")).filterBy(text("SCB")).shouldHave(CollectionCondition.size(45));

        // choose FCA Broker option
        $(byCssSelector("div.broker button")).shouldBe(visible).click();
        $(byCssSelector("div.broker li:nth-child(4)")).shouldBe(visible).click();
        clickUpdateButton();
        $$(byCssSelector("td.broker")).filterBy(text("FCA")).shouldHave(CollectionCondition.size(6));
    }

    @Test
    @Disabled
    void searchByCompany() {
        String dateFrom = "18/06/2021";
        String dateTo = "16/07/2021";

        // log in admin
        login();

        // check how much companies for filter
        setDateRange(dateFrom, dateTo);
        $(byCssSelector("div.company button")).shouldBe(visible).click();

        // choose Bitculture DT
        $(byId("companyId-BITCULTURE_DT")).shouldBe(visible).click();
        clickUpdateButton();
        $$("td.company").filter(text("Bitculture DT")).shouldHave(CollectionCondition.size(100));
        $(byCssSelector("div.company button")).shouldBe(visible).click();
        $(byId("companyId-BITCULTURE_DT")).shouldBe(visible).click();

        // choose Bitculture SAF
        $(byId("companyId-BITCULTURE_SAF")).shouldBe(visible).click();
        clickUpdateButton();
        $$("td.company").filter(text("Bitculture SAF")).shouldHave(CollectionCondition.size(56));
        $(byCssSelector("div.company button")).shouldBe(visible).click();
        $(byId("companyId-BITCULTURE_SAF")).shouldBe(visible).click();

        // choose The Brokers Capital
        $(byId("companyId-BROKERS_CAPITAL")).shouldBe(visible).click();
        clickUpdateButton();
        $$("td.company").filter(text("The Brokers Capital")).shouldHave(CollectionCondition.size(2));
        $(byCssSelector("div.company button")).shouldBe(visible).click();
        $(byId("companyId-BROKERS_CAPITAL")).shouldBe(visible).click();

        // choose Infinox
        $(byId("companyId-INFINOX")).shouldBe(visible).click();
        clickUpdateButton();
        $$("td.company").filter(text("Infinox")).shouldHave(CollectionCondition.size(100));
    }

    @Test
    void searchByDate() {
        String dateFrom = "30/06/2021";
        String dateTo = "01/07/2021";

        // log in admin
        login();

        // set up needed interval
        setDateRange(dateFrom, dateTo);
        clickUpdateButton();

        // get list of displayed dates
        ElementsCollection result = $$(byClassName("created-date"));
        result.shouldHave(CollectionCondition.size(50));
        result.filter(text("30/06/2021")).shouldHave(CollectionCondition.size(19));
        result.filter(text("01/07/2021")).shouldHave(CollectionCondition.size(31));
    }

    @Test
    void searchByIntegrationType() {
        String neededDate = "08/07/2021";

        // log in admin
        login();

        // search transactions by Praxis Integration Type
        setDateRange(neededDate, neededDate);
        $(byCssSelector("div.integration-type button")).click();
        $(byText("Praxis")).click();
        clickUpdateButton();

        // check that displayed 3 Praxis transactions
        ElementsCollection allPraxis = $$(byCssSelector("td.integration-type"));
        allPraxis.filterBy(text("Praxis")).shouldHave(CollectionCondition.size(3));

        // search transactions by Non-Praxis Integration Type
        $(byCssSelector("div.integration-type button")).click();
        $(byText("Non-Praxis")).click();
        clickUpdateButton();

        // check that displayed 42 Non-Praxis transactions
        ElementsCollection allNonPraxis = $$(byCssSelector("td.integration-type"));
        allNonPraxis.filterBy(text("Non-Praxis")).shouldHave(CollectionCondition.size(42));
    }

    @Test
    void searchByName() {
        String clientA = "Bred Pitt";
        String clientB = "Ivan BRK";
        String clientC = "Pablito Brasil";
        String dateFrom = "17/06/2021";
        String dateTo = "03/08/2021";

        // log in and set needed date range
        login();
        setDateRange(dateFrom, dateTo);

        // search Bred`s transactions
        $(byId("accountOwner")).setValue(clientA);
        clickUpdateButton();
        $$(byId("transactionAccountOwner")).filterBy(text(clientA)).shouldHave(CollectionCondition.size(67));

        // search Ivan`s transactions
        $(byId("accountOwner")).setValue(clientB);
        clickUpdateButton();
        $$(byId("transactionAccountOwner")).filterBy(text(clientB)).shouldHave(CollectionCondition.size(2));

        // search Pablito`s transactions
        $(byId("accountOwner")).setValue(clientC);
        clickUpdateButton();
        $$(byId("transactionAccountOwner")).filterBy(text(clientC)).shouldHave(CollectionCondition.size(5));
    }

    @Test
    void searchByOffice() {
        String dateFrom = "13/07/2021";
        String dateTo = "23/07/2021";

        // log in and set needed date range
        login();
        setDateRange(dateFrom, dateTo);

        // choose Philippines office
        $(byCssSelector("div.office button")).shouldBe(visible).click();
        $(byCssSelector("div.office li:nth-child(14)")).click();
        clickUpdateButton();
        $$(byId("transactionOffice")).filterBy(text("Philippines")).shouldHave(CollectionCondition.size(5));
        $(byCssSelector("div.office button")).shouldBe(visible).click();
        $(byCssSelector("div.office li:nth-child(14)")).click();

        // choose Malaysia office
        $(byCssSelector("div.office li:nth-child(10)")).click();
        clickUpdateButton();
        $$(byId("transactionOffice")).filterBy(text("Malaysia")).shouldHave(CollectionCondition.size(2));
        $(byCssSelector("div.office button")).shouldBe(visible).click();
        $(byCssSelector("div.office li:nth-child(10)")).click();

        // choose Portugal office
        $(byCssSelector("div.office li:nth-child(15)")).click();
        clickUpdateButton();
        $$(byId("transactionOffice")).filterBy(text("Portugal")).shouldHave(CollectionCondition.size(73));
        $(byCssSelector("div.office button")).shouldBe(visible).click();
        $(byCssSelector("div.office li:nth-child(15)")).click();

        // choose Thailand office
        $(byCssSelector("div.office li:nth-child(19)")).click();
        clickUpdateButton();
        $$(byId("transactionOffice")).filterBy(text("Thailand")).shouldHave(CollectionCondition.size(44));
        $(byCssSelector("div.office button")).shouldBe(visible).click();
        $(byCssSelector("div.office li:nth-child(19)")).click();

        // choose UK office
        $(byCssSelector("div.office li:nth-child(21)")).click();
        clickUpdateButton();
        $$(byId("transactionOffice")).filterBy(text("UK")).shouldHave(CollectionCondition.size(31));
        $(byCssSelector("div.office button")).shouldBe(visible).click();
        $(byCssSelector("div.office li:nth-child(21)")).click();
    }

    @Test
    void searchByOrder() {
        String orderId = "346996";
        String dateFrom = "20/06/2021";

        // log in admin
        login();

        // set needed date
        PayapiSteps.setDateFrom(dateFrom);

        // try to search transaction by order id
        element(byId("orderId")).setValue(orderId);
        clickUpdateButton();
        element(byId("transactionOrder")).shouldHave(text(orderId));
    }

    @Test
    @Disabled
    void searchByPlatform(){
        String dateFrom = "02/08/2021";
        String dateTo = "03/08/2021";

        // log in and set needed date
        login();
        setDateRange(dateFrom, dateTo);

        // search MT4 transactions
        $(byCssSelector("div.broker button")).click();
        $(byCssSelector("div.broker ul")).find(byText("SCB")).click();
        $(byCssSelector("div.platform button")).click();
        $(byCssSelector("div.platform ul")).find(byText("MT4")).click();
        clickUpdateButton();
        $$(byCssSelector("td.platform")).filterBy(Condition.text("MT4")).shouldHave(CollectionCondition.size(38));

        // search MT5 transactions
        $(byCssSelector("div.platform button")).click();
        $(byCssSelector("div.platform ul")).find(byText("MT5")).click();
        clickUpdateButton();
        $$(byCssSelector("td.platform")).filterBy(Condition.text("MT5")).shouldHave(CollectionCondition.size(9));
    }

    @Test
    void searchByPraxis(){
        String neededDate = "29/07/2021";
        String praxisID = "1015155533";

        // log in admin
        login();
        PayapiSteps.setDateFrom(neededDate);

        // try to search transaction with corresponding praxis id
        $(byId("praxisTraceId")).setValue(praxisID);
        clickUpdateButton();
        $$(byId("traceId")).filterBy(Condition.text(praxisID)).shouldHave(CollectionCondition.size(1));
    }

    @Test
    @Disabled
    void searchByPsp() {
        String neededDate = "06/07/2021";

        // log in admin
        login();

        // set up needed date and PSP
        setDateRange(neededDate, neededDate);
        clickUpdateButton();
        $(byId("spinner")).shouldBe(hidden, EnvConfig.MAX_TIMEOUT_SEC);

        $(byCssSelector("button.dropdown-toggle")).shouldBe(visible).click();
        $(byCssSelector("div.payment-provider ul")).find(byText("Bank - Wealthum")).
                shouldBe(visible).click();
        Selenide.sleep(TIMEOUT_MSEC);
        clickUpdateButton();

        // check that displayed correct value of transactions
        ElementsCollection result = $$(byClassName("created-date"));
        result.filter(text(neededDate)).shouldHave(CollectionCondition.size(6));
    }

    @Test
    void searchByStatus(){
        String dateFrom = "02/08/2021";
        String dateTo = "03/08/2021";

        // log in and set needed date
        login();
        setDateRange(dateFrom, dateTo);

        // Pending
        $(byCssSelector("div.status button")).click();
        $(byCssSelector("div.status ul")).find(byText("Pending")).click();
        clickUpdateButton();
        $$(byCssSelector("td.transaction-status")).filterBy(Condition.text("Pending")).
                shouldHave(CollectionCondition.size(17));

        // Reconciled
        $(byCssSelector("div.status button")).click();
        $(byCssSelector("div.status ul")).find(byText("Reconciled")).click();
        clickUpdateButton();
        $$(byCssSelector("td.transaction-status")).filterBy(Condition.text("Reconciled")).
                shouldHave(CollectionCondition.size(19));

        // Success
        $(byCssSelector("div.status button")).click();
        $(byCssSelector("div.status ul")).find(byText("Success")).click();
        clickUpdateButton();
        $$(byCssSelector("td.transaction-status")).filterBy(Condition.text("Success")).
                shouldHave(CollectionCondition.size(2));
    }

    @Test
    void searchByTransactionType(){
        String neededDate = "28/07/2021";

        //log in admin and set needed date
        login();
        setDateRange(neededDate, neededDate);

        // choose deposit type
        $(byCssSelector("div.transaction-type button")).shouldBe(visible).click();
        $(byText("Deposit")).click();
        clickUpdateButton();
        $$("td.type").filterBy(text("Deposit")).shouldHave(CollectionCondition.size(6));

        // choose withdrawal type
        $(byCssSelector("div.transaction-type button")).shouldBe(visible).click();
        $(byText("Withdrawal")).click();
        clickUpdateButton();
        $$("td.type").filterBy(text("Withdrawal")).shouldHave(CollectionCondition.size(19));
        $(byCssSelector("div.transaction-type button")).shouldBe(visible).click();

        // choose all option
        $(byCssSelector("div.transaction-type li")).shouldBe(visible).click();
        clickUpdateButton();
        $$("td.type").shouldHave(CollectionCondition.size(25));
    }
}