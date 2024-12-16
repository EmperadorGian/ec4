package com.idat.restserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idat.restserver.entity.Mercancia;
import com.idat.restserver.repository.MercanciaRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Path("/mercancia")
public class MercanciaController {

    @Autowired
    private MercanciaRepository mercanciaRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMercancias() {
        try {
            List<Mercancia> mercancias = mercanciaRepository.findAll();
            String json = objectMapper.writeValueAsString(mercancias);
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al convertir a JSON")
                    .build();
        }
    }

    @GET
    @Path("/{nombre}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMercanciaByName(@PathParam("nombre") String nombre) {
        Mercancia mercancia = mercanciaRepository.findByNombre(nombre);
        if (mercancia != null) {
            try {
                String json = objectMapper.writeValueAsString(mercancia);
                return Response.ok(json, MediaType.APPLICATION_JSON).build();
            } catch (JsonProcessingException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Error al convertir a JSON")
                        .build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\":\"Mercancia no encontrada\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMercancia(@PathParam("id") Long id, String json) {
        try {
            Mercancia updateMercancia = objectMapper.readValue(json, Mercancia.class);
            Mercancia mercancia = mercanciaRepository.findById(id).orElse(null);
            if (mercancia != null) {
                mercancia.setNombre(updateMercancia.getNombre());
                mercancia.setStock(updateMercancia.getStock());
                mercancia.setPrecio(updateMercancia.getPrecio());
                mercanciaRepository.save(mercancia);
                String responseMessage = "{\"message\":\"Mercancia actualizada correctamente\"}";
                return Response.status(Response.Status.OK)
                        .entity(responseMessage)
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity("Mercancia no encontrada").build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al procesar la solicitud")
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMercancia(String json) {
        try {
            Mercancia mercancia = objectMapper.readValue(json, Mercancia.class);
            mercanciaRepository.save(mercancia);
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\":\"Mercancia creada correctamente\"}")
                    .build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al procesar la solicitud")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMercancia(@PathParam("id") Long id) {
        Mercancia mercancia = mercanciaRepository.findById(id).orElse(null);
        if (mercancia != null) {
            mercanciaRepository.delete(mercancia);
            return Response.status(Response.Status.OK)
                    .entity("{\"message\":\"Mercancia eliminada correctamente\"}")
                    .build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\":\"Mercancia no encontrada\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
