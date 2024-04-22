package ru.hogwarts.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.model.Avatar;
import ru.hogwarts.model.Student;
import ru.hogwarts.repository.AvatarRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {
    @Value("/avatars")
    private String avatarsDir;
@Autowired
    private AvatarRepository avatarRepository;
    private StudentService studentService;

    public AvatarService(String avatarsDir,
                         AvatarRepository avatarRepository,
                         StudentService studentService) {
        this.avatarsDir = avatarsDir;
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }


    public void uploadAvatar(long studentId, MultipartFile file) throws IOException {
        Student student = studentService.getStudent(studentId);

        Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectory(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }

        Avatar avatar = findStudentAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(generateImagePreview(filePath));

        avatarRepository.save(avatar);
    }

    public Avatar findStudentAvatar(long studentId) {
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    private byte[] generateImagePreview(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
             BufferedImage image = ImageIO.read(bis);

             int height = image.getHeight() / (image.getWidth() / 100);
             BufferedImage preview = new BufferedImage(100, height, image.getType());
             Graphics2D graphics = preview.createGraphics();
             graphics.drawImage(image, 0,0,100,height, null);
             graphics.dispose();

             ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
             return baos.toByteArray();
        }
    }

    public String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}