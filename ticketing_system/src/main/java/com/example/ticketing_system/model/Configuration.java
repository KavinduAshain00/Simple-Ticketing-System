package com.example.ticketing_system.model;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalTickets;
    private int maxPoolSize;
    private int vendorReleaseTime; // in milliseconds
    private int customerBuyTime;  // in milliseconds
}

