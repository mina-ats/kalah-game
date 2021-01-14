package nl.backbase.kalah.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nl.backbase.kalah.model.KalahGame;
import nl.backbase.kalah.service.KalahService;
import nl.backbase.kalah.web.dto.GameInfo;
import nl.backbase.kalah.web.dto.GameStatusInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Mina Sharifi
 */
@RestController
@Api(value = "Kalah Game")
public class KalahController {

    private KalahService kalahService;
    private static final Logger LOGGER = LoggerFactory.getLogger(KalahController.class);

    @Value("${HOSTNAME:server.address}")
    private String hostname;

    public KalahController(KalahService kalahService) {
        this.kalahService = kalahService;
    }

    private void iplog(HttpServletRequest request){
        String[] ips={"X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA",
                "REMOTE_ADDR",
                "X-Real-IP",
                "x-real-ip",
                "VIA",
                "Origin",
                "proxy_add_x_forwarded_for",
                "x-forwarded-for",
                "http_host"};

        Arrays.stream(ips).forEach(s -> LOGGER.info(s + " : " + request.getHeader(s)));
    }

    @PostMapping("/games")
    @ApiOperation(value = "Create a new game")
    public ResponseEntity<GameInfo> createGame(HttpServletRequest request) {
        LOGGER.info("Request type :"+ request.getClass());
        LOGGER.info("hostname : {}", hostname);
        LOGGER.info("session id "+ request.getRequestedSessionId());
        LOGGER.info("Remote address {}",request.getRemoteAddr());
        LOGGER.info("Remote host {}",request.getRemoteHost());
        LOGGER.info("Remote user {}",request.getRemoteUser());
        LOGGER.info("RequestURL {}",request.getRequestURL());
        iplog(request);
        String userIP = Optional.ofNullable(request.getHeader("X-FORWARDED-FOR")).orElse(request.getRemoteAddr());
        KalahGame kalahGame = kalahService.createKalahGame(userIP);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GameInfo(kalahGame.getId(), "http://" + request.getRemoteHost()+ ":"+ request.getRemotePort() + "/games/" + kalahGame.getId()));
    }

    @PutMapping("/games/{gameId}/pits/{pitId}")
    @ApiOperation(value = "Make a move")
    public ResponseEntity<GameStatusInfo> moveStones(@PathVariable("gameId") int gameId,
                                               @PathVariable("pitId") int pitId,
                                               HttpServletRequest request) {
        LOGGER.info("X-FORWARDED-FOR value {}", request.getHeader("X-FORWARDED-FOR"));
        String userIP = Optional.ofNullable(request.getHeader("X-FORWARDED-FOR")).orElse(request.getRemoteAddr());
        LOGGER.info("Game id : {}, Pit id : {}, User IP : {}", gameId, pitId, userIP);
        KalahGame updatedGame = kalahService.move(gameId, pitId, userIP);
        Map<String, String> status = updatedGame.getPits().stream().collect(Collectors.toMap(pit -> String.valueOf(pit.getId()), pit -> String.valueOf(pit.getStoneCounts())));
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GameStatusInfo(updatedGame.getId(), "http://" +request.getRemoteHost()+ ":"+ request.getRemotePort() + "/games/" + updatedGame.getId(), status));
    }

}
