package team28.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team28.backend.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

}
