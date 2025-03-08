package org.acme;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.HashMap;
import java.util.Map;

public class WireMockTestResource implements QuarkusTestResourceLifecycleManager {
    private static final GenericContainer<?> wiremock = new GenericContainer<>("wiremock/wiremock:2.27.2")
            .withExposedPorts(8080)
            .waitingFor(Wait.forHttp("/__admin/"));

    private static String host;
    private static int port;

    @Override
    public Map<String, String> start() {
        wiremock.start();
        host = wiremock.getHost();
        port = wiremock.getMappedPort(8080);
        String wiremockUrl = "http://" + host + ":" + port;
        Map<String, String> props = new HashMap<>();
        props.put("quarkus.rest-client.github-rest-client.url", wiremockUrl);
        return props;
    }

    @Override
    public void stop() {
        wiremock.stop();
    }

    public static String getHost() {
        return host;
    }

    public static int getPort() {
        return port;
    }
}
