/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.paymentmgmt.facade;

import com.bank.paymentmgmt.facade.domain.Payment;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author GhostLeader
 */

@Remote
public interface BankingServiceRemote extends BankingServiceEndpointInterface {
    
    // récupère tous les paiement stockés dans la Map du DAO.
    List<Payment> lookupAllStoredPayments();
    
    // récupère un paiement en fonction de son identité.
    Payment lookupStoredPayment(Long id);
    
    // supprime de la Map un paiement.
    Payment deleteStoredPayment(Long id);
}
