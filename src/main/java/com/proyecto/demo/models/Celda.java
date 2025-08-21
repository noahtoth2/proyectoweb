import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OnetoMany;

@Entity
public class Tablero {

    @Id
    @GeneratedValue(strategy=GenerationType.Auto)
    private Long id;

}