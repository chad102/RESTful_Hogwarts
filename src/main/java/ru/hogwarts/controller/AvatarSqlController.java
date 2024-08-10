package ru.hogwarts.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.model.Avatar;
import ru.hogwarts.service.AvatarService;

import java.util.List;

@RestController
@RequestMapping("/avatar")
public class AvatarSqlController {
    private final AvatarService avatarService;

    public AvatarSqlController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping("/get-avatars-by-5")
    public ResponseEntity<List<Avatar>> getAvatarBy5 (Integer pageNumber, Integer pageSize) {
        return ResponseEntity.ok(avatarService.getAvatars(pageNumber, pageSize));
    }
}
