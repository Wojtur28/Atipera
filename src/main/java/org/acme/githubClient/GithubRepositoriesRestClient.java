package org.acme.githubClient;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.githubClient.dto.GithubRepositoryResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/users/{owner}/repos")
@RegisterRestClient(configKey = "github-rest-client")
public interface GithubRepositoriesRestClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Uni<List<GithubRepositoryResponse>> getRepositories(@PathParam("owner") String owner);
}
