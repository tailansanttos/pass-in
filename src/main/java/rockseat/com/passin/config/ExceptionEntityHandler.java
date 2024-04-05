package rockseat.com.passin.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import rockseat.com.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import rockseat.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import rockseat.com.passin.domain.checkin.exceptions.CheckinAlreadyExistsExceptions;
import rockseat.com.passin.domain.event.exceptions.EventFullException;
import rockseat.com.passin.domain.event.exceptions.EventNotFoundException;
import rockseat.com.passin.dto.general.ErrorResponseDTO;

@ControllerAdvice
public class ExceptionEntityHandler {

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity handleEventNotFound(EventNotFoundException exception){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(EventFullException.class)
    public ResponseEntity<ErrorResponseDTO> handleEventFull(EventFullException exception){
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(exception.getMessage()));
    }

    @ExceptionHandler(AttendeeNotFoundException.class)
    public ResponseEntity handleAttendeeNotFound(AttendeeNotFoundException exception){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AttendeeAlreadyExistException.class)
    public ResponseEntity handleAttendeeAlreadyExistException(AttendeeAlreadyExistException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(CheckinAlreadyExistsExceptions.class)
    public ResponseEntity handleCheckinAlreadyExists(CheckinAlreadyExistsExceptions exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
