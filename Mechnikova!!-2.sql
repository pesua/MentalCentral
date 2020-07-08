CREATE TABLE `patient` (
  `id_patient` int PRIMARY KEY AUTO_INCREMENT,
  `fio` varchar(255),
  `date_birthday` date,
  `address` varchar(255),
  `phone` varchar(255),
  `diagnosis` int
);

CREATE TABLE `doctor` (
  `id_doctor` int PRIMARY KEY AUTO_INCREMENT,
  `fio` varchar(255),
  `phone` varchar(255)
);

CREATE TABLE `history` (
  `id_patient` int,
  `date_record` date,
  `type` varchar(255),
  `info` varchar(255)
);

CREATE TABLE `visit` (
  `id_patient` int,
  `id_doctor` int,
  `type` varchar(255),
  `time` datetime,
  `teraphy` varchar(255)
);

ALTER TABLE `patient` ADD FOREIGN KEY (`id_patient`) REFERENCES `history` (`id_patient`);

ALTER TABLE `visit` ADD FOREIGN KEY (`id_patient`) REFERENCES `patient` (`id_patient`);

ALTER TABLE `visit` ADD FOREIGN KEY (`id_doctor`) REFERENCES `doctor` (`id_doctor`);
