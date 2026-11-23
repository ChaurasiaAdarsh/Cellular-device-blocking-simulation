package org.dep.imeiRegistory.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//this setups table name and constraints
@Table(
        name = "imei_records",
        uniqueConstraints = {
                @UniqueConstraint(name = "imei_number", columnNames = "imei_number")
        }
)
public class Imei{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //primary key
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
    //last action performed
    private String lastAction;

    @Column(name = "last_updated")
    //timestamp for last updated
    private LocalDateTime lastUpdated;

    @Column(name = "created_at")
    //timestamp for creation
    private LocalDateTime createdAt;

    //trying gpt method
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
