-- =====================================================
-- Initialize Data for Book Management System
-- =====================================================

USE `book_manage_system`;

-- -----------------------------------------------------
-- Insert Roles
-- -----------------------------------------------------
INSERT INTO `sys_role` (`id`, `role_name`, `role_key`, `description`, `status`, `sort`) VALUES
(1, 'Administrator', 'admin', 'System administrator with full access', 1, 1),
(2, 'Librarian', 'librarian', 'Library staff for book and borrow management', 1, 2),
(3, 'Reader', 'reader', 'Normal reader with borrow permission', 1, 3);

-- -----------------------------------------------------
-- Insert Users (Password: 123456, encrypted with BCrypt)
-- BCrypt hash for '123456': $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH
-- -----------------------------------------------------
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `phone`, `email`, `avatar`, `gender`, `status`) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'System Admin', '13800138000', 'admin@booklib.com', '/avatars/admin.png', 1, 1),
(2, 'librarian01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'Zhang San', '13800138001', 'zhangsan@booklib.com', '/avatars/librarian01.png', 1, 1),
(3, 'librarian02', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'Li Si', '13800138002', 'lisi@booklib.com', '/avatars/librarian02.png', 2, 1),
(4, 'reader01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'Wang Wu', '13800138003', 'wangwu@example.com', '/avatars/reader01.png', 1, 1),
(5, 'reader02', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'Zhao Liu', '13800138004', 'zhaoliu@example.com', '/avatars/reader02.png', 2, 1),
(6, 'reader03', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'Sun Qi', '13800138005', 'sunqi@example.com', '/avatars/reader03.png', 1, 1),
(7, 'reader04', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'Zhou Ba', '13800138006', 'zhouba@example.com', '/avatars/reader04.png', 2, 1),
(8, 'reader05', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'Wu Jiu', '13800138007', 'wujiu@example.com', '/avatars/reader05.png', 1, 1);

-- -----------------------------------------------------
-- Insert User-Role Relations
-- -----------------------------------------------------
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2),
(3, 2),
(4, 3),
(5, 3),
(6, 3),
(7, 3),
(8, 3);

-- -----------------------------------------------------
-- Insert Book Categories
-- -----------------------------------------------------
INSERT INTO `book_category` (`id`, `name`, `parent_id`, `sort`, `icon`, `status`) VALUES
-- Level 1 Categories
(1, 'Programming', 0, 1, 'code', 1),
(2, 'Literature', 0, 2, 'book-open', 1),
(3, 'Science', 0, 3, 'flask', 1),
(4, 'History', 0, 4, 'clock', 1),
(5, 'Art', 0, 5, 'palette', 1),
(6, 'Economics', 0, 6, 'trending-up', 1),
(7, 'Philosophy', 0, 7, 'brain', 1),

-- Level 2 Categories (Programming)
(11, 'Java', 1, 1, NULL, 1),
(12, 'Python', 1, 2, NULL, 1),
(13, 'JavaScript', 1, 3, NULL, 1),
(14, 'C/C++', 1, 4, NULL, 1),
(15, 'Database', 1, 5, NULL, 1),
(16, 'Algorithm', 1, 6, NULL, 1),

-- Level 2 Categories (Literature)
(21, 'Novel', 2, 1, NULL, 1),
(22, 'Poetry', 2, 2, NULL, 1),
(23, 'Drama', 2, 3, NULL, 1),

-- Level 2 Categories (Science)
(31, 'Physics', 3, 1, NULL, 1),
(32, 'Chemistry', 3, 2, NULL, 1),
(33, 'Biology', 3, 3, NULL, 1),

-- Level 2 Categories (History)
(41, 'World History', 4, 1, NULL, 1),
(42, 'Chinese History', 4, 2, NULL, 1);

-- -----------------------------------------------------
-- Insert Books
-- -----------------------------------------------------
INSERT INTO `book_info` (`id`, `title`, `author`, `isbn`, `publisher`, `publish_date`, `category_id`, `price`, `stock`, `available_stock`, `location`, `cover`, `description`, `status`) VALUES
-- Programming - Java
(1, 'Effective Java (3rd Edition)', 'Joshua Bloch', '978-7-111-59369-5', 'Machine Press', '2018-01-01', 11, 119.00, 10, 8, 'A-1-001', '/covers/effective-java.jpg', 'Comprehensive guide to Java programming best practices and design patterns.', 1),
(2, 'Java Concurrency in Practice', 'Brian Goetz', '978-7-111-58942-1', 'Machine Press', '2016-01-01', 11, 89.00, 8, 6, 'A-1-002', '/covers/java-concurrency.jpg', 'In-depth exploration of Java concurrent programming.', 1),
(3, 'Spring Boot in Action', 'Craig Walls', '978-7-115-45432-8', 'Posts & Telecom Press', '2017-01-01', 11, 79.00, 15, 12, 'A-1-003', '/covers/spring-boot.jpg', 'Practical guide to building applications with Spring Boot.', 1),
(4, 'Head First Java (3rd Edition)', 'Kathy Sierra', '978-7-519-80234-5', 'China Youth Press', '2020-01-01', 11, 128.00, 20, 18, 'A-1-004', '/covers/head-first-java.jpg', 'Brain-friendly guide for learning Java programming.', 1),

-- Programming - Python
(5, 'Python Crash Course', 'Eric Matthes', '978-7-115-51087-1', 'Posts & Telecom Press', '2020-01-01', 12, 89.00, 12, 10, 'A-2-001', '/covers/python-crash.jpg', 'Fast-paced introduction to Python programming.', 1),
(6, 'Fluent Python (2nd Edition)', 'Luciano Ramalho', '978-7-115-58923-4', 'Posts & Telecom Press', '2022-01-01', 12, 139.00, 6, 5, 'A-2-002', '/covers/fluent-python.jpg', 'Advanced Python programming techniques and best practices.', 1),
(7, 'Python Machine Learning', 'Sebastian Raschka', '978-7-111-59370-1', 'Machine Press', '2019-01-01', 12, 99.00, 8, 6, 'A-2-003', '/covers/python-ml.jpg', 'Machine learning with Python and scikit-learn.', 1),

-- Programming - JavaScript
(8, 'JavaScript: The Good Parts', 'Douglas Crockford', '978-7-121-18616-7', 'Electronic Industry Press', '2015-01-01', 13, 49.00, 15, 13, 'A-3-001', '/covers/js-good-parts.jpg', 'Essential JavaScript patterns and best practices.', 1),
(9, 'React Up and Running', 'Stoyan Stefanov', '978-7-115-46789-2', 'Posts & Telecom Press', '2018-01-01', 13, 69.00, 10, 8, 'A-3-002', '/covers/react-up.jpg', 'Building web applications with React.', 1),
(10, 'Node.js Design Patterns', 'Mario Casciaro', '978-7-115-48123-5', 'Posts & Telecom Press', '2019-01-01', 13, 99.00, 7, 6, 'A-3-003', '/covers/node-patterns.jpg', 'Advanced Node.js patterns and practices.', 1),

-- Programming - Algorithm
(11, 'Introduction to Algorithms (4th Edition)', 'Thomas H. Cormen', '978-7-111-62345-6', 'Machine Press', '2022-01-01', 16, 168.00, 5, 4, 'A-6-001', '/covers/algo-intro.jpg', 'Comprehensive textbook on algorithms and data structures.', 1),
(12, 'Design Patterns', 'Gang of Four', '978-7-111-21054-3', 'Machine Press', '2010-01-01', 16, 79.00, 10, 7, 'A-6-002', '/covers/design-patterns.jpg', 'Classic book on software design patterns.', 1),
(13, 'Clean Code', 'Robert C. Martin', '978-7-115-21654-3', 'Posts & Telecom Press', '2011-01-01', 16, 69.00, 12, 10, 'A-6-003', '/covers/clean-code.jpg', 'A handbook of agile software craftsmanship.', 1),

-- Literature - Novel
(14, 'One Hundred Years of Solitude', 'Gabriel Garcia Marquez', '978-7-544-22678-9', 'Nanhai Publishing', '2011-01-01', 21, 55.00, 20, 15, 'B-1-001', '/covers/solitude.jpg', 'A masterpiece of magical realism literature.', 1),
(15, 'To Kill a Mockingbird', 'Harper Lee', '978-7-544-73421-5', 'Yilin Press', '2017-01-01', 21, 48.00, 18, 14, 'B-1-002', '/covers/mockingbird.jpg', 'Classic American novel about justice and morality.', 1),
(16, '1984', 'George Orwell', '978-7-530-21234-5', 'Beijing October Literature', '2010-01-01', 21, 38.00, 25, 20, 'B-1-003', '/covers/1984.jpg', 'Dystopian novel about totalitarian society.', 1),
(17, 'The Great Gatsby', 'F. Scott Fitzgerald', '978-7-544-71234-8', 'Yilin Press', '2016-01-01', 21, 35.00, 15, 12, 'B-1-004', '/covers/gatsby.jpg', 'Classic novel about the American Dream.', 1),

-- Science - Physics
(18, 'A Brief History of Time', 'Stephen Hawking', '978-7-535-78901-2', 'Hunan Science Tech', '2010-01-01', 31, 45.00, 30, 25, 'C-1-001', '/covers/brief-time.jpg', 'Introduction to cosmology and theoretical physics.', 1),
(19, 'The Elegant Universe', 'Brian Greene', '978-7-535-72345-6', 'Hunan Science Tech', '2012-01-01', 31, 52.00, 15, 12, 'C-1-002', '/covers/elegant.jpg', 'String theory and the quest for the ultimate theory.', 1),

-- History
(20, 'Sapiens: A Brief History of Humankind', 'Yuval Noah Harari', '978-7-508-65432-1', 'CITIC Press', '2017-01-01', 41, 68.00, 25, 20, 'D-1-001', '/covers/sapiens.jpg', 'A brief history of human evolution and civilization.', 1),
(21, 'Guns, Germs, and Steel', 'Jared Diamond', '978-7-532-78901-8', 'Shanghai Translation', '2016-01-01', 41, 62.00, 18, 15, 'D-1-002', '/covers/guns-germs.jpg', 'The fates of human societies throughout history.', 1),

-- Economics
(22, 'Capital in the Twenty-First Century', 'Thomas Piketty', '978-7-508-64567-9', 'CITIC Press', '2014-01-01', 6, 98.00, 12, 10, 'E-1-001', '/covers/capital.jpg', 'Analysis of wealth inequality in the modern world.', 1),
(23, 'The Wealth of Nations', 'Adam Smith', '978-7-100-01234-5', 'Commercial Press', '2015-01-01', 6, 58.00, 20, 18, 'E-1-002', '/covers/wealth.jpg', 'Classic work on economics and free markets.', 1),

-- Philosophy
(24, 'Meditations', 'Marcus Aurelius', '978-7-201-08901-2', 'Tianjin People', '2018-01-01', 7, 38.00, 22, 18, 'F-1-001', '/covers/meditations.jpg', 'Stoic philosophy from the Roman Emperor.', 1),
(25, 'Being and Time', 'Martin Heidegger', '978-7-108-01234-5', 'SDX Joint Publishing', '2010-01-01', 7, 78.00, 8, 6, 'F-1-002', '/covers/being-time.jpg', 'Fundamental ontology and phenomenology.', 1);

-- -----------------------------------------------------
-- Insert Borrow Records
-- -----------------------------------------------------
INSERT INTO `borrow_record` (`id`, `user_id`, `book_id`, `borrow_date`, `due_date`, `return_date`, `renew_count`, `status`, `remark`) VALUES
-- Currently borrowed
(1, 4, 1, '2024-03-01 10:00:00', '2024-03-31 10:00:00', NULL, 0, 0, NULL),
(2, 4, 5, '2024-03-05 14:30:00', '2024-04-04 14:30:00', NULL, 0, 0, NULL),
(3, 5, 2, '2024-03-08 09:15:00', '2024-04-07 09:15:00', NULL, 1, 0, NULL),
(4, 5, 11, '2024-03-10 16:45:00', '2024-04-09 16:45:00', NULL, 0, 0, NULL),
(5, 6, 14, '2024-03-12 11:20:00', '2024-04-11 11:20:00', NULL, 0, 0, NULL),
(6, 6, 18, '2024-03-15 13:00:00', '2024-04-14 13:00:00', NULL, 0, 0, NULL),
(7, 7, 8, '2024-03-16 10:30:00', '2024-04-15 10:30:00', NULL, 0, 0, NULL),

-- Returned
(8, 4, 3, '2024-02-01 09:00:00', '2024-03-02 09:00:00', '2024-02-28 15:30:00', 0, 1, 'Returned on time'),
(9, 5, 6, '2024-02-10 14:00:00', '2024-03-11 14:00:00', '2024-03-08 10:00:00', 1, 1, 'Renewed once'),
(10, 6, 15, '2024-02-15 11:00:00', '2024-03-16 11:00:00', '2024-03-10 16:45:00', 0, 1, NULL),
(11, 7, 20, '2024-02-20 16:30:00', '2024-03-21 16:30:00', '2024-03-18 09:00:00', 0, 1, 'Returned early');

-- -----------------------------------------------------
-- Insert Borrow Configuration
-- -----------------------------------------------------
INSERT INTO `borrow_config` (`config_key`, `config_value`, `description`) VALUES
('max_borrow_count', '5', 'Maximum books a user can borrow at once'),
('max_borrow_days', '30', 'Default borrowing period in days'),
('max_renew_times', '2', 'Maximum renewal times per borrow'),
('renew_days', '15', 'Days added per renewal'),
('overdue_fine_rate', '0.50', 'Daily fine rate for overdue books (CNY)'),
('max_overdue_days', '90', 'Maximum overdue days before marked as lost');
