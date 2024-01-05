package cuboid_tests.hvt_tab;

import com.codeborne.selenide.Selenide;
import cuboid_tests.PayapiSteps;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;
import static cuboid_tests.EnvConfig.*;
import static cuboid_tests.HvtSteps.selectWithdrawal;
import static cuboid_tests.PayapiSteps.*;


public class WithdrawalTests {

    @Tag("smoke-tests")
    @ParameterizedTest
    @ValueSource(strings = {REGULAR})
    void makeWithdrawal(String accountNumber){
        String randomAmountStr = getRandomAmount();

        // login and open HVT tab
        login();
        createTransaction();

        // create a withdrawal in HVT tab
        selectWithdrawal();
        $(byId("accountNumber")).setValue(accountNumber);
        $(byTagName("h3")).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byId("bankName")).shouldBe(visible);
        $(byCssSelector("div.paymentMethod")).click();
        $(byText("Bank - Wealthum")).click();
        $(byId("executedAmount")).setValue(randomAmountStr);
        $(byCssSelector("button.btn-success")).click();
        createTransactionsButton();
        checkTransaction(accountNumber, randomAmountStr, "Pending");
    }

    @ParameterizedTest
    @ValueSource(strings = {REGULAR, WALLET})
    void makeWithdrawalCreateFlow(String accountNumber){
        String randomAmountStr = getRandomAmount();

        // login and open HVT tab
        login();
        createTransaction();

        // create a withdrawal in HVT tab
        selectWithdrawal();
        $(byId("accountNumber")).setValue(accountNumber);
        $(byTagName("h3")).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byId("bankName")).shouldBe(visible);
        $(byCssSelector("div.paymentMethod")).click();
        $(byText("Bank - Wealthum")).click();
        $(byId("executedAmount")).setValue(randomAmountStr);
        $(byCssSelector("button.btn-success")).click();
        createTransactionsButton();
        checkTransaction(accountNumber, randomAmountStr, "Pending");

        // make an AML approval
        $(byCssSelector("li.withdrawalsManageLink")).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byCssSelector("tr.transaction-row")).shouldBe(visible, TIMEOUT_SEC);
        $(byCssSelector("td.account-number")).shouldHave(text(accountNumber)).find("a").click();
        $(byId("amountInput")).shouldBe(visible, TIMEOUT_SEC);
        $(byId("allBoxes")).click();
        $(byCssSelector("button.amlApproveButton")).click();
        checkTransaction(accountNumber, randomAmountStr, "Accounts OK");

        // make a Sales approval
        $(byCssSelector("tr.transaction-row")).shouldBe(visible, TIMEOUT_SEC);
        $(byCssSelector("td.account-number")).shouldHave(text(accountNumber)).find("a").click();
        $(byTagName("h3")).shouldBe(visible, TIMEOUT_SEC);
        $(byId("allBoxesSales")).click();
        $(byCssSelector("button.salesApproveButton")).click();
        checkTransaction(accountNumber, randomAmountStr, "Sales OK");

        // make a Manual withdrawal
        $(byCssSelector("tr.transaction-row")).shouldBe(visible, TIMEOUT_SEC);
        $(byCssSelector("td.account-number")).shouldHave(text(accountNumber)).find("a").click();
        $(byTagName("h3")).shouldBe(visible, TIMEOUT_SEC);
        $(byCssSelector("button.moneyApproveButton")).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byCssSelector("h4")).shouldBe(visible);

        // check that in Archive sub-tab presents current transaction
        clickTransactionsTab();
        clickUpdateButton();

        checkTransaction(accountNumber, randomAmountStr, "Accepted");
    }

    @ParameterizedTest
    @ValueSource(strings = {REGULAR, WALLET})
    void makeWithdrawalCreateAndApplyFlow(String accountNumber){
        String randomAmountStr = getRandomAmount();

        // login and open HVT tab
        login();
        createTransaction();

        // create a withdrawal in HVT tab
        selectWithdrawal();
        $(byId("accountNumber")).setValue(accountNumber);
        $(byTagName("h3")).click();
        $(byId("bankName")).shouldBe(visible);
        $(byCssSelector("div.paymentMethod")).click();
        $(byText("Bank - Wealthum")).click();
        $(byId("executedAmount")).setValue(randomAmountStr);
        $(byCssSelector("button.btn-success")).click();
        createAndApplyTransactionsButton();
        $(byId("confirmAndApplyButton")).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);

        checkTransaction(accountNumber, randomAmountStr, "Reconciled");
    }

    @ParameterizedTest
    @ValueSource(strings = {REGULAR, WALLET})
    void cancelWithdrawalFirstStep(String accountNumber){
        String randomAmountStr = getRandomAmount();
        System.out.println(randomAmountStr);

        // login and open HVT tab
        login();
        createTransaction();

        // create a withdrawal in HVT tab
        selectWithdrawal();
        $(byId("accountNumber")).setValue(accountNumber);
        $(byTagName("h3")).click();
        $(byId("bankName")).shouldBe(visible);
        $(byCssSelector("div.paymentMethod")).click();
        $(byText("Bank - Wealthum")).click();
        $(byId("executedAmount")).setValue(randomAmountStr);
        $(byCssSelector("button.btn-success")).click();
        createTransactionsButton();
        $(byId("showButton")).shouldBe(visible, TIMEOUT_SEC);
        checkTransaction(accountNumber, randomAmountStr, "Pending");

        // open Withdrawal tab and cancel transaction
        $(byId("withdrawalsManageLink")).shouldBe(visible, TIMEOUT_SEC).click();
        $(byCssSelector("tr.transaction-row")).shouldHave(text(accountNumber)).shouldHave(text(randomAmountStr)).
                find("a.ng-binding").click();
        sleep(TIMEOUT_MSEC);
        $(byCssSelector("button.cancelWithdrawButton")).click();
        $(byId("delete")).shouldBe(visible).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byId("showButton")).shouldBe(visible, TIMEOUT_SEC);

        // check that transaction has a "Failure" status
        clickTransactionsTab();
        checkTransaction(accountNumber, randomAmountStr, "Failure");
    }

    @ParameterizedTest
    @ValueSource(strings = {REGULAR, WALLET})
    void cancelWithdrawalSecondStep(String accountNumber) {
        String randomAmountStr = getRandomAmount();
        System.out.println(randomAmountStr);

        // login and open HVT tab
        login();
        createTransaction();

        // create a withdrawal in HVT tab
        selectWithdrawal();
        $(byId("accountNumber")).setValue(accountNumber);
        $(byTagName("h3")).click();
        $(byId("bankName")).shouldBe(visible);
        $(byCssSelector("div.paymentMethod")).click();
        $(byText("Bank - Wealthum")).click();
        $(byId("executedAmount")).setValue(randomAmountStr);
        $(byCssSelector("button.btn-success")).click();
        createTransactionsButton();
        $(byId("showButton")).shouldBe(visible, TIMEOUT_SEC);
        checkTransaction(accountNumber, randomAmountStr, "Pending");

        // make an AML approval
        $(byCssSelector("li.withdrawalsManageLink")).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byCssSelector("tr.transaction-row")).shouldBe(visible, TIMEOUT_SEC);
        $(byCssSelector("td.account-number")).shouldHave(text(accountNumber)).find("a").click();
        $(byId("amountInput")).shouldBe(visible, TIMEOUT_SEC);
        $(byId("allBoxes")).click();
        $(byCssSelector("button.amlApproveButton")).click();
        checkTransaction(accountNumber, randomAmountStr, "Accounts OK");

        // open Withdrawal tab and cancel transaction
        $(byId("withdrawalsManageLink")).click();
        $(byCssSelector("tr.transaction-row")).shouldHave(text(accountNumber)).shouldHave(text(randomAmountStr)).
                find("a.ng-binding").click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byCssSelector("div[ng-if=\"!vm.salesDisabled\"]")).find("button.btn-danger").click();
        $(byId("delete")).shouldBe(visible).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byId("showButton")).shouldBe(visible, TIMEOUT_SEC);

        // check that transaction has a "Failure" status
        clickTransactionsTab();
        checkTransaction(accountNumber, randomAmountStr, "Failure");
    }

    @ParameterizedTest
    @ValueSource(strings = {REGULAR, WALLET})
    void cancelWithdrawalThirdStep(String accountNumber) {
        String randomAmountStr = getRandomAmount();
        System.out.println(randomAmountStr);

        // login and open HVT tab
        login();
        createTransaction();

        // create a withdrawal in HVT tab
        selectWithdrawal();
        $(byId("accountNumber")).setValue(accountNumber);
        $(byTagName("h3")).click();
        $(byId("bankName")).shouldBe(visible);
        $(byCssSelector("div.paymentMethod")).click();
        $(byText("Bank - Wealthum")).click();
        $(byId("executedAmount")).setValue(randomAmountStr);
        $(byCssSelector("button.btn-success")).click();
        createTransactionsButton();
        $(byId("showButton")).shouldBe(visible, TIMEOUT_SEC);
        checkTransaction(accountNumber, randomAmountStr, "Pending");

        // make an AML approval
        $(byCssSelector("li.withdrawalsManageLink")).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byCssSelector("tr.transaction-row")).shouldBe(visible, TIMEOUT_SEC);
        $(byCssSelector("td.account-number")).shouldHave(text(accountNumber)).find("a").click();
        $(byId("amountInput")).shouldBe(visible, TIMEOUT_SEC);
        $(byId("allBoxes")).click();
        $(byCssSelector("button.amlApproveButton")).click();
        checkTransaction(accountNumber, randomAmountStr, "Accounts OK");

        // make a Sales approval
        $(byCssSelector("tr.transaction-row")).shouldBe(visible, TIMEOUT_SEC);
        $(byCssSelector("td.account-number")).shouldHave(text(accountNumber)).find("a").click();
        $(byTagName("h3")).shouldBe(visible, TIMEOUT_SEC);
        $(byId("allBoxesSales")).click();
        $(byCssSelector("button.salesApproveButton")).click();
        checkTransaction(accountNumber, randomAmountStr, "Sales OK");

        // open Withdrawal tab and cancel transaction
        $(byId("withdrawalsManageLink")).click();
        $(byCssSelector("tr.transaction-row")).shouldHave(text(accountNumber)).shouldHave(text(randomAmountStr)).
                find("a.ng-binding").click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byCssSelector("div.tab-content")).shouldBe(visible, TIMEOUT_SEC);
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byText("Cancel")).shouldBe(visible).click();
        sleep(TIMEOUT_MSEC);
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byId("delete")).shouldBe(visible).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byId("showButton")).shouldBe(visible, TIMEOUT_SEC);

        // check that transaction has a "Failure" status
        clickTransactionsTab();
        checkTransaction(accountNumber, randomAmountStr, "Failure");
    }

    @ParameterizedTest
    @ValueSource(strings = {REGULAR, WALLET})
    void backToSales(String accountNumber) {
        String randomAmountStr = getRandomAmount();
        System.out.println(randomAmountStr);

        // login and open HVT tab
        PayapiSteps.login();
        createTransaction();

        // create a withdrawal in HVT tab
        selectWithdrawal();
        $(byId("accountNumber")).setValue(accountNumber);
        $(byTagName("h3")).click();
        $(byId("bankName")).shouldBe(visible);
        $(byCssSelector("div.paymentMethod")).click();
        $(byText("Bank - Wealthum")).click();
        $(byId("executedAmount")).setValue(randomAmountStr);
        $(byCssSelector("button.btn-success")).click();
        createTransactionsButton();
        $(byId("showButton")).shouldBe(visible, TIMEOUT_SEC);
        checkTransaction(accountNumber, randomAmountStr, "Pending");

        // make an AML approval
        $(byCssSelector("li.withdrawalsManageLink")).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byCssSelector("tr.transaction-row")).shouldBe(visible, TIMEOUT_SEC);
        $(byCssSelector("td.account-number")).shouldHave(text(accountNumber)).find("a").click();
        $(byId("amountInput")).shouldBe(visible, TIMEOUT_SEC);
        $(byId("allBoxes")).click();
        $(byCssSelector("button.amlApproveButton")).click();
        checkTransaction(accountNumber, randomAmountStr, "Accounts OK");

        // make a Sales approval
        $(byCssSelector("tr.transaction-row")).shouldBe(visible, TIMEOUT_SEC);
        $(byCssSelector("td.account-number")).shouldHave(text(accountNumber)).find("a").click();
        $(byTagName("h3")).shouldBe(visible, TIMEOUT_SEC);
        $(byId("allBoxesSales")).click();
        $(byCssSelector("button.salesApproveButton")).click();
        checkTransaction(accountNumber, randomAmountStr, "Sales OK");

        // open Withdrawal details page and make back to sales
        $(byId("withdrawalsManageLink")).click();
        $(byCssSelector("tr.transaction-row")).shouldHave(text(accountNumber)).shouldHave(text(randomAmountStr)).
                find("a.ng-binding").click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byCssSelector("div.tab-content")).shouldBe(visible, TIMEOUT_SEC);
        $(byId("backToSalesButton")).should(enabled, TIMEOUT_SEC).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byId("showButton")).shouldBe(visible, TIMEOUT_SEC);

        // check that transaction has "Accounts OK" status
        checkTransaction(accountNumber, randomAmountStr, "Accounts OK");
    }

    @ParameterizedTest
    @ValueSource(strings = {REGULAR, WALLET})
    void makeAmlApproval(String accountNumber) {
        String randomAmountStr = getRandomAmount();
        System.out.println(randomAmountStr);

        // login and open HVT tab
        login();
        createTransaction();

        // create a withdrawal in HVT tab
        selectWithdrawal();
        $(byId("accountNumber")).setValue(accountNumber);
        $(byTagName("h3")).click();
        $(byId("bankName")).shouldBe(visible);
        $(byCssSelector("div.paymentMethod")).click();
        $(byText("Bank - Wealthum")).click();
        $(byId("executedAmount")).setValue(randomAmountStr);
        $(byCssSelector("button.btn-success")).click();
        createTransactionsButton();
        $(byId("showButton")).shouldBe(visible, TIMEOUT_SEC);
        checkTransaction(accountNumber, randomAmountStr, "Pending");

        // make an AML approval
        $(byCssSelector("li.withdrawalsManageLink")).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byCssSelector("tr.transaction-row")).shouldBe(visible, TIMEOUT_SEC);
        $(byCssSelector("td.account-number")).shouldHave(text(accountNumber)).find("a").click();
        $(byId("amountInput")).shouldBe(visible, TIMEOUT_SEC);
        $(byId("allBoxes")).shouldBe(visible, TIMEOUT_SEC).click();
        $(byCssSelector("button.amlApproveButton")).shouldBe(visible, TIMEOUT_SEC).click();
        checkTransaction(accountNumber, randomAmountStr, "Accounts OK");
    }

    @ParameterizedTest
    @ValueSource(strings = {REGULAR, WALLET})
    void makeSalesApproval(String accountNumber) {
        String randomAmountStr = getRandomAmount();
        System.out.println(randomAmountStr);

        // login and open HVT tab
        login();
        createTransaction();

        // create a withdrawal in HVT tab
        selectWithdrawal();
        $(byId("accountNumber")).setValue(accountNumber);
        $(byTagName("h3")).click();
        $(byId("bankName")).shouldBe(visible);
        $(byCssSelector("div.paymentMethod")).click();
        $(byText("Bank - Wealthum")).click();
        $(byId("executedAmount")).setValue(randomAmountStr);
        $(byCssSelector("button.btn-success")).click();
        createTransactionsButton();
        $(byId("showButton")).shouldBe(visible, TIMEOUT_SEC);
        checkTransaction(accountNumber, randomAmountStr, "Pending");

        // make an AML approval
        $(byCssSelector("li.withdrawalsManageLink")).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byCssSelector("tr.transaction-row")).shouldBe(visible, TIMEOUT_SEC);
        $(byCssSelector("td.account-number")).shouldHave(text(accountNumber)).find("a").click();
        $(byId("amountInput")).shouldBe(visible, TIMEOUT_SEC);
        $(byId("allBoxes")).shouldBe(visible, TIMEOUT_SEC).click();
        $(byCssSelector("button.amlApproveButton")).shouldBe(visible, TIMEOUT_SEC).click();
        checkTransaction(accountNumber, randomAmountStr, "Accounts OK");

        // make a Sales approval
        $(byCssSelector("tr.transaction-row")).shouldBe(visible, TIMEOUT_SEC);
        $(byCssSelector("td.account-number")).shouldHave(text(accountNumber)).find("a").click();
        $(byTagName("h3")).shouldBe(visible, TIMEOUT_SEC);
        $(byId("allBoxesSales")).click();
        $(byCssSelector("button.salesApproveButton")).click();
        checkTransaction(accountNumber, randomAmountStr, "Sales OK");
    }

    @ParameterizedTest
    @ValueSource(strings = {REGULAR, WALLET})
    void makeTemplateComment(String accountNumber) {
        String randomAmountStr = getRandomAmount();
        System.out.println(randomAmountStr);

        // login and open HVT tab
        login();
        createTransaction();

        // create a withdrawal in HVT tab
        selectWithdrawal();
        $(byId("accountNumber")).setValue(accountNumber);
        $(byTagName("h3")).click();
        $(byId("bankName")).shouldBe(visible);
        $(byCssSelector("div.paymentMethod")).click();
        $(byText("Bank - Wealthum")).click();
        $(byId("executedAmount")).setValue(randomAmountStr);
        $(byCssSelector("button.btn-success")).shouldBe(visible, TIMEOUT_SEC).click();
        createTransactionsButton();
        $(byId("showButton")).shouldBe(visible, TIMEOUT_SEC);
        checkTransaction(accountNumber, randomAmountStr, "Pending");

        // open Withdrawal details page and try to make a comment
        $(byCssSelector("li.withdrawalsManageLink")).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byCssSelector("tr.transaction-row")).shouldBe(visible, TIMEOUT_SEC);
        $(byCssSelector("td.account-number")).shouldHave(text(accountNumber)).find("a").click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byId("amountInput")).shouldBe(visible, TIMEOUT_SEC);
        $(byCssSelector("div.tab-content select")).shouldBe(enabled, TIMEOUT_SEC).click();
        String noteText = $(byCssSelector("div.tab-content select option:nth-child(2)")).getText();
        $(byCssSelector("div.tab-content select option:nth-child(2)")).click();
        $(byCssSelector("div.tab-content button")).click();
        $(byCssSelector("button.addNoteButton")).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byCssSelector("td.note")).shouldHave(text(noteText));
    }

    @ParameterizedTest
    @ValueSource(strings = {REGULAR, WALLET})
    void makeCustomComment(String accountNumber) {
        String randomAmountStr = getRandomAmount();
        String testMessage = "Autotest comment";
        System.out.println(randomAmountStr);

        // login and open HVT tab
        login();
        createTransaction();

        // create a withdrawal in HVT tab
        Selenide.refresh();
        selectWithdrawal();
        $(byId("accountNumber")).setValue(accountNumber);
        $(byTagName("h3")).click();
        $(byId("bankName")).shouldBe(visible);
        $(byCssSelector("div.paymentMethod")).click();
        $(byText("Bank - Wealthum")).click();
        $(byId("executedAmount")).setValue(randomAmountStr);
        $(byCssSelector("button.btn-success")).click();
        createTransactionsButton();
        $(byId("showButton")).shouldBe(visible, TIMEOUT_SEC);
        checkTransaction(accountNumber, randomAmountStr, "Pending");

        // open Withdrawal details page and try to make a comment
        $(byCssSelector("li.withdrawalsManageLink")).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byCssSelector("tr.transaction-row")).shouldBe(visible, TIMEOUT_SEC);
        $(byCssSelector("td.account-number")).shouldHave(text(accountNumber)).find("a").click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byId("amountInput")).shouldBe(visible, TIMEOUT_SEC);
        $(byCssSelector("textarea.accountComment")).shouldBe(enabled, TIMEOUT_SEC)
                .setValue(testMessage);
        $(byCssSelector("button.addNoteButton")).click();
        $(byId("spinner")).shouldBe(hidden, TIMEOUT_SEC);
        $(byCssSelector("td.note")).shouldHave(text(testMessage));
    }
}
