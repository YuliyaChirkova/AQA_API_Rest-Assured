package testPetShop;


import com.sun.org.glassfish.gmbal.Description;
import endpoints.EndPointPetShop;
import io.restassured.specification.RequestSpecification;
import model.Category;
import model.Pet;
import model.TagPet;
import org.junit.jupiter.api.*;
import util.PetShopApiSpecification;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import static org.hamcrest.Matchers.*;
import static util.Status.AVAILABLE;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("API tests for petstore")
public class TestPetShop extends BeforeAfterMethods {

    protected static RequestSpecification requestSpec = PetShopApiSpecification.getRequestSpecification();
    protected static File jsonSchema = new File("src/test/resources/json/petJsonSchema.json");

    @Test
    @Order(1)
    @DisplayName("POST request with validation JsonScheme ")
    @Description("POST request")
    @Tag("SkipCreate")
    public void testPostValidateJsonScheme() {
        given().spec(requestSpec)
                .when()
                .body(pet)
                .post(EndPointPetShop.PET)
                .then()
                .assertThat()
                .body(matchesJsonSchema(jsonSchema));
    }

    @Test
    @Order(2)
    @Description("GET request")
    @DisplayName("GET request with body validation")
     void testGetMyPet() {
        given()
                .spec(requestSpec)
                .when()
                .get(EndPointPetShop.PET + "/" + pet.getId())
                .prettyPeek()
                .then()
                .body("name", equalTo(pet.getName()));
    }

    @Test
    @Order(3)
    @Description("GET request")
    @DisplayName("GET request with validation JsonScheme ")
     void testGetValidateJsonScheme() {
                given()
                .spec(requestSpec)
                .when()
                .get(EndPointPetShop.PET+"/"+pet.getId())
                .then()
                        .assertThat()
                        .body(matchesJsonSchema(jsonSchema));
    }

    @Test
    @Order(4)
    @Description("PUT request")
    @DisplayName("PUT request with body validation ")
     void testPutMyPet() {
        TagPet tag = new TagPet(111, " test2 cat");
        Category category = new Category(1, "Cat");
        Pet petForUpdate = new Pet(1022, category, "Gigi", new ArrayList<>(), new ArrayList<>(Collections.singletonList(tag)), AVAILABLE);

        given().spec(requestSpec)
                .when()
                .body(petForUpdate)
                .put(EndPointPetShop.PET)
                .prettyPeek()
                .then()
                .body("name", equalTo(petForUpdate.getName()));
    }

    @Test
    @Order(5)
    @Description("DELETE request")
    @DisplayName("DELETE request with body and statusCode validation ")
    @Tag("SkipDelete")
     void testDeleteMyPet() {
        given()
                .spec(requestSpec)
                .when()
                .delete(EndPointPetShop.PET + "/" + pet.getId())
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("type", equalTo("unknown"))
                .body(containsString(String.valueOf(pet.getId())));
    }
    }
