package gr.technico.technikon.model;

import gr.europeandynamics.web.technico.model.Property;
import gr.europeandynamics.web.technico.model.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 9, max = 9)
    @Column(nullable = false, unique = true)
    private String vat;

    @Size(min = 1, max = 50)
    @NotNull
    private String name;

    @Size(min = 1, max = 50)
    @NotNull
    private String surname;

    @Size(max = 50)
    private String address;

    @Column(name = "phone_number", length = 14)
    private String phoneNumber;

    @Email
    @NotNull
    @Column(unique = true)
    private String email;

    @Size(min = 8, max = 50)
    @NotNull
    private String password;

    @NotNull
    private boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;
   
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Property> properties;
    
    @Override
    public String toString() {
        return "User {"
                + "\n    ID = " + id
                + "\n    VAT = " + vat
                + "\n    Name = " + name
                + "\n    Surname = " + surname
                + "\n    Address = " + address
                + "\n    Phone Number = " + phoneNumber
                + "\n    Email = " + email
                + "\n    Role = " + role
                + "\n}";
    }
}
