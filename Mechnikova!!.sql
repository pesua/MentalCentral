CREATE TABLE "patient" (
  "id_patient" SERIAL PRIMARY KEY,
  "fio" varchar,
  "date_birthday" date,
  "address" varchar,
  "phone" varchar,
  "diagnosis" int
);

CREATE TABLE "doctor" (
  "id_doctor" SERIAL PRIMARY KEY,
  "fio" varchar,
  "phone" varchar
);

CREATE TABLE "history" (
  "id_patient" int,
  "date_record" date,
  "type" varchar,
  "info" varchar
);

CREATE TABLE "visit" (
  "id_patient" int,
  "id_doctor" int,
  "type" varchar,
  "time" datetime,
  "teraphy" varchar
);

ALTER TABLE "patient" ADD FOREIGN KEY ("id_patient") REFERENCES "history" ("id_patient");

ALTER TABLE "visit" ADD FOREIGN KEY ("id_patient") REFERENCES "patient" ("id_patient");

ALTER TABLE "visit" ADD FOREIGN KEY ("id_doctor") REFERENCES "doctor" ("id_doctor");
