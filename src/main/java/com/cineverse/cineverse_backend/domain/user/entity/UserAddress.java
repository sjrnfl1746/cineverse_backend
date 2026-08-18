package com.cineverse.cineverse_backend.domain.user.entity;

import com.cineverse.cineverse_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "user_addresses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAddress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 10)
    private String zipCode;


    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String district;

    @Column(length = 100)
    private String street;

    @Column(length = 100)
    private String detail;

    @Builder.Default
    private Boolean primaryAddress = false;

    // 주소 변경
    public void updateAddress(String zipCode, String city, String district, String street, String detail) {
        this.zipCode = zipCode;
        this.city = city;
        this.district = district;
        this.street = street;
        this.detail = detail;
    }
}
