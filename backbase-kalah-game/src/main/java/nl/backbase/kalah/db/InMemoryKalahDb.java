package nl.backbase.kalah.db;

import nl.backbase.kalah.model.KalahGame;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mina Sharifi
 */
@Component
public class InMemoryKalahDb implements IKalahGameDb {

    private ConcurrentMap<Integer, KalahGame> kalahMap;
    private AtomicInteger idGenerator = new AtomicInteger(0);

    public InMemoryKalahDb(){
        kalahMap = new ConcurrentHashMap<>();
    }

    public KalahGame save(KalahGame kalahGame){
        kalahGame.setId(idGenerator.incrementAndGet());
        kalahMap.put(kalahGame.getId(), kalahGame);
        return kalahGame;
    }

    public Optional<KalahGame> findById(Integer id){
        return Optional.of(kalahMap.get(id));
    }

    @Override
    public void update(KalahGame kalahGame) {
        // it is already updated
    }
}
