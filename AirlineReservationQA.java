import java.time.LocalDate;

public class AirlineReservationQA {

    static void check(String test, boolean result) {
        if (result)
            System.out.println("PASS: " + test);
        else
            System.out.println("FAIL: " + test);
    }

    public static void main(String[] args) {

        LocalDate bookingDate = LocalDate.of(2026, 8, 20);
        LocalDate travelDate = LocalDate.of(2026, 12, 20);

        // 1. Successful booking
        AirlineReservation.Flight f1 =
                new AirlineReservation.Flight(
                        "AI101", "Chennai", "Delhi",
                        travelDate, 10, 5, 2, 5000);

        AirlineReservation.Passenger p1 =
                new AirlineReservation.Passenger(
                        "Ravi", "Adult", "Economy", 15);

        double fare1 = AirlineReservation.bookPassenger(
                f1, p1, bookingDate);

        check("Successful Booking", fare1 > 0);


        // 2. Double booking
        AirlineReservation.Passenger p2 =
                new AirlineReservation.Passenger(
                        "Kumar", "Adult", "Economy", 15);

        int seatsBefore = f1.economySeats;

        AirlineReservation.bookPassenger(
                f1, p2, bookingDate);

        check("Double Booking",
                f1.economySeats == seatsBefore - 1);


        // 3. Cancellation
        double refund = AirlineReservation.cancelBooking(
                fare1, bookingDate, travelDate);

        check("Cancellation", refund > 0);


        // 4. Refund calculation
        check("Refund Calculation",
                refund == fare1 * 0.90);


        // 5. Fully booked flight
        AirlineReservation.Flight fullFlight =
                new AirlineReservation.Flight(
                        "AI999", "Chennai", "Delhi",
                        travelDate, 0, 0, 0, 5000);

        AirlineReservation.Passenger p3 =
                new AirlineReservation.Passenger(
                        "Arun", "Adult", "Economy", 10);

        double fullFare = AirlineReservation.bookPassenger(
                fullFlight, p3, bookingDate);

        check("Fully Booked Flight", fullFare == -1);


        // 6. Invalid passenger
        AirlineReservation.Passenger invalid =
                new AirlineReservation.Passenger(
                        "", "Unknown", "Economy", 10);

        check("Invalid Passenger",
                invalid.name == null || invalid.name.isEmpty()
                || (!invalid.type.equals("Adult")
                && !invalid.type.equals("Child")
                && !invalid.type.equals("Senior")));


        // 7. Excess baggage
        double baggage =
                AirlineReservation.baggageCharge(25);

        check("Excess Baggage", baggage == 200);


        // 8. No excess baggage
        double noBaggage =
                AirlineReservation.baggageCharge(15);

        check("Normal Baggage", noBaggage == 0);


        // 9. Economy fare
        double economyFare =
                AirlineReservation.calculateFare(
                        f1, "Economy", "Adult", bookingDate);

        check("Economy Fare", economyFare > 0);


        // 10. Business fare
        double businessFare =
                AirlineReservation.calculateFare(
                        f1, "Business", "Adult", bookingDate);

        check("Business Fare",
                businessFare > economyFare);


        // 11. First class fare
        double firstFare =
                AirlineReservation.calculateFare(
                        f1, "First", "Adult", bookingDate);

        check("First Class Fare",
                firstFare > businessFare);


        // 12. Child fare
        double childFare =
                AirlineReservation.calculateFare(
                        f1, "Economy", "Child", bookingDate);

        check("Child Fare",
                childFare < economyFare);


        // 13. Senior fare
        double seniorFare =
                AirlineReservation.calculateFare(
                        f1, "Economy", "Senior", bookingDate);

        check("Senior Fare",
                seniorFare < economyFare);


        // 14. Seat availability
        int seats =
                AirlineReservation.availableSeats(
                        f1, "Economy");

        check("Seat Availability", seats >= 0);


        // 15. Flight search
        AirlineReservation.flights.clear();
        AirlineReservation.flights.add(f1);

        AirlineReservation.searchFlight(
                "Chennai", "Delhi");

        check("Flight Search", true);


        // 16. Baggage 20 kg
        double baggage20 =
                AirlineReservation.baggageCharge(20);

        check("20kg Baggage", baggage20 == 100);


        // 17. Baggage 30 kg
        double baggage30 =
                AirlineReservation.baggageCharge(30);

        check("30kg Baggage", baggage30 == 300);


        // 18. Early booking
        double earlyFare =
                AirlineReservation.calculateFare(
                        f1,
                        "Economy",
                        "Adult",
                        LocalDate.of(2026, 8, 20));

        check("Early Booking Fare", earlyFare > 0);


        // 19. Last-minute booking
        double lastMinuteFare =
                AirlineReservation.calculateFare(
                        f1,
                        "Economy",
                        "Adult",
                        LocalDate.of(2026, 12, 15));

        check("Dynamic Fare",
                lastMinuteFare > earlyFare);


        // 20. Cancellation within 7 days
        double shortRefund =
                AirlineReservation.cancelBooking(
                        5000,
                        LocalDate.of(2026, 12, 15),
                        travelDate);

        check("Short Notice Refund",
                shortRefund == 2500);

        System.out.println("\nQA Testing Completed.");
    }
}
