package org.catools.mcp.entities;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class Address {
    private String id;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private boolean primary;
    private Instant createdAt;
    private Instant updatedAt;
}
