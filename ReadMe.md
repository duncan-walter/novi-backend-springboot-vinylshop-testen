# Inleiding
Je hebt in de vorige opdracht voor `GenreEntity` een Controller, Service en Repository gemaakt.
Je hebt hetzelfde gedaan voor de `PublisherEntity`.

Heb je de opdracht van vorige week niet gemaakt of niet af gekregen, dan kun je de [voorbeeld uitwerkingen](https://github.com/hogeschoolnovi/backend-springboot-vinylshop-repository-uitwerkingen) clonen.

# Opdrachtbeschrijving

Je gaat in deze opdracht verder werken aan de opdracht die je vorige week gemaakt hebt. 

Je gaat de entities uitbreiden met models en DTO's. 
Uiteraard ga je er ook zorgen dat jouw controllers en services met die DTO's en models werken.
        

# Randvoorwaarden

- Zorg dat je mappenstructuur aan de Maven voorwaarden voldoet
- Je mappen structuur heeft ten minste de packages: 
  - controllers
  - repositories
  - services
  - dtos
  - models 
  - entities
- Je hebt twee controllers:
  - GenreController
  - PublisherController
- Je hebt twee service:
  - GenreService
  - PublisherService
- Je hebt twee repositories:
  - GenreRepository
  - PublisherRepository
- Je hebt drie entiteiten:
  - GenreEntity
  - PublisherEntity
  - BaseEntity
- Je hebt vier DTO's
  - GenreRequestDTO
  - GenreResponseDTO
  - PublisherRequestDTO
  - PublisherResponseDTO
- Je hebt drie models (optioneel)
  - GenreModel
  - PublisherModel
  - BaseModel
- Je hebt vier mappers
  - GenreDTOMapper
  - GenreEntityMapper
  - PublisherDTOMapper
  - PublisherEntityMapper
- Je hebt de juiste instellingen in je application.properties om de database connectie goed tot stand te brengen.
- Je hebt de juiste dependencies aan het project gekoppeld (ook voor validatie).
- Je hebt minimaal één record van beide entiteiten in je data.sql staan.
- Je hebt een export van Postman met up-to-date requests voor beide entiteiten


# Stappenplan

## Stap 0 (packages)

Maak de juiste packages aan. De structuur van de nieuwe packages ziet er als volgt uit:

- models
- dtos
  - genre
  - publisher
- mappers
  - dto
  - entity

## Stap 1 (Response DTO's)

Maak een `dto` map met daarin een een `genre` en een `publisher` map.

Maak een `GenreResponseDto` met de volgende velden:
- id
- name 
- description


Maak een `PublisherResponseDto` met de volgende velden:
- id
- name
- address
- contactDetails

## Stap 2 (Request DTO's)

Maak een `GenreRequestDto` met de volgende validatie regels: 
- De naam mag niet leeg zijn
- De naam mag niet langer dan 100 karakters zijn en niet korter dan 2
- De description mag niet langer dan 255 karakters zijn.

Maak een `PublisherRequestDto` met de volgende validatie regels:
- De naam mag niet leeg zijn
- De naam mag niet langer dan 50 karakters zijn.

Zorg er bij beide DTO's voor dat er ook een goede message gemaakt wordt.
 
## Stap 3 (Models)
Deze stap is optioneel en vooral nuttig wanneer je duidelijke, gescheiden functionaliteiten in je service hebt waarbij je specifieke, afgeleide data nodig hebt. 
Voor dit voorbeeld implementeren we de models wel, ter illustratie.

Maak de `PublisherModel` en de `GenreModel` in de `model` package.
Beide hebben dezelfde inhoud als de bijbehorende entities. 
Laat de klassen overerven van een `BaseModel`.
Het `BaseModel` bevat de volgende data:
- id
- createdDate
- editDate

en de volgende afgeleide data:
- public long getDaysSinceCreated() 
- public boolean isEdited()

## Stap 4 (POM)
Voeg de validatie dependency toe aan je pom.xml.

## Stap 5 (Mappers)

### DTO mappers
De DTO mappers gaan we simpel aanpakken. 

Je maakt een `GenreDTOMapper` met de volgende methodes:
- public GenreResponseDTO mapToDto(GenreModel model)
- public List<GenreResponseDTO> mapToDto(List<GenreModel> models)
- public GenreModel mapToModel(GenreRequestDTO genreModel)

Je maakt een `PublisherDTOMapper` met de volgende methodes:
- public PublisherResponseDTO mapToDto(PublisherModel publisher)
- public List<PublisherResponseDTO> mapToDto(List<PublisherModel> publishers)
- public PublisherModel mapToModel(PublisherRequestDTO dto)

Vergeet niet de juiste annotatie boven de mapper klassen te zetten.

> Merk op dat elke DTOMapper-klasse dus twee methodes heeft met de naam "mapToDto". Één met een model als input en een dto als output en één met een List<model> als input en een List<dto> als output. Dit noemen we "method overloading". 

### Entity mappers

De Entity mapper pakken we iets slimmer aan.  
In plaats van dat we de `BaseModel` elke keer meenemen in de vertaling, zorgen we dat dit automatisch gebeurd door een `EntityMapper` interface te definieren die voor elke soort entity of model in ieder geval de BaseModel en BaseEntity alvast vertaalt.

De interface mag je als volgt definieren:

``` java
public interface EntityMapper<M extends BaseModel, E extends BaseEntity> {
    M fromEntity(E entity);

    E toEntity(M dto);


    default void fromEntityBase(E entity, M model) {
        if (entity == null || model == null) {
            return;
        }
        model.setId(entity.getId());
        model.setCreateDate(entity.getCreateDate());
        model.setEditDate(entity.getEditDate());
    }

    default void toEntityBase(M model, E entity ) {
        if (model == null || entity == null) {
            return;
        }
        entity.setId(model.getId());
        entity.setCreateDate(model.getCreateDate());
        entity.setEditDate(model.getEditDate());
    }

    default List<M> fromEntities(List<E> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    default List<E> toEntities(List<M> models) {
        if (models == null) {
            return null;
        }
        return models.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
```

Maak nu de `GenreEntityMapper` waarin je de volgende methodes vanuit de interface overschrijft:
- public GenreModel fromEntity(GenreEntity entity)
- public GenreEntity toEntity(GenreModel model)

Maak ook de `PublisherEntityMapper` waarin je de volgende methodes vanuit de interface overschrijft:
- public PublisherModel fromEntity(PublisherEntity entity)
- public PublisherEntity toEntity(PublisherModel model)

Maak in beide gevallen gebruik van de `toEntityBase` en `fromEntityBase` default methodes, zodat je niet elke keer de BaseEntity of BaseModel opnieuw hoeft te vertalen.
Vergeet ook hier niet de juiste annotatie boven de mapper klassen te zetten.

## Stap 6 (Service) 

Injecteer de `GenreEntityMapper` in de `GenreService` en injecteer de `PublisherEntityMapper` in de `PublisherService`.

Zorg dat alle methodes die nu een `GenreEntity` naar de controller returnen, aangepast worden zodat ze een `GenreModel` naar de controller returnen.

Maak hierbij gebruik van de `GenreEntityMapper`.

Hier is een voorbeeld van de aangepaste `createGenre` methode: 

```java
   public GenreModel createGenre(GenreModel genreModel) {
        GenreEntity genreEntity = genreEntityMapper.toEntity(genreModel);
        genreEntity = genreRepository.save(genreEntity);
        return genreEntityMapper.fromEntity(genreEntity);
    }

```

Pas de `PublisherService` op dezelfde manier aan.

## Stap 7 (Controller)

Injecteer de `GenreDtoMapper` in de `GenreController` en injecteer de `PublisherDTOMapper` in de `PublisherController`.

Zorg dat jouw controllers, en alle methodes (GET-, POST-, PUT- en DELETE-mappings) gebruik maken van de DTO's als input en als output.

Als voorbeeld hier de aangepaste POST mapping van de GenreController: 
```java
    @PostMapping
    public ResponseEntity<GenreResponseDTO> createGenre(@RequestBody @Valid GenreRequestDTO genreModel) {
        var newGenre = genreService.createGenre(genreDTOMapper.mapToModel(genreModel));
        var genreResponseDTO = genreDTOMapper.mapToDto(newGenre);
        return ResponseEntity.created(urlHelper.getCurrentUrlWithId(genreResponseDTO.getId())).body(genreResponseDTO);
    }
```

Vergeet niet om de `@Valid` annotatie te gebruiken, anders worden je validatie regels niet gevalideerd.



## Stap 8 (Postman)
Voeg een actuele export van je postman collectie toe aan de `resources` map.

Waarschijnlijk kun je hier dezelfde postman export voor gebruiken als in de vorige opdracht, maar controleer het wel even. 

Test vooral ook goed of je geen onverwachte `null` waardes terug krijgt, omdat je een mapper niet goed ingevuld hebt. 

Test ook wat er gebeurt als je een PostMan request verstuurd die de validatieregels overtreedt.

