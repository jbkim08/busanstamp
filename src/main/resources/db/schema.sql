CREATE DATABASE IF NOT EXISTS busan_stamp
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE busan_stamp;

CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_users_email UNIQUE (email)
);

CREATE TABLE places (
    place_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    address VARCHAR(255) NOT NULL,
    latitude DECIMAL(10, 7) NOT NULL,  -- 위도
    longitude DECIMAL(10, 7) NOT NULL, -- 경도
    category VARCHAR(50) NOT NULL,
    image_url VARCHAR(500),      -- 이미지 주소
    created_by BIGINT NOT NULL,  -- 외래키 users.user_id 참조함
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_places_created_by
        FOREIGN KEY (created_by) REFERENCES users(user_id)
);

ALTER TABLE places
    ADD COLUMN kakao_place_id VARCHAR(50),
    ADD COLUMN phone VARCHAR(30),
    ADD COLUMN kakao_place_url VARCHAR(500),
    ADD COLUMN kakao_category_name VARCHAR(255),
    ADD CONSTRAINT uk_places_kakao_place_id
        UNIQUE (kakao_place_id);

CREATE TABLE checkins (
    checkin_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    place_id BIGINT NOT NULL,
    checked_in_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_checkins_user
    FOREIGN KEY (user_id) REFERENCES users(user_id)
    ON DELETE CASCADE,

    CONSTRAINT fk_checkins_place
    FOREIGN KEY (place_id) REFERENCES places(place_id)
    ON DELETE CASCADE,

    CONSTRAINT uk_checkins_user_place UNIQUE (user_id, place_id)
);