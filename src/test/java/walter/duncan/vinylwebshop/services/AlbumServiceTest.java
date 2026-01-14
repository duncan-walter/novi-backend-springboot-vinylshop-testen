package walter.duncan.vinylwebshop.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;
import walter.duncan.vinylwebshop.dtos.album.AlbumExtendedResponseDto;
import walter.duncan.vinylwebshop.dtos.album.AlbumRequestDto;
import walter.duncan.vinylwebshop.dtos.album.AlbumResponseDto;
import walter.duncan.vinylwebshop.dtos.artist.ArtistResponseDto;
import walter.duncan.vinylwebshop.dtos.genre.GenreResponseDto;
import walter.duncan.vinylwebshop.dtos.publisher.PublisherResponseDto;
import walter.duncan.vinylwebshop.dtos.stock.StockResponseDto;
import walter.duncan.vinylwebshop.entities.*;
import walter.duncan.vinylwebshop.mappers.AlbumDtoMapper;
import walter.duncan.vinylwebshop.mappers.AlbumExtendedDtoMapper;
import walter.duncan.vinylwebshop.repositories.AlbumRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {
    @InjectMocks
    private AlbumService albumService;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private AlbumDtoMapper albumDtoMapper;

    @Mock
    private AlbumExtendedDtoMapper albumExtendedDtoMapper;

    @Mock
    private GenreService genreService;

    @Mock
    private PublisherService publisherService;

    @Mock
    private ArtistService artistService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findAllAlbums_shouldReturnListOfAlbumExtendedResponseDtos() {
        // Arrange
        var albumExtendedResponseDtos = List.of(
                AlbumTestData.albumExtendedResponseDto(1L, 1L),
                AlbumTestData.albumExtendedResponseDto(1L, 2L)
        );

        var genreEntity = GenreTestData.genreEntity(1L);
        var publisherEntity = PublisherTestData.publisherEntity(1L);

        var stockEntity = new StockEntity();
        ReflectionTestUtils.setField(stockEntity, "id", 1L);
        stockEntity.setCondition("Used");
        stockEntity.setPrice(10.0);

        var artistEntity = new ArtistEntity();
        ReflectionTestUtils.setField(artistEntity, "id", 1L);
        artistEntity.setName("Mr. Skeleton");
        artistEntity.setBiography("He has lived his year spooking many people with his music, now he's dead. Serves him right.");

        var albumEntity1 = AlbumTestData.albumEntity(1L);
        albumEntity1.setGenre(genreEntity);
        albumEntity1.setPublisher(publisherEntity);
        albumEntity1.setStockItems(new HashSet<>(List.of(stockEntity)));
        albumEntity1.setArtists(new HashSet<>(List.of(artistEntity)));

        var albumEntity2 = AlbumTestData.albumEntity(2L);
        albumEntity1.setGenre(genreEntity);
        albumEntity1.setPublisher(publisherEntity);
        albumEntity1.setStockItems(new HashSet<>(List.of()));
        albumEntity1.setArtists(new HashSet<>(List.of(artistEntity)));

        var albumEntities = List.of(
                albumEntity1,
                albumEntity2
        );

        when(albumRepository.findAll()).thenReturn(albumEntities);
        when(albumExtendedDtoMapper.toDto(albumEntities)).thenReturn(albumExtendedResponseDtos);

        // Act
        var result = albumService.findAllAlbums();

        // Assert
        assertEquals(2, result.size());
        assertEquals(albumExtendedResponseDtos.getFirst(), result.getFirst());
        assertEquals(albumExtendedResponseDtos.get(1), result.get(1));
        verify(albumRepository, times(1)).findAll();
        verify(albumExtendedDtoMapper, times(1)).toDto(albumEntities);
    }

    @Test
    void findAlbumById_shouldReturnExtendedAlbumDto() {
        // Arrange
        var albumId = 2L;
        var albumExtendedResponseDto = AlbumTestData.albumExtendedResponseDto(albumId, 2L);
        var albumEntity = AlbumTestData.albumEntity(albumId);

        when(albumRepository.findById(albumId)).thenReturn(Optional.of(albumEntity));
        when(albumExtendedDtoMapper.toDto(albumEntity)).thenReturn(albumExtendedResponseDto);

        // Act
        var result = albumService.findAlbumById(albumId);

        // Assert
        assertEquals(albumExtendedResponseDto, result);
        verify(albumRepository, times(1)).findById(albumId);
        verify(albumExtendedDtoMapper, times(1)).toDto(albumEntity);
    }

    @Test
    void createAlbum_withExistingGenreAndPublisher_shouldReturnCreatedAlbumResponseDto() {
        // Arrange
        var albumId = 1L;
        var genreId = 1L;
        var publisherId = 1L;
        var albumRequestDto = AlbumTestData.albumRequestDto();
        var albumResponseDto = AlbumTestData.albumResponseDto(albumId);
        var mappedAlbumEntity = AlbumTestData.albumEntity();
        var createdAlbumEntity = AlbumTestData.albumEntity(albumId);
        var genreEntity = GenreTestData.genreEntity(genreId);
        var publisherEntity = PublisherTestData.publisherEntity(publisherId);

        when(albumRepository.save(mappedAlbumEntity)).thenReturn(createdAlbumEntity);
        when(albumDtoMapper.toEntity(albumRequestDto)).thenReturn(mappedAlbumEntity);
        when(genreService.getExistingById(genreId)).thenReturn(genreEntity);
        when(publisherService.getExistingById(publisherId)).thenReturn(publisherEntity);
        when(albumDtoMapper.toDto(createdAlbumEntity)).thenReturn(albumResponseDto);

        // Act
        var result = albumService.createAlbum(albumRequestDto);

        // Assert
        assertEquals(albumResponseDto, result);
        verify(albumRepository, times(1)).save(mappedAlbumEntity);
        verify(genreService, times(1)).getExistingById(genreId);
        verify(publisherService, times(1)).getExistingById(publisherId);
    }

    @Test
    void updateAlbum() {
    }

    @Test
    void deleteAlbum() {
    }

    @Test
    void linkArtist() {
    }

    @Test
    void unlinkArtist() {
    }
}

// TODO: Introduce builder
//  Used in tests as a proof of concept, this can be expanded to a proper builder pattern that builds complex album relations.
class AlbumTestData {
    private AlbumTestData() { }

    public static AlbumEntity albumEntity() {
        return albumEntity(null);
    }

    public static AlbumEntity albumEntity(Long id) {
        var albumEntity = new AlbumEntity();

        if (id != null) {
            // Using reflection since there is no setter for the id field and there is no point introducing it in production code since it's not used.
            ReflectionTestUtils.setField(albumEntity, "id", id);
        }

        albumEntity.setTitle("The Nightmare Before Christmas");
        albumEntity.setReleaseYear(1993);
        albumEntity.setGenre(GenreTestData.genreEntity(1L));
        albumEntity.setPublisher(PublisherTestData.publisherEntity(1L));

        return albumEntity;
    }

    public static AlbumRequestDto albumRequestDto() {
        var albumRequestDto = new AlbumRequestDto();
        ReflectionTestUtils.setField(albumRequestDto, "title", "The Nightmare Before Christmas");
        ReflectionTestUtils.setField(albumRequestDto, "releaseYear", 1993);
        ReflectionTestUtils.setField(albumRequestDto, "genreId", 1L);
        ReflectionTestUtils.setField(albumRequestDto, "publisherId", 1L);

        return albumRequestDto;
    }

    public static AlbumResponseDto albumResponseDto(Long id) {
        return new AlbumResponseDto(
                id,
                "The Nightmare Before Christmas",
                1993,
                new GenreResponseDto(1L, "Halloween", "Very spooky"),
                new PublisherResponseDto(1L, "Spooky records", "1 Spooky avenue", "+1 217-703-2085")
        );
    }

    public static AlbumExtendedResponseDto albumExtendedResponseDto(Long id, Long stockId) {
        return new AlbumExtendedResponseDto(
                id,
                "The Nightmare Before Christmas",
                1993,
                new GenreResponseDto(1L, "Halloween", "Very spooky"),
                new PublisherResponseDto(1L, "Spooky records", "1 Spooky avenue", "+1 217-703-2085"),
                List.of(new StockResponseDto(stockId, "Used", 10.0)),
                List.of(new ArtistResponseDto(1L, "Mr. Skeleton", "He has lived his year spooking many people with his music, now he's dead. Serves him right."))
        );
    }
}

class GenreTestData {
    private GenreTestData() { }

    public static GenreEntity genreEntity(Long id) {
        var genreEntity = new GenreEntity();

        if (id != null) {
            ReflectionTestUtils.setField(genreEntity, "id", id);
        }

        genreEntity.setName("Halloween");
        genreEntity.setDescription("Very spooky");

        return genreEntity;
    }
}

class PublisherTestData {
    private PublisherTestData() { }

    public static PublisherEntity publisherEntity(Long id) {
        var publisherEntity = new PublisherEntity();

        if (id != null) {
            ReflectionTestUtils.setField(publisherEntity, "id", id);
        }

        publisherEntity.setName("Spooky records");
        publisherEntity.setAddress("1 Spooky avenue");
        publisherEntity.setContactDetails("+1 217-703-2085");

        return publisherEntity;
    }
}