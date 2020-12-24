package tests.issue;

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

public class CleanSelenideTest {

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
    open(GITHUB_URI);
    $(String.format("[href='%s']", LOGIN_PATH)).click();
    $("#login_field").setValue(USER);
    $("#password").setValue(PASSWORD);
    $("[type='submit']").click();
  }

  @Test
  @DisplayName("Clean Selenide")
  @Feature("Create an issue")
  @Story("An authorized user can create an issue, add a label and assign it.")
  @Link(url = "https://github.com", name = "GitHub")
  @Owner("kuznetsov")
  public void createIssue() {
    $(String.format("#repos-container [href*='%s']", USER)).click();
    $("nav[aria-label='Repository']").$("[data-content='Issues']").click();
    $$(String.format("[href*='%s']", CREATE_ISSUE_PATH)).find(visible).click();
    $("#issue_title").setValue(ISSUE_NAME);
    $("#assignees-select-menu").$("summary").click();
    $("#assignees-select-menu").$(byText(USER)).closest("label").click();
    $("#assignees-select-menu").$("summary").click();
    $("#labels-select-menu").$("summary").click();
    $("#labels-select-menu").$(byText(ISSUE_LABEL)).closest("label").click();
    $("#labels-select-menu").$("summary").click();
    $("#new_issue [type='submit']").click();

    $("nav[aria-label='Repository']").$("[data-content='Issues']").click();
    $("[role='group']").$("[id*='issue_']:first-child")
      .shouldHave(text(ISSUE_NAME), text(USER), text(ISSUE_LABEL));
  }

  @AfterEach
  public void removeIssue() {
    $("[role='group']").$("[id*='issue_']:first-child").$("a").click();
    $("#partial-discussion-sidebar").$("[aria-labelledby='delete-issue-dialog-title']").preceding(0).click();
    $("[aria-labelledby='delete-issue-dialog-title']").$("button[type='submit']").click();
  }

  @AfterAll
  static void logout() {
    $("[aria-label='View profile and more']").click();
    $(".logout-form").$("[type='submit']").click();
  }

}
