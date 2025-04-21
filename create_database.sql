
use twiliodb;
-- Users table (renamed to avoid reserved words)

CREATE TABLE User (
    id      	 INT PRIMARY KEY auto_increment,
    full_name    VARCHAR(100),
    username     VARCHAR(50) UNIQUE NOT NULL,
    passwd       VARCHAR(60) NOT NULL,
    job          VARCHAR(50),
    birth_date   DATE,
    email        VARCHAR(100),
    address      VARCHAR(100),
    role    VARCHAR(50) NOT NULL  -- e.g., 'customer' or 'administrator'
);

-- Twilio table
CREATE TABLE TwilioAccount (
	id			  INT PRIMARY KEY auto_increment,
    sid           VARCHAR(50) UNIQUE,
    token         VARCHAR(255) UNIQUE,
    sender_id     CHAR(15),
    phone_number  CHAR(15),
    isVerified	  BOOLEAN NOT NULL,
    number_of_msg INT DEFAULT 0,
    user_id       INT,
    CONSTRAINT fk_twilio_user
        FOREIGN KEY (user_id)
        REFERENCES User(id)
        ON DELETE CASCADE
);


-- Verification Code table
CREATE TABLE VerificationCode (
    id            	   INT PRIMARY KEY auto_increment,
    verification_code  INT,
    expired_date       TIMESTAMP,
    created_date       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id            INT,
    CONSTRAINT fk_vcode_user
        FOREIGN KEY (user_id)
        REFERENCES User(id)
        ON DELETE CASCADE
);

-- Outgoing SMS Messages table
CREATE TABLE OutboundMsg (
    id         INT PRIMARY KEY auto_increment,
    from_num   CHAR(15),
    to_num     CHAR(15),
    body       VARCHAR(255),
    msg_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id    INT,
    CONSTRAINT fk_sendmsg_user
        FOREIGN KEY (user_id)
        REFERENCES User(id)
        ON DELETE CASCADE
);

-- Inbound (or Bounded) SMS Messages table (renamed to inbound_msg for clarity)
CREATE TABLE InboundMsg (
    id         INT PRIMARY KEY auto_increment,
    from_num   CHAR(15),
    to_num     CHAR(15),
    body       VARCHAR(255),
    msg_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id    INT,
    CONSTRAINT fk_inboundmsg_user
        FOREIGN KEY (user_id)
        REFERENCES User(id)
        ON DELETE CASCADE
);

-- Create a user with admin privileges
INSERT INTO twiliodb.User VALUES(1, 'System Admin', 'admin', 'admin', 'Telecom Eng.', '2000-09-06', 'admin@example.com', 'Cairo, Egypt', 'admin');


