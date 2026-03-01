package org.catools.mcp.context;

import org.catools.mcp.entities.*;
import org.catools.mcp.enums.Country;
import org.catools.mcp.enums.PhoneType;
import org.catools.mcp.utils.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class McpStorageConvertorTest {

    @Test
    public void testSchemaToJson() {
        JsonNode employee = McpStorageConvertor.schemaToJson("employee");
        String actualResult = JsonUtil.toString(employee);
        assertEquals(actualResult, """
                {
                  "employeeId" : null,
                  "userId" : null,
                  "lastLoginDate" : null,
                  "userName" : null,
                  "password" : null,
                  "firstName" : null,
                  "lastName" : null,
                  "mainPhone" : {
                    "country" : null,
                    "countryCode" : null,
                    "number" : null,
                    "extension" : null,
                    "type" : null
                  },
                  "status" : null,
                  "comments" : [ {
                    "commentId" : null,
                    "comment" : null,
                    "subject" : null,
                    "authorName" : null,
                    "userId" : null,
                    "publicNote" : null,
                    "creationDate" : null,
                    "commentType" : null,
                    "attachmentCount" : null,
                    "uploadFile" : null,
                    "fileName" : null
                  } ],
                  "address" : {
                    "addrId" : null,
                    "addrLine1" : null,
                    "addrLine2" : null,
                    "addrLine3" : null,
                    "city" : null,
                    "stateProviceCode" : null,
                    "postalCode" : null,
                    "countryCode" : null,
                    "modifiedUserId" : null,
                    "modifiedDate" : null,
                    "name" : null
                  }
                }""");
    }

    @Test
    public void testSchemaToPojo() {
        JsonNode schema = McpStorageConvertor.readSchema("employee");
        JsonNode employee = McpStorageConvertor.schemaToJson("employee");
        McpStorageRecord storageRecord = McpStorageRecord.of(schema, "employee1", employee);
        Object pojo = storageRecord.getPojo();

        Employee expected = new Employee();
        expected.setAddress(new Address());
        expected.setMainPhone(new PhoneNumber().type(PhoneType.UNKNOWN).country(Country.US));
        expected.setComments(new Comments());
        expected.getComments().add(new Comment());

        assertEquals(pojo, expected);
    }
}
