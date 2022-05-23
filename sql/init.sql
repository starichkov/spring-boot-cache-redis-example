CREATE SCHEMA IF NOT EXISTS book_store DEFAULT CHARACTER SET = utf8mb4;
CREATE USER IF NOT EXISTS 'librarian'@'%' IDENTIFIED BY 'Knowledge!sPower';
GRANT ALL PRIVILEGES ON book_store.* TO 'librarian'@'%';