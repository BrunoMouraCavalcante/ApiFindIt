package com.findit.dota2api;

import com.google.gson.Gson;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/dota2api/player")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class ApiDota2 {

    private static String API_OPENDOTA = "https://api.opendota.com/api/";

    /**
     * GET /api/dota2api/player/:id : get the resource specified by the identifier.
     *
     * @param id id of dota 2 account
     * @return the {@code Resource} with status 200 (OK) and body or status 404 (NOT FOUND)
     */
    @javax.ws.rs.GET
    @Path("{id: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayer(@PathParam("id") Long id) {
        ResponseBody response = null;
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_OPENDOTA)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiInterface service = retrofit.create(ApiInterface.class);
            Call<ResponseBody> call = service.getPlayers(id);
            response  = call.execute().body();
            Gson gson = new Gson();
            String responseGson = gson.toJson(response.string());
            return Response.ok(responseGson,MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    /**
     * GET /api/dota2api/player/:id/wl : get the resource specified by the identifier.
     *
     * @param id id of dota 2 account
     * @return the {@code Resource} with status 200 (OK) and body or status 404 (NOT FOUND)
     */
    @javax.ws.rs.GET
    @Path("{id: [0-9]+}/wl")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayerWl(@PathParam("id") Long id) {
        ResponseBody response = null;
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_OPENDOTA)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiInterface service = retrofit.create(ApiInterface.class);
            Call<ResponseBody> call = service.getPlayersWl(id);
            response  = call.execute().body();
            Gson gson = new Gson();
            String responseGson = gson.toJson(response.string());
            return Response.ok(responseGson,MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    /**
     * GET /api/dota2api/player/:id/heroes : get the resource specified by the identifier.
     *
     * @param id id of dota 2 account
     * @return the {@code Resource} with status 200 (OK) and body or status 404 (NOT FOUND)
     */
    @javax.ws.rs.GET
    @Path("{id: [0-9]+}/heroes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayerHeroes(@PathParam("id") Long id) {
        ResponseBody response = null;
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_OPENDOTA)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiInterface service = retrofit.create(ApiInterface.class);
            Call<ResponseBody> call = service.getPlayersHeroes(id);
            response  = call.execute().body();
            Gson gson = new Gson();
            String responseGson = gson.toJson(response.string());
            return Response.ok(responseGson,MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    public interface ApiInterface {
        @GET("players/{id}")
        Call<ResponseBody> getPlayers(@retrofit2.http.Path("id") Long id);

        @GET("players/{id}/wl")
        Call<ResponseBody> getPlayersWl(@retrofit2.http.Path("id")Long id);

        @GET("players/{id}/heroes")
        Call<ResponseBody> getPlayersHeroes(@retrofit2.http.Path("id")Long id);
    }
}
