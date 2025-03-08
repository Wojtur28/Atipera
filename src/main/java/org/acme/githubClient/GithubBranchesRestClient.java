package org.acme.githubClient;


import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.model.GithubBranch;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/repos/{owner}/{repo}/branches")
@RegisterRestClient(configKey = "github-rest-client")
public interface GithubBranchesRestClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Uni<List<GithubBranch>> getBranches(@PathParam("owner") String owner, @PathParam("repo") String repo);
}
