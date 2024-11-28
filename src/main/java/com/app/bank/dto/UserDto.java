package com.app.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

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
            name = "Email Address",
            description = "The email id of the user"
    )
    private String email;

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
}
