-- Add Tables
CREATE TABLE IF NOT EXISTS users
(
    user_id    INT                 NOT NULL PRIMARY KEY AUTO_INCREMENT,
    uuid       UUID                NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL,
    name       VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    bio        VARCHAR(255),
    image      VARCHAR(255),
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6)
);

CREATE TABLE IF NOT EXISTS article
(
    article_id  INT  NOT NULL PRIMARY KEY AUTO_INCREMENT,
    uuid        UUID NOT NULL,
    title       VARCHAR(255),
    description VARCHAR(255),
    body        VARCHAR(255),
    author_id   INT,
    created_at  TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP(6),
    CONSTRAINT fk_article_author FOREIGN KEY (author_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS comment
(
    comment_id INT  NOT NULL PRIMARY KEY AUTO_INCREMENT,
    uuid       UUID NOT NULL,
    article_id INT  NOT NULL,
    body       VARCHAR(255),
    author_id  INT,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6),
    CONSTRAINT fk_comment_article FOREIGN KEY (article_id) REFERENCES article (article_id),
    CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS tag
(
    tag_id     INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    article_id INT,
    name       VARCHAR(255),
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6),
    CONSTRAINT fk_tag_article FOREIGN KEY (article_id) REFERENCES article (article_id)
);

CREATE TABLE IF NOT EXISTS user_follow
(
    follow_id   INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    followee_id INT NOT NULL,
    follower_id INT NOT NULL,
    created_at  TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP(6),
    UNIQUE (follower_id, followee_id),
    CONSTRAINT fk_user_follow_followee FOREIGN KEY (followee_id) REFERENCES users (user_id),
    CONSTRAINT fk_user_follow_follower FOREIGN KEY (follower_id) REFERENCES users (user_id)
);

-- Add Indexs
CREATE INDEX idx_users_uuid ON users (uuid);