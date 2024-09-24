CREATE TABLE products
(
    product_id       varchar(255) PRIMARY KEY,
    seller_id        BIGINT       NOT NULL,
    category         VARCHAR(255) NOT NULL,
    product_name     VARCHAR(255) NOT NULL,
    sales_start_date DATE,
    sales_end_date   DATE,
    product_status   VARCHAR(50),
    brand            VARCHAR(255),
    manufacturer     VARCHAR(255),
    sales_price      INTEGER      NOT NULL,
    stock_quantity   INTEGER   DEFAULT 0,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_status ON products (product_status);
CREATE INDEX idx_products_category ON products (category);
CREATE INDEX idx_products_brand ON products (brand);
CREATE INDEX idx_products_manufacturer ON products (manufacturer);
CREATE INDEX idx_products_seller_id ON products (seller_id);

CREATE TABLE transaction_reports
(
    transaction_date     date,
    transaction_type     VARCHAR(50)    NOT NULL,
    transaction_count    BIGINT         NOT NULL,
    total_amount         BIGINT         NOT NULL,
    customer_count       BIGINT         NOT NULL,
    order_count          BIGINT         NOT NULL,
    payment_method_count BIGINT         NOT NULL,
    avg_product_count    DECIMAL(15, 0) NOT NULL,
    total_item_quantity  BIGINT         NOT NULL,
    PRIMARY KEY (transaction_date, transaction_type)
);



CREATE TABLE category_reports
(
    stat_date              DATE           NOT NULL,
    category               VARCHAR(255)   NOT NULL,
    product_count          INTEGER        NOT NULL,
    avg_sales_price        DECIMAL(15, 0) NOT NULL,
    max_sales_price        DECIMAL(15, 0) NOT NULL,
    min_sales_price        DECIMAL(15, 0) NOT NULL,
    total_stock_quantity   INTEGER        NOT NULL,
    potential_sales_amount DECIMAL(20, 0) NOT NULL,
    PRIMARY KEY (stat_date, category)
);



CREATE TABLE brand_reports
(
    stat_date            DATE           NOT NULL,
    brand                VARCHAR(255)   NOT NULL,
    product_count        INTEGER        NOT NULL,
    avg_sales_price      DECIMAL(15, 0) NOT NULL,
    max_sales_price      DECIMAL(15, 0) NOT NULL,
    min_sales_price      DECIMAL(15, 0) NOT NULL,
    total_stock_quantity INTEGER        NOT NULL,
    avg_stock_quantity   DECIMAL(15, 0) NOT NULL,
    total_stock_value    DECIMAL(20, 0) NOT NULL,
    PRIMARY KEY (stat_date, brand)
);



CREATE TABLE manufacturer_reports
(
    stat_date            DATE           NOT NULL,
    manufacturer         VARCHAR(255)   NOT NULL,
    product_count        INTEGER        NOT NULL,
    avg_sales_price      DECIMAL(15, 0) NOT NULL,
    total_stock_quantity INTEGER        NOT NULL,
    PRIMARY KEY (stat_date, manufacturer)
);



CREATE TABLE product_status_reports
(
    stat_date          DATE           NOT NULL,
    product_status     VARCHAR(50)    NOT NULL,
    product_count      INTEGER        NOT NULL,
    avg_stock_quantity DECIMAL(15, 0) NOT NULL,
    PRIMARY KEY (stat_date, product_status)
);

