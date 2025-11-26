package walter.duncan.vinylwebshop.services;

import org.springframework.stereotype.Service;
import walter.duncan.vinylwebshop.entities.GenreEntity;
import walter.duncan.vinylwebshop.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<GenreEntity> findAllGenres() {
        return this.genreRepository.findAll();
    }

    public Optional<GenreEntity> findGenreById(Long id) {
        return this.genreRepository.findById(id);
    }

    public GenreEntity createGenre(GenreEntity genreEntity) {
        return this.genreRepository.save(genreEntity);
    }

    // De opdracht geeft aan dat een id parameter gebruikt moet worden, maar die is (nog) niet nodig.
    public GenreEntity updateGenre(Long id, GenreEntity genreEntity) {
        return this.genreRepository.save(genreEntity);
    }

    public void deleteGenre(Long id) {
        this.genreRepository.deleteById(id);
    }
}