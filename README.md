# GitHub API Client with Quarkus

## Endpoints
```http
GET /github/{owner}
```
- Description:
  Retrieves all non-fork repositories for a given GitHub owner along with their branch details.
- Success Response:
  A JSON array of repository objects containing:
    - repositoryName
    - ownerLogin
    - branches (each branch includes its name and last commit sha)
- Error Scenarios:
  - 404 Not Found: If the GitHub user is not found.
  - 429 Too Many Requests: If the GitHub API rate limit is exceeded.
  - 500 Internal Server Error: For any other unexpected errors.

## Use Cases & Scenarios

- Happy Path:
  When a valid GitHub owner exists and has non-fork repositories, the endpoint returns detailed repository and branch data.
- User Not Found:
  The API responds with a 404 error if the specified user does not exist.
- Rate Limiting:
  The API responds with a 429 error when GitHub's API rate limit is exceeded.
- Error Handling:
  Other errors result in a 500 error with an appropriate message.

## Running Tests

This project uses Testcontainers for integration testing. The tests start a WireMock container to simulate GitHub API responses, ensuring isolated and consistent test runs.

**Important❗️: Docker must be running on your system for the tests to work, as the test setup relies on starting Docker containers.**

## Build and Test

Run the following command to build the project and execute tests:

    ./gradlew quarkusBuild

This command builds the project and runs tests that leverage the Quarkus testing framework along with Testcontainers.

## Additional Notes

- WireMock Integration:
  A custom test resource (WireMockTestResource) is used to dynamically start a WireMock container. This container intercepts REST calls to simulate GitHub responses during tests.
- Dynamic Configuration:
  The application properties for the GitHub REST client are overridden at test runtime with the dynamically mapped WireMock URL.
