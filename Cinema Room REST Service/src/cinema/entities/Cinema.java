package cinema.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class Cinema {
    @JsonProperty("total_rows")
    private final int totalRows;

    @JsonProperty("total_columns")
    private final int totalColumns;

    @JsonProperty("available_seats")
    private final Ticket[] availableSeats;

    private int currentIncome = 0;
    private int numberOfPurchasedTickets = 0;

    public Cinema(int totalRows, int totalColumns) {
        this.totalRows = totalRows;
        this.totalColumns = totalColumns;
        this.availableSeats = new Ticket[81];

        for (int index = 0, row = 1; row < totalRows + 1; row++) {
            for (int column = 1; column < totalColumns + 1; index++, column++) {
                availableSeats[index] = new Ticket(index, UUID.randomUUID(), row, column, row <= 4 ? 10 : 8);
            }
        }
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getTotalColumns() {
        return totalColumns;
    }

    public Ticket[] getAvailableSeats() {
        return availableSeats;
    }

    @JsonIgnore
    public int getCurrentIncome() {
        return currentIncome;
    }

    @JsonIgnore
    public long getNumberOfAvailableSeats() {
        return Stream.of(availableSeats).filter(elem -> !elem.isPurchased()).count();
    }

    @JsonIgnore
    public int getNumberOfPurchasedTickets() {
        return numberOfPurchasedTickets;
    }

    public Optional<Ticket> findTicketByUUID(String uuid) {
        for (Ticket ticket : availableSeats) {
            if (ticket.getId().compareTo(UUID.fromString(uuid)) == 0)
                return Optional.of(ticket);
        }
        return Optional.empty();
    }

    public Optional<Ticket> findTicketByRowAndColumn(int row, int column) {
        for (Ticket ticket : availableSeats) {
            if (ticket.getRow() == row && ticket.getColumn() == column)
                return Optional.of(ticket);
        }
        return Optional.empty();
    }

    public String returnTicket(int index) {
        availableSeats[index].setPurchased(false);
        currentIncome = currentIncome - availableSeats[index].getPrice();
        numberOfPurchasedTickets--;

        return "{\n" +
                "    \"returned_ticket\": {\n" +
                "        \"row\": " + availableSeats[index].getRow() + ",\n" +
                "        \"column\": " + availableSeats[index].getColumn() + ",\n" +
                "        \"price\": " + availableSeats[index].getPrice() + "\n" +
                "    }\n" +
                "}";
    }
    public void buyTicket(int index) {
        availableSeats[index].setPurchased(true);
        currentIncome = currentIncome + availableSeats[index].getPrice();
        numberOfPurchasedTickets++;
    }
}
