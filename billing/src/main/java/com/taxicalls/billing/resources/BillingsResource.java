package com.taxicalls.billing.resources;

import com.taxicalls.billing.model.Billing;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/billings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class BillingsResource {

    private final EntityManager em;

    public BillingsResource() {
        Map<String, String> env = System.getenv();
        Map<String, Object> configOverrides = new HashMap<>();
        env.keySet().forEach((envName) -> {
            if (envName.contains("DATABASE_USER")) {
                configOverrides.put("javax.persistence.jdbc.user", env.get(envName));
            } else if (envName.contains("DATABASE_PASS")) {
                configOverrides.put("javax.persistence.jdbc.password", env.get(envName));
            }
        });
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("billing", configOverrides);
        this.em = emf.createEntityManager();
    }

    @POST
    public Response createBilling(Billing billing) {
        em.getTransaction().begin();
        em.persist(billing);
        em.getTransaction().commit();
        return Response.status(Response.Status.CREATED).entity(billing).build();
    }

    @GET
    public Response getBillings() {
        List<Billing> billings = em.createNamedQuery("Billing.findAll", Billing.class).getResultList();
        return Response.ok(billings).build();
    }

    @GET
    @Path("/{id}")
    public Response getBilling(@PathParam("id") Integer id) {
        Billing billing = em.find(Billing.class, id);
        if (billing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(billing).build();
    }
}
