import com.google.gson.annotations.Expose;

public class ArtistWithExcludedFields {
    @Expose(serialize = true, deserialize = false)
    public String name;
    @Expose(serialize = true, deserialize = false)
    public String genre;
    @Expose(serialize = true, deserialize = false)
    public int albums;
    public boolean dead;

//    public transient boolean dead;

    public ArtistWithExcludedFields(String name, String genre, int albums, boolean dead) {
        this.name = name;
        this.genre = genre;
        this.albums = albums;
        this.dead = dead;
    }
}
