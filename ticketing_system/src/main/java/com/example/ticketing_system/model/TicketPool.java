package com.example.ticketing_system.model;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketPool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int currentPoolSize;
    private int remainingTickets;
}

