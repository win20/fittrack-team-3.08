package sample;

import java.net.URLEncoder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Scanner;

public class Food {
    private String name;
    private int calories;
    private int servingSize;

    Food(){}

    Food(String name, int calories) {
        this.name = name;
        this.calories = calories;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public void setCalories(int calories) {
        this.calories = calories;
    }
    public int getCalories() { return calories; }

    public int getServingSize() { return servingSize; }
    public void setServingSize(int servingSize) { this.servingSize = servingSize; }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", calories=" + calories +
                '}';
    }

    public void ConnectToAPI(String httpQuery) throws UnirestException {
        if (httpQuery.contains(" ")) {
            httpQuery = httpQuery.replaceAll(" ", "%20");
        }

        HttpResponse<String> response = Unirest.get("https://calorieninjas.p.rapidapi.com/v1/nutrition?query=" + httpQuery)
                .header("x-rapidapi-key", "82f8d41c9fmshd30e1c4e8273ffcp177ab0jsn32983111e46b")
                .header("x-rapidapi-host", "calorieninjas.p.rapidapi.com")
                .asString();

        System.out.println(response.getBody());
        parse(response.getBody());
    }
    public String parse(String responseBody) {
        JSONObject tmp = new JSONObject(responseBody);
        JSONArray itemsArray = tmp.getJSONArray("items");

        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject foodObj = itemsArray.getJSONObject(i);
            this.name = foodObj.getString("name");
            this.calories = foodObj.getInt("calories");
            this.servingSize = foodObj.getInt("serving_size_g");
//            System.out.println("Name: " + this.name);
//            System.out.println("serving: " + this.servingSize);
        }
        return null;
    }

    public static void main(String[] args) throws UnirestException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter query:");
        String httpQuery = scanner.nextLine();

        Food food = new Food();
        food.ConnectToAPI(httpQuery);
    }
}
