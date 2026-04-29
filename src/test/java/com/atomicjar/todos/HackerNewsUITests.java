package com.atomicjar.todos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {ContainersConfig.class})
class HackerNewsUITests {

    @LocalServerPort
    private int localServerPort;
    private RemoteWebDriver driver;

    @BeforeEach
    void setUp() {
        Testcontainers.exposeHostPorts(localServerPort);

        BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>()
            .withCapabilities(new ChromeOptions());

        chrome.start();

        driver = chrome.getWebDriver();
        String baseUrl = "http://host.testcontainers.internal:" + localServerPort;
        String appUrl = baseUrl + "/index.html?" + baseUrl + "/todos";

        driver.get(appUrl);
    }

    @Test
    void getsNewsFromHackerNewsThroughUI() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement hackerNewsButton = loadPageAndGetHackerNewsButton(wait);

        hackerNewsButton.click();

        waitForTodoListToLoad(wait);

        List<WebElement> labels = driver.findElements(By.cssSelector("#todo-list li label"));
        List<String> titles = labels.stream().map(WebElement::getText).toList();

        assertThat(titles).contains(
            "WireMock has an official Testcontainers module!",
            "WireMock and AtomicJar partnership on Testcontainers",
            "State of Local Development and Testing 2023",
            "Check it out: Testcontainers for C/C++"
        );
    }

    private void waitForTodoListToLoad(WebDriverWait wait) {
        wait.until(driver1 -> {
            List<WebElement> items = driver1.findElements(By.cssSelector("#todo-list li"));
            return items.size() == 6;
        });
    }

    private WebElement loadPageAndGetHackerNewsButton(WebDriverWait wait) {
        WebElement hnButton = wait.until(
            ExpectedConditions.elementToBeClickable(By.id("read-hackernews")));
        return hnButton;
    }
}
