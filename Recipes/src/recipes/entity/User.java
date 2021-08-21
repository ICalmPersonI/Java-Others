package recipes.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column
    @Email()
    @Pattern(regexp=".+@.+\\..+")
    private String email;
    @Column
    //@NotBlank
    //@NotNull
    @Pattern(regexp="^[^\\s]+$")
    @Size(min = 8)
    private String password;
    @Column
    private boolean isActive;
    @Column
    private String roles;

    public User(String email, String password, String roles) {
        this.email = email;
        this.password = password;
        this.isActive = true;
        this.roles = roles;

    }
}
