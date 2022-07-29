import com.google.gson.annotations.SerializedName;

public class FootballClub {
    public String name;

    @SerializedName("stadium_capacity")
    public int stadiumCapacity;

    public FootballClub(String name, int stadiumCapacity) {
        this.name = name;
        this.stadiumCapacity = stadiumCapacity;
    }
}
