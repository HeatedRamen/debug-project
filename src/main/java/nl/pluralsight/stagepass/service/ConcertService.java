package nl.pluralsight.stagepass.service;

import nl.pluralsight.stagepass.exception.InvalidIdException;
import nl.pluralsight.stagepass.model.Concert;
import nl.pluralsight.stagepass.model.ConcertSummary;
import nl.pluralsight.stagepass.repository.ConcertRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ConcertService {

    private final ConcertRepository concertRepository;

    public ConcertService(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    public List<Concert> getAllConcerts() {
        return concertRepository.findAll();
    }

    public Optional<Concert> getConcertById(Long id) {
        return concertRepository.findById(id);
    }

    public List<Concert> getConcertsByArtist(Long artistId) {
        return concertRepository.findByArtistId(artistId);
    }

    public Concert createConcert(Concert concert) {
        return concertRepository.save(concert);
    }

    public Optional<Concert> updateConcert(Long id, Concert updatedConcert) {
        return concertRepository.findById(id).map(existing -> {
            existing.setTitle(updatedConcert.getTitle());
            existing.setDate(updatedConcert.getDate());
            existing.setArtist(updatedConcert.getArtist());
            existing.setVenue(updatedConcert.getVenue());
            existing.setTotalSeats(updatedConcert.getTotalSeats());
            existing.setAvailableSeats(updatedConcert.getAvailableSeats());
            existing.setTicketPrice(updatedConcert.getTicketPrice());
            return concertRepository.save(existing);
        });
    }

    public List<Concert> getUpcomingConcerts() {
        return concertRepository.findByDateAfterOrderByDateAsc(LocalDate.now());
    }

    public ConcertSummary summarizeConcert(Long id) {
        Concert concert = concertRepository.findById(id)
                .orElseThrow(() -> new InvalidIdException("Concert not found"));

        int seatsBooked = concert.getTotalSeats() - concert.getAvailableSeats();

        return new ConcertSummary(concert.getId(),
                                  concert.getTitle(),
                                  concert.getTotalSeats(),
                                  concert.getAvailableSeats(),
                                  seatsBooked,
                                  concert.getTicketPrice().multiply(BigDecimal.valueOf(seatsBooked)));
    }

    public boolean deleteConcert(Long id) {
        if (concertRepository.existsById(id)) {
            concertRepository.deleteById(id);
            return true;
        }
        return false;
    }


}
