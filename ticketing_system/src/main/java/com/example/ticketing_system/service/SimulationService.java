@Service
public class SimulationService {

    @Autowired
    private TicketPoolRepository ticketPoolRepository;

    @Autowired
    private RealTimeUpdateController updateController;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    public void startSimulation(Configuration config) {
        TicketPool pool = new TicketPool(0, config.getTotalTickets());
        ticketPoolRepository.save(pool);

        Runnable vendorTask = () -> {
            synchronized (pool) {
                if (pool.getRemainingTickets() > 0 && pool.getCurrentPoolSize() < config.getMaxPoolSize()) {
                    pool.setCurrentPoolSize(pool.getCurrentPoolSize() + 1);
                    pool.setRemainingTickets(pool.getRemainingTickets() - 1);
                    ticketPoolRepository.save(pool);
                    String message = "Vendor added a ticket. Pool size: " + pool.getCurrentPoolSize() +
                                     ", Remaining tickets: " + pool.getRemainingTickets();
                    updateController.sendUpdate(message);
                }
            }
        };

        Runnable customerTask = () -> {
            synchronized (pool) {
                if (pool.getCurrentPoolSize() > 0) {
                    pool.setCurrentPoolSize(pool.getCurrentPoolSize() - 1);
                    ticketPoolRepository.save(pool);
                    String message = "Customer bought a ticket. Pool size: " + pool.getCurrentPoolSize();
                    updateController.sendUpdate(message);
                }
            }
        };

        for (int i = 0; i < 2; i++) {
            scheduler.scheduleAtFixedRate(vendorTask, 0, config.getVendorReleaseTime(), TimeUnit.MILLISECONDS);
        }

        for (int i = 0; i < 3; i++) {
            scheduler.scheduleAtFixedRate(customerTask, 0, config.getCustomerBuyTime(), TimeUnit.MILLISECONDS);
        }
    }
}
