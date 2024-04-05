package rockseat.com.passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rockseat.com.passin.domain.checkin.Checkin;

import java.util.Optional;

public interface CheckinRepository extends JpaRepository<Checkin, Integer> {
    Optional<Checkin> findByAttendeeId(String attendeeId);
}
