package com.example.videogamesshop.service;

import com.example.videogamesshop.dto.publisher.PublisherCreateRequest;
import com.example.videogamesshop.dto.publisher.PublisherFullResponse;
import com.example.videogamesshop.entity.Publisher;
import com.example.videogamesshop.exception.PublisherNotFoundException;
import com.example.videogamesshop.mapper.PublisherMapper;
import com.example.videogamesshop.repository.PublisherRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public List<PublisherFullResponse> getAllPublishers() {
        return publisherRepository.findAll().stream()
                .map(PublisherMapper::toFullResponse)
                .toList();
    }

    public PublisherFullResponse getPublisherById(Long id) {
        return PublisherMapper.toFullResponse(findPublisherById(id));
    }

    public PublisherFullResponse createPublisher(PublisherCreateRequest request) {
        Publisher publisher = PublisherMapper.toEntity(request);
        Publisher saved = publisherRepository.save(publisher);
        return PublisherMapper.toFullResponse(saved);
    }

    public PublisherFullResponse updatePublisher(Long id, PublisherCreateRequest request) {
        Publisher publisher = findPublisherById(id);
        PublisherMapper.updateEntity(publisher, request);
        return PublisherMapper.toFullResponse(publisher);
    }

    public void deletePublisher(Long id) {
        if (!publisherRepository.existsById(id)) {
            throw new PublisherNotFoundException(id);
        }
        publisherRepository.deleteById(id);
    }

    private Publisher findPublisherById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException(id));
    }
}
