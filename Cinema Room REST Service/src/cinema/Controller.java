package cinema;

import cinema.entities.*;
import cinema.entities.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
public class Controller {

    private final Cinema cinema = new Cinema(9, 9);

    @PostMapping("/stats")
    public ResponseEntity<?> stats(@RequestParam (value = "password") Optional<String> password) {
        if (password.isPresent() && "super_secret".equals(password.get())) {
            return  ResponseEntity.ok(new Status(cinema.getCurrentIncome(),
                    cinema.getNumberOfAvailableSeats(),
                    cinema.getNumberOfPurchasedTickets()));
        } else {
            return ResponseEntity.status(401).body(new Error("The password is wrong!"));
        }
    }


    @GetMapping("/seats")
    public Cinema seats() {
        return cinema;
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnTicket(@RequestBody Token token) {
        Optional<Ticket> ticket = cinema.findTicketByUUID(token.getToken());
        if (ticket.isPresent() && ticket.get().isPurchased()) {
            return ResponseEntity.ok(cinema.returnTicket(ticket.get().getIndex()));
        } else {
            return ResponseEntity.status(400).body(new Error("Wrong token!"));
        }
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchase(@RequestBody Place place) {
        int row = place.getRow();
        int column = place.getColumn();
        Optional<Ticket> ticket = cinema.findTicketByRowAndColumn(row, column);

        if (ticket.isPresent()) {
            if (ticket.get().isPurchased()) {
                return ResponseEntity.status(400).body(new Error("The ticket has been already purchased!"));
            }

            cinema.buyTicket(ticket.get().getIndex());
            return ResponseEntity.ok(ticket.get().toString());

        } else {
            return ResponseEntity.status(400).body(new Error("The number of a row or a column is out of bounds!"));
        }
    }
}
