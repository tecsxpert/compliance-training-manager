package com.internship.tool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.tool.entity.TrainingRecord;
import com.internship.tool.service.TrainingRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import com.internship.tool.security.JwtUtil;
import com.internship.tool.security.CustomUserDetailsService;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainingRecordController.class)
public class TrainingRecordControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private TrainingRecordService service;

        @MockBean
        private JwtUtil jwtUtil;

        @MockBean
        private CustomUserDetailsService customUserDetailsService;

        @MockBean
        private JpaMetamodelMappingContext jpaMappingContext;

        // ✅ TEST GET ALL (paginated)
        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        void testGetAll() throws Exception {
                TrainingRecord record = new TrainingRecord();
                record.setId(1L);
                record.setTitle("GDPR Training");
                record.setStatus("PENDING");

                var page = new PageImpl<>(List.of(record), PageRequest.of(0, 5), 1);
                when(service.getAll(anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

                mockMvc.perform(get("/api/training"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isArray())
                                .andExpect(jsonPath("$.content[0].title").value("GDPR Training"));
        }

        // ✅ TEST SEARCH
        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        void testSearch() throws Exception {
                TrainingRecord record = new TrainingRecord();
                record.setTitle("Security Training");

                when(service.search(eq("Security"), isNull())).thenReturn(List.of(record));

                mockMvc.perform(get("/api/training/search?q=Security"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].title").value("Security Training"));
        }

        // ✅ TEST STATS
        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        void testStats() throws Exception {
                Map<String, Long> stats = new HashMap<>();
                stats.put("total", 10L);
                stats.put("completed", 4L);
                stats.put("pending", 6L);
                stats.put("overdue", 0L);
                stats.put("inProgress", 0L);

                when(service.getStats()).thenReturn(stats);

                mockMvc.perform(get("/api/training/stats"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.total").value(10))
                                .andExpect(jsonPath("$.completed").value(4))
                                .andExpect(jsonPath("$.pending").value(6));
        }

        // ✅ TEST CREATE
        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        void testCreate() throws Exception {
                TrainingRecord record = new TrainingRecord();
                record.setId(1L);
                record.setTitle("New Training");
                record.setStatus("PENDING");

                when(service.create(any(TrainingRecord.class))).thenReturn(record);

                String body = """
                                {
                                  "title": "New Training",
                                  "status": "PENDING"
                                }
                                """;

                mockMvc.perform(post("/api/training")
                                .with(csrf())
                                .contentType("application/json")
                                .content(body))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("New Training"));
        }

        // ✅ TEST UPDATE
        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        void testUpdate() throws Exception {
                TrainingRecord record = new TrainingRecord();
                record.setId(1L);
                record.setTitle("Updated Training");
                record.setStatus("COMPLETED");

                when(service.update(eq(1L), any(TrainingRecord.class), anyString())).thenReturn(record);

                String body = """
                                {
                                  "title": "Updated Training",
                                  "status": "COMPLETED"
                                }
                                """;

                mockMvc.perform(put("/api/training/1")
                                .with(csrf())
                                .contentType("application/json")
                                .content(body))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Updated Training"));
        }

        // ✅ TEST DELETE
        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        void testDelete() throws Exception {
                mockMvc.perform(delete("/api/training/1").with(csrf()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("Record deleted successfully"));
        }

        // ✅ TEST UPDATE STATUS
        @Test
        @WithMockUser(username = "manager", roles = { "MANAGER" })
        void testUpdateStatus() throws Exception {
                TrainingRecord record = new TrainingRecord();
                record.setId(1L);
                record.setStatus("COMPLETED");

                when(service.updateStatus(eq(1L), eq("COMPLETED"), anyString())).thenReturn(record);

                mockMvc.perform(put("/api/training/1/status")
                                .with(csrf())
                                .param("status", "COMPLETED"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("COMPLETED"));
        }

        // ✅ TEST UNAUTHORIZED ACCESS
        @Test
        void testUnauthorizedAccess() throws Exception {
                mockMvc.perform(get("/api/training"))
                                .andExpect(status().isUnauthorized());
        }
}