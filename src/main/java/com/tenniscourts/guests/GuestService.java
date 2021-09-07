package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;

    private final GuestMapper guestMapper;

    public GuestDTO addGuest(GuestDTO guestDTO) {
        return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(guestDTO)));
    }

    public GuestDTO findGuest(Long guestId) {
        Optional<Guest> guest = Optional.of(
                guestRepository.findById(guestId).orElseThrow(() -> new EntityNotFoundException("Guest not found")));
        return guestMapper.map(guest.get());
    }

    public GuestDTO updateGuest(Long guestId, GuestDTO guestRequestDTO) {
        // Try to find the guest; findGuest throws EntityNotFoundException otherwise
        GuestDTO guestDTO = findGuest(guestId);
        guestDTO.setName(guestRequestDTO.getName());
        return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(guestDTO)));
    }

    public void deleteGuest(Long guestId) {
        // Try to find the guest; findGuest throws EntityNotFoundException otherwise
        GuestDTO guestDTO = findGuest(guestId);
        guestRepository.deleteById(guestDTO.getId());
    }

    public List<GuestDTO> findAllGuests() {
        List<GuestDTO> response = new ArrayList<>();
        List<Guest> guests = guestRepository.findAll();
        guests.forEach(g -> response.add(guestMapper.map(g)));
        return response;
    }

    public List<GuestDTO> findGuestsByName(String name) {
        Optional<List<Guest>> guests = Optional.of(guestRepository.findGuestsByNameOrderById(name).orElseThrow(
                () -> new EntityNotFoundException("Guests not found using provided name")));
        List<GuestDTO> response = new ArrayList<>();
        guests.get().forEach(g -> response.add(guestMapper.map(g)));
        return response;
    }
}
