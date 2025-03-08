package org.acme.model;

public record GithubBranch(String name,
                           Commit commit) {

    public record Commit(String sha) {
    }
}
