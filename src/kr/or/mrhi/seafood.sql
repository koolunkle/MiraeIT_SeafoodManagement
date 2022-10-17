-- 수산물 데이터베이스 생성
DROP DATABASE IF EXISTS seafooddb;
CREATE DATABASE IF NOT EXISTS seafooddb;

-- 수산물 데이터베이스 사용 선언
USE seafooddb;

-- 수산물 테이블 생성
DROP TABLE IF EXISTS seafood;
CREATE TABLE seafood (
id CHAR(6),
naming VARCHAR(10) NOT NULL,
freshness INT NOT NULL,
size INT NOT NULL,
weight INT NOT NULL,
total INT NULL,
avr DECIMAL(5,2) NULL,
grade VARCHAR(2) NULL,
rate INT NULL,
CONSTRAINT pk_seafood_id PRIMARY KEY (id)
);

-- 수산물 수정 테이블 생성
DROP TABLE IF EXISTS update_seafood;
CREATE TABLE update_seafood (
id CHAR(6) NOT NULL,
naming VARCHAR(10) NOT NULL,
freshness INT NOT NULL,
size INT NOT NULL,
weight INT NOT NULL,
total INT NULL,
avr DECIMAL(5,2) NULL,
grade VARCHAR(2) NULL,
rate INT NULL,
updateDate DATETIME
);

-- 수산물 삭제 테이블 생성
DROP TABLE IF EXISTS delete_seafood;
CREATE TABLE delete_seafood (
id CHAR(6) NOT NULL,
naming VARCHAR(10) NOT NULL,
freshness INT NOT NULL,
size INT NOT NULL,
weight INT NOT NULL,
total INT NULL,
avr DECIMAL(5,2) NULL,
grade VARCHAR(2) NULL,
rate INT NULL,
deleteDate DATETIME
);

-- 수산물 수정 트리거 생성
DROP TRIGGER IF EXISTS trg_update_seafood;
DELIMITER $$
CREATE TRIGGER trg_update_seafood
AFTER UPDATE ON seafood
FOR EACH ROW
BEGIN
INSERT INTO update_seafood VALUES (OLD.id, OLD.naming, OLD.freshness,
OLD.size, OLD.weight, OLD.total, OLD.avr, OLD.grade, OLD.rate, now());
END$$ 
DELIMITER ;

-- 수산물 삭제 트리거 생성
DROP TRIGGER IF EXISTS trg_delete_seafood;
DELIMITER $$
CREATE TRIGGER trg_delete_seafood
AFTER DELETE ON seafood
FOR EACH ROW
BEGIN
INSERT INTO delete_seafood VALUES (OLD.id, OLD.naming, OLD.freshness,
OLD.size, OLD.weight, OLD.total, OLD.avr, OLD.grade, OLD.rate, now());
END$$ 
DELIMITER ;

-- 수산물 데이터 입력 프로시저 생성
DROP PROCEDURE IF EXISTS insert_data;
DELIMITER $$
CREATE PROCEDURE insert_data(
IN in_id CHAR(6),
IN in_naming VARCHAR(10),
IN in_freshness INT,
IN in_size INT,
IN in_weight INT
)
BEGIN
DECLARE in_total INT;
DECLARE in_avr DOUBLE;
DECLARE in_grade VARCHAR(2);
SET in_total = (in_freshness + in_size + in_weight);
SET in_avr = (in_total / 3.0);
SET in_grade = 
CASE
WHEN in_avr >= 90.0 THEN 'A'
WHEN in_avr >= 80.0 THEN 'B'
WHEN in_avr >= 70.0 THEN 'C'
WHEN in_avr >= 60.0 THEN 'D'
WHEN in_avr >= 50.0 THEN 'E'
ELSE
'F'
END;
INSERT INTO seafood(id, naming, freshness, size, weight, total, avr, grade)
VALUES(in_id, in_naming, in_freshness, in_size, in_weight, in_total, in_avr, in_grade);
END$$
DELIMITER ;

-- 수산물 데이터 수정 프로시저 생성
DROP PROCEDURE IF EXISTS update_data;
DELIMITER $$
CREATE PROCEDURE update_data(
IN in_id CHAR(6),
IN in_freshness INT,
IN in_size INT,
IN in_weight INT
)
BEGIN
DECLARE in_total INT;
DECLARE in_avr DOUBLE;
DECLARE in_grade VARCHAR(2);
SET in_total = (in_freshness + in_size + in_weight);
SET in_avr = (in_total / 3.0);
SET in_grade = 
CASE
WHEN in_avr >= 90.0 THEN 'A'
WHEN in_avr >= 80.0 THEN 'B'
WHEN in_avr >= 70.0 THEN 'C'
WHEN in_avr >= 60.0 THEN 'D'
WHEN in_avr >= 50.0 THEN 'E'
ELSE
'F'
END;
UPDATE seafood 
SET 
freshness = in_ freshness,
size = in_ size,
weight = in_weight,
total = in_total,
avr = in_avr,
grade = in_grade
WHERE
id = in_id;
END$$
DELIMITER ;

-- 수산물 데이터 인덱스 생성 (1)
ALTER TABLE seafood DROP INDEX idx_seafood_id;
CREATE INDEX idx_seafood_id ON seafood(id);

-- 수산물 데이터 인덱스 생성 (2)
ALTER TABLE seafood DROP INDEX idx_seafood_naming;
CREATE INDEX idx_seafood_naming ON seafood(naming);

-- 수산물 데이터 최댓값 조회 함수 생성
SET GLOBAL log_bin_trust_function_creators = 1;
DROP FUNCTION IF EXISTS getMaxTotal;
DELIMITER $$
CREATE FUNCTION getMaxTotal()
RETURNS VARCHAR(100)
BEGIN
DECLARE result VARCHAR(100);
SET result = 0;
SELECT MAX(total) INTO result FROM seafood;
RETURN result;
END $$
DELIMITER ;
SELECT * FROM seafood WHERE total = (SELECT getMaxTotal());

-- 수산물 데이터 최솟값 조회 함수 생성
SET GLOBAL log_bin_trust_function_creators = 1;
DROP FUNCTION IF EXISTS getMinTotal;
DELIMITER $$
CREATE FUNCTION getMinTotal()
RETURNS VARCHAR(100)
BEGIN
DECLARE result VARCHAR(100);
SET result = 0;
SELECT MIN(total) INTO result FROM seafood;
RETURN result;
END $$
DELIMITER ;
SELECT * FROM seafood WHERE total = (SELECT getMinTotal());