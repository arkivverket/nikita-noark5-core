package no.arkivlab.hioa.nikita.webapp.service.impl;

import nikita.model.noark5.v4.Series;
import nikita.repository.n5v4.ISeriesRepository;
import no.arkivlab.hioa.nikita.webapp.service.interfaces.ISeriesService;
import no.arkivlab.hioa.nikita.webapp.util.validation.Utils;
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

import static nikita.config.N5ResourceMappings.STATUS_CLOSED;

@Service
@Transactional
public class SeriesService implements ISeriesService {

    @Autowired
    ISeriesRepository seriesRepository;

    public SeriesService() {
    }

    // All CREATE operations
    public Series save(Series series){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getName();

        if (!Utils.checkDocumentMediumValid(series.getDocumentMedium())) {
            // throw an error! Something is wrong. Either null or incorrect value
        }

        series.setSystemId(UUID.randomUUID().toString());
        series.setCreatedDate(new Date());
        series.setOwnedBy(username);
        series.setCreatedBy(username);
        series.setDeleted(false);

        // Have to handle referenceToFonds. If it is not set do not allow persisit
        // throw illegalstructure exception

        // How do handle referenceToPrecusor? Update the entire object?? No patch?

        return seriesRepository.save(series);
    }

    // All READ operations
    public Iterable<Series> findAll() {
        return seriesRepository.findAll();
    }

    public List<Series> findAll(Sort sort) {
        return seriesRepository.findAll(sort);
    }

    public Page<Series> findAll(Pageable pageable) {
        return seriesRepository.findAll(pageable);
    }

    // id
    public Series findById(Long id) {
        return seriesRepository.findById(id);
    }

    // systemId
    public Series findBySystemId(String systemId) {
        return seriesRepository.findBySystemId(systemId);
    }

    // title
    public List<Series> findByTitleAndOwnedBy(String title, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByTitleAndOwnedBy(title,  ownedBy);
    }

    public List<Series> findByTitleContainingAndOwnedBy(String title, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByTitleContainingAndOwnedBy(title, ownedBy);
    }

    public List<Series> findByTitleIgnoreCaseContainingAndOwnedBy(String title, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByTitleIgnoreCaseContainingAndOwnedBy(title, ownedBy);
    }

    public List<Series> findByTitleAndOwnedBy(String title, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByTitleAndOwnedBy(title, ownedBy, sort);
    }

    public List<Series> findByTitleContainingAndOwnedBy(String title, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByTitleContainingAndOwnedBy(title, ownedBy, sort);
    }

    public List<Series> findByTitleIgnoreCaseContainingAndOwnedBy(String title, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByTitleIgnoreCaseContainingAndOwnedBy(title, ownedBy, sort);
    }

    public Page<Series> findByTitleAndOwnedBy(String title, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByTitleAndOwnedBy(title, ownedBy, pageable);
    }

    public Page<Series> findByTitleContainingAndOwnedBy(String title, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByTitleContainingAndOwnedBy(title, ownedBy, pageable);
    }

    public Page<Series> findByTitleIgnoreCaseContainingAndOwnedBy(String title, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByTitleIgnoreCaseContainingAndOwnedBy(title, ownedBy, pageable);
    }

    // description
    public List<Series> findByDescriptionAndOwnedBy(String description, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByDescriptionAndOwnedBy(description, ownedBy);
    }

    public List<Series> findByDescriptionContainingAndOwnedBy(String description, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByDescriptionContainingAndOwnedBy(description, ownedBy);
    }

    public List<Series> findByDescriptionIgnoreCaseContainingAndOwnedBy(String description, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByDescriptionIgnoreCaseContainingAndOwnedBy(description, ownedBy);
    }

    public List<Series> findByDescriptionAndOwnedBy(String description, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByDescriptionAndOwnedBy(description, ownedBy, sort);
    }

    public List<Series> findByDescriptionContainingAndOwnedBy(String description, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByDescriptionContainingAndOwnedBy(description, ownedBy, sort);
    }

    public List<Series> findByDescriptionIgnoreCaseContainingAndOwnedBy(String description, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByDescriptionIgnoreCaseContainingAndOwnedBy(description, ownedBy, sort);
    }

    public Page<Series> findByDescriptionAndOwnedBy(String description, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByDescriptionAndOwnedBy(description, ownedBy, pageable);
    }

    public Page<Series> findByDescriptionContainingAndOwnedBy(String description, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByDescriptionContainingAndOwnedBy(description, ownedBy, pageable);
    }

    public Page<Series> findByDescriptionIgnoreCaseContainingAndOwnedBy(String description, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByDescriptionIgnoreCaseContainingAndOwnedBy(description, ownedBy, pageable);
    }

    // fondStatus
    public List<Series> findBySeriesStatusAndOwnedBy(String seriesStatus, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findBySeriesStatusAndOwnedBy(seriesStatus, ownedBy);
    }

    public List<Series> findBySeriesStatusAndOwnedBy(String seriesStatus, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findBySeriesStatusAndOwnedBy(seriesStatus, ownedBy, sort);
    }

    public Page<Series> findBySeriesStatusAndOwnedBy(String seriesStatus, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findBySeriesStatusAndOwnedBy(seriesStatus, ownedBy, pageable);
    }

    // documentMedium
    public List<Series> findByDocumentMediumAndOwnedBy(String documentMedium, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByDocumentMediumAndOwnedBy(documentMedium, ownedBy);
    }

    public List<Series> findByDocumentMediumAndOwnedBy(String documentMedium, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByDocumentMediumAndOwnedBy(documentMedium, ownedBy, sort);
    }

    public Page<Series> findByDocumentMediumAndOwnedBy(String documentMedium, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByDocumentMediumAndOwnedBy(documentMedium, ownedBy, pageable);
    }

    // createdDate
    public List<Series> findByCreatedDateAndOwnedBy(Date createdDate, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByCreatedDateAndOwnedBy(createdDate, ownedBy);
    }

    public List<Series> findByCreatedDateAndOwnedBy(Date createdDate, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByCreatedDateAndOwnedBy(createdDate, ownedBy, sort);
    }

    public List<Series> findByCreatedDateBetweenAndOwnedBy(Date start, Date end, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByCreatedDateBetweenAndOwnedBy(start, end, ownedBy);
    }

    public Page<Series> findByCreatedDateAndOwnedBy(Date createdDate, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByCreatedDateAndOwnedBy(createdDate, ownedBy, pageable);
    }

    public Page<Series> findByCreatedDateBetweenAndOwnedBy(Date start, Date end, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByCreatedDateBetweenAndOwnedBy(start, end, ownedBy, pageable);
    }

    // createdBy
    public List<Series> findByCreatedByAndOwnedBy(String createdBy, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByCreatedByAndOwnedBy(createdBy, ownedBy);
    }

    public List<Series> findByCreatedByContainingAndOwnedBy(String createdBy, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByCreatedByContainingAndOwnedBy(createdBy, ownedBy);
    }

    public List<Series> findByCreatedByIgnoreCaseContainingAndOwnedBy(String createdBy, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByCreatedByIgnoreCaseContainingAndOwnedBy(createdBy, ownedBy);
    }

    public List<Series> findByCreatedByAndOwnedBy(String createdBy, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByCreatedByAndOwnedBy(createdBy, ownedBy, sort);
    }

    public List<Series> findByCreatedByContainingAndOwnedBy(String createdBy, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByCreatedByContainingAndOwnedBy(createdBy, ownedBy, sort);
    }

    public List<Series> findByCreatedByIgnoreCaseContainingAndOwnedBy(String createdBy, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByCreatedByIgnoreCaseContainingAndOwnedBy(createdBy, ownedBy, sort);
    }

    public Page<Series> findByCreatedByAndOwnedBy(String createdBy, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByCreatedByAndOwnedBy(createdBy, ownedBy, pageable);
    }

    public Page<Series> findByCreatedByContainingAndOwnedBy(String createdBy, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByCreatedByContainingAndOwnedBy(createdBy, ownedBy, pageable);
    }

    public Page<Series> findByCreatedByIgnoreCaseContainingAndOwnedBy(String createdBy, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByCreatedByIgnoreCaseContainingAndOwnedBy(createdBy, ownedBy, pageable);
    }

    // finalisedDate
    public List<Series> findByFinalisedDateAndOwnedBy(Date finalisedDate, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByFinalisedDateAndOwnedBy(finalisedDate, ownedBy);
    }

    public List<Series> findByFinalisedDateAndOwnedBy(Date finalisedDate, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByFinalisedDateAndOwnedBy(finalisedDate, ownedBy);
    }

    public List<Series> findByFinalisedDateBetweenAndOwnedBy(Date start, Date end, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByFinalisedDateBetweenAndOwnedBy(start, end, ownedBy);
    }

    public Page<Series> findByFinalisedDateAndOwnedBy(Date finalisedDate, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByFinalisedDateAndOwnedBy(finalisedDate, ownedBy, pageable);
    }

    public Page<Series> findByFinalisedDateBetweenAndOwnedBy(Date start, Date end, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByFinalisedDateBetweenAndOwnedBy(start, end, ownedBy, pageable);
    }

    // finalisedBy
    public List<Series> findByFinalisedByAndOwnedBy(String finalisedBy, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByFinalisedByAndOwnedBy(finalisedBy, ownedBy);
    }

    public List<Series> findByFinalisedByContainingAndOwnedBy(String finalisedBy, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByFinalisedByContainingAndOwnedBy(finalisedBy, ownedBy);
    }

    public List<Series> findByFinalisedByIgnoreCaseContainingAndOwnedBy(String finalisedBy, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByFinalisedByIgnoreCaseContainingAndOwnedBy(finalisedBy, ownedBy);
    }

    public List<Series> findByFinalisedByAndOwnedBy(String finalisedBy, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByFinalisedByAndOwnedBy(finalisedBy, ownedBy, sort);
    }

    public List<Series> findByFinalisedByContainingAndOwnedBy(String finalisedBy, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByFinalisedByContainingAndOwnedBy(finalisedBy, ownedBy, sort);
    }

    public List<Series> findByFinalisedByIgnoreCaseContainingAndOwnedBy(String finalisedBy, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByFinalisedByIgnoreCaseContainingAndOwnedBy(finalisedBy, ownedBy, sort);
    }

    public Page<Series> findByFinalisedByAndOwnedBy(String finalisedBy, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByFinalisedByAndOwnedBy(finalisedBy, ownedBy, pageable);
    }

    public Page<Series> findByFinalisedByContainingAndOwnedBy(String finalisedBy, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByFinalisedByContainingAndOwnedBy(finalisedBy, ownedBy, pageable);
    }

    public Page<Series> findByFinalisedByIgnoreCaseContainingAndOwnedBy(String finalisedBy, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByFinalisedByIgnoreCaseContainingAndOwnedBy(finalisedBy, ownedBy, pageable);
    }

    // deleted
    public List<Series> findByDeletedAndOwnedBy(String deleted, String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByDeletedAndOwnedBy(deleted, ownedBy);
    }

    public List<Series> findByDeletedAndOwnedBy(String deleted, String ownedBy, Sort sort) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByDeletedAndOwnedBy(deleted, ownedBy, sort);
    }

    public Page<Series> findByDeletedAndOwnedBy(String deleted, String ownedBy, Pageable pageable) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByDeletedAndOwnedBy(deleted, ownedBy, pageable);
    }

    // ownedBy
    public List<Series> findByOwnedBy(String ownedBy) {
        ownedBy = (ownedBy == null) ? SecurityContextHolder.getContext().getAuthentication().getName():ownedBy;
        return seriesRepository.findByOwnedBy(ownedBy);
    }

    public List<Series> findByOwnedBy(String ownedBy, Sort sort) {return seriesRepository.findByOwnedBy(ownedBy, sort);}

    public Page<Series> findByOwnedBy(String ownedBy, Pageable pageable) {return seriesRepository.findByOwnedBy(ownedBy, pageable);}

    // All UPDATE operations
    public Series update(Series series){
        if (series.getSeriesStatus() == STATUS_CLOSED) {
            //throw an exception back
        }
        return seriesRepository.save(series);
    }

    public Series updateSeriesSetFinalized(Long id){
        Series series = seriesRepository.findById(id);

        if (series == null) {
            // throw Object not find
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        series.setSeriesStatus(STATUS_CLOSED);
        series.setFinalisedDate(new Date());
        series.setFinalisedBy(username);

        return seriesRepository.save(series);
    }

    public Series updateSeriesSetTitle(Long id, String newTitle){

        Series series = seriesRepository.findById(id);

        if (series == null) {
            // throw Object not find
        } else if (series.getSeriesStatus().equals(STATUS_CLOSED)) {
            // throw Object finalises, cannot be edited
        }
        series.setTitle(newTitle);
        return seriesRepository.save(series);
    }


    // All DELETE operations


}
