package rockseat.com.passin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rockseat.com.passin.dto.attendee.AttendeeBadgeDTO;
import rockseat.com.passin.dto.attendee.AttendeeBadgeResponseDTO;
import rockseat.com.passin.services.AttendeeService;
import rockseat.com.passin.services.CheckInService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attendees")
public class AttendeeController {
    private final AttendeeService attendeeService;



    @GetMapping("/{attendeeId}/badge")
    public ResponseEntity <AttendeeBadgeResponseDTO> getAttendeeBadge(@PathVariable String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        AttendeeBadgeResponseDTO response = this.attendeeService.getAttendeeBadge(attendeeId, uriComponentsBuilder);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{attendeeId}/check-in")
    public ResponseEntity registerCheckin(@PathVariable String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        this.attendeeService.checkinAttendees(attendeeId);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(attendeeId).toUri();

        return ResponseEntity.created(uri).build();


    }
}
