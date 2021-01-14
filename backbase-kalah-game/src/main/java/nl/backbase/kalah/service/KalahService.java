package nl.backbase.kalah.service;

import nl.backbase.kalah.db.IKalahGameDb;
import nl.backbase.kalah.model.KalahGame;
import nl.backbase.kalah.model.Pit;
import nl.backbase.kalah.service.validation.IGameValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Mina Sharifi
 */
@Service
public class KalahService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KalahService.class);

    private IKalahGameDb kalahDb;
    private IGameValidator gameValidator;

    public KalahService(IKalahGameDb kalahDb, IGameValidator gameValidator) {
        this.kalahDb = kalahDb;
        this.gameValidator = gameValidator;
    }

    public KalahGame createKalahGame(String userIP) {
        KalahGame kalahGame = new KalahGame();
        kalahGame.setPlayer1(userIP);
        kalahGame.setPits(initializePits(userIP));
        kalahGame.setGameState(KalahGame.GameState.PROGRESS);
        kalahGame.setTurn(userIP);
        KalahGame saved = kalahDb.save(kalahGame);
        LOGGER.info("Game was initialized, {}", saved);
        return saved;
    }

    private List<Pit> initializePits(String userIP) {
        Pit pit1 = new Pit(1, 6, Pit.PitType.NORMAL, userIP);
        Pit pit2 = new Pit(2, 6, Pit.PitType.NORMAL, userIP);
        Pit pit3 = new Pit(3, 6, Pit.PitType.NORMAL, userIP);
        Pit pit4 = new Pit(4, 6, Pit.PitType.NORMAL, userIP);
        Pit pit5 = new Pit(5, 6, Pit.PitType.NORMAL, userIP);
        Pit pit6 = new Pit(6, 6, Pit.PitType.NORMAL, userIP);
        Pit pit7 = new Pit(7, 6, Pit.PitType.KALAH, userIP);
        Pit pit8 = new Pit(8, 6, Pit.PitType.NORMAL, null);
        Pit pit9 = new Pit(9, 6, Pit.PitType.NORMAL, null);
        Pit pit10 = new Pit(10, 6, Pit.PitType.NORMAL, null);
        Pit pit11 = new Pit(11, 6, Pit.PitType.NORMAL, null);
        Pit pit12 = new Pit(12, 6, Pit.PitType.NORMAL, null);
        Pit pit13 = new Pit(13, 6, Pit.PitType.NORMAL, null);
        Pit pit14 = new Pit(14, 6, Pit.PitType.KALAH, null);
        return Arrays.asList(pit1, pit2, pit3, pit4, pit5, pit6, pit7, pit8, pit9, pit10, pit11, pit12, pit13, pit14);
    }

    public KalahGame move(int gameId, int pitId, String userId) {
        Optional<KalahGame> gameOptional = kalahDb.findById(gameId);
        KalahGame kalahGame = gameOptional.orElseThrow(RuntimeException::new);

        this.gameValidator.preValidation(kalahGame, userId, pitId);

        int index = pitId - 1;
        Pit startPin = kalahGame.getPits().get(pitId - 1);
        int toBeSown = startPin.getStoneCounts();
        startPin.setStoneCounts(0);
        int i = 1;
        // apply circular sowing
        while (i <= toBeSown) {
            index++;
            index = index % 14;
            Pit pit = kalahGame.getPits().get(index);
            if (pit.getPitType() == Pit.PitType.NORMAL ||
                    (pit.getPitType() == Pit.PitType.KALAH && userId.equals(pit.getOwner()))) {
                pit.setStoneCounts(pit.getStoneCounts() + 1);
                i++;
            }
        }

        checkSecondScoreRule(kalahGame, index, userId);
        setTurn(kalahGame, index, userId);

        this.gameValidator.postValidation(kalahGame);
        this.kalahDb.update(kalahGame);

        LOGGER.info("Updated board game : {}", kalahGame);
        return kalahGame;
    }

    private void checkSecondScoreRule(KalahGame kalahGame, int lastIndex, String userId){
        Pit lastPin = kalahGame.getPits().get(lastIndex);
        if (userId.equals(lastPin.getOwner()) && lastPin.getStoneCounts() == 1) {
            Pit opponentPin = kalahGame.getPits().get(kalahGame.getPits().size() - lastPin.getId() - 1);
            Pit kalah = kalahGame.getPits().stream()
                    .filter(pit -> pit.getPitType() == Pit.PitType.KALAH && pit.getOwner().equals(userId)).findFirst()
                    .orElseThrow(RuntimeException::new);
            kalah.setStoneCounts(kalah.getStoneCounts() + lastPin.getStoneCounts() + opponentPin.getStoneCounts());
            lastPin.setStoneCounts(0);
            opponentPin.setStoneCounts(0);
        }
    }

    private void setTurn(KalahGame kalahGame, int lastIndex, String userId){
        Pit lastPin = kalahGame.getPits().get(lastIndex);
        if (lastPin.getPitType() == Pit.PitType.KALAH && userId.equals(lastPin.getOwner())) {
            kalahGame.setTurn(userId);
        }else{
            if(userId.equals(kalahGame.getPlayer1())){
                kalahGame.setTurn(kalahGame.getPlayer2());
            }else{
                kalahGame.setTurn(kalahGame.getPlayer1());
            }
        }
    }
}
