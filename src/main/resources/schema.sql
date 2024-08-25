CREATE TABLE IF NOT EXISTS users
(
    id         UUID                NOT NULL PRIMARY KEY,
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
    id          UUID NOT NULL PRIMARY KEY,
    title       VARCHAR(255),
    description VARCHAR(255),
    body        VARCHAR(255),
    created_at  TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP(6)
);

CREATE TABLE IF NOT EXISTS comment
(
    id         UUID NOT NULL PRIMARY KEY,
    article_id UUID NOT NULL,
    body       VARCHAR(255),
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6),
    CONSTRAINT fk_comment_article FOREIGN KEY (article_id) REFERENCES article (id)
);

CREATE TABLE IF NOT EXISTS tag
(
    id         UUID NOT NULL PRIMARY KEY,
    article_id UUID,
    name       VARCHAR(255),
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6),
    CONSTRAINT fk_tag_article FOREIGN KEY (article_id) REFERENCES article (id)
);

CREATE TABLE IF NOT EXISTS user_follow
(
    id          UUID NOT NULL PRIMARY KEY,
    followee_id UUID NOT NULL,
    follower_id UUID NOT NULL,
    created_at  TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (follower_id, followee_id),
    CONSTRAINT fk_user_follow_followee FOREIGN KEY (followee_id) REFERENCES users (id),
    CONSTRAINT fk_user_follow_follower FOREIGN KEY (follower_id) REFERENCES users (id)
);