CREATE TABLE USER ("ID" INTEGER not null primary key, "NAME" VARCHAR(50) not null)
CREATE TABLE ROLE("ID" INTEGER not null primary key, "NAME" VARCHAR(50) not null)
CREATE TABLE USER_ROLE("USER_ID" INTEGER not null, "ROLE_ID" INTEGER not null)
ALTER TABLE USER_ROLE ADD CONSTRAINT USER_ID_FK FOREIGN KEY ("USER_ID") REFERENCES USER ("ID")
ALTER TABLE USER_ROLE ADD CONSTRAINT ROLE_ID_FK FOREIGN KEY ("ROLE_ID") REFERENCES ROLE ("ID")