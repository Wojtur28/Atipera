package org.acme.githubClient.dto;

public record GithubRepositoryResponse(String name,
                                       OwnerResponse owner,
                                       boolean fork) {
    public record OwnerResponse(
            String login
    ) { }
}

