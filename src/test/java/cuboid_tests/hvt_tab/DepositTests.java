package cuboid_tests.hvt_tab;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;
import static cuboid_tests.EnvConfig.*;
import static cuboid_tests.HvtSteps.selectDeposit;
import static cuboid_tests.HvtSteps.setBankFields;
import static cuboid_tests.PayapiSteps.*;

public class DepositTests {

    @Tag("smoke-tests")
    @ParameterizedTest
    @ValueSource(strings = {MAM})
    void makeDeposit(String accountNumber){
        String randomAmountStr = getRandomAmount();

        // log in and make a deposit on regular acc
        login();
        createTransaction();
        selectDeposit();

        $(byId("accountNumber")).setValue(accountNumber);
        $(byId("executedAmount")).setValue(randomAmountStr);
        $(byCssSelector("div.form-group")).click();
        sleep(TIMEOUT_MSEC);
        setBankFields();
        $(byClassName("btn-success")).click();
        createTransactionsButton();

        // check that transaction displayed in list
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byTagName("h4")).shouldBe(visible, TIMEOUT_SEC)
                .shouldHave(text("High Value Transactions for"), TIMEOUT_SEC);
        $(byCssSelector("tr.transaction-row")).shouldBe(visible, TIMEOUT_SEC);
        $(byCssSelector("tr.transaction-row")).shouldHave(text(accountNumber)).
                shouldHave(text(randomAmountStr));
    }

    @ParameterizedTest
    @ValueSource(strings = {REGULAR, WALLET})
    void makeDepositCreateFlow(String accountNumber){
        String randomAmountStr = getRandomAmount();

        // log in and make a deposit on regular acc
        login();
        createTransaction();
        selectDeposit();

        $(byId("accountNumber")).setValue(accountNumber);
        $(byId("executedAmount")).setValue(randomAmountStr);
        $(byCssSelector("div.form-group")).click();
        sleep(TIMEOUT_MSEC);
        setBankFields();
        $(byClassName("btn-success")).click();
        createTransactionsButton();

        // check that transaction displayed in list
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byTagName("h4")).shouldBe(visible, TIMEOUT_SEC)
                .shouldHave(text("High Value Transactions for"), TIMEOUT_SEC);
        $(byCssSelector("tr.transaction-row")).shouldBe(visible, TIMEOUT_SEC);
        $(byCssSelector("tr.transaction-row")).shouldHave(text(accountNumber)).
                shouldHave(text(randomAmountStr));

        // approve deposit
        $(byCssSelector("tr.transaction-row")).find(byId("addTransactionsButton")).click();
        $(byId("delete")).click();

        // check that displayed notification that deposit is success
        $(byCssSelector("div.alert-success")).shouldBe(visible, TIMEOUT_SEC);
        $(byCssSelector("div.alert-success")).shouldHave(text("Transaction has been approved"));
    }
}
