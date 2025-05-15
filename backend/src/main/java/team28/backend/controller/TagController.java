package team28.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import team28.backend.model.Tag;
import team28.backend.service.TagService;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService TagService;

    public TagController(TagService TagService) {
        this.TagService = TagService;
    }

    @GetMapping
    public List<Tag> GetAllTags() {
        return TagService.GetAllTags();
    }
}
