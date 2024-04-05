package rockseat.com.passin.domain.checkin.exceptions;

public class CheckinAlreadyExistsExceptions extends  RuntimeException{

    public CheckinAlreadyExistsExceptions(String message){
        super(message);
    }
}
