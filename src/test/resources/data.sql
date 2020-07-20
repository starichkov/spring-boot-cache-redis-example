create table book_store.books
(
    id     bigint auto_increment primary key,
    title  varchar(255) not null,
    author varchar(255) null
);

