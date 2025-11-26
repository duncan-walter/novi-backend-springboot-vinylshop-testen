package walter.duncan.vinylwebshop.services;

import walter.duncan.vinylwebshop.entities.GenreEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Deze GenreService is een tijdelijke oplossing om een echte GenreService na te bootsen.
 * In de volgende les zul je deze GenreService moeten aanpassen of een nieuwe GenreService maken,
 * zodat het gebruik maakt van een database in plaats van een ArrayList.
 * De beschikbare methodes in deze Service zijn:
 * - findAllGenres
 * - findGenreById
 * - createGenre
 * - updateGenre
 * - deleteGenre
 */
@Service
public class GenreService {
    private final ArrayList<GenreEntity> genreRepository;

    public GenreService() {
        genreRepository = new ArrayList<>();
    }

    /**
     * Haalt alle record uit de mock-database op.
     * Als de mock-database leeg is, wordt een lege lijst geretourneerd.
     *
     * @return
     */
    public List<GenreEntity> findAllGenres() {
        return genreRepository;
    }

    /**
     * Haalt een bestaande Genre-record op uit de mock-database op basis van het id.
     * Als er geen record bestaat met dat id, wordt een Exception opgegooid.
     *
     * @param id
     * @return
     */
    public GenreEntity findGenreById(Long id) {
        return genreRepository.stream().filter(g -> g.getId().equals(id)).findFirst().orElseThrow(() -> new IndexOutOfBoundsException("Genre met ID " + id + " niet gevonden"));
    }

    /**
     * Slaat een nieuw Genre-record op in de mock-database en maakt daarbij een uniek ID aan voor het object.
     *
     * @param genre Het te creëren en op te slaan genre. Moet niet `null` zijn.
     * @return Het opgeslagen Genre-object met het toegekende id.
     */
    public GenreEntity createGenre(GenreEntity genre) {
        genre.setId(findNextId(genreRepository));
        genreRepository.add(genre);

        return genre;
    }

    /**
     * Update een bestaande Genre-record uit de mock-database op basis van het id.
     *
     * @param id
     * @param genreInput
     * @return
     */
    public GenreEntity updateGenre(Long id, GenreEntity genreInput) {
        GenreEntity existingGenreEntity = findGenreById(id);

        existingGenreEntity.setName(genreInput.getName());
        existingGenreEntity.setDescription(genreInput.getDescription());

        return existingGenreEntity;
    }

    /**
     * Verwijderd een Genre uit de mock-database op basis van het id
     *
     * @param id
     */
    public void deleteGenre(Long id) {
        try {
            GenreEntity existingGenreEntity = findGenreById(id);
            genreRepository.remove(existingGenreEntity);
        } catch (IndexOutOfBoundsException e) {
        }
    }

    /**
     * Een database maakt automatisch de volgende, unieke Primary Key voor je.
     * Deze helper-methode bootst die functionaliteit na in de ArrayList.
     */
    private Long findNextId(ArrayList<GenreEntity> genreRepository) {
        Long highest = 0L;

        if (!genreRepository.isEmpty()) {
            for (GenreEntity genre : genreRepository) {
                if (genre.getId() > highest) {
                    highest = genre.getId();
                }
            }
        }

        return highest + 1;
    }
}
