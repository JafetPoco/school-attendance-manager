package com.IEASmart.sistemaAsistencias.service;

import com.IEASmart.sistemaAsistencias.domain.model.Parent;
import com.IEASmart.sistemaAsistencias.domain.model.Student;
import com.IEASmart.sistemaAsistencias.domain.repository.ParentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ParentService {
    private final ParentRepository parentRepository;

    public ParentService(ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }

    public List<Parent> getAllParents() {
        return parentRepository.getAll();
    }

    public Optional<Parent> getParentById(Long parentId) {
        return parentRepository.findById(parentId);
    }

    public Optional<Parent> getParentByChildId(String studentId) {
        return parentRepository.findByAlumnoId(studentId);
    }

    @Transactional
    public Parent saveParent(Parent parent) {
        if (parent == null) throw new IllegalArgumentException("Parent cannot be null");
        return parentRepository.save(parent);
    }

    @Transactional
    public Optional<Parent> addChildToParent(Long parentId, Student student) {
        Optional<Parent> parentOpt = parentRepository.findById(parentId);
        if (parentOpt.isPresent()) {
            Parent parent = parentOpt.get();
            parent.addChild(student);
            Parent saved = parentRepository.save(parent);
            return Optional.of(saved);
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<Parent> removeChildFromParent(Long parentId, Student student) {
        Optional<Parent> parentOpt = parentRepository.findById(parentId);
        if (parentOpt.isPresent()) {
            Parent parent = parentOpt.get();
            parent.removeChild(student);
            Parent saved = parentRepository.save(parent);
            return Optional.of(saved);
        }
        return Optional.empty();
    }
}
