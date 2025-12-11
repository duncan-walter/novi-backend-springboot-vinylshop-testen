package walter.duncan.vinylwebshop.dtos.album;

import walter.duncan.vinylwebshop.dtos.genre.GenreResponseDto;
import walter.duncan.vinylwebshop.dtos.publisher.PublisherResponseDto;

public record AlbumResponseDto(Long id, String title, int releaseYear, GenreResponseDto genre, PublisherResponseDto publisher) { }