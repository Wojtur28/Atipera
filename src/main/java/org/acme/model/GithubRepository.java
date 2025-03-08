package org.acme.model;

import java.util.List;

public record GithubRepository(String repositoryName,
                               String ownerLogin,
                               List<GithubBranch> branches) {

}


