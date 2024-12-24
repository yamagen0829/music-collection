package com.example.musiccollection.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
	 @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "user_id")
     private Integer userId;
     
     @Column(name = "user_name")
     private String userName;
         
     @Column(name = "furigana")
     private String furigana;    
         
     @Column(name = "postal_code")
     private String postalCode;
         
     @Column(name = "address")
     private String address;
         
     @Column(name = "phone_number")
     private String phoneNumber;
     
     @Column(name = "user_email")
     private String userEmail;
         
     @Column(name = "password")
     private String password;    
     
     @ManyToOne
     @JoinColumn(name = "role_id")
     private Role role;   
     
     @Column(name = "enabled")
     private Boolean enabled;
     
     @Column(name = "paid")
     private Boolean paid = false;
     
     @Column(name = "stripe_customer_id")
     private String stripeCustomerId;
     
     @Column(name = "created_at", insertable = false, updatable = false)
     private Timestamp createdAt;
     
     @Column(name = "updated_at", insertable = false, updatable = false)
     private Timestamp updatedAt;
}
