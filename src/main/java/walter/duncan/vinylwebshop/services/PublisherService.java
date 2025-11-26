package walter.duncan.vinylwebshop.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import walter.duncan.vinylwebshop.entities.PublisherEntity;
import walter.duncan.vinylwebshop.repositories.PublisherRepository;

import java.util.List;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<PublisherEntity> findAllPublishers() {
        return this.publisherRepository.findAll();
    }

    public PublisherEntity findPublisherById(Long id) {
        return this.getExistingById(id);
    }

    public PublisherEntity createPublisher(PublisherEntity publisherEntity) {
        publisherEntity.setId(null);

        return this.publisherRepository.save(publisherEntity);
    }

    public PublisherEntity updatePublisher(Long id, PublisherEntity publisherEntity) {
        var persistedEntity = this.getExistingById(id);
        persistedEntity.setName(publisherEntity.getName());
        persistedEntity.setAddress(publisherEntity.getAddress());

        return this.publisherRepository.save(persistedEntity);
    }

    public void deletePublisher(Long id) {
        this.ensureExistsById(id);
        this.publisherRepository.deleteById(id);
    }

    private void ensureExistsById(Long id) {
        if (!this.publisherRepository.existsById(id)) {
            this.throwNotFound(id);
        }
    }

    private PublisherEntity getExistingById(Long id) {
        var publisherEntity = this.publisherRepository.findById(id);

        if (publisherEntity.isEmpty()) {
            this.throwNotFound(id);
        }

        return publisherEntity.get();
    }

    private void throwNotFound(Long id) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Publisher with id " + id + " not found"
        );
    }
}
