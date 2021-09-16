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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        CreateGuestRequestDTO createGuestRequestDTO = TestDataFactory.createGuestRequestDTO("Pete Sampras");
        GuestDTO guestDTO = TestDataFactory.createGuestDTO("Pete Sampras", 22L);
        Mockito.doReturn(new Guest()).when(guestMapper).map(Mockito.any(CreateGuestRequestDTO.class));
        Mockito.doReturn(new Guest()).when(guestRepository).saveAndFlush(Mockito.any(Guest.class));
        Mockito.doReturn(guestDTO).when(guestMapper).map(Mockito.any(Guest.class));

        GuestDTO response = guestService.addGuest(createGuestRequestDTO);

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
        Mockito.doThrow(new EntityNotFoundException("Guest not found")).when(guestRepository)
               .findById(Mockito.anyLong());

        guestService.findGuest(guestDTO.getId());
    }

    @Test
    public void updateGuest() {
        GuestDTO guestDTO = TestDataFactory.createGuestDTO("Boris Becker", 33L);
        Optional<Guest> guest = Optional.of(new Guest("Boris Becker"));
        GuestDTO updatedGuestDTO = TestDataFactory.createGuestDTO("Ivan Lendl", 33L);
        Mockito.doReturn(guest).when(guestRepository).findById(Mockito.anyLong());
        // Mockito.doReturn(guestDTO).when(guestMapper).map(Mockito.any(Guest.class));
        Mockito.doReturn(new Guest()).when(guestMapper).map(Mockito.any(GuestDTO.class));
        Mockito.doReturn(new Guest()).when(guestRepository).saveAndFlush(Mockito.any(Guest.class));
        Mockito.doReturn(updatedGuestDTO).when(guestMapper).map(Mockito.any(Guest.class));

        GuestDTO response = guestService.updateGuest(guestDTO.getId(), updatedGuestDTO);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getName(), updatedGuestDTO.getName());
    }

    @Test(expected = com.tenniscourts.exceptions.EntityNotFoundException.class)
    public void updateGuest_GuestNotFound() {
        GuestDTO guestDTO = TestDataFactory.createGuestDTO("Boris Becker", 33L);
        Mockito.doThrow(new EntityNotFoundException("Guest not found")).when(guestRepository)
               .findById(Mockito.anyLong());

        guestService.updateGuest(guestDTO.getId(), guestDTO);
    }

    @Test
    public void deleteGuest() {
        GuestDTO guestDTO = TestDataFactory.createGuestDTO("Serena Williams", 54L);
        Optional<Guest> guest = Optional.of(new Guest("Serena Williams"));
        Mockito.doReturn(guest).when(guestRepository).findById(Mockito.anyLong());
        Mockito.doReturn(guestDTO).when(guestMapper).map(Mockito.any(Guest.class));
        Mockito.doNothing().when(guestRepository).deleteById(Mockito.anyLong());

        guestService.deleteGuest(guestDTO.getId());

        Mockito.verify(guestRepository, Mockito.times(1)).deleteById(guestDTO.getId());
    }

    @Test(expected = com.tenniscourts.exceptions.EntityNotFoundException.class)
    public void deleteGuest_GuestNotFound() {
        GuestDTO guestDTO = TestDataFactory.createGuestDTO("Serena Williams", 54L);
        Mockito.doThrow(new EntityNotFoundException("Guest not found")).when(guestRepository)
               .findById(Mockito.anyLong());

        guestService.deleteGuest(guestDTO.getId());
    }

    @Test
    public void findAllGuests() {
        Guest guest1 = TestDataFactory.createGuest("Steffi Graff");
        Guest guest2 = TestDataFactory.createGuest("Maria Sharapova");
        List<Guest> guests = Arrays.asList(guest1, guest2);
        Mockito.doReturn(guests).when(guestRepository).findAll();

        List<GuestDTO> response = guestService.findAllGuests();

        Assert.assertNotNull(response);
        Assert.assertEquals(response.size(), guests.size());
    }

    @Test
    public void findAllGuests_EmptyList() {
        List<Guest> guests = new ArrayList<>();
        Mockito.doReturn(guests).when(guestRepository).findAll();

        List<GuestDTO> response = guestService.findAllGuests();

        Assert.assertNotNull(response);
        Assert.assertEquals(response.size(), guests.size());
    }

    @Test
    public void findGuestsByName() {
        List<Guest> guests = List.of(TestDataFactory.createGuest("Venus Williams"));
        List<GuestDTO> guestsDTO = List.of(TestDataFactory.createGuestDTO("Venus Williams", 111L));
        Mockito.doReturn(guests).when(guestRepository).findGuestsByNameOrderById(Mockito.anyString());
        Mockito.doReturn(guestsDTO).when(guestMapper).map(Mockito.anyList());

        List<GuestDTO> response = guestService.findGuestsByName("Venus Williams");

        Assert.assertNotNull(response);
        Assert.assertEquals(response.size(), guests.size());
    }

    @Test(expected = com.tenniscourts.exceptions.EntityNotFoundException.class)
    public void findGuestsByName_NoGuestsFound() {
        Mockito.doThrow(new EntityNotFoundException("Guests not found using provided name")).when(guestRepository)
               .findGuestsByNameOrderById(Mockito.anyString());

        guestService.findGuestsByName("Venus Williams");
    }
}
