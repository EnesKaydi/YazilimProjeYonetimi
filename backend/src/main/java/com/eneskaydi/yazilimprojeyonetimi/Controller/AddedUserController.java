package com.eneskaydi.yazilimprojeyonetimi.Controller;

import com.eneskaydi.yazilimprojeyonetimi.Dto.AddedUserCreateDTO;
import com.eneskaydi.yazilimprojeyonetimi.Dto.AddedUserDTO;
import com.eneskaydi.yazilimprojeyonetimi.Service.AddedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/added-users")
public class AddedUserController {

    private final AddedUserService addedUserService;

    @Autowired
    public AddedUserController(AddedUserService addedUserService) {
        this.addedUserService = addedUserService;
    }

    @PostMapping
    public ResponseEntity<AddedUserDTO> createUser(@RequestBody AddedUserCreateDTO createDTO) {
        try {
            AddedUserDTO newUser = addedUserService.createUser(createDTO);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Örneğin email zaten varsa
        }
    }

    @GetMapping
    public ResponseEntity<List<AddedUserDTO>> getAllUsers() {
        List<AddedUserDTO> users = addedUserService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddedUserDTO> getUserById(@PathVariable Long id) {
        return addedUserService.getUserById(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddedUserDTO> updateUser(@PathVariable Long id, @RequestBody AddedUserCreateDTO updateDTO) {
        return addedUserService.updateUser(id, updateDTO)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (addedUserService.deleteUser(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
} 