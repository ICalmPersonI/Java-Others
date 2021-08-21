package cinema.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public class Ticket {
    private int index;
    private UUID id;
    private int row;
    private int column;
    private int price;
    private boolean purchased = false;

    Ticket(int index, UUID id, int row, int column, int price) {
        this.index = index;
        this.id = id;
        this.row = row;
        this.column = column;
        this.price = price;

    }

    @JsonIgnore
    public int getIndex() {
        return index;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getPrice() {
        return price;
    }

    @JsonIgnore
    public UUID getId() {
        return id;
    }

    @JsonIgnore
    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    @Override
    public String toString() {
        return "{\n" +
                "    \"token\": \"" + id.toString() + "\",\n" +
                "    \"ticket\": {\n" +
                "        \"row\": " + row + ",\n" +
                "        \"column\": " + column + ",\n" +
                "        \"price\": " + price + "\n" +
                "    }\n" +
                "}";
    }
}
