-- CREATE TABLE IF NOT EXISTS books (
--     id BIGINT PRIMARY KEY,
--     title VARCHAR(255) NOT NULL,
--     author VARCHAR(255) NOT NULL,
--     publication_year INT NOT NULL,
--     rating DECIMAL(2, 1) NOT NULL,
--     description TEXT,
--     genre VARCHAR(50) NOT NULL
-- );

-- Fiction Books
INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('Where the Crawdads Sing', 'Delia Owens', 2018, 4.8, 'A novel about a young woman who is abandoned as a child and raised in the swamps of North Carolina.', 'Fiction');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('The Silent Patient', 'Alex Michaelides', 2019, 4.3, 'A psychological thriller about a woman who shoots her husband and then stops speaking.', 'Thriller');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('The Nightingale', 'Kristin Hannah', 2015, 4.6, 'A novel about two sisters in Nazi-occupied France during World War II.', 'Historical Fiction');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('The Midnight Library', 'Matt Haig', 2020, 4.2, 'A novel about a woman who finds herself in a library between life and death.', 'Fantasy');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('The Vanishing Half', 'Brit Bennett', 2020, 4.5, 'A novel about twin sisters who choose to live in two very different worlds, one black and one white.', 'Contemporary Fiction');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('A Man Called Ove', 'Fredrik Backman', 2012, 4.7, 'A heartwarming story about a grumpy yet loveable man whose world changes when a new family moves in next door.', 'Fiction');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('The Book Thief', 'Markus Zusak', 2005, 4.4, 'A novel set in Nazi Germany narrated by Death, about a young girl who steals books to share with others.', 'Historical Fiction');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('Little Fires Everywhere', 'Celeste Ng', 2017, 4.3, 'A novel about family dynamics and the clash between a picture-perfect family and a single mother.', 'Fiction');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('Circe', 'Madeline Miller', 2018, 4.5, 'A reimagining of the story of Circe, the enchantress from Homer’s Odyssey.', 'Fantasy');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('The Alchemist', 'Paulo Coelho', 1988, 4.7, 'A philosophical novel about a young Andalusian shepherd on a journey to find treasure.', 'Adventure');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('To Kill a Mockingbird', 'Harper Lee', 1960, 4.8, 'A classic novel about racial injustice in the Deep South, seen through the eyes of a young girl.', 'Classic Fiction');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('The Great Gatsby', 'F. Scott Fitzgerald', 1925, 4.4, 'A novel set in the Jazz Age, exploring themes of decadence, idealism, and excess.', 'Classic Fiction');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('1984', 'George Orwell', 1949, 4.6, 'A dystopian novel about a totalitarian regime that uses surveillance and mind control.', 'Dystopian');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('Pride and Prejudice', 'Jane Austen', 1813, 4.7, 'A classic novel about love and social class in 19th century England.', 'Romance');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('The Catcher in the Rye', 'J.D. Salinger', 1951, 4.2, 'A novel about a teenage boy’s alienation and rebellion against the adult world.', 'Classic Fiction');

-- Non-Fiction Books
INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('Becoming', 'Michelle Obama', 2018, 4.8, 'A memoir by the former First Lady of the United States.', 'Memoir');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('Educated', 'Tara Westover', 2018, 4.7, 'A memoir about a woman who grows up in a strict and abusive household in rural Idaho but escapes to learn about the wider world through education.', 'Memoir');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('Sapiens: A Brief History of Humankind', 'Yuval Noah Harari', 2011, 4.6, 'A book about the history and impact of Homo sapiens.', 'History');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('The Subtle Art of Not Giving a F*ck', 'Mark Manson', 2016, 4.3, 'A counterintuitive approach to living a good life.', 'Self-Help');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('Atomic Habits', 'James Clear', 2018, 4.8, 'A book about building good habits and breaking bad ones.', 'Self-Help');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('Thinking, Fast and Slow', 'Daniel Kahneman', 2011, 4.5, 'A book about the two systems that drive the way we think.', 'Psychology');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('Outliers: The Story of Success', 'Malcolm Gladwell', 2008, 4.2, 'A book that explores the factors that contribute to high levels of success.', 'Business');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('The Power of Habit', 'Charles Duhigg', 2012, 4.4, 'A book about how habits work and how they can be changed.', 'Self-Help');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('The Immortal Life of Henrietta Lacks', 'Rebecca Skloot', 2010, 4.6, 'The story of a woman whose cells were used for medical research without her knowledge.', 'Biography');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('Quiet: The Power of Introverts in a World That Can''t Stop Talking', 'Susan Cain', 2012, 4.4, 'A book about the value of introverts in society.', 'Psychology');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('Steve Jobs', 'Walter Isaacson', 2011, 4.5, 'The biography of Apple co-founder Steve Jobs.', 'Biography');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('The Wright Brothers', 'David McCullough', 2015, 4.7, 'A biography of the Wright brothers, who invented the first successful airplane.', 'Biography');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('Into the Wild', 'Jon Krakauer', 1996, 4.4, 'The story of Christopher McCandless, a young man who hiked into the Alaskan wilderness.', 'Adventure');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('Born a Crime: Stories from a South African Childhood', 'Trevor Noah', 2016, 4.8, 'A memoir by the comedian about growing up in South Africa during and after apartheid.', 'Memoir');

INSERT INTO books (title, author, publication_year, rating, description, genre)
VALUES ('Grit: The Power of Passion and Perseverance', 'Angela Duckworth', 2016, 4.5, 'A book about the power of persistence and resilience.', 'Self-Help');
