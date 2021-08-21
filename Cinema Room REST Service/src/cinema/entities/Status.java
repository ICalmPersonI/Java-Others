package cinema.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Status {
    @JsonProperty("current_income")
    private int currentIncome;

    @JsonProperty("number_of_available_seats")
    private long numberOfAvailableSeats;

    @JsonProperty("number_of_purchased_tickets")
    private int numberOfPurchasedTickets;

    public Status(int currentIncome, long numberOfAvailableSeats, int numberOfPurchasedTickets) {
        this.currentIncome = currentIncome;
        this.numberOfAvailableSeats = numberOfAvailableSeats;
        this.numberOfPurchasedTickets = numberOfPurchasedTickets;
    }

    public int getCurrentIncome() {
        return currentIncome;
    }

    public int getNumberOfPurchasedTickets() {
        return numberOfPurchasedTickets;
    }

    public long getNumberOfAvailableSeats() {
        return numberOfAvailableSeats;
    }
}
