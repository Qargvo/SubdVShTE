CREATE TABLE IF NOT EXISTS products
(
    id                 SERIAL PRIMARY KEY,
    arduino_board_type VARCHAR(32),
    processor_type     VARCHAR(32),
    io_voltage         VARCHAR(16),
    cpu_frequency      INT,
    analogue_inp_out   VARCHAR(16),
    digital_inp_out    VARCHAR(16),
    eeprom             FLOAT,
    scram              FLOAT,
    flash              INT,
    usb                VARCHAR(16),
    uart               VARCHAR(8),
    photo              VARCHAR(128),
    rate               FLOAT,
    price              FLOAT

);
CREATE TABLE IF NOT EXISTS points
(
    point_id SERIAL PRIMARY KEY,
    tel      VARCHAR(11),
    address  VARCHAR(32)

);
CREATE TABLE IF NOT EXISTS availability
(
    point_id   BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    cnt        INT,
    PRIMARY KEY (point_id, product_id)
);