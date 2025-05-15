package team28.backend.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import team28.backend.model.Tag;
import team28.backend.repository.TagRepository;
import team28.backend.service.TagService;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @Mock
    private TagRepository TagRepository;

    @InjectMocks
    private TagService TagService;

    @Mock
    private Tag tag;

    @BeforeEach
    void setUp() {
        tag = new Tag("Tag123");
        tag.setId(1L);
    }

    @Test
    public void givenAllScans_whenAllScansIsRequested_thenGiveListOfAllScans() {
        List<Tag> tags = List.of(tag);
        when(TagRepository.findAll()).thenReturn(tags);

        List<Tag> result = TagService.GetAllTags();

        assertEquals(1, result.size());
        assertEquals("Tag123", result.get(0).getName());
        verify(TagRepository, times(1)).findAll();
    }
}
