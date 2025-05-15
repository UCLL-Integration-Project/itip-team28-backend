package team28.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import team28.backend.model.Tag;
import team28.backend.repository.TagRepository;

@Service
public class TagService {
    private final TagRepository TagRepository;

    public TagService(TagRepository TagRepository) {
        this.TagRepository = TagRepository;
    }

    public List<Tag> GetAllTags() {
        return TagRepository.findAll();
    }
}
