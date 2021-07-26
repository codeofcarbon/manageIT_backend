package com.in.demo.manage.manageit.service;

import com.in.demo.manage.manageit.error.DataNotFoundException;
import com.in.demo.manage.manageit.model.Sprint;
import com.in.demo.manage.manageit.repository.SprintRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.in.demo.manage.manageit.data.TestsData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SprintServiceTest {

    @Mock
    private SprintRepository repository;

    @InjectMocks
    private SprintService service;

    @Test
    void testGetAllSprints() {
        var s1 = generateSampleSprint();
        var s2 = generateSampleSprint();
        when(repository.findAll()).thenReturn(List.of(s1, s2));

        List<Sprint> actual = service.getAllSprints();

        assertThat(actual)
                .hasSize(2)
                .containsExactly(s1, s2)
                .doesNotContainNull();

        assertThat(actual.get(0))
                .isEqualTo(s1);
    }

    @Test
    void testGetSprintById_WhenSuccess() throws DataNotFoundException {
        Sprint s1 = generateSampleSprint();

        when(repository.findById(s1.getId())).thenReturn(Optional.of(s1));

        Sprint actual = service.getSprintById(s1.getId());

        assertThat(actual).isEqualTo(s1);
    }

    @Test
    void testGetSprintById_ShouldThrowException_WhenNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Throwable throwable = Assertions.assertThrows(DataNotFoundException.class,
                () -> service.getSprintById(anyLong()));

        assertThat(throwable).isExactlyInstanceOf(DataNotFoundException.class);
    }

    @Test
    void testAddNewSprint_WhenSuccess() throws DataNotFoundException {
        var sprint = generateSampleSprint();
        sprint.setId(null);

        when(repository.save(sprint)).thenReturn(generateSampleSprint());

        Sprint actual = service.addNewSprint(sprint);

        assertNotNull(actual.getId());
        assertEquals(actual.getName(), sprint.getName());
        assertEquals(actual.getStartDate(), sprint.getStartDate());
        assertEquals(actual.getEndDate(), sprint.getEndDate());
        assertEquals(actual.getStoryPointsToSpend(), sprint.getStoryPointsToSpend());
        assertEquals(actual.getTasks(), sprint.getTasks());
        assertEquals(actual.isActive(), sprint.isActive());
    }

    @Test
    void testAddNewSprint_ShouldThrowException_WhenIdIsNotNull() {
        var sprint = generateSampleSprint();
        assertThrows(IllegalArgumentException.class, () -> service.addNewSprint(sprint));
    }

    @Test
    void testDeleteSprint_WhenSuccess() {
        var sprint = generateSampleSprint();

        service.deleteSprint(sprint.getId());

        verify(repository, times(1)).deleteById(sprint.getId());
    }

    @Test
    void testDeleteSprint_WhenNotExist() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> service.getSprintById(10000L));
    }

    @Test
    void testUpdateSprint_WhenSuccess() throws DataNotFoundException {
        var s1 = generateSampleSprint();
        var s2 = generateSampleSprint();

        when(repository.findById(s1.getId())).thenReturn(Optional.of(s1));
        s2 = service.updateSprint(s1);

        assertEquals(s1.getId(), s2.getId());
        assertEquals(s1.getName(), s2.getName());
        assertEquals(s1.getStartDate(), s2.getStartDate());
        assertEquals(s1.getEndDate(), s2.getEndDate());
        assertEquals(s1.getStoryPointsToSpend(), s2.getStoryPointsToSpend());
        assertEquals(s1.getTasks(), s2.getTasks());
        assertEquals(s1.isActive(), s2.isActive());
    }
}