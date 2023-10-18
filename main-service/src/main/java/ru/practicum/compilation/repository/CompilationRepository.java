package ru.practicum.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query("select comp " +
            "from Compilation as comp " +
            "where (:pinned is null OR comp.pinned = :pinned)")
    List<Compilation> findAllCompilations(@Param("pinned") Boolean pinned, Pageable pageable);
}
