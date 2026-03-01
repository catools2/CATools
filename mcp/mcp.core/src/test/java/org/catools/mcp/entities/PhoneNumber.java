package org.catools.mcp.entities;

import org.catools.mcp.enums.Country;
import org.catools.mcp.enums.PhoneType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Klavdiia Koval
 */

@NoArgsConstructor
@Data
@Accessors(fluent = true)
public class PhoneNumber {
    private Country country = Country.US;
    private String countryCode;
    private String number;
    private String extension;
    private PhoneType type = PhoneType.UNKNOWN;
}
