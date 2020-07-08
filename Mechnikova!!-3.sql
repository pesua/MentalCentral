CREATE TABLE [patient] (
  [id_patient] int PRIMARY KEY IDENTITY(1, 1),
  [fio] nvarchar(255),
  [date_birthday] date,
  [address] nvarchar(255),
  [phone] nvarchar(255),
  [diagnosis] int
)
GO

CREATE TABLE [doctor] (
  [id_doctor] int PRIMARY KEY IDENTITY(1, 1),
  [fio] nvarchar(255),
  [phone] nvarchar(255)
)
GO

CREATE TABLE [history] (
  [id_patient] int,
  [date_record] date,
  [type] nvarchar(255),
  [info] nvarchar(255)
)
GO

CREATE TABLE [visit] (
  [id_patient] int,
  [id_doctor] int,
  [type] nvarchar(255),
  [time] datetime,
  [teraphy] nvarchar(255)
)
GO

ALTER TABLE [patient] ADD FOREIGN KEY ([id_patient]) REFERENCES [history] ([id_patient])
GO

ALTER TABLE [visit] ADD FOREIGN KEY ([id_patient]) REFERENCES [patient] ([id_patient])
GO

ALTER TABLE [visit] ADD FOREIGN KEY ([id_doctor]) REFERENCES [doctor] ([id_doctor])
GO
