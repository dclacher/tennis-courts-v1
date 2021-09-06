package com.tenniscourts.guests;

import com.tenniscourts.TestDataFactory;
import com.tenniscourts.exceptions.EntityNotFoundException;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = GuestService.class)
public class GuestServiceTest {

    @InjectMocks
    GuestService guestService;

    @Mock
    GuestRepository guestRepository;

    @Mock
    GuestMapper guestMapper;

    @Test
    public void addGuest() {
        GuestDTO guestDTO = TestDataFactory.createGuestDTO("Pete Sampras", 22L);
        Mockito.doReturn(new Guest()).when(guestMapper).map(Mockito.any(GuestDTO.class));
        Mockito.doReturn(new Guest()).when(guestRepository).saveAndFlush(Mockito.any(Guest.class));
        Mockito.doReturn(guestDTO).when(guestMapper).map(Mockito.any(Guest.class));

        GuestDTO response = guestService.addGuest(guestDTO);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getName(), guestDTO.getName());
    }

    @Test
    public void findGuest() {
        GuestDTO guestDTO = TestDataFactory.createGuestDTO("Andre Agassi", 23L);
        Optional<Guest> guest = Optional.of(new Guest("Andre Agassi"));
        Mockito.doReturn(guest).when(guestRepository).findById(Mockito.anyLong());
        Mockito.doReturn(guestDTO).when(guestMapper).map(Mockito.any(Guest.class));

        GuestDTO response = guestService.findGuest(guestDTO.getId());

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getName(), guestDTO.getName());
    }

    @Test(expected = com.tenniscourts.exceptions.EntityNotFoundException.class)
    public void findGuest_GuestNotFound() {
        GuestDTO guestDTO = TestDataFactory.createGuestDTO("Andre Agassi", 23L);
        Mockito.doThrow(new EntityNotFoundException("Guest not found")).when(guestRepository).findById(Mockito.anyLong());

        guestService.findGuest(guestDTO.getId());
    }
}
