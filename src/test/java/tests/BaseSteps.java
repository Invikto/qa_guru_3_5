package tests;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class BaseSteps {

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
    $$(String.format("[href*='%s']", createIssuePath)).find(visible).click();
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
  public void checkIssueAttributes(final String issueName, final String user, final String issueLabel) {
    $("[role='group']").$("[id*='issue_']:first-child")
      .shouldHave(text(issueName), text(user), text(issueLabel));
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
