package ru.hogwarts.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
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
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {
    private final String avatarsDir;
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;
    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    public AvatarService(@Value("${path.to.avatars.folder}") String avatarsDir,
                         AvatarRepository avatarRepository,
                         StudentService studentService) {
        this.avatarsDir = avatarsDir;
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }


    public void uploadAvatar(long studentId, MultipartFile file) throws IOException {
        logger.info("Was invoked method for upload avatar");
        logger.debug("Uploading avatar");
        logger.warn("The size of the uploaded object must not exceed 1024");
        Student student = studentService.getStudent(studentId);

        Path filePath = Path.of(avatarsDir, studentId + "." + getFilenameExtension(file.getOriginalFilename()));
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
        logger.info("Was invoked method for get avatar by student id");
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    private byte[] generateImagePreview(Path filePath) throws IOException {
        logger.info("Was invoked method for generate preview avatar");
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
             BufferedImage image = ImageIO.read(bis);

             int height = image.getHeight() / (image.getWidth() / 100);
             BufferedImage preview = new BufferedImage(100, height, image.getType());
             Graphics2D graphics = preview.createGraphics();
             graphics.drawImage(image, 0,0,100,height, null);
             graphics.dispose();

             ImageIO.write(preview, getFilenameExtension(filePath.getFileName().toString()), baos);
             return baos.toByteArray();
        }
    }

    public String getFilenameExtension (String fileName) {
        logger.info("Was invoked method for get file name extension");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public List<Avatar> getAvatars(Integer pageNumber, Integer pageSize) {
        logger.info("Was invoked method for get all avatars page by page");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }
}
