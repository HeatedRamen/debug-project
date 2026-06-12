package nl.pluralsight.stagepass.model;

import java.math.BigDecimal;

public record ConcertSummary(
        Long id,
        String concertTitle,
        int totalSeats,
        int availableSeats,
        int bookedSeats,
        BigDecimal totalRevenue
) {}






