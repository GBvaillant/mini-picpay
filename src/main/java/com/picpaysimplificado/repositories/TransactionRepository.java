package com.picpaysimplificado.repositories;

import com.picpaysimplificado.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}