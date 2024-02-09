package com.example.auth.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Table(name = "reset_password_operations")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordOperationEntity {
    @Id
    @GeneratedValue(generator = "reset_password_operations_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "reset_password_operations_id_seq",sequenceName = "reset_password_operations_id_seq",allocationSize = 1)
    private long id;
    @ManyToOne
    @JoinColumn(name = "users")
    private UserEntity user;
    @Column(name = "create_date")
    private Timestamp createDate;
    private String uid;
}
