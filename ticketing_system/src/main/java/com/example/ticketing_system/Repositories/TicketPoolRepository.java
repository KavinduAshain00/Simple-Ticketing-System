package com.example.ticketing_system.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ticketing_system.model.TicketPool;

@Repository
public interface TicketPoolRepository extends JpaRepository<TicketPool, Long> {}