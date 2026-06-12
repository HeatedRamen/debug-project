package nl.pluralsight.stagepass.controller;

import jakarta.validation.Valid;
import nl.pluralsight.stagepass.model.Concert;
import nl.pluralsight.stagepass.model.ConcertSummary;
import nl.pluralsight.stagepass.service.BookingService;
import nl.pluralsight.stagepass.service.ConcertService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/concerts")
public class ConcertController {

    private final ConcertService concertService;
    private final BookingService bookingService;

    public ConcertController(ConcertService concertService, BookingService bookingService) {
        this.concertService = concertService;
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<Concert>> getAllConcerts() {
        return ResponseEntity.ok(concertService.getAllConcerts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Concert> getConcertById(@PathVariable Long id) {
        return concertService.getConcertById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<Concert>> findArtistId(@PathVariable Long artistId){

        return ResponseEntity.ok(concertService.getConcertsByArtist(artistId));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Concert>> getUpcomingConcerts(){
        return ResponseEntity.ok(concertService.getUpcomingConcerts());
    }

    @GetMapping("{id}/summary")
    public ResponseEntity<ConcertSummary> summarizeConcertById(@PathVariable Long id){
        return ResponseEntity.ok(concertService.summarizeConcert(id));
    }

    @PostMapping
    public ResponseEntity<Concert> createConcert(@RequestBody Concert concert) {
        Concert created = concertService.createConcert(concert);

        // Return the created
        return ResponseEntity.status(HttpStatus.CREATED).body(created);

        // 200 response
        //return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Concert> updateConcert(@PathVariable Long id, @RequestBody Concert concert) {
        return concertService.updateConcert(id, concert)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConcert(@PathVariable Long id) {

        concertService.deleteConcert(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
