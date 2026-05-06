package com.internship.tool.config;

import com.internship.tool.entity.Role;
import com.internship.tool.entity.TrainingRecord;
import com.internship.tool.entity.User;
import com.internship.tool.repository.RoleRepository;
import com.internship.tool.repository.TrainingRecordRepository;
import com.internship.tool.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.List;
import java.util.Set;

@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TrainingRecordRepository trainingRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           RoleRepository roleRepository,
                           TrainingRecordRepository trainingRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.trainingRepository = trainingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        logger.info("=== Running DataInitializer ===");

        // Seed roles
        Role adminRole   = seedRole("ADMIN");
        Role managerRole = seedRole("MANAGER");
        Role viewerRole  = seedRole("VIEWER");

        // Seed admin user
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@compliance.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);
            logger.info("Created admin user (admin/admin123)");
        }

        // Seed manager user
        if (!userRepository.existsByUsername("manager")) {
            User manager = new User();
            manager.setUsername("manager");
            manager.setEmail("manager@compliance.com");
            manager.setPassword(passwordEncoder.encode("manager123"));
            manager.setRoles(Set.of(managerRole));
            userRepository.save(manager);
            logger.info("Created manager user (manager/manager123)");
        }

        // Seed viewer user
        if (!userRepository.existsByUsername("viewer")) {
            User viewer = new User();
            viewer.setUsername("viewer");
            viewer.setEmail("viewer@compliance.com");
            viewer.setPassword(passwordEncoder.encode("viewer123"));
            viewer.setRoles(Set.of(viewerRole));
            userRepository.save(viewer);
            logger.info("Created viewer user (viewer/viewer123)");
        }

        // Seed sample training records
        if (trainingRepository.count() == 0) {
            List<TrainingRecord> records = List.of(
                createRecord("GDPR Data Protection Training", "Annual GDPR compliance training for all staff",
                    "GDPR", "COMPLETED", "HIGH", "john.doe", 92, LocalDate.now().minusDays(10)),
                createRecord("Cybersecurity Awareness", "Phishing and social engineering awareness training",
                    "SECURITY", "PENDING", "HIGH", "jane.smith", null, LocalDate.now().plusDays(14)),
                createRecord("Anti-Bribery and Corruption", "ABC policy training per regulatory requirements",
                    "COMPLIANCE", "IN_PROGRESS", "MEDIUM", "bob.johnson", null, LocalDate.now().plusDays(7)),
                createRecord("Health & Safety Induction", "Workplace health and safety mandatory training",
                    "SAFETY", "COMPLETED", "LOW", "alice.wong", 88, LocalDate.now().minusDays(30)),
                createRecord("Data Handling Procedures", "Proper data classification and handling procedures",
                    "DATA", "PENDING", "HIGH", "charlie.brown", null, LocalDate.now().plusDays(3)),
                createRecord("Code of Conduct Training", "Annual code of conduct and ethics refresher",
                    "ETHICS", "OVERDUE", "HIGH", "diana.prince", null, LocalDate.now().minusDays(5))
            );
            trainingRepository.saveAll(records);
            logger.info("Seeded {} training records", records.size());
        }

        logger.info("=== DataInitializer complete ===");
    }

    private Role seedRole(String name) {
        return roleRepository.findByName(name).orElseGet(() -> {
            Role role = new Role();
            role.setName(name);
            Role saved = roleRepository.save(role);
            logger.info("Created role: {}", name);
            return saved;
        });
    }

    private TrainingRecord createRecord(String title, String description, String category,
                                         String status, String priority, String assignedTo,
                                         Integer score, LocalDate dueDate) {
        TrainingRecord r = new TrainingRecord();
        r.setTitle(title);
        r.setDescription(description);
        r.setCategory(category);
        r.setStatus(status);
        r.setPriority(priority);
        r.setAssignedTo(assignedTo);
        r.setScore(score);
        if (score != null) r.setComplianceScore(new BigDecimal(score));
        r.setDueDate(dueDate);
        r.setCreatedBy("system");
        return r;
    }
}
