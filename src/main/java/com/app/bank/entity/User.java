package com.app.bank.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
            name = "User Id",
            description = "The unique identifier of the user"
    )
    private Long id;

    @Schema(
            name = "First Name",
            description = "The first name of the user"
    )
    private String firstName;

    @Schema(
            name = "Last Name",
            description = "The lastname of the user"
    )
    private String lastName;

    @Schema(
            name = "Middle Name",
            description = "The middle name of the user"
    )
    private String middleName;

    @Schema(
            name = "Gender",
            description = "The gender of the user"
    )
    private String gender;

    @Schema(
            name = "Address Line 1",
            description = "The house number of the user"
    )
    private String addressLine1;

    @Schema(
            name = "Address Line 2",
            description = "The street number,locality of the user"
    )
    private String addressLine2;

    @Schema(
            name = "City",
            description = "The residing city of the user"
    )
    private String city;

    @Schema(
            name = "State",
            description = "The residing state of the user"
    )
    private String stateOfOrigin;

    @Schema(
            name = "Pin Code",
            description = "The pin code of the user"
    )
    private String pinCode;

    @Schema(
            name = "Country",
            description = "The residing country of the user"
    )
    private String country;

    @Schema(
            name = "Account Number",
            description = "The account number of the user's bank account"
    )
    private String accountNumber;

    @Schema(
            name = "Account Balance",
            description = "The unique identifier of the user"
    )
    private BigDecimal accountBalance;

    @Schema(
            name = "Email Address",
            description = "The email id of the user"
    )
    private String email;

    @Schema(
            name = "Password",
            description = "The password for the account"
    )
    private String password;

    @Schema(
            name = "Role",
            description = "The role of the user"
    )
    private String role;

    @Schema(
            name = "Phone Number",
            description = "The phone number of the user"
    )
    private String phoneNumber;

    @Schema(
            name = "Alternate phone number",
            description = "The backup phone number of the user"
    )
    private String alternativePhoneNumber;

    @Schema(
            name = "Status",
            description = "The current status of the user's bank account"
    )
    private String status;

    @Schema(
            name = "Created At",
            description = "The account creation date of the user"
    )
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Schema(
            name = "Updated At",
            description = "The last updated timestamp of the user's account information"
    )
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
