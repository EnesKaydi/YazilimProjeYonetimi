package com.eneskaydi.yazilimprojeyonetimi.Service;

import com.eneskaydi.yazilimprojeyonetimi.Dto.AddedUserCreateDTO;
import com.eneskaydi.yazilimprojeyonetimi.Dto.AddedUserDTO;
import com.eneskaydi.yazilimprojeyonetimi.Entity.AddedUser;
import com.eneskaydi.yazilimprojeyonetimi.Repository.AddedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddedUserService {

    private final AddedUserRepository addedUserRepository;
    private final PasswordEncoder passwordEncoder; // Şifreleme için

    @Autowired
    public AddedUserService(AddedUserRepository addedUserRepository, PasswordEncoder passwordEncoder) {
        this.addedUserRepository = addedUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // DTO'ya dönüştürme metodu
    private AddedUserDTO convertToDTO(AddedUser user) {
        return new AddedUserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getStatus()
        );
    }

    // Yeni kullanıcı oluşturma
    public AddedUserDTO createUser(AddedUserCreateDTO createDTO) {
        if (addedUserRepository.findByEmail(createDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + createDTO.getEmail());
        }
        AddedUser user = new AddedUser();
        user.setName(createDTO.getName());
        user.setEmail(createDTO.getEmail());
        user.setPassword(passwordEncoder.encode(createDTO.getPassword())); // Şifreyi hashle
        user.setRole(createDTO.getRole());
        user.setPhoneNumber(createDTO.getPhoneNumber());
        user.setAddress(createDTO.getAddress());
        user.setStatus(createDTO.getStatus());
        
        AddedUser savedUser = addedUserRepository.save(user);
        return convertToDTO(savedUser);
    }

    // Tüm kullanıcıları getirme
    public List<AddedUserDTO> getAllUsers() {
        return addedUserRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ID ile kullanıcı getirme
    public Optional<AddedUserDTO> getUserById(Long id) {
        return addedUserRepository.findById(id).map(this::convertToDTO);
    }

    // Kullanıcı güncelleme
    public Optional<AddedUserDTO> updateUser(Long id, AddedUserCreateDTO updateDTO) {
        return addedUserRepository.findById(id).map(user -> {
            user.setName(updateDTO.getName());
            user.setEmail(updateDTO.getEmail()); // Email güncellemesi dikkatli yapılmalı, unique constraint var
            if (updateDTO.getPassword() != null && !updateDTO.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
            }
            user.setRole(updateDTO.getRole());
            user.setPhoneNumber(updateDTO.getPhoneNumber());
            user.setAddress(updateDTO.getAddress());
            user.setStatus(updateDTO.getStatus());
            AddedUser updatedUser = addedUserRepository.save(user);
            return convertToDTO(updatedUser);
        });
    }

    // Kullanıcı silme
    public boolean deleteUser(Long id) {
        if (addedUserRepository.existsById(id)) {
            addedUserRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 