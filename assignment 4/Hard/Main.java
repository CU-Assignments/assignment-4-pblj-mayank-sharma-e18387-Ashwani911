import java.util.*;
class TicketBookingSystem {
    private final int totalSeats;
    private final Set<Integer> bookedSeats;
    private final Object lock = new Object();
    public TicketBookingSystem(int totalSeats) {
        this.totalSeats = totalSeats;
        this.bookedSeats = new HashSet<>();
    }
    public void bookSeat(int seatNumber, boolean isVIP) {
        synchronized (lock) {
            if (bookedSeats.contains(seatNumber)) {
                System.out.println((isVIP ? "VIP Booking: " : "Regular Booking: ") + "Error: Seat " + seatNumber + " already booked.");
            } else if (seatNumber < 1 || seatNumber > totalSeats) {
                System.out.println((isVIP ? "VIP Booking: " : "Regular Booking: ") + "Error: Seat " + seatNumber + " is invalid.");
            } else {
                bookedSeats.add(seatNumber);
                System.out.println((isVIP ? "VIP Booking: " : "Regular Booking: ") + "Seat " + seatNumber + " confirmed.");
            }
        }
    }
}
class BookingThread extends Thread {
    private final TicketBookingSystem bookingSystem;
    private final int seatNumber;
    private final boolean isVIP;
    public BookingThread(TicketBookingSystem bookingSystem, int seatNumber, boolean isVIP) {
        this.bookingSystem = bookingSystem;
        this.seatNumber = seatNumber;
        this.isVIP = isVIP;
        if (isVIP) {
            this.setPriority(Thread.MAX_PRIORITY); 
        } else {
            this.setPriority(Thread.NORM_PRIORITY); 
        }
    }
    @Override
    public void run() {
        bookingSystem.bookSeat(seatNumber, isVIP);
    }
}
public class Main {
    public static void main(String[] args) {
        TicketBookingSystem bookingSystem = new TicketBookingSystem(10); // 10 seats available
        Thread vip1 = new BookingThread(bookingSystem, 1, true);
        Thread vip2 = new BookingThread(bookingSystem, 2, true);
        Thread regular1 = new BookingThread(bookingSystem, 1, false); // Attempt to book seat 1 again
        Thread regular2 = new BookingThread(bookingSystem, 3, false);
        vip1.start();
        vip2.start();
        regular1.start();
        regular2.start();
        try {
            vip1.join();
            vip2.join();
            regular1.join();
            regular2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e.getMessage());
        }
        System.out.println("Booking process completed.");
    }
}