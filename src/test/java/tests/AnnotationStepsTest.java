package tests;

import com.codeborne.selenide.Condition;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import utils.PropertiesReader;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class AnnotationStepsTest {

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
    final BaseSteps steps = new BaseSteps();

    steps.openStartPage(GITHUB_URI);
    steps.logIn(LOGIN_PATH, USER, PASSWORD);
  }

  @Test
  @DisplayName("Annotation Steps")
  @Feature("Create an issue")
  @Story("An authorized user can create an issue, add a label and assign it.")
  @Link(url = "https://github.com", name = "GitHub")
  @Owner("kuznetsov")
  public void createIssue() {
    final BaseSteps steps = new BaseSteps();

    steps.openRepoPage(USER);
    steps.openCreateIssuePage(CREATE_ISSUE_PATH);
    steps.addIssueName(ISSUE_NAME);
    steps.assignIssue(USER);
    steps.addIssueLabel(ISSUE_LABEL);
    steps.clickCreateIssueButton();
    steps.openIssuesListPage();
    steps.checkIssueAttributes(ISSUE_NAME, USER, ISSUE_LABEL);
    steps.openIssueDetailsPage();
  }

  @AfterEach
  public void removeIssue() {
    final BaseSteps steps = new BaseSteps();

    steps.removeIssue();
  }

  @AfterAll
  static void logOut() {
    final BaseSteps steps = new BaseSteps();

    steps.logOut();
  }

  public static class BaseSteps {

    @Step("Open GitHub.")
    public void openStartPage(final String mainUri) {
      open(mainUri);
    }

    @Step("Log in the account.")
    public void logIn(final String loginPath, final String user, final String password) {
      $(String.format("[href='%s']", loginPath)).click();
      $("#login_field").setValue(user);
      $("#password").setValue(password);
      $("[type='submit']").click();
    }

    @Step("Go to the page of any current user's repository.")
    public void openRepoPage(final String user) {
      $(String.format("#repos-container [href*='%s']", user)).click();
    }

    @Step("Go to the Create Issue page.")
    public void openCreateIssuePage(final String createIssuePath) {
      $("nav[aria-label='Repository']").$("[data-content='Issues']").click();
      $$(String.format("[href*='%s']", createIssuePath)).find(Condition.visible).click();
    }

    @Step("Entry the issue's name.")
    public void addIssueName(final String issueName) {
      $("#issue_title").setValue(issueName);
    }

    @Step("Assign the issue to the current user.")
    public void assignIssue(final String user) {
      $("#assignees-select-menu").$("summary").click();
      $("#assignees-select-menu").$(byText(user)).closest("label").click();
      $("#assignees-select-menu").$("summary").click();
    }

    @Step("Apply a label to the issue.")
    public void addIssueLabel(final String issueLabel) {
      $("#labels-select-menu").$("summary").click();
      $("#labels-select-menu").$(byText(issueLabel)).closest("label").click();
      $("#labels-select-menu").$("summary").click();
    }

    @Step("Confirm issue creation.")
    public void clickCreateIssueButton() {
      $("#new_issue [type='submit']").click();
    }

    @Step("Go to the page with a list of issues.")
    public void openIssuesListPage() {
      $("nav[aria-label='Repository']").$("[data-content='Issues']").click();
    }

    @Step("Check that the issue has all entered attributes.")
    public void checkIssueAttributes(final String  issueName, final String  user, final String issueLabel) {
      $("[role='group']").$("[id*='issue_']:first-child").shouldHave(Condition.text(issueName));
      $("[role='group']").$("[id*='issue_']:first-child").shouldHave(Condition.text(user));
      $("[role='group']").$("[id*='issue_']:first-child").shouldHave(Condition.text(issueLabel));
    }

    @Step("Go to the Issue Details page.")
    public void openIssueDetailsPage() {
      $("[role='group']").$("[id*='issue_']:first-child").$("a").click();
    }

    @Step("Remove the issue.")
    public void removeIssue() {
      $("#partial-discussion-sidebar").$("[aria-labelledby='delete-issue-dialog-title']").preceding(0).click();
      $("[aria-labelledby='delete-issue-dialog-title']").$("button[type='submit']").click();
    }

    @Step("Log out of the current account.")
    public void logOut() {
      $("[aria-label='View profile and more']").click();
      $(".logout-form").$("[type='submit']").click();
    }

  }

}
