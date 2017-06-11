/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.paymentmgmt.integration;

import com.bank.paymentmgmt.facade.domain.Payment;
import com.bank.paymentmgmt.facade.domain.PaymentStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author GhostLeader
 */

@ApplicationScoped
public class MapPaymentDAO implements PaymentDAO{
    
    // compteur initialisé à 1
    private AtomicLong count = new AtomicLong(1);
    private Map<Long, Payment> payments = new ConcurrentHashMap<>();
    
    @Override
    public Payment add(Payment payment) {
        payment.setId(count.getAndIncrement()); // on genere un id
        payment.setStatus(PaymentStatus.VALIDATED);
        // on stock le paiement dans la Map
        payments.put(payment.getId(), payment);
        return payment;
    }

    @Override
    public Payment delete(Long id) {
        Payment deletedPayment = payments.remove(id);
        if (deletedPayment == null) {
            return null;
        }
        deletedPayment.setStatus(PaymentStatus.CANCELLED);
        return deletedPayment;
    }

    @Override
    public Payment find(Long id) {
        return payments.get(id);
    }

    @Override
    public List<Payment> getAllStoredPayments() {
        List<Payment> paymentList = new ArrayList<>(payments.values());
        // on boucle pour tracer la liste
        paymentList.forEach((p) -> {
            System.out.println(p);
        });
        return paymentList;
    }
    
}
