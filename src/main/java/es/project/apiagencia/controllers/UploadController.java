package es.project.apiagencia.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class UploadController {

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("image") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get("src/main/resources/images/" + file.getOriginalFilename());
            Files.write(path, bytes);
            return "Imagen subida correctamente";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al subir la imagen";
        }
    }
}


