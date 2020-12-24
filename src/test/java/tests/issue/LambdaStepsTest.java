package tests.issue;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import utils.PropertiesReader;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

public class LambdaStepsTest {

  private static final String
    GITHUB_URI = "https://github.com",
    LOGIN_PATH = "/login",
    USER = new PropertiesReader("credentials").getProperty("user"),
    PASSWORD = new PropertiesReader("credentials").getProperty("password"),
    CREATE_ISSUE_PATH = "/issues/new",
    ISSUE_NAME = "Test issue",
    ISSUE_LABEL = "good first issue";

  @BeforeEach
  public void logIn() {
    step("Open GitHub.", () -> {
      open(GITHUB_URI);
    });

    step("Log in the account.", () -> {
      $(String.format("[href='%s']", LOGIN_PATH)).click();
      $("#login_field").setValue(USER);
      $("#password").setValue(PASSWORD);
      $("[type='submit']").click();
    });
  }

  @Test
  @DisplayName("Lambda Steps")
  @Feature("Create an issue")
  @Story("An authorized user can create an issue, add a label and assign it.")
  @Link(url = "https://github.com", name = "GitHub")
  @Owner("kuznetsov")
  public void createIssue() {
    step("Go to the page of any current user's repository.", () -> {
      $(String.format("#repos-container [href*='%s']", USER)).click();
    });

    step("Go to the Create Issue page.", () -> {
      $("nav[aria-label='Repository']").$("[data-content='Issues']").click();
      $$(String.format("[href*='%s']", CREATE_ISSUE_PATH)).find(visible).click();
    });

    step("Entry the issue's name.", () -> {
      $("#issue_title").setValue(ISSUE_NAME);
    });

    step("Assign the issue to the current user.", () -> {
      $("#assignees-select-menu").$("summary").click();
      $("#assignees-select-menu").$(byText(USER)).closest("label").click();
      $("#assignees-select-menu").$("summary").click();
    });

    step("Apply a label to the issue.", () -> {
      $("#labels-select-menu").$("summary").click();
      $("#labels-select-menu").$(byText(ISSUE_LABEL)).closest("label").click();
      $("#labels-select-menu").$("summary").click();
    });

    step("Confirm issue creation.", () -> {
      $("#new_issue [type='submit']").click();
    });

    step("Go to the page with a list of issues.", () -> {
      $("nav[aria-label='Repository']").$("[data-content='Issues']").click();
    });

    step("Check that the issue has all entered attributes.", () -> {
      $("[role='group']").$("[id*='issue_']:first-child")
        .shouldHave(text(ISSUE_NAME), text(USER), text(ISSUE_LABEL));
    });
  }

  @AfterEach
  public void removeIssue() {
    step("Go to the Issue Details page.", () -> {
      $("[role='group']").$("[id*='issue_']:first-child").$("a").click();
    });

    step("Remove the issue.", () -> {
      $("#partial-discussion-sidebar").$("[aria-labelledby='delete-issue-dialog-title']").preceding(0).click();
      $("[aria-labelledby='delete-issue-dialog-title']").$("button[type='submit']").click();
    });
  }

  @AfterAll
  static void logOut() {
    step("Log out of the current account.", () -> {
      $("[aria-label='View profile and more']").click();
      $(".logout-form").$("[type='submit']").click();
    });
  }

}
