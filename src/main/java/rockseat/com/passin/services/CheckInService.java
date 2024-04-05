package rockseat.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rockseat.com.passin.domain.attendee.Attendee;
import rockseat.com.passin.domain.checkin.Checkin;
import rockseat.com.passin.domain.checkin.exceptions.CheckinAlreadyExistsExceptions;
import rockseat.com.passin.repositories.CheckinRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckInService {
    private final CheckinRepository checkinRepository;

    public void registerCheckin(Attendee attendee){
        this.verifyCheckinExists(attendee.getId());

        Checkin newCheckin = new Checkin();
        newCheckin.setAttendee(attendee);
        newCheckin.setCreatedAt(LocalDateTime.now());

        this.checkinRepository.save(newCheckin);
    }
    
    private void verifyCheckinExists(String attendeeId){
        Optional<Checkin> isCheckedIn = this.getCheckIn(attendeeId);
        if (isCheckedIn.isPresent())throw new CheckinAlreadyExistsExceptions("Attendee already checked in");
    }

    public Optional<Checkin> getCheckIn(String attendeeId){
      return this.checkinRepository.findByAttendeeId(attendeeId);
    }
}

