package no.arkivlab.hioa.nikita.webapp.service.impl;

import nikita.model.noark5.v4.ClassificationSystem;
import nikita.repository.n5v4.IClassificationSystemRepository;
import no.arkivlab.hioa.nikita.webapp.service.interfaces.IClassificationSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ClassificationSystemService implements IClassificationSystemService {

    @Autowired
    IClassificationSystemRepository classificationSystemRepository;

    public ClassificationSystemService() {
    }

    // All CREATE operations
    public ClassificationSystem save(ClassificationSystem classificationSystem){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getName();

        classificationSystem.setSystemId(UUID.randomUUID().toString());
        classificationSystem.setCreatedDate(new Date());
        classificationSystem.setOwnedBy(username);
        classificationSystem.setCreatedBy(username);
        classificationSystem.setDeleted(false);


        return classificationSystemRepository.save(classificationSystem);
    }

    // All READ operations
    public Iterable<ClassificationSystem> findAll() {
        return classificationSystemRepository.findAll();
    }

    public List<ClassificationSystem> findAll(Sort sort) {
        return classificationSystemRepository.findAll(sort);
    }

    public Page<ClassificationSystem> findAll(Pageable pageable) {
        return classificationSystemRepository.findAll(pageable);
    }

    // id
    public ClassificationSystem findById(Long id) {
        return classificationSystemRepository.findById(id);
    }

    // systemId
    public ClassificationSystem findBySystemId(String systemId) {
        return classificationSystemRepository.findBySystemId(systemId);
    }

    // title
    public List<ClassificationSystem> findByTitleAndOwnedBy(String title, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByTitleAndOwnedBy(title,  ownedBy);
    }

    public List<ClassificationSystem> findByTitleContainingAndOwnedBy(String title, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByTitleContainingAndOwnedBy(title, ownedBy);
    }

    public List<ClassificationSystem> findByTitleIgnoreCaseContainingAndOwnedBy(String title, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByTitleIgnoreCaseContainingAndOwnedBy(title, ownedBy);
    }

    public List<ClassificationSystem> findByTitleAndOwnedBy(String title, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByTitleAndOwnedBy(title, ownedBy, sort);
    }

    public List<ClassificationSystem> findByTitleContainingAndOwnedBy(String title, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByTitleContainingAndOwnedBy(title, ownedBy, sort);
    }

    public List<ClassificationSystem> findByTitleIgnoreCaseContainingAndOwnedBy(String title, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByTitleIgnoreCaseContainingAndOwnedBy(title, ownedBy, sort);
    }

    public Page<ClassificationSystem> findByTitleAndOwnedBy(String title, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByTitleAndOwnedBy(title, ownedBy, pageable);
    }

    public Page<ClassificationSystem> findByTitleContainingAndOwnedBy(String title, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByTitleContainingAndOwnedBy(title, ownedBy, pageable);
    }

    public Page<ClassificationSystem> findByTitleIgnoreCaseContainingAndOwnedBy(String title, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByTitleIgnoreCaseContainingAndOwnedBy(title, ownedBy, pageable);
    }

    // description
    public List<ClassificationSystem> findByDescriptionAndOwnedBy(String description, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByDescriptionAndOwnedBy(description, ownedBy);
    }

    public List<ClassificationSystem> findByDescriptionContainingAndOwnedBy(String description, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByDescriptionContainingAndOwnedBy(description, ownedBy);
    }

    public List<ClassificationSystem> findByDescriptionIgnoreCaseContainingAndOwnedBy(String description, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByDescriptionIgnoreCaseContainingAndOwnedBy(description, ownedBy);
    }

    public List<ClassificationSystem> findByDescriptionAndOwnedBy(String description, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByDescriptionAndOwnedBy(description, ownedBy, sort);
    }

    public List<ClassificationSystem> findByDescriptionContainingAndOwnedBy(String description, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByDescriptionContainingAndOwnedBy(description, ownedBy, sort);
    }

    public List<ClassificationSystem> findByDescriptionIgnoreCaseContainingAndOwnedBy(String description, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByDescriptionIgnoreCaseContainingAndOwnedBy(description, ownedBy, sort);
    }

    public Page<ClassificationSystem> findByDescriptionAndOwnedBy(String description, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByDescriptionAndOwnedBy(description, ownedBy, pageable);
    }

    public Page<ClassificationSystem> findByDescriptionContainingAndOwnedBy(String description, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByDescriptionContainingAndOwnedBy(description, ownedBy, pageable);
    }

    public Page<ClassificationSystem> findByDescriptionIgnoreCaseContainingAndOwnedBy(String description, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByDescriptionIgnoreCaseContainingAndOwnedBy(description, ownedBy, pageable);
    }

    // createdDate
    public List<ClassificationSystem> findByCreatedDateAndOwnedBy(Date createdDate, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByCreatedDateAndOwnedBy(createdDate, ownedBy);
    }

    public List<ClassificationSystem> findByCreatedDateAndOwnedBy(Date createdDate, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByCreatedDateAndOwnedBy(createdDate, ownedBy, sort);
    }

    public List<ClassificationSystem> findByCreatedDateBetweenAndOwnedBy(Date start, Date end, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByCreatedDateBetweenAndOwnedBy(start, end, ownedBy);
    }

    public Page<ClassificationSystem> findByCreatedDateAndOwnedBy(Date createdDate, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByCreatedDateAndOwnedBy(createdDate, ownedBy, pageable);
    }

    public Page<ClassificationSystem> findByCreatedDateBetweenAndOwnedBy(Date start, Date end, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByCreatedDateBetweenAndOwnedBy(start, end, ownedBy, pageable);
    }

    // createdBy
    public List<ClassificationSystem> findByCreatedByAndOwnedBy(String createdBy, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByCreatedByAndOwnedBy(createdBy, ownedBy);
    }

    public List<ClassificationSystem> findByCreatedByContainingAndOwnedBy(String createdBy, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByCreatedByContainingAndOwnedBy(createdBy, ownedBy);
    }

    public List<ClassificationSystem> findByCreatedByIgnoreCaseContainingAndOwnedBy(String createdBy, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByCreatedByIgnoreCaseContainingAndOwnedBy(createdBy, ownedBy);
    }

    public List<ClassificationSystem> findByCreatedByAndOwnedBy(String createdBy, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByCreatedByAndOwnedBy(createdBy, ownedBy, sort);
    }

    public List<ClassificationSystem> findByCreatedByContainingAndOwnedBy(String createdBy, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByCreatedByContainingAndOwnedBy(createdBy, ownedBy, sort);
    }

    public List<ClassificationSystem> findByCreatedByIgnoreCaseContainingAndOwnedBy(String createdBy, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByCreatedByIgnoreCaseContainingAndOwnedBy(createdBy, ownedBy, sort);
    }

    public Page<ClassificationSystem> findByCreatedByAndOwnedBy(String createdBy, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByCreatedByAndOwnedBy(createdBy, ownedBy, pageable);
    }

    public Page<ClassificationSystem> findByCreatedByContainingAndOwnedBy(String createdBy, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByCreatedByContainingAndOwnedBy(createdBy, ownedBy, pageable);
    }

    public Page<ClassificationSystem> findByCreatedByIgnoreCaseContainingAndOwnedBy(String createdBy, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByCreatedByIgnoreCaseContainingAndOwnedBy(createdBy, ownedBy, pageable);
    }

    // finalisedDate
    public List<ClassificationSystem> findByFinalisedDateAndOwnedBy(Date finalisedDate, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByFinalisedDateAndOwnedBy(finalisedDate, ownedBy);
    }

    public List<ClassificationSystem> findByFinalisedDateAndOwnedBy(Date finalisedDate, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByFinalisedDateAndOwnedBy(finalisedDate, ownedBy);
    }

    public List<ClassificationSystem> findByFinalisedDateBetweenAndOwnedBy(Date start, Date end, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByFinalisedDateBetweenAndOwnedBy(start, end, ownedBy);
    }

    public Page<ClassificationSystem> findByFinalisedDateAndOwnedBy(Date finalisedDate, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByFinalisedDateAndOwnedBy(finalisedDate, ownedBy, pageable);
    }

    public Page<ClassificationSystem> findByFinalisedDateBetweenAndOwnedBy(Date start, Date end, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByFinalisedDateBetweenAndOwnedBy(start, end, ownedBy, pageable);
    }

    // finalisedBy
    public List<ClassificationSystem> findByFinalisedByAndOwnedBy(String finalisedBy, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByFinalisedByAndOwnedBy(finalisedBy, ownedBy);
    }

    public List<ClassificationSystem> findByFinalisedByContainingAndOwnedBy(String finalisedBy, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByFinalisedByContainingAndOwnedBy(finalisedBy, ownedBy);
    }

    public List<ClassificationSystem> findByFinalisedByIgnoreCaseContainingAndOwnedBy(String finalisedBy, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByFinalisedByIgnoreCaseContainingAndOwnedBy(finalisedBy, ownedBy);
    }

    public List<ClassificationSystem> findByFinalisedByAndOwnedBy(String finalisedBy, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByFinalisedByAndOwnedBy(finalisedBy, ownedBy, sort);
    }

    public List<ClassificationSystem> findByFinalisedByContainingAndOwnedBy(String finalisedBy, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByFinalisedByContainingAndOwnedBy(finalisedBy, ownedBy, sort);
    }

    public List<ClassificationSystem> findByFinalisedByIgnoreCaseContainingAndOwnedBy(String finalisedBy, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByFinalisedByIgnoreCaseContainingAndOwnedBy(finalisedBy, ownedBy, sort);
    }

    public Page<ClassificationSystem> findByFinalisedByAndOwnedBy(String finalisedBy, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByFinalisedByAndOwnedBy(finalisedBy, ownedBy, pageable);
    }

    public Page<ClassificationSystem> findByFinalisedByContainingAndOwnedBy(String finalisedBy, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByFinalisedByContainingAndOwnedBy(finalisedBy, ownedBy, pageable);
    }

    public Page<ClassificationSystem> findByFinalisedByIgnoreCaseContainingAndOwnedBy(String finalisedBy, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByFinalisedByIgnoreCaseContainingAndOwnedBy(finalisedBy, ownedBy, pageable);
    }

    // deleted
    public List<ClassificationSystem> findByDeletedAndOwnedBy(String deleted, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByDeletedAndOwnedBy(deleted, ownedBy);
    }

    public List<ClassificationSystem> findByDeletedAndOwnedBy(String deleted, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByDeletedAndOwnedBy(deleted, ownedBy, sort);
    }

    public Page<ClassificationSystem> findByDeletedAndOwnedBy(String deleted, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByDeletedAndOwnedBy(deleted, ownedBy, pageable);
    }

    // ownedBy
    public List<ClassificationSystem> findByOwnedBy(String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return classificationSystemRepository.findByOwnedBy(ownedBy);
    }

    public List<ClassificationSystem> findByOwnedBy(String ownedBy, Sort sort) {return classificationSystemRepository.findByOwnedBy(ownedBy, sort);}

    public Page<ClassificationSystem> findByOwnedBy(String ownedBy, Pageable pageable) {return classificationSystemRepository.findByOwnedBy(ownedBy, pageable);}

    // All UPDATE operations
    public ClassificationSystem update(ClassificationSystem classificationSystem){

        return classificationSystemRepository.save(classificationSystem);
    }

    public ClassificationSystem updateClassificationSystemSetFinalized(Long id){
        ClassificationSystem classificationSystem = classificationSystemRepository.findById(id);

        if (classificationSystem == null) {
            // throw Object not find
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        classificationSystem.setFinalisedDate(new Date());
        classificationSystem.setFinalisedBy(username);

        return classificationSystemRepository.save(classificationSystem);
    }

    public ClassificationSystem updateClassificationSystemSetTitle(Long id, String newTitle){

        ClassificationSystem classificationSystem = classificationSystemRepository.findById(id);


        return classificationSystemRepository.save(classificationSystem);
    }


    // All DELETE operations


}
