package nl.backbase.kalah.web.dto;

import java.util.Map;

/**
 * @author Mina Sharifi
 */
public class GameStatusInfo extends GameInfo {

    private Map<String, String> status;

    public GameStatusInfo(int id, String uri, Map<String, String> status){
        super(id, uri);
        this.status = status;
    }

    public Map<String, String> getStatus() {
        return status;
    }
}
