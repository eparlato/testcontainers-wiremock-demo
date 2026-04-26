package com.atomicjar.todos.hn;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

@Testcontainers
class HackerNewsClientIntegrationTests {

    @Container
    static WireMockContainer wireMock = new WireMockContainer("wiremock/wiremock:3.1.0")
        .withMappingFromResource("hackernews", "hackernews_v0-stubs.json");

    @Test
    void shouldFetchItem() {
        HackerNewsClient client = new HackerNewsClient(wireMock.getBaseUrl());

        // TODO complete
    }

    // TODO should fetch top stories id

    // TODO error scenarios to be tested: 500, 404, empty list, malformed JSON
}
