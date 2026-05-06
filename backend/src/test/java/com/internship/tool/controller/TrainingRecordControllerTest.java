package com.internship.tool.controller;

import com.internship.tool.config.JacksonConfig;
import com.internship.tool.config.JpaConfig;
import com.internship.tool.entity.TrainingRecord;
import com.internship.tool.repository.TrainingRecordRepository;
import com.internship.tool.repository.AuditLogRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = TrainingRecordController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JpaConfig.class
    ),
    excludeAutoConfiguration = {
        RedisAutoConfiguration.class,
        RedisRepositoriesAutoConfiguration.class
    }
)
@Import(JacksonConfig.class)
@TestPropertySource(properties = {
    "spring.cache.type=none"
})
public class TrainingRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingRecordRepository repository;

    @MockBean
    private AuditLogRepository auditRepository;

    @MockBean
    private UserDetailsService userDetailsService;

    // ✅ TEST GET ALL
    @Test
    @WithMockUser
    void testGetAll() throws Exception {
        // Use PageImpl to get proper content/totalElements/totalPages structure
        when(repository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        mockMvc.perform(get("/api/training"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    // ✅ TEST SEARCH
    @Test
    @WithMockUser
    void testSearch() throws Exception {
        when(repository.findByTitleContainingIgnoreCase("test"))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/training/search?q=test"))
                .andExpect(status().isOk());
    }

    // ✅ TEST STATS
    @Test
    @WithMockUser
    void testStats() throws Exception {
        when(repository.count()).thenReturn(5L);
        when(repository.findByStatus("COMPLETED")).thenReturn(new ArrayList<>());
        when(repository.findByStatus("PENDING")).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/training/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(5))
                .andExpect(jsonPath("$.completed").value(0))
                .andExpect(jsonPath("$.pending").value(0));
    }

    // ✅ TEST UPDATE - needs csrf() for PUT/DELETE
    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdate() throws Exception {

        TrainingRecord record = new TrainingRecord();
        record.setId(1L);
        record.setTitle("Original");

        when(repository.findById(1L)).thenReturn(Optional.of(record));
        when(repository.save(any(TrainingRecord.class))).thenReturn(record);

        String body = """
                {
                  "title": "Updated"
                }
                """;

        mockMvc.perform(put("/api/training/1?role=ADMIN")
                        .with(csrf())
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());
    }

    // ✅ TEST DELETE - needs csrf() for DELETE
    @Test
    @WithMockUser(roles = "ADMIN")
    void testDelete() throws Exception {

        TrainingRecord record = new TrainingRecord();
        record.setId(1L);
        record.setTitle("Test");

        when(repository.findById(1L)).thenReturn(Optional.of(record));
        when(repository.save(any(TrainingRecord.class))).thenReturn(record);

        mockMvc.perform(delete("/api/training/1?role=ADMIN")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}