/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.paymentmgmt.facade;

import com.bank.paymentmgmt.facade.domain.Payment;
import com.bank.paymentmgmt.integration.PaymentDAO;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;

/**
 *
 * @author Michel Kansou
 */

@Stateless
@WebService(
        endpointInterface = "com.bank.paymentmgmt.facade.BankingServiceEndpointInterface",
        portName = "BankingPort",
        serviceName = "BankingService"
)
public class BankingServiceBean implements BankingServiceEndpointInterface, BankingServiceRemote {

    @Inject
    private PaymentDAO paymentDAO;
    
    @Override
    public Boolean createPayment(String ccNumber, Double amount) {
        if(ccNumber.length() > 10) {
            System.out.println("Montant payé: " + amount);
            Payment p = new Payment();
            p.setCcNumber(ccNumber);
            p.setAmount(amount);
            
            // add payment 
            p = paymentDAO.add(p);
            paymentDAO.getAllStoredPayments();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Payment> lookupAllStoredPayments() {
        return paymentDAO.getAllStoredPayments();
    }

    @Override
    public Payment lookupStoredPayment(Long id) {
        return paymentDAO.find(id);
    }

    @Override
    public Payment deleteStoredPayment(Long id) {
        return paymentDAO.delete(id);
    }
}
