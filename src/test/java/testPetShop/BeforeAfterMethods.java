package testPetShop;

import endpoints.EndPointPetShop;
import model.Category;
import model.Pet;
import model.TagPet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import java.util.ArrayList;
import java.util.Collections;
import static io.restassured.RestAssured.given;
import static testPetShop.TestPetShop.requestSpec;
import static util.Status.AVAILABLE;


public class BeforeAfterMethods {

  protected  TagPet tag = new TagPet(100, " test cat");
    protected Category category = new Category(1, "Cat");
    protected Pet pet = new Pet(1022, category, "Bibi", new ArrayList<>(), new ArrayList<>(Collections.singletonList(tag)), AVAILABLE);

    @BeforeEach
    void CreatePet(TestInfo testInfo) {
        if (testInfo.getTags().contains("SkipCreate")) {
            return;
        }
        given().spec(requestSpec)
                .when()
                .body(pet)
                .post(EndPointPetShop.PET);
    }

    @AfterEach
     void DeletePet(TestInfo testInfo) {
        if (testInfo.getTags().contains("SkipDelete")) {
            return;
        }
        given().spec(requestSpec)
                .when()
                .delete(EndPointPetShop.PET + "/" + pet.getId());
    }
}
