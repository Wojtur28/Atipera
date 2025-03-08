package org.acme;

import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import org.acme.githubClient.GithubBranchesRestClient;
import org.acme.githubClient.GithubRepositoriesRestClient;
import org.acme.model.GithubException;
import org.acme.model.GithubRepository;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class GithubService {

    @RestClient
    GithubRepositoriesRestClient githubRepositoriesRestClient;

    @RestClient
    GithubBranchesRestClient githubBranchesRestClient;

    public Multi<GithubRepository> getRepositories(String owner) {
        return githubRepositoriesRestClient.getRepositories(owner)
                .onItem().transformToMulti(list -> Multi.createFrom().iterable(list))
                .filter(repo -> !repo.fork())
                .onItem().transformToUni(repo ->
                        githubBranchesRestClient.getBranches(owner, repo.name())
                                .onItem().transform(branches ->
                                        new GithubRepository(repo.name(), repo.owner().login(), branches)
                                )
                )
                .merge()
                .onFailure().recoverWithMulti(e -> {
                    if (e instanceof WebApplicationException) {
                        int status = ((WebApplicationException) e).getResponse().getStatus();
                        if (status == 404) {
                            return Multi.createFrom().failure(new GithubException(404, "User not found in github"));
                        } else if (status == 403) {
                            return Multi.createFrom().failure(new GithubException(429, "Rate limit exceeded"));
                        }
                    }
                    return Multi.createFrom().failure(new GithubException(500, e.getMessage()));
                });
    }
}
