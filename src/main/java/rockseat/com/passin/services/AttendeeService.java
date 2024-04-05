package rockseat.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import rockseat.com.passin.domain.attendee.Attendee;
import rockseat.com.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import rockseat.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import rockseat.com.passin.domain.checkin.Checkin;
import rockseat.com.passin.dto.attendee.AttendeeBadgeResponseDTO;
import rockseat.com.passin.dto.attendee.AttendeeDetails;
import rockseat.com.passin.dto.attendee.AttendeesListResponseDTO;
import rockseat.com.passin.dto.attendee.AttendeeBadgeDTO;
import rockseat.com.passin.repositories.AttendeeRepository;
import rockseat.com.passin.repositories.CheckinRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckInService checkInService;

    public List<Attendee> getAllAttendeesFromEvent(String eventId){
       return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventsAttendee(String eventId) {
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
          Optional<Checkin>  checkIn = this.checkInService.getCheckIn(attendee.getId());
            LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(Checkin::getCreatedAt).orElse(null);
            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailsList);
    }

    public void verifyAttendeeSubscription(String email, String eventId){
      Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
      if (isAttendeeRegistered.isPresent()) throw new AttendeeAlreadyExistException("Attendee is already registered");
    }


    public Attendee registerAttendee (Attendee newAttendee){
        this.attendeeRepository.save(newAttendee);
        return newAttendee;
    }

    public void checkinAttendees(String attendeeId){
        Attendee attendee = this.getAttendee(attendeeId);
        this.checkInService.registerCheckin(attendee);

    }

    private Attendee getAttendee(String attendeeId){
        return this.attendeeRepository.findById(attendeeId).orElseThrow(() -> new AttendeeNotFoundException("attendee not found with ID: " + attendeeId));
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();

        Attendee attendee = this.getAttendee(attendeeId);

        AttendeeBadgeDTO attendeeBadgeDTO = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId());
        return new AttendeeBadgeResponseDTO(attendeeBadgeDTO);
    }
}
