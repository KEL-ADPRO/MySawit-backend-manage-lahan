package com.mysawit.mysawit_kebun.controller;

import com.mysawit.mysawit_kebun.dto.AreaDto;
import com.mysawit.mysawit_kebun.dto.KebunRequestDto;
import com.mysawit.mysawit_kebun.dto.KoordinatDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.restassured.http.ContentType;
import io.restassured.RestAssured;
import net.serenitybdd.annotations.Title;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

import static org.hamcrest.Matchers.*;

@ExtendWith(SerenityJUnit5Extension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "jwt.secret=mysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkey"
    }
)
public class KebunFunctionalTest {

    @LocalServerPort
    private int port;

    private String adminToken;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        adminToken = generateAdminToken();
    }

    private String generateAdminToken() {
        SecretKey key = Keys.hmacShaKeyFor("mysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkey".getBytes());
        return Jwts.builder()
                .subject("admin-user")
                .claim("role", "ADMIN")
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();
    }

    @Test
    @Title("Verify Land Registration, Querying and Overlap Validation Business Flows")
    public void testLandRegistrationAndOverlapVerificationFlow() {
        // 1. Create first land area (Kebun A) - valid coordinates (0,0) - (10,10)
        KoordinatDto bl = new KoordinatDto(0, 0);
        KoordinatDto br = new KoordinatDto(10, 0);
        KoordinatDto tr = new KoordinatDto(10, 10);
        KoordinatDto tl = new KoordinatDto(0, 10);
        AreaDto area1 = new AreaDto(bl, br, tr, tl);

        KebunRequestDto kebunRequest1 = new KebunRequestDto("Kebun A", 100.0, area1);

        String kebunId = SerenityRest.given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body(kebunRequest1)
                .when()
                .post("/api/kebun")
                .then()
                .statusCode(200)
                .body("message", equalTo("Kebun created successfully"))
                .body("data.nama", equalTo("Kebun A"))
                .body("data.luas", equalTo(100.0f))
                .extract()
                .path("data.id");

        assert kebunId != null;

        // 2. Get the Kebun by ID
        SerenityRest.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/kebun/" + kebunId)
                .then()
                .statusCode(200)
                .body("message", equalTo("Success"))
                .body("data.id", equalTo(kebunId))
                .body("data.nama", equalTo("Kebun A"));

        // 3. Get the Kebun by Name
        SerenityRest.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/kebun/name/Kebun A")
                .then()
                .statusCode(200)
                .body("message", equalTo("Success"))
                .body("data.id", equalTo(kebunId))
                .body("data.nama", equalTo("Kebun A"));

        // 4. Get all Kebun list and check if the new Kebun is included
        SerenityRest.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/kebun")
                .then()
                .statusCode(200)
                .body("message", equalTo("Success"))
                .body("data", hasSize(greaterThanOrEqualTo(1)))
                .body("data.id", hasItem(kebunId));

        // 5. Assign Mandor to Kebun
        String mandorId = UUID.randomUUID().toString();
        SerenityRest.given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .when()
                .patch("/api/kebun/" + kebunId + "/mandor/" + mandorId)
                .then()
                .statusCode(200)
                .body("message", equalTo("Mandor assigned successfully"))
                .body("data.mandorId", equalTo(mandorId));

        // 6. Check Mandor Assignment
        SerenityRest.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/kebun/check-mandor/" + mandorId)
                .then()
                .statusCode(200)
                .body("message", equalTo("Mandor assignment found"))
                .body("data.isAssigned", equalTo(true))
                .body("data.kebunId", equalTo(kebunId));

        // 6a. Create a second land area (Kebun B) - coordinates (20,20) - (30,30) (no overlap)
        KoordinatDto bl2 = new KoordinatDto(20, 20);
        KoordinatDto br2 = new KoordinatDto(30, 20);
        KoordinatDto tr2 = new KoordinatDto(30, 30);
        KoordinatDto tl2 = new KoordinatDto(20, 30);
        AreaDto area2 = new AreaDto(bl2, br2, tr2, tl2);

        KebunRequestDto kebunRequest2 = new KebunRequestDto("Kebun B", 100.0, area2);

        String kebunId2 = SerenityRest.given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body(kebunRequest2)
                .when()
                .post("/api/kebun")
                .then()
                .statusCode(200)
                .body("message", equalTo("Kebun created successfully"))
                .body("data.nama", equalTo("Kebun B"))
                .extract()
                .path("data.id");

        assert kebunId2 != null;

        // 6b. Automatic Switching - Assign the same mandorId to Kebun B
        // This must automatically clear mandorId from Kebun A and assign it to Kebun B
        SerenityRest.given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .when()
                .patch("/api/kebun/" + kebunId2 + "/mandor/" + mandorId)
                .then()
                .statusCode(200)
                .body("message", equalTo("Mandor assigned successfully"))
                .body("data.mandorId", equalTo(mandorId));

        // Verify Kebun A's mandor is now null
        SerenityRest.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/kebun/" + kebunId)
                .then()
                .statusCode(200)
                .body("data.mandorId", is(nullValue()));

        // Verify mandor check endpoint now points to Kebun B
        SerenityRest.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/kebun/check-mandor/" + mandorId)
                .then()
                .statusCode(200)
                .body("data.isAssigned", equalTo(true))
                .body("data.kebunId", equalTo(kebunId2));

        // 6c. Overwrite Prohibition - Try to assign a different mandor (mandorId2) to Kebun B which already has one
        // This should return 400 Bad Request
        String mandorId2 = UUID.randomUUID().toString();
        SerenityRest.given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .when()
                .patch("/api/kebun/" + kebunId2 + "/mandor/" + mandorId2)
                .then()
                .statusCode(400)
                .body("error", equalTo("Invalid Operation"))
                .body("message", equalTo("Kebun already has a different Mandor assigned. Reassign that Mandor first."));

        // 7. Assign Supir to Kebun
        String supirId = UUID.randomUUID().toString();
        SerenityRest.given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .when()
                .patch("/api/kebun/" + kebunId + "/supir/" + supirId)
                .then()
                .statusCode(200)
                .body("message", equalTo("Supir Truk assigned successfully"))
                .body("data.supirIds", hasItem(supirId));

        // 8. Check Supir Assignment
        SerenityRest.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/kebun/check-supir/" + supirId)
                .then()
                .statusCode(200)
                .body("message", equalTo("Supir assignment found"))
                .body("data.isAssigned", equalTo(true))
                .body("data.kebunId", equalTo(kebunId));

        // 9. Remove Supir from Kebun
        SerenityRest.given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/kebun/" + kebunId + "/supir/" + supirId)
                .then()
                .statusCode(200)
                .body("message", equalTo("Supir Truk removed successfully"))
                .body("data.supirIds", not(hasItem(supirId)));

        // 10. Check Supir is not assigned anymore
        SerenityRest.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/kebun/check-supir/" + supirId)
                .then()
                .statusCode(200)
                .body("message", equalTo("Supir Truk is not assigned to any kebun"))
                .body("data.isAssigned", equalTo(false));

        // 11. Try creating overlap Kebun: coordinates (5,5) - (15,15)
        KoordinatDto overlapBl = new KoordinatDto(5, 5);
        KoordinatDto overlapBr = new KoordinatDto(15, 5);
        KoordinatDto overlapTr = new KoordinatDto(15, 15);
        KoordinatDto overlapTl = new KoordinatDto(5, 15);
        AreaDto areaOverlap = new AreaDto(overlapBl, overlapBr, overlapTr, overlapTl);

        KebunRequestDto kebunRequestOverlap = new KebunRequestDto("Kebun Overlap", 100.0, areaOverlap);

        SerenityRest.given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body(kebunRequestOverlap)
                .when()
                .post("/api/kebun")
                .then()
                .statusCode(409)
                .body("message", equalTo("Kebun overlaps with an existing kebun."));

        // 12. Try creating Kebun with negative coordinates
        KoordinatDto invalidBl = new KoordinatDto(-1, 0);
        KoordinatDto invalidBr = new KoordinatDto(10, 0);
        KoordinatDto invalidTr = new KoordinatDto(10, 10);
        KoordinatDto invalidTl = new KoordinatDto(0, 10);
        AreaDto areaInvalid = new AreaDto(invalidBl, invalidBr, invalidTr, invalidTl);

        KebunRequestDto kebunRequestInvalid = new KebunRequestDto("Kebun Invalid", 100.0, areaInvalid);

        SerenityRest.given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body(kebunRequestInvalid)
                .when()
                .post("/api/kebun")
                .then()
                .statusCode(400);
    }
}
