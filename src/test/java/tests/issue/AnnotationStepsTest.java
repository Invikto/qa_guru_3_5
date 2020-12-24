package tests.issue;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import tests.BaseSteps;
import utils.PropertiesReader;

public class AnnotationStepsTest {

  private static final String
    GITHUB_URI = "https://github.com",
    LOGIN_PATH = "/login",
    USER = new PropertiesReader("credentials").getProperty("user"),
    PASSWORD = new PropertiesReader("credentials").getProperty("password"),
    CREATE_ISSUE_PATH = "/issues/new",
    ISSUE_NAME = "Test issue",
    ISSUE_LABEL = "good first issue";

  static final BaseSteps steps = new BaseSteps();

  @BeforeEach
  public void logIn() {
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
    steps.removeIssue();
  }

  @AfterAll
  static void logOut() {
    steps.logOut();
  }

}
