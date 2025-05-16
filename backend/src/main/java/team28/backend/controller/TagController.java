package team28.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team28.backend.model.Tag;
import team28.backend.service.TagService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.GetAllTags();
    }

    @GetMapping("/data")
    public List<Map<String, String>> getTagData() {
        return tagService.getTagDataFromInflux();
    }
}
