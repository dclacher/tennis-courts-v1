package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/guests")
@Api(tags = "Guests Management")
public class GuestController extends BaseRestController {

    private final GuestService guestService;

    @PostMapping
    @ApiOperation(value = "addGuest", tags = "Add a new guest", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Operation completed successfully"),
            @ApiResponse(code = 400, message = "Operation not completed - bad request")})
    public ResponseEntity<Void> addGuest(
            @ApiParam(value = "Create guest", required = true) @RequestBody
                    CreateGuestRequestDTO createGuestRequestDTO) {
        return ResponseEntity.created(locationByEntity(guestService.addGuest(createGuestRequestDTO).getId())).build();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "findByGuestId", tags = "Find guest by ID", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation completed successfully"),
            @ApiResponse(code = 400, message = "Operation not completed - bad request"),
            @ApiResponse(code = 404, message = "Guest not found")})
    public ResponseEntity<GuestDTO> findByGuestId(
            @ApiParam(value = "Guest ID", required = true) @PathVariable(name = "id") Long guestId) {
        return ResponseEntity.ok(guestService.findGuest(guestId));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "updateGuest", tags = "Update guest's name", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Operation completed successfully"),
            @ApiResponse(code = 400, message = "Operation not completed - bad request"),
            @ApiResponse(code = 404, message = "Guest not found")})
    public ResponseEntity<GuestDTO> updateGuest(
            @ApiParam(value = "Guest ID", required = true) @PathVariable(name = "id") Long guestId,
            @ApiParam(value = "Update guest", required = true) @RequestBody GuestDTO guestRequestDTO) {
        return ResponseEntity.created(locationByEntity(guestService.updateGuest(guestId, guestRequestDTO).getId()))
                             .build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "deleteGuest", tags = "Delete a guest", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Operation completed successfully"),
            @ApiResponse(code = 400, message = "Operation not completed - bad request"),
            @ApiResponse(code = 404, message = "Guest not found")})
    public ResponseEntity<Void> deleteGuest(
            @ApiParam(value = "Guest ID", required = true) @PathVariable(name = "id") Long guestId) {
        guestService.deleteGuest(guestId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    @ApiOperation(value = "listAllGuests", tags = "List all guests", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation completed successfully"),
            @ApiResponse(code = 400, message = "Operation not completed - bad request")})
    public ResponseEntity<List<GuestDTO>> listAllGuests() {
        Optional<List<GuestDTO>> optionalGuestDTOList = Optional.of(guestService.findAllGuests());
        return ResponseEntity.of(optionalGuestDTOList);
    }

    @GetMapping("/find-by-name")
    @ApiOperation(value = "listGuestsByName", tags = "List guests by name", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation completed successfully"),
            @ApiResponse(code = 400, message = "Operation not completed - bad request")})
    public ResponseEntity<List<GuestDTO>> listGuestsByName(
            @ApiParam(value = "Guest name", required = true) @RequestParam("name") String guestName) {
        Optional<List<GuestDTO>> optionalGuestDTOList = Optional.of(guestService.findGuestsByName(guestName));
        return ResponseEntity.of(optionalGuestDTOList);
    }
}
