import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class JsonLectureTests {
    public static void main(String[] args) {
    }

    @Test
    @SuppressWarnings("unchecked")
    public void jsonObjectToStringWithPrintln(){
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("name", "slim shady");
        jsonObject.put("genre", "hip-hop");
        jsonObject.put("albums", 10);

        System.out.println(jsonObject);
        for (Object key :
                jsonObject.keySet()) {
            System.out.println(key);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void jsonObjectToStringManually() throws IOException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("name", "manuchari ibragimovi");
        jsonObject.put("genre", "ar aqvs");
        jsonObject.put("albums", 100);

        StringWriter stringWriter = new StringWriter();
        jsonObject.writeJSONString(stringWriter);

        String jsonText = stringWriter.toString();
        System.out.println(jsonText);
    }

    @Test
    public void deserializeJsonArray(){
        JSONParser jsonParser = new JSONParser();
        String jsonText = "[{\"0\": {\"albums\":100,\"name\":\"manuchari ibragimovi\",\"genre\":\"ar aqvs\"}}, {\"1\": {\"albums\":100,\"name\":\"gia suramelashvili\",\"genre\":\"ar aqvs\"}}]";

        try {
            Object object = jsonParser.parse(jsonText);
            JSONArray array = (JSONArray) object;

            System.out.println(array.get(1));

            // ===================================
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = (JSONObject) array.get(i);
                System.out.println(jsonObject.get(String.valueOf(i)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // magaliti romelic gamovtove, json-simple-s ar sheudzlia deserializacia custom tipis gamoyenebit (Artist.class magalidat).
    // amitom moyvanilia JSONObject magaliti.
    @Test
    public void deserializeJsonObject() throws ParseException {
        JSONParser jsonParser = new JSONParser();
        String jsonText = "{\n" +
                "  \"name\":\"eminem\",\n" +
                "  \"genre\":\"hip-hop\",\n" +
                "  \"albums\":10\n" +
                "}";

        Object artist = jsonParser.parse(jsonText);
        System.out.println(artist);
    }

    @Test
    public void gsonDeserializeSingleObject(){
        // 1
        Gson gson = new Gson();

        // 2
//        GsonBuilder builder = new GsonBuilder(); // gsonbuilder sheicavs konfiguraciebs
//        Gson gson2 = builder.create();

        String json = "{\n" +
                "  \"name\":\"gladiatori chkadua\",\n" +
                "  \"genre\":\"ar aqvs\",\n" +
                "  \"albums\":10\n" +
                "}";
        Artist artist1 = gson.fromJson(json, Artist.class);
        System.out.println(artist1.name);
    }

    @Test
    public void gsonDeserializeListOfObjects(){
        Gson gson = new Gson();

        String json = "{\n" +
                "  \"artists\": [\n" +
                "    {\n" +
                "      \"name\": \"zumba\",\n" +
                "      \"genre\": \"folk\",\n" +
                "      \"albums\": 4\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"gojira\",\n" +
                "      \"genre\": \"metal\",\n" +
                "      \"albums\": 6\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"metallica\",\n" +
                "      \"genre\": \"metal\",\n" +
                "      \"albums\": 10\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        Artists artistList = gson.fromJson(json, Artists.class);
        System.out.println(artistList.getArtists().get(0).name);
    }

    @Test
    public void gsonSerializeSingleObject(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        Gson gson = gsonBuilder.create(); // amas hqvia builder pattern

        Artist artist1 = new Artist("50 cent", "rap", 5);

        String serializedJson = gson.toJson(artist1);
        System.out.println(serializedJson);
    }

    @Test
    public void gsonSerializeListOfObjects(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        Gson gson = gsonBuilder.create();

        Artist artist1 = new Artist("50 cent", "rap", 5);
        Artist artist2 = new Artist("mf doom", "rap", 4);
        Artist artist3 = new Artist("linkin park", "rock", 5);

        List<Artist> artists = new ArrayList<>(){
            {
                add(artist1);
                add(artist2);
                add(artist3);
            }
        };

        Artists artists1 = new Artists();
        artists1.setArtists(artists);

        String serializedJson = gson.toJson(artists1);
        System.out.println(serializedJson);
    }

    @Test
    public void gsonSerializeWithNulls(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();

        Gson gson = gsonBuilder.create();

        Artist artist1 = new Artist("50 cent", null, 5);

        String serializedJson = gson.toJson(artist1);
        System.out.println(serializedJson);
    }

    @Test
    public void gsonExcludeTransientFields(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();

        Gson gson = gsonBuilder.create();

        ArtistWithExcludedFields artist1 = new ArtistWithExcludedFields("50 cent", null, 5, false);

        String serializedJson = gson.toJson(artist1);
        System.out.println(serializedJson);
    }

    @Test
    public void gsonExcludeNonExposed(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();

        Gson gson = gsonBuilder.create();

        ArtistWithExcludedFields artist1 = new ArtistWithExcludedFields("50 cent", null, 5, false);

        String serializedJson = gson.toJson(artist1);
        System.out.println(serializedJson);
    }

    @Test
    public void gsonExclusionStrategy(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        ExclusionStrategy es = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                if (fieldAttributes.getName().equals("name")){
                    return true; // daskipavs tu fieldis saxelia name
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        };

        gsonBuilder.setExclusionStrategies(es);
        Gson gson = gsonBuilder.create();

        ArtistWithExcludedFields artist1 = new ArtistWithExcludedFields("50 cent", null, 5, false);
        String serializedJson = gson.toJson(artist1);
        System.out.println(serializedJson);
    }

    @Test
    public void gsonSerializedName(){
        Gson gson = new Gson();

        FootballClub manchesterUnited = new FootballClub("manchester united", 87000);

        String serializedJson = gson.toJson(manchesterUnited);
        System.out.println(serializedJson);

        FootballClub manchesterUnitedDeserialized = gson.fromJson(serializedJson, FootballClub.class);
        System.out.println(manchesterUnitedDeserialized.stadiumCapacity);
    }

    @Test
    public void networkDeserialization(){
        Response response = given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/1");

        response.then().log().all();
        String jsonString = response.asPrettyString();

        Gson gson = new Gson();
        User user = gson.fromJson(jsonString, User.class);
        System.out.println("USER ID: " + user.getUserId());

        User user2 = response.as(User.class);
        System.out.println("USER2 ID: " + user2.getUserId());
    }

    @Test
    public void networkSerialization(){
        Post post = new Post("florida man eats 666 pancakes", "florida man has officially eated 666 pancakes", 1);
        Gson gson = new Gson();
        String jsonText = gson.toJson(post);

        Response response = given()
                .when()
                .body(jsonText)
                .post("https://jsonplaceholder.typicode.com/posts");

        response.then().log().all();

        JsonPath jPath = response.jsonPath();
        System.out.println(jPath.getInt("id"));
    }

    @Test
    public void readJsonFromFile(){
        try {
            Gson gson = new Gson();
            Reader reader = new FileReader("src/main/resources/example.json");

            Map<?, ?> map = gson.fromJson(reader, Map.class);
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                System.out.println(entry.getKey() + "=" + entry.getValue());
            }
            reader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
