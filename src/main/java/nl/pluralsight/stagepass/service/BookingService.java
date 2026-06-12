package nl.pluralsight.stagepass.service;

import nl.pluralsight.stagepass.exception.InsufficientSeatsException;
import nl.pluralsight.stagepass.model.Booking;
import nl.pluralsight.stagepass.model.Concert;
import nl.pluralsight.stagepass.repository.BookingRepository;
import nl.pluralsight.stagepass.repository.ConcertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ConcertRepository concertRepository;

    public BookingService(BookingRepository bookingRepository, ConcertRepository concertRepository) {
        this.bookingRepository = bookingRepository;
        this.concertRepository = concertRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getBookingsByConcert(Long concertId) {
        return bookingRepository.findByConcertId(concertId);
    }

    @Transactional
    public Booking createBooking(Booking booking) {
        Concert concert = concertRepository.findById(booking.getConcert().getId())
                .orElseThrow(() -> new RuntimeException("Concert not found"));

        if (concert.getAvailableSeats() < booking.getNumberOfTickets()) {
            throw new InsufficientSeatsException(String.format(
                    "Not enough seats, only %d seats are available, but %d tickets were requested",
                    concert.getAvailableSeats(),
                    booking.getNumberOfTickets()
                )
            );
        } else {
            // Compute total price
            booking.setTotalPrice(concert.getTicketPrice().multiply
                    (BigDecimal.valueOf(booking.getNumberOfTickets())));

            // Set booking date and concert reference
            booking.setBookingDate(LocalDate.now());
            booking.setConcert(concert);

            // Seat Decrement
            concert.setAvailableSeats(concert.getAvailableSeats() - booking.getNumberOfTickets());

            return bookingRepository.save(booking);
        }
    }

    public boolean cancelBooking(Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
