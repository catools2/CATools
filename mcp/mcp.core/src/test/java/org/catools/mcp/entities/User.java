package org.catools.mcp.entities;

import org.catools.mcp.enums.StatusCode;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class User {
    private String userId;
    private Date lastLoginDate;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private PhoneNumber mainPhone;
    private StatusCode status;
    private Address address;
    private Comments comments;
}
