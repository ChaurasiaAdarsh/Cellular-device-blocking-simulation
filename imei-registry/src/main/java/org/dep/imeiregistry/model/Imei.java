package org.dep.imeiregistry.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "imei_records",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_imei_number", columnNames = "imei_number")
        }
)
public class Imei {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imei_number", length = 15, nullable = false, unique = true)
    private String imeiNumber;

    @Column(name = "username")
    private String userName;

    @Column(name = "mobile_model")
    private String mobileModel;

    @Column(name = "telecom_company", length = 100, nullable = false)
    private String telecomCompany;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "last_action", length = 20)
    private String lastAction;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        lastUpdated = now;
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }
}
