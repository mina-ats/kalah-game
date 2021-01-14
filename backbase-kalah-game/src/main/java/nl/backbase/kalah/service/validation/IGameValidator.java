package nl.backbase.kalah.service.validation;

import nl.backbase.kalah.model.KalahGame;

/**
 * @author Mina Sharifi
 */
public interface IGameValidator {

    void preValidation(KalahGame kalahGame, String userId, int pitId);

    void postValidation(KalahGame kalahGame);
}
