-- categoriesテーブル
INSERT IGNORE INTO genres (genre_id, genre_name) VALUES (1, 'HipHop');
INSERT IGNORE INTO genres (genre_id, genre_name) VALUES (2, 'Jazz');
INSERT IGNORE INTO genres (genre_id, genre_name) VALUES (3, 'Pops');
INSERT IGNORE INTO genres (genre_id, genre_name) VALUES (4, 'Rock');

-- musicsテーブル
INSERT IGNORE INTO musics (music_id, genre_id, song_title, artist, image_name, music_file, description, price, email) VALUES (1, 1, 'Deep Sea', 'Negto', 'deep_sea.jpeg', 'deep_sea.mp3', '深海をイメージしたインストロメンタル楽曲です。', 1500, 'negto@example.com');
INSERT IGNORE INTO musics (music_id, genre_id, song_title, artist, image_name, music_file, description, price, email) VALUES (2, 1, 'Wish', 'Negto', 'wish.jpeg', 'wish.mp3', '願いを込めたインストロメンタル楽曲です。', 1000, 'negto@example.com');
INSERT IGNORE INTO musics (music_id, genre_id, song_title, artist, image_name, music_file, description, price, email) VALUES (3, 1, 'Fleur Blanche', 'Negto', 'fleur_blanche.jpeg', 'fleur_blanche.mp3', '綺麗な花をイメージしたインストロメンタル楽曲です。', 500, 'negto@example.com');
INSERT IGNORE INTO musics (music_id, genre_id, song_title, artist, image_name, music_file, description, price, email) VALUES (4, 2, 'Breeze', 'Negto', 'breeze.jpeg', 'breeze.mp3', 'そよ風をイメージしたインストロメンタル楽曲です。', 250, 'negto@example.com');
INSERT IGNORE INTO musics (music_id, genre_id, song_title, artist, image_name, music_file, description, price, email) VALUES (5, 4, 'Eclair', 'Negto', 'eclair.jpeg', 'eclair.mp3', '閃光をイメージしたインストロメンタル楽曲です。', 1500, 'negto@example.com');
INSERT IGNORE INTO musics (music_id, genre_id, song_title, artist, image_name, music_file, description, price, email) VALUES (6, 3, 'Liberte', 'Negto', 'liberte.jpeg', 'liberte.mp3', '自由をイメージしたインストロメンタル楽曲です。', 1250, 'negto@example.com');
--INSERT IGNORE INTO musics (music_id, genre_id, song_title, artist, image_name, music_file, description, price, email) VALUES (7, 1, 'Everlasting Truth', 're:plus', 'everlasting_truth.jpg', 'everlasting_truth.mp3', 're:plusさんの曲を耳コピしたインストロメンタル楽曲です。', 1000, 'negto@example.com');
--INSERT IGNORE INTO musics (music_id, genre_id, song_title, artist, image_name, music_file, description, price, email) VALUES (8, 1, 'FOREVER YOURS', 'Hidetake Takayama', 'forever_yours.jpg', 'forever_yours.mp3', 'Hidetake Takayamaさんの曲を耳コピしたインストロメンタル楽曲です。', 250, 'negto@example.com');
--INSERT IGNORE INTO musics (music_id, genre_id, song_title, artist, image_name, music_file, description, price, email) VALUES (9, 1, 'Lost', 'Murohashi Takuya', 'lost.jpg', 'lost.mp3', 'Murohashi Takuyaさんの曲を耳コピしたインストロメンタル楽曲です。', 500, 'negto@example.com');
--INSERT IGNORE INTO musics (music_id, genre_id, song_title, artist, image_name, music_file, description, price, email) VALUES (10, 1, 'Nighttime', 're:plus', 'nighttime.jpg', 'nighttime.mp3', 're:plusさんの曲を耳コピしたインストロメンタル楽曲です。', 1000, 'negto@example.com');
--INSERT IGNORE INTO musics (music_id, genre_id, song_title, artist, image_name, music_file, description, price, email) VALUES (11, 1, 'Rainy Song', 'Tomoya Naka', 'rainy_song.jpg', 'rainy_song.mp3', 'Tomoya nakaさんの曲をHipHopアレンジを加えたインストロメンタル楽曲です。', 1250, 'negto@example.com');
--INSERT IGNORE INTO musics (music_id, genre_id, song_title, artist, image_name, music_file, description, price, email) VALUES (12, 1, 'voices', 'Murohashi Takuya', 'voices.jpg', 'voices.mp3', 'Murohashi Takuyaさんの曲を耳コピしたインストロメンタル楽曲です。', 1500, 'negto@example.com');

-- rolesテーブル
INSERT IGNORE INTO roles (role_id, role_name) VALUES (1, 'ROLE_GENERAL');
INSERT IGNORE INTO roles (role_id, role_name) VALUES (2, 'ROLE_ADMIN');

-- usersテーブル
INSERT IGNORE INTO users (user_id, user_name, furigana, postal_code, address, phone_number, user_email, password, role_id, enabled, paid, stripe_customer_id) VALUES (1, '音楽 太郎', 'オンガク タロウ', '101-0022', '東京都千代田区神田練塀町300番地', '090-1234-5678', 'taro.ongaku@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 1, true, true, null);
INSERT IGNORE INTO users (user_id, user_name, furigana, postal_code, address, phone_number, user_email, password, role_id, enabled, paid, stripe_customer_id) VALUES (2, '音楽 花子', 'オンガク ハナコ', '101-0022', '東京都千代田区神田練塀町300番地', '090-1234-5678', 'hanako.ongaku@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 2, true, true, null);
INSERT IGNORE INTO users (user_id, user_name, furigana, postal_code, address, phone_number, user_email, password, role_id, enabled, paid, stripe_customer_id) VALUES (3, '音楽 義勝', 'オンガク ヨシカツ', '638-0644', '奈良県五條市西吉野町湯川X-XX-XX', '090-1234-5678', 'yoshikatsu.ongaku@example.com', 'password', 1, false, false, null);
INSERT IGNORE INTO users (user_id, user_name, furigana, postal_code, address, phone_number, user_email, password, role_id, enabled, paid, stripe_customer_id) VALUES (4, '音楽 幸美', 'オンガク サチミ', '342-0006', '埼玉県吉川市南広島X-XX-XX', '090-1234-5678', 'sachimi.ongaku@example.com', 'password', 1, false, false, null);
INSERT IGNORE INTO users (user_id, user_name, furigana, postal_code, address, phone_number, user_email, password, role_id, enabled, paid, stripe_customer_id) VALUES (5, '音楽 雅', 'オンガク ミヤビ', '527-0209', '滋賀県東近江市佐目町X-XX-XX', '090-1234-5678', 'miyabi.ongaku@example.com', 'password', 1, false, false, null);
INSERT IGNORE INTO users (user_id, user_name, furigana, postal_code, address, phone_number, user_email, password, role_id, enabled, paid, stripe_customer_id) VALUES (6, 'Negto', 'ネグト', '101-0022', '東京都千代田区神田練塀町300番地', '090-1234-5678', 'negto@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 2, true, true, null);

-- reveiwsテーブル
INSERT IGNORE INTO reviews (review_id, music_id, user_id, content, score, created_at, updated_at) VALUES (1, 1, 1, '良かった。', 3, '2024-12-18', '2024-12-19');
INSERT IGNORE INTO reviews (review_id, music_id, user_id, content, score, created_at, updated_at) VALUES (2, 1, 2, 'まあまあ', 2, '2024-12-18', '2024-12-19');
INSERT IGNORE INTO reviews (review_id, music_id, user_id, content, score, created_at, updated_at) VALUES (3, 1, 4, 'とても良かった。', 4, '2024-12-18', '2024-12-19');
INSERT IGNORE INTO reviews (review_id, music_id, user_id, content, score, created_at, updated_at) VALUES (4, 1, 3, '感動したし、とても良かった。', 5, '2024-12-18', '2024-12-19');
INSERT IGNORE INTO reviews (review_id, music_id, user_id, content, score, created_at, updated_at) VALUES (5, 1, 5, 'いまいち', 1, '2024-12-18', '2024-12-19');
INSERT IGNORE INTO reviews (review_id, music_id, user_id, content, score, created_at, updated_at) VALUES (6, 1, 6, 'とても良かった。', 4, '2024-12-18', '2024-12-19');
INSERT IGNORE INTO reviews (review_id, music_id, user_id, content, score, created_at, updated_at) VALUES (7, 1, 3, '感動して、涙がでた。', 5, '2024-12-18', '2024-12-19');
INSERT IGNORE INTO reviews (review_id, music_id, user_id, content, score, created_at, updated_at) VALUES (8, 1, 1, 'いまいち', 2, '2024-12-18', '2024-12-19');
INSERT IGNORE INTO reviews (review_id, music_id, user_id, content, score, created_at, updated_at) VALUES (9, 1, 2, '良かった。', 3, '2024-12-18', '2024-12-19');
INSERT IGNORE INTO reviews (review_id, music_id, user_id, content, score, created_at, updated_at) VALUES (10, 1, 3, 'とても良かった。', 4, '2024-12-18', '2024-12-19');
INSERT IGNORE INTO reviews (review_id, music_id, user_id, content, score, created_at, updated_at) VALUES (11, 1, 4, 'いまいちかな', 1, '2024-12-18', '2024-12-19'); 

-- favoritesテーブル
INSERT IGNORE INTO favorites (favorite_id, music_id, user_id) VALUES (1, 1, 1);
INSERT IGNORE INTO favorites (favorite_id, music_id, user_id) VALUES (2, 2, 1);
INSERT IGNORE INTO favorites (favorite_id, music_id, user_id) VALUES (3, 3, 1);
INSERT IGNORE INTO favorites (favorite_id, music_id, user_id) VALUES (4, 4, 1);
INSERT IGNORE INTO favorites (favorite_id, music_id, user_id) VALUES (5, 5, 1);
INSERT IGNORE INTO favorites (favorite_id, music_id, user_id) VALUES (6, 6, 1);
INSERT IGNORE INTO favorites (favorite_id, music_id, user_id) VALUES (7, 7, 1);
INSERT IGNORE INTO favorites (favorite_id, music_id, user_id) VALUES (8, 8, 1);
INSERT IGNORE INTO favorites (favorite_id, music_id, user_id) VALUES (9, 9, 1);
INSERT IGNORE INTO favorites (favorite_id, music_id, user_id) VALUES (10, 10, 1);
INSERT IGNORE INTO favorites (favorite_id, music_id, user_id) VALUES (11, 11, 1);