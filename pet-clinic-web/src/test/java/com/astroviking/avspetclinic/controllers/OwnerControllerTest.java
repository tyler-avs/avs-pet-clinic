package com.astroviking.avspetclinic.controllers;

import com.astroviking.avspetclinic.model.Owner;
import com.astroviking.avspetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

  @Mock OwnerService ownerService;

  @InjectMocks OwnerController ownerController;

  Set<Owner> owners;

  MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    owners = new HashSet<>();
    owners.add(Owner.builder().id(1L).build());
    owners.add(Owner.builder().id(2L).build());

    mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
  }

  @Test
  void findOwners() throws Exception {
    mockMvc
        .perform(get("/owners/find"))
        .andExpect(status().isOk())
        .andExpect(view().name("owners/findOwners"))
        .andExpect(model().attributeExists("owner"));

    verifyNoInteractions(ownerService);
  }

  @Test
  void processFindFormReturnMany() throws Exception {
    when(ownerService.findAllByLastNameLike(anyString()))
        .thenReturn(Arrays.asList(Owner.builder().id(1L).build(), Owner.builder().id(2L).build()));

    mockMvc
        .perform(get("/owners"))
        .andExpect(status().isOk())
        .andExpect(view().name("owners/ownersList"))
        .andExpect(model().attribute("selections", hasSize(2)));
  }

  @Test
  void processFindFormReturnOne() throws Exception {
    when(ownerService.findAllByLastNameLike(anyString()))
        .thenReturn(Collections.singletonList(Owner.builder().id(1L).build()));

    mockMvc
        .perform(get("/owners"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/owners/1"));
  }

  @Test
  void displayOwner() throws Exception {
    Long ownerId = 1L;
    Owner owner = Owner.builder().id(1L).build();
    when(ownerService.findById(ownerId)).thenReturn(owner);

    mockMvc
        .perform(get("/owners/" + ownerId))
        .andExpect(status().isOk())
        .andExpect(view().name("owners/ownerDetails"))
        .andExpect(model().attribute("owner", hasProperty("id", is(ownerId))));
  }

  @Test
  void initCreationForm() throws Exception {
    mockMvc
        .perform(get("/owners/new"))
        .andExpect(status().isOk())
        .andExpect(view().name("owners/createOrUpdateOwnerForm"))
        .andExpect(model().attributeExists("owner"));

    verifyNoInteractions(ownerService);
  }

  @Test
  void processCreationForm() throws Exception {
    when(ownerService.save(any())).thenReturn(Owner.builder().id(1L).build());

    mockMvc
        .perform(post("/owners/new"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/owners/1"))
        .andExpect(model().attributeExists("owner"));

    verify(ownerService).save(any());
  }

  @Test
  void initUpdateOwnerForm() throws Exception {
    when(ownerService.findById(anyLong())).thenReturn(Owner.builder().id(1L).build());

    mockMvc
        .perform(get("/owners/1/edit"))
        .andExpect(status().isOk())
        .andExpect(view().name("owners/createOrUpdateOwnerForm"))
        .andExpect(model().attributeExists("owner"));

    verify(ownerService).findById(1L);
  }

  @Test
  void processUpdateOwnerForm() throws Exception {
    when(ownerService.save(any())).thenReturn(Owner.builder().id(1L).build());

    mockMvc
        .perform(post("/owners/1/edit"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/owners/1"))
        .andExpect(model().attributeExists("owner"));

    verify(ownerService).save(any());
  }
}
