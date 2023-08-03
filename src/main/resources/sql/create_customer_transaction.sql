CREATE TABLE transaction (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             customer_id VARCHAR(50),
                             transaction_id VARCHAR(50),
                             transaction_date DATE,
                             transaction_amount DECIMAL(10, 2)
);