package nl.backbase.kalah.service.validation;

import nl.backbase.kalah.model.KalahGame;
import nl.backbase.kalah.model.Pit;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author Mina Sharifi
 */
@Component
public class GameValidator implements IGameValidator {

    @Override
    public void preValidation(KalahGame kalahGame, String userId, int pitId) {

        if (kalahGame.getGameState() == KalahGame.GameState.OVER) {
            throw new ValidationException("The game is already over");
        }
        if (!kalahGame.getPlayer1().equals(userId) ||
                (kalahGame.getPlayer2() != null && !userId.equals(kalahGame.getPlayer2()))) {
            throw new ValidationException("You are not invited!");
        }

        if (kalahGame.getTurn() == null &&
                kalahGame.getPlayer2() == null &&
                !kalahGame.getPlayer1().equals(userId)) {
            kalahGame.getPits().stream().filter(pit -> pit.getOwner() == null).forEach(pit -> pit.setOwner(userId));
            kalahGame.setPlayer2(userId);
            kalahGame.setTurn(userId);
        }

        if (!kalahGame.getTurn().equals(userId)) {
            throw new ValidationException("No your turn");
        }

        Pit startPin = kalahGame.getPits().get(pitId - 1);
        if (!userId.equals(startPin.getOwner())) {
            throw new ValidationException("Not valid move, start from your side");
        }

        if (startPin.getPitType() == Pit.PitType.KALAH) {
            throw new ValidationException("Don't pick from KALAH");
        }
    }

    @Override
    public void postValidation(KalahGame kalahGame) {
        checkGameOver(kalahGame, kalahGame.getPlayer1(), kalahGame.getPlayer2());
        if (kalahGame.getGameState() != KalahGame.GameState.OVER) {
            checkGameOver(kalahGame, kalahGame.getPlayer2(), kalahGame.getPlayer1());
        }
    }

    private void checkGameOver(KalahGame kalehGame, String player, String opponentPlayer) {
        if (player != null && opponentPlayer != null) {
            Optional<Integer> sum = countStonesInPlayerSide(kalehGame.getPits(), player);
            if (sum.isPresent() && sum.get() == 0) {
                Optional<Integer> sumOpponent = countStonesInPlayerSide(kalehGame.getPits(), opponentPlayer);
                kalehGame.getPits().forEach(pit -> {
                    if (pit.getPitType() == Pit.PitType.KALAH && pit.getOwner().equals(opponentPlayer)) {
                        pit.setStoneCounts(pit.getStoneCounts() + sumOpponent.get());
                    }
                });
                kalehGame.setGameState(KalahGame.GameState.OVER);
            }
        }
    }

    private Optional<Integer> countStonesInPlayerSide(List<Pit> pits, String player) {
        return pits.stream()
                .filter(pit -> player.equals(pit.getOwner()) && pit.getPitType() == Pit.PitType.NORMAL)
                .map(Pit::getStoneCounts).reduce(Integer::sum);
    }
}
