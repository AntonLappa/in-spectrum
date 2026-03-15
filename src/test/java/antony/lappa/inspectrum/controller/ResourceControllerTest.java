package antony.lappa.inspectrum.controller;

import antony.lappa.inspectrum.controller.dto.resource.ResourceResponseDto;
import antony.lappa.inspectrum.mapper.ResourceMapper;
import antony.lappa.inspectrum.service.model.Resource;
import antony.lappa.inspectrum.service.resource.ResourceService;
import antony.lappa.inspectrum.service.token.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResourceController.class)
@AutoConfigureMockMvc(addFilters = false)
class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ResourceService resourceService;

    @MockitoBean
    private ResourceMapper resourceMapper;

    @MockitoBean
    private TokenService tokenService;

    @Test
    void listResources_shouldReturn200() throws Exception {
        //given
        Resource resource = new Resource();
        resource.setId(UUID.randomUUID());

        ResourceResponseDto dto = new ResourceResponseDto();
        dto.setId(resource.getId());
        dto.setTitle("Test Resource");

        when(resourceService.listResource(any(), any(), any(), any(), eq(20), eq(0)))
                .thenReturn(List.of(resource));
        when(resourceMapper.toResponseDto(resource)).thenReturn(dto);

        //when & then
        mockMvc.perform(get("/resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Resource"));
    }

    @Test
    void getResourceById_shouldReturn200() throws Exception {
        //given
        UUID resourceId = UUID.randomUUID();

        Resource resource = new Resource();
        resource.setId(resourceId);

        ResourceResponseDto dto = new ResourceResponseDto();
        dto.setId(resourceId);
        dto.setTitle("Test Resource");

        when(resourceService.getResourceById(resourceId)).thenReturn(resource);
        when(resourceMapper.toResponseDto(resource)).thenReturn(dto);

        //when & then
        mockMvc.perform(get("/resources/{resourceId}", resourceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(resourceId.toString()))
                .andExpect(jsonPath("$.title").value("Test Resource"));
    }
}
