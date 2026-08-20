import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class AirlineReservation {

    static class Flight {
        String flightNo;
        String from;
        String to;
        LocalDate travelDate;
        int economySeats;
        int businessSeats;
        int firstSeats;
        double baseFare;

        Flight(String flightNo, String from, String to,
               LocalDate travelDate, int economySeats,
               int businessSeats, int firstSeats,
               double baseFare) {

            this.flightNo = flightNo;
            this.from = from;
            this.to = to;
            this.travelDate = travelDate;
            this.economySeats = economySeats;
            this.businessSeats = businessSeats;
            this.firstSeats = firstSeats;
            this.baseFare = baseFare;
        }
    }

    static class Passenger {
        String name;
        String type; // Adult, Child, Senior
        String seatClass;
        int baggage;

        Passenger(String name, String type,
                  String seatClass, int baggage) {
            this.name = name;
            this.type = type;
            this.seatClass = seatClass;
            this.baggage = baggage;
        }
    }

    static List<Flight> flights = new ArrayList<>();

    // Flight search
    static void searchFlight(String from, String to) {

        System.out.println("\nAvailable Flights:");

        for (Flight f : flights) {
            if (f.from.equalsIgnoreCase(from)
                    && f.to.equalsIgnoreCase(to)) {

                System.out.println(
                        f.flightNo + " | " +
                        f.from + " -> " + f.to +
                        " | " + f.travelDate);
            }
        }
    }

    // Seat availability
    static int availableSeats(Flight f, String seatClass) {

        if (seatClass.equalsIgnoreCase("Economy"))
            return f.economySeats;

        if (seatClass.equalsIgnoreCase("Business"))
            return f.businessSeats;

        if (seatClass.equalsIgnoreCase("First"))
            return f.firstSeats;

        return 0;
    }

    // Dynamic pricing
    static double calculateFare(
            Flight f,
            String seatClass,
            String passengerType,
            LocalDate bookingDate) {

        double fare = f.baseFare;

        // Class price
        if (seatClass.equalsIgnoreCase("Business"))
            fare *= 2;

        else if (seatClass.equalsIgnoreCase("First"))
            fare *= 3;

        // Seat availability pricing
        int seats = availableSeats(f, seatClass);

        if (seats <= 5)
            fare *= 1.50;
        else if (seats <= 10)
            fare *= 1.25;

        // Booking date / travel date pricing
        long days = ChronoUnit.DAYS.between(
                bookingDate, f.travelDate);

        if (days <= 7)
            fare *= 1.30;
        else if (days <= 30)
            fare *= 1.15;

        // Passenger type
        if (passengerType.equalsIgnoreCase("Child"))
            fare *= 0.75;

        else if (passengerType.equalsIgnoreCase("Senior"))
            fare *= 0.80;

        return fare;
    }

    // Baggage charges
    static double baggageCharge(int baggage) {

        if (baggage <= 15)
            return 0;

        return (baggage - 15) * 20;
    }

    // Booking
    static double bookPassenger(
            Flight f,
            Passenger p,
            LocalDate bookingDate) {

        int seats = availableSeats(f, p.seatClass);

        if (seats <= 0) {
            System.out.println("No seats available.");
            return -1;
        }

        double fare = calculateFare(
                f,
                p.seatClass,
                p.type,
                bookingDate);

        double baggage = baggageCharge(p.baggage);

        double total = fare + baggage;

        // Reduce seat
        if (p.seatClass.equalsIgnoreCase("Economy"))
            f.economySeats--;

        else if (p.seatClass.equalsIgnoreCase("Business"))
            f.businessSeats--;

        else if (p.seatClass.equalsIgnoreCase("First"))
            f.firstSeats--;

        System.out.println("\nBooking Successful");
        System.out.println("Passenger: " + p.name);
        System.out.println("Class: " + p.seatClass);
        System.out.printf("Fare: %.2f%n", fare);
        System.out.printf("Baggage Charge: %.2f%n", baggage);
        System.out.printf("Total: %.2f%n", total);

        return total;
    }

    // Cancellation and refund
    static double cancelBooking(
            double amount,
            LocalDate bookingDate,
            LocalDate travelDate) {

        long days = ChronoUnit.DAYS.between(
                bookingDate, travelDate);

        double refund;

        if (days > 30)
            refund = amount * 0.90;

        else if (days > 7)
            refund = amount * 0.75;

        else
            refund = amount * 0.50;

        System.out.printf(
                "Refund Amount: %.2f%n", refund);

        return refund;
    }

    public static void main(String[] args) {

        Flight f1 = new Flight(
                "AI101",
                "Chennai",
                "Delhi",
                LocalDate.of(2026, 12, 20),
                20,
                10,
                5,
                5000
        );

        Flight f2 = new Flight(
                "AI202",
                "Chennai",
                "Mumbai",
                LocalDate.of(2026, 12, 25),
                15,
                8,
                3,
                4000
        );

        flights.add(f1);
        flights.add(f2);

        // Search flight
        searchFlight("Chennai", "Delhi");

        // Seat availability
        System.out.println(
                "\nEconomy Seats: "
                + availableSeats(f1, "Economy"));

        System.out.println(
                "Business Seats: "
                + availableSeats(f1, "Business"));

        System.out.println(
                "First Class Seats: "
                + availableSeats(f1, "First"));

        // Passenger booking
        Passenger p1 = new Passenger(
                "Jayasurya",
                "Adult",
                "Economy",
                20
        );

        double amount = bookPassenger(
                f1,
                p1,
                LocalDate.of(2026, 8, 20));

        // Cancellation
        if (amount > 0) {
            System.out.println("\nCancellation:");
            cancelBooking(
                    amount,
                    LocalDate.of(2026, 8, 20),
                    f1.travelDate);
        }
    }
}
