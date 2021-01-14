package nl.backbase.kalah.db;

import nl.backbase.kalah.model.KalahGame;

import java.util.Optional;

/**
 * @author Mina Sharifi
 */
public interface IKalahGameDb {

    Optional<KalahGame> findById(Integer id);
    KalahGame save(KalahGame kalahGame);
    void update(KalahGame kalahGame);
}
