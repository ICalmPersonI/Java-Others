package recipes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;


@Data
@Getter
@NoArgsConstructor
@Entity
@Table(name = "Recipes")
public class Recipe {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(AccessLevel.PRIVATE)
    @Setter
    private int id;
    @Column
    @NotBlank
    private String name;
    @Column
    @NotBlank
    private String category;
    @Column
    @Setter
    private LocalDateTime date;
    @Column
    @NotBlank
    private String description;
    @Column
    @Size(min = 1)
    @NotNull
    private String[] ingredients;
    @Column
    @Size(min = 1)
    @NotNull
    private String[] directions;
    @Column
    @JsonIgnore
    private String creator;

    public int id() {
        return id;
    }
}
