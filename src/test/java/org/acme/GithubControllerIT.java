package org.acme;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;

@QuarkusTest
@QuarkusTestResource(WireMockTestResource.class)
public class GithubControllerIT {

    @Test
    public void testHappyPath() {
        configureFor(WireMockTestResource.getHost(), WireMockTestResource.getPort());
        stubFor(get(urlEqualTo("/users/owner/repos"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("["
                                + "{\"name\":\"repo1\",\"fork\":false,\"owner\":{\"login\":\"owner\"}},"
                                + "{\"name\":\"repo2\",\"fork\":true,\"owner\":{\"login\":\"owner\"}}"
                                + "]")));
        stubFor(get(urlEqualTo("/repos/owner/repo1/branches"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"name\":\"master\",\"commit\":{\"sha\":\"abc123\"}}]")));

        given()
                .pathParam("owner", "owner")
                .when().get("/github/{owner}")
                .then()
                .statusCode(200)
                .body("$.size()", is(1))
                .body("[0].repositoryName", equalTo("repo1"))
                .body("[0].ownerLogin", equalTo("owner"))
                .body("[0].branches[0].name", equalTo("master"))
                .body("[0].branches[0].commit.sha", equalTo("abc123"));

        verify(1, getRequestedFor(urlEqualTo("/users/owner/repos")));
        verify(1, getRequestedFor(urlEqualTo("/repos/owner/repo1/branches")));
    }
}
