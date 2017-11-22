package com.findit;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

@Path("/api/v1.0/resources")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class ResourceService {
    private static List<Resources> resources = null;

    static {
        resources = new ArrayList<>();
        resources.add(new Resources(1L, "Resource One", LocalDateTime.now(), null));
        resources.add(new Resources(2L, "Resource Two", LocalDateTime.now(), null));
        resources.add(new Resources(3L, "Resource Three", LocalDateTime.now(), null));
        resources.add(new Resources(4L, "Resource Four", LocalDateTime.now(), null));
        resources.add(new Resources(5L, "Resource Five", LocalDateTime.now(), null));
        resources.add(new Resources(6L, "Resource Six", LocalDateTime.now(), null));
        resources.add(new Resources(7L, "Resource Seven", LocalDateTime.now(), null));
        resources.add(new Resources(8L, "Resource Eight", LocalDateTime.now(), null));
        resources.add(new Resources(9L, "Resource Nine", LocalDateTime.now(), null));
        resources.add(new Resources(10L, "Resource Ten", LocalDateTime.now(), null));
    }

    /**
     * GET  /api/v1.0/resources : get all resources.
     *
     * @return the {@code List<Resource>} of resources with status code 200 (OK)
     */
    @GET
    public List<Resources> getResources() {
        return resources;
    }

    /**
     * GET /api/v1.0/resources/:id : get the resource specified by the identifier.
     *
     * @param id the id to the resource being looked up
     * @return the {@code Resource} with status 200 (OK) and body or status 404 (NOT FOUND)
     */
    @GET
    @Path("{id: [0-9]+}")
    public Resources getResource(@PathParam("id") Long id) {
        Resources resource = new Resources(id, null, null, null);

        // Search the Resource list for a resource with the given id and return its index in the list
        int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resources::getId));

        if (index >= 0)
            return resources.get(index);
        else
            throw new WebApplicationException(Response.Status.NOT_FOUND);
    }

    /**
     * POST /api/v1.0/resources : creates a new resource.
     *
     * @param resource the resource being sent by the client as payload
     * @return the {@code Resource} with status 201 (CREATED) and no -content or status
     *         400 (BAD REQUEST) if the resource does not contain an Id or status
     *         409 (CONFLICT) if the resource being created already exists in the list
     */
    @POST
    public Response createResource(Resources resource) {
        if (Objects.isNull(resource.getId()))
            throw new WebApplicationException(Response.Status.BAD_REQUEST);

        int index = Collections.binarySearch(resources, resource, Comparator.comparing(Resources::getId));

        if (index < 0) {
            resource.setCreatedTime(LocalDateTime.now());
            resources.add(resource);
            return Response
                    .status(Response.Status.CREATED)
                    .location(URI.create(String.format("/api/v1.0/resources/%s", resource.getId())))
                    .build();
        } else
            throw new WebApplicationException(Response.Status.CONFLICT);
    }
}
