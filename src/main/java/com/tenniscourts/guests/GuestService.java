package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class GuestService {

    private final GuestRepository guestRepository;

    private final GuestMapper guestMapper;

    public GuestDTO addGuest(CreateGuestRequestDTO createGuestRequestDTO) {
        return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(createGuestRequestDTO)));
    }

    public GuestDTO findGuest(Long guestId) {
        Optional<Guest> guest = Optional.of(
                guestRepository.findById(guestId).orElseThrow(() -> new EntityNotFoundException("Guest not found")));
        log.info("Guest with ID {} found. The name is: {}", guestId, guest.get().getName());
        return guestMapper.map(guest.get());
    }

    public GuestDTO updateGuest(GuestDTO guestRequestDTO) {
        // Try to find the guest; findGuest throws EntityNotFoundException otherwise
        GuestDTO guestDTO = findGuest(guestRequestDTO.getId());
        log.info("Updating the name to: {}", guestRequestDTO.getName());
        guestDTO.setName(guestRequestDTO.getName());
        return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(guestDTO)));
    }

    public void deleteGuest(Long guestId) {
        // Try to find the guest; findGuest throws EntityNotFoundException otherwise
        GuestDTO guestDTO = findGuest(guestId);
        log.info("Deleting the guest with ID {} and name {}", guestId, guestDTO.getName());
        guestRepository.deleteById(guestDTO.getId());
    }

    public List<GuestDTO> findAllGuests() {
        List<GuestDTO> response = new ArrayList<>();
        List<Guest> guests = guestRepository.findAll();
        if (guests.isEmpty()) {
            log.info("No guests found in the system");
        }
        guests.forEach(g -> response.add(guestMapper.map(g)));
        return response;
    }

    public List<GuestDTO> findGuestsByName(String name) {
        List<Guest> guests = guestRepository.findGuestsByNameOrderById(name);
        if (guests.isEmpty()) {
            log.info("No guests found in the system with the provided name");
        }
        return guestMapper.map(guests);
    }
}
