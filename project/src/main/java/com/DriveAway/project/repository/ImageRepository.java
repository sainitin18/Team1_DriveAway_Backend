package com.DriveAway.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.DriveAway.project.model.Image;

import java.util.Optional;

public interface ImageRepository  extends JpaRepository<Image, Long> {
}