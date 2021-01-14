package nl.backbase.kalah.web.dto;

/**
 * @author Mina Sharifi
 */
public class GameInfo {

    private int id;
    private String uri;

    public GameInfo(int id, String uri){
        this.id = id;
        this. uri = uri;
    }

    public int getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }
}
