package com.cdac.blog.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.cdac.blog.entities.Image;
import com.cdac.blog.repositories.ImageRepository;
import com.cdac.blog.util.ImageUtility;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
//@CrossOrigin(origins = "http://localhost:8082") open for specific port
@CrossOrigin() // open for all ports
public class ImageController {

    @Autowired
    ImageRepository imageRepository;

    @PostMapping("/upload/image/{post_Id}")
    public ResponseEntity<ImageUploadResponse> uplaodImage(@RequestParam("image") MultipartFile file,@PathVariable Integer post_Id)
            throws IOException {

    	imageRepository.save(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .postId(post_Id)
                .image(ImageUtility.compressImage(file.getBytes())).build());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ImageUploadResponse("Image uploaded successfully: " +
                        file.getOriginalFilename()));
    }

    @GetMapping(path = {"/get/image/info/{post_Id}"})
    public Image getImageDetails(@PathVariable("post_Id") Integer post_Id) throws IOException {

        final Optional<Image> dbImage = imageRepository.findByPostId(post_Id);

        return Image.builder()
                .name(dbImage.get().getName())
                .type(dbImage.get().getType())
                .image(ImageUtility.decompressImage(dbImage.get().getImage())).build();
    }

    @GetMapping(path = {"/get/image/{post_Id}"})
    public ResponseEntity<byte[]> getImage(@PathVariable("post_Id") Integer post_Id) throws IOException {

    	final Optional<Image> dbImage = imageRepository.findByPostId(post_Id);

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(dbImage.get().getType()))
                .body(ImageUtility.decompressImage(dbImage.get().getImage()));
    }
}