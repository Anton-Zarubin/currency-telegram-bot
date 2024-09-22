package com.skillbox.cryptobot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subscribers")
@Entity
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name = "telegram_user_id", nullable = false)
    private Long userId;

    @Column(name = "subscription_value")
    private Double subscriptionValue;
}