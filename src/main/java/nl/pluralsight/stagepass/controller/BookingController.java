package nl.pluralsight.stagepass.controller;
import jakarta.validation.Valid;
import nl.pluralsight.stagepass.model.Booking;
import nl.pluralsight.stagepass.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/concert/{concertId}")
    public ResponseEntity<List<Booking>> getBookingsByConcert(@PathVariable Long concertId) {
        return ResponseEntity.ok(bookingService.getBookingsByConcert(concertId));
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking) {
        Booking created = bookingService.createBooking(booking);

        // Return the created
        return ResponseEntity.status(HttpStatus.CREATED).body(created);

        // 200 response
        // return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {

            bookingService.cancelBooking(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
