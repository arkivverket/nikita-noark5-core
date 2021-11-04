package nikita.webapp.service.impl;

import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.metadata.SeriesStatus;
import nikita.common.model.noark5.v5.secondary.StorageLocation;
import nikita.common.repository.n5v5.ISeriesRepository;
import nikita.common.util.exceptions.NoarkEntityEditWhenClosedException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.service.interfaces.ICaseFileService;
import nikita.webapp.service.interfaces.IFileService;
import nikita.webapp.service.interfaces.secondary.IStorageLocationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class SeriesServiceTest {

    @Mock
    private ISeriesRepository seriesRepository;

    @Mock
    private ICaseFileService caseFileService;

    @Mock
    private IFileService fileService;

    @Mock
    private IStorageLocationService storageLocationService;

    private Series createStubClosedSeries() {
        Series series = new Series();
        series.setSeriesStatus(new SeriesStatus(N5ResourceMappings.SERIES_STATUS_CLOSED_CODE));
        return series;
    }

    private SeriesService createSubject() {
        return new SeriesService(
            null,
            null,
            null,
            null,
            null,
            fileService,
            caseFileService,
            null,
            seriesRepository,
            null,
            null,
            storageLocationService,
            null
        );
    }

    @Test
    void createCaseFileAssociatedWithNullSeriesThrowsNoarkEntityNotFoundException() {
        when(seriesRepository.findBySystemId(any(UUID.class))).thenReturn(null);

        SeriesService seriesService = createSubject();


        assertThrows(NoarkEntityNotFoundException.class, () ->
            seriesService.createCaseFileAssociatedWithSeries(UUID.randomUUID(), new CaseFile())
        );
    }

    @Test
    void createCaseFileAssociatedWithClosedSeriesThrowsNoarkEntityEditWhenClosedException() {
        when(seriesRepository.findBySystemId(any(UUID.class))).thenReturn(createStubClosedSeries());

        SeriesService seriesService = createSubject();

        assertThrows(NoarkEntityEditWhenClosedException.class, () ->
            seriesService.createCaseFileAssociatedWithSeries(UUID.randomUUID(), new CaseFile())
        );
    }

    @Test
    void createStorageLocationAssociatedWithNullSeriesThrowsNoarkEntityNotFoundException() {
        when(seriesRepository.findBySystemId(any(UUID.class))).thenReturn(null);
        SeriesService seriesService = createSubject();

        assertThrows(NoarkEntityNotFoundException.class, () ->
            seriesService.createStorageLocationAssociatedWithSeries(UUID.randomUUID(), new StorageLocation())
        );
    }

    @Test
    void createStorageLocationAssociatedWithClosedSeriesThrowsNoarkEntityEditWhenClosedException() {
        when(seriesRepository.findBySystemId(any(UUID.class))).thenReturn(createStubClosedSeries());
        SeriesService seriesService = createSubject();

        assertThrows(NoarkEntityEditWhenClosedException.class, () ->
            seriesService.createStorageLocationAssociatedWithSeries(UUID.randomUUID(), new StorageLocation())
        );
    }

    @Test
    void createFileAssociatedWithNullSeriesThrowsNoarkEntityNotFoundException() {
        when(seriesRepository.findBySystemId(any(UUID.class))).thenReturn(null);
        SeriesService seriesService = createSubject();

        assertThrows(NoarkEntityNotFoundException.class, () ->
            seriesService.createFileAssociatedWithSeries(UUID.randomUUID(), new File())
        );
    }

    @Test
    void createFileAssociatedWithClosedSeriesThrowsNoarkEntityEditWhenClosedException() {
        when(seriesRepository.findBySystemId(any(UUID.class))).thenReturn(createStubClosedSeries());
        SeriesService seriesService = createSubject();

        assertThrows(NoarkEntityEditWhenClosedException.class, () ->
            seriesService.createFileAssociatedWithSeries(UUID.randomUUID(), new File())
        );
    }
}