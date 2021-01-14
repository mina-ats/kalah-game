package nl.backbase.kalah.model;

import java.util.List;
import java.util.Objects;

/**
 * @author Mina Sharifi
 */
public class KalahGame {

    public enum GameState {
        PROGRESS,
        OVER
    }

    private Integer id;
    private String player1;
    private String player2;
    private List<Pit> pits;
    private String turn;
    private GameState gameState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public List<Pit> getPits() {
        return pits;
    }

    public void setPits(List<Pit> pits) {
        this.pits = pits;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KalahGame kalahGame = (KalahGame) o;
        return id.equals(kalahGame.id) &&
                player1.equals(kalahGame.player1) &&
                Objects.equals(player2, kalahGame.player2) &&
                pits.equals(kalahGame.pits) &&
                turn.equals(kalahGame.turn) &&
                gameState == kalahGame.gameState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, player1, player2, pits, turn, gameState);
    }

    @Override
    public String toString() {
        return "KalahGame{" +
                "id=" + id +
                ", player1='" + player1 + '\'' +
                ", player2='" + player2 + '\'' +
                ", turn='" + turn + '\'' +
                ", gameState=" + gameState + '\'' +
                ", pits=" + "\n" +
                "      "+pits.get(0) + "   " + pits.get(1) + "   " + pits.get(2) + "   " + pits.get(3) + "   " + pits.get(4) + "   " + pits.get(5) + "\n" +
                pits.get(13) + "                                                          " + pits.get(6) + "\n" +
                "      "+pits.get(12) + "   " + pits.get(11) + "   " + pits.get(10) + "   " + pits.get(9) + "   " + pits.get(8) + "   " + pits.get(7) +
                '}';
    }
}
