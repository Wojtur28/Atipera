package org.acme;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.acme.model.ErrorResponse;
import org.acme.model.GithubException;


@Path("/github")
public class GithubController {

    @Inject
    GithubService githubService;

    @GET
    @Path("/{owner}")
    public Uni<Response> getRepositories(@PathParam("owner") String owner) {
        return githubService.getRepositories(owner)
                .collect().asList()
                .onItem().transform(list -> Response.ok(list).build())
                .onFailure().recoverWithItem(e -> {
                    if (e instanceof GithubException ge) {
                        return Response.status(ge.getStatusCode())
                                .entity(new ErrorResponse(ge.getStatusCode(), ge.getMessage()))
                                .build();
                    }
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(new ErrorResponse(500, e.getMessage()))
                            .build();
                });
    }
}
