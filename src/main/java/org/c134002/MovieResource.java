package org.c134002;

import com.oracle.svm.core.annotate.Delete;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.annotations.Pos;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Path("/movies")
@Tag(name = "Movie Resource",description = "MOVIE Rest APIs")
public class MovieResource {
    public static List<Movie> movies = new ArrayList<>();
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "getMovies",
            summary = "Get Movies",
            description = "Get all movies in the list"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation Complete",
            content =  @Content(mediaType = MediaType.APPLICATION_JSON)
    )

    public Response getMovie(){
        return Response.ok(movies).build();

    }
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/size")
    @Operation(
            operationId = "countMovies",
            summary = "Count Movies",
            description = "Size of the list of all movies"
    )
    @APIResponse(
            responseCode = "200",
            description = "Operation Complete",
            content =  @Content(mediaType = MediaType.TEXT_PLAIN)
    )
    public Integer countMovies(){
        return movies.size();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "createMovie",
            summary = "Create Movie",
            description = "Create entries of movies  to add in the list"
    )
    @APIResponse(
            responseCode = "201",
            description = "Movie Created",
            content =  @Content(mediaType = MediaType.APPLICATION_JSON)
    )

    public Response createMovie(
            @RequestBody(
                    description = "Movie to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Movie.class))

                            
                                        )

            Movie newMovie) {
        movies.add(newMovie);
        return Response.status(Response.Status.CREATED).entity(movies).build();
    }

    @PUT
    @Path("{id}/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "updateMovie",
            summary = "Update an existing movie",
            description = "Update existing movie in the list"
    )
    @APIResponse(
            responseCode = "200",
            description = "Movie Updated",
            content =  @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response updateMovie(
            @Parameter(
                    description = "Movie Id",
                    required = true
            )
            @PathParam("id")  Long id,

            @Parameter(
                    description = "Movie Title",
                    required = true
            )
            @PathParam("title") String title){
//            @QueryParam("movie") String updateMovie) {
        movies = movies.stream().map(movie -> {
            if (movie.getId().equals(id)) {
                movie.setTitle(title);
            }
                return movie;

            }).collect(Collectors.toList());
        return  Response.ok(movies).build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "deleteMovie",
            summary = "Delete an existing movie",
            description = "Delete existing movie in the list"
    )
    @APIResponse(
            responseCode = "204",
            description = "Movie delete",
            content =  @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Movie not valid",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response deleteMovie (
            @PathParam("id") Long id){
       Optional <Movie> movieToDelete = movies.stream().filter(movies -> movies.getId().equals(id))
                .findFirst();
       boolean removed = false;
        if (movieToDelete.isPresent()) {
            removed = movies.remove(movieToDelete.get());
        }
        if (removed) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
