package com.example.playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlaywrightTests {
    String externalUrl = "https://www.kimmoahola.net/playwright.html";

    Playwright playwright;
    Browser browser;
    BrowserContext browserContext;
    Page page;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();

        boolean headLess = System.getenv("HEADLESS") != null; // toggle headless in CI environment

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(headLess)
                        .setSlowMo(headLess ? 0 : 2000) // 2 second delay so you can view it locally
                        .setArgs(List.of("--start-maximized"))

        );

        browserContext = browser.newContext(
                new Browser.NewContextOptions()
                        .setViewportSize(null)
        );
        page = browserContext.newPage();
        page.navigate(externalUrl);
    }

    @AfterEach
    void tearDown() {
        browser.close();
        playwright.close();
    }

    // 1. Grab the heading with ID page-title and print its text
    @Test
    void shouldReadPageTitleHeading() {
        Locator heading = page.locator("#page-title");
        System.out.println("Heading text: " + heading.textContent());
        assertThat(heading).hasText("Playwright Test Page");
    }

    // 2. Fill the input with placeholder Email with a test email
    @Test
    void shouldFillInEmailInInput() {
        Locator email = page.locator("input[placeholder='Email']");
        email.fill("testemail@test.com");
        assertThat(email).hasValue("testemail@test.com");
    }

    // 3. Click the button inside the card with class btn-green
    @Test
    void shouldClickGreenButton() {
        Locator button = page.locator("button.btn-green");
        button.click();
        assertThat(button).isVisible();
    }

    // 4. Select the checkbox labeled Accept Terms
    @Test
    void shouldCheckAcceptTermsCheckbox() {
        Locator checkbox = page.locator("input[name='terms']");
        checkbox.check();
        assertThat(checkbox).isChecked();
    }

    // 5. Choose the radio button for Option B from the group
    @Test
    void shouldSelectOptionBRadio() {
        Locator optionB = page.locator("input[name='options'][value='B']");
        optionB.check();
        assertThat(optionB).isChecked();
    }

    // 6. Pick "red" from the dropdown with ID color-select
    @Test
    void shouldSelectGreenFromDropdown() {
        Locator dropdown = page.locator("#color-select");
        dropdown.selectOption("red");
        assertThat(dropdown).hasValue("red");
    }

    // 7. Get the text of all list items with class feature
    @Test
    void shouldGetAllFeatureItems() {
        List<String> features = page.locator("li.feature").allTextContents();
        features.forEach(System.out::println);
        assertEquals(3, features.size());
        assertTrue(features.contains("Fast performance"));
    }

    // 8. Print the alt attribute of the image with ID logo
    @Test
    void shouldReadLogoAlt() {
        Locator logo = page.locator("#logo");
        String alt = logo.getAttribute("alt");
        System.out.println("Logo alt: " + alt);
        assertEquals("Playwright Logo", alt);
    }

    // 9. Click the link with text Learn more without navigating away
    @Test
    void shouldClickLearnMoreLink() {
        String urlBefore = page.url();
        page.locator("#learn-link").click();
        // href="#" just appends a hash, so the base URL stays the same
        assertTrue(page.url().contains(urlBefore));
    }

    // 10. Retrieve text from the nested span inside .outer-div .inner-div
    @Test
    void shouldReadNestedSpanText() {
        Locator span = page.locator(".outer-div .inner-div #nested-span");
        System.out.println("Nested span: " + span.textContent());
        assertThat(span).hasText("Nested Text");
    }

    @Test
    @Tag("smoke")
    void bodyShouldNotBeEmpty() {
        Locator heading = page.locator("#page-title");
        System.out.println("Heading text: " + heading.textContent());
        assertThat(heading).hasText("Playwright Test Page");
    }
}
