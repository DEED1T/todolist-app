package m2sdl.prjdevops.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tache")
public class Tache {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @NotNull
    private String titre;

    @NotEmpty
    @NotNull
    private String texte;

    private Boolean isDone = false;

    @NotNull
    @NotEmpty
    private String utilisateur;

    @DateTimeFormat
    public LocalDateTime date;

    public Tache(String titre, String description, String utilisateur) {
        this.titre = titre;
        this.texte = description;
        this.utilisateur = utilisateur;
    }
}
