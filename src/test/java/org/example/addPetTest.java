package org.example;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.dataPojo.Category;
import org.example.dataPojo.Pet;
import org.example.dataPojo.Tag;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

import static io.restassured.RestAssured.given;

public class addPetTest {

    @Test(dataProvider = "petData")
    public void addPet(String id, String categoryId, String categoryName, String petName,
                       String photoUrl, String tag1Id, String tag1Name, String tag2Id,
                       String tag2Name, String status) throws JsonProcessingException {
        String endpoint = "https://petstore.swagger.io/v2/pet";

        Pet pet = new Pet();
        pet.setId(Long.parseLong(id));

        Category category = new Category();
        category.setId(Long.parseLong(categoryId));
        category.setName(categoryName);
        pet.setCategory(category);

        pet.setName(petName);

        pet.setPhotoUrls(Collections.singletonList(photoUrl));

        Tag tag1 = new Tag((int) Long.parseLong(tag1Id), tag1Name);
        Tag tag2 = new Tag((int) Long.parseLong(tag2Id), tag2Name);
        pet.setTags(Arrays.asList(tag1, tag2));

        pet.setStatus(status);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .post(endpoint)
                .then()
                .statusCode(200) // Assuming a successful response code
                .extract().response();

        // Print the response body
        System.out.println("Response Body:");
        System.out.println(response.getBody().asString());


        ObjectMapper objectMapper = new ObjectMapper();
        Pet petResponse = objectMapper.readValue(response.getBody().asString(), Pet.class);

        System.out.println(petResponse.getId() );
        System.out.println(pet.getId());

    }




    @DataProvider(name = "petData")
    public Object[][] petData() throws IOException {
        List<Object[]> data = new ArrayList<>();

        FileReader fileReader = new FileReader("src/test/java/dataFiles/petData.csv");
        CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader());

        for (CSVRecord record : csvParser) {
            data.add(new Object[]{
                    record.get("id"),
                    record.get("category_id"),
                    record.get("category_name"),
                    record.get("name"),
                    record.get("photo_url_1"),
                    record.get("tag1_id"),
                    record.get("tag1_name"),
                    record.get("tag2_id"),
                    record.get("tag2_name"),
                    record.get("status")
            });
        }

        csvParser.close();
        fileReader.close();

        return data.toArray(new Object[0][]);
    }
}
