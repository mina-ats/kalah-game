package nl.backbase.kalah.service.validation;

/**
 * @author Mina Sharifi
 */
public class ValidationException extends RuntimeException{

    public ValidationException(String message){
        super(message);
    }
}
