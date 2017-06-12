/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.paymentmgmt.facade;

import com.bank.paymentmgmt.facade.domain.Payment;
import java.io.StringReader;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author GhostLeader
 */
@Path("payment")
@RequestScoped
public class PaymentResource {

    @EJB(lookup = "java:global/bankFacade-ejb/BankingServiceBean")
    private BankingServiceRemote bankingService;
    
    /**
     * Creates a new instance of PaymentResource
     */
    public PaymentResource() {
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response pay(String content) {
        
        StringReader reader = new StringReader(content);
        String ccNumber;
        Double amount;
        
        try (JsonReader jreader = Json.createReader(reader)) {
            JsonObject paymentInfo = jreader.readObject();
            ccNumber = paymentInfo.getString("ccNumber");
            amount = paymentInfo.getJsonNumber("amount").doubleValue();
        }
        
        Boolean isValid = bankingService.createPayment(ccNumber, amount);
        
        if (isValid) {
             return Response.accepted().build();
        } else {
            return Response.status(400).entity("n CB invalide").build();
        }
    }
    
    @Path("payments")
    @GET
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Response getStoredPayments(){
        //récupération de tous les ordres de paiement stockés
        List<Payment> storedPayments = bankingService.lookupAllStoredPayments();
        //création d'une entité générique pour pouvoir mapper un type paramétré (List<Payment>) avec //un corps de réponse
        GenericEntity<List<Payment>> genericList = new GenericEntity<List<Payment>>(storedPayments){};
        //construction de la réponse embarquant dans son corps les ordres de paiements
        Response resp = Response.ok(genericList).build();
        return resp;
    }
    
    @Path("{id}")
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getStoredPayment(@PathParam("id") Long paymentId) {
        //@PathParam permet //d'extraire la valeur du template parameter
        
        Payment storedPayment = bankingService.lookupStoredPayment(paymentId);
        
        if (storedPayment == null) {
            throw new NotFoundException();
        }
        return Response.ok(storedPayment).build();
    }
    
    @Path("{id}")
    @DELETE
    public Response cancelStoredPayment(@PathParam("id") Long paymentId){
        Payment cancelledPayment = bankingService.deleteStoredPayment(paymentId);
        
        if (cancelledPayment == null) {
            throw new NotFoundException();
        }
        
        return Response.noContent().build();
    } 

}
