package nl.backbase.kalah.model;

/**
 * @author Mina Sharifi
 */
public class Pit {

    public enum PitType {
        NORMAL,
        KALAH
    }

    private int id;
    private int stoneCounts;
    private PitType pitType;
    private String owner;

    public Pit(int id, int stoneCounts, PitType pitType, String owner){
        this.id= id;
        this.stoneCounts = stoneCounts;
        this.pitType = pitType;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStoneCounts() {
        return stoneCounts;
    }

    public void setStoneCounts(int stoneCounts) {
        this.stoneCounts = stoneCounts;
    }

    public PitType getPitType() {
        return pitType;
    }

    public void setPitType(PitType pitType) {
        this.pitType = pitType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return id + " -> " + stoneCounts;
    }
}
