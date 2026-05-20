
CREATE TABLE niveau (
  id int PRIMARY KEY AUTO_INCREMENT,
  libelle varchar(100) NOT NULL
);

CREATE TABLE classe (
  id int PRIMARY KEY AUTO_INCREMENT,
  libelle varchar(50) NOT NULL,
  niveau_id int,
  CONSTRAINT fk_niveau FOREIGN KEY (niveau_id)
    REFERENCES niveau(id)
);


CREATE TABLE role (
  id smallint PRIMARY KEY,
  libelle varchar(100) NOT NULL
);
CREATE TABLE user (
  id int PRIMARY KEY AUTO_INCREMENT,
  login varchar(100) NOT NULL,
  password varchar(255) NOT NULL,
  role_id smallint NOT NULL,
  CONSTRAINT fk_role FOREIGN KEY (role_id)
    REFERENCES role(id),
  CONSTRAINT unique_login UNIQUE (login)
);

CREATE TABLE professeur (
  id int PRIMARY KEY,
  nom varchar(100) NOT NULL,
  prenom varchar(100) NOT NULL,
  CONSTRAINT fk_user_professeur FOREIGN KEY (id)
    REFERENCES user(id)
);


CREATE TABLE responsable (
  id int PRIMARY KEY,
  CONSTRAINT fk_user_responsable FOREIGN KEY (id)
    REFERENCES user(id)
);

CREATE TABLE projet (
  id int PRIMARY KEY AUTO_INCREMENT,
  libelle varchar(100) NOT NULL,
  description longtext NOT NULL
);

CREATE TABLE groupe (
  id int PRIMARY KEY AUTO_INCREMENT,
  libelle varchar(50) NOT NULL,
  projet_id int,
  CONSTRAINT fk_projet FOREIGN KEY (projet_id)
    REFERENCES projet(id)
);

CREATE TABLE etudiant (
  id int PRIMARY KEY,
  nom varchar(50) NOT NULL,
  prenom varchar(50) NOT NULL,
  groupe_id int,
  CONSTRAINT fk_groupe FOREIGN KEY (groupe_id)
    REFERENCES groupe(id),
  classe_id int,
  CONSTRAINT fk_classe FOREIGN KEY (classe_id)
    REFERENCES classe(id),
  CONSTRAINT fk_user_etudiant FOREIGN KEY (id)
    REFERENCES user(id)
);


CREATE TABLE tache (
  id int PRIMARY KEY AUTO_INCREMENT,

  titre varchar(100) NOT NULL,
  description longtext NOT NULL,

  professeur_id int NOT NULL,
  CONSTRAINT fk_professeur FOREIGN KEY (professeur_id)
    REFERENCES professeur(id)
);

CREATE TABLE cible_tache_groupe (
  id int PRIMARY KEY AUTO_INCREMENT,
  tache_id int NOT NULL,
  groupe_id int NOT NULL,
  CONSTRAINT uq_tache_groupe UNIQUE (tache_id, groupe_id),
  CONSTRAINT fk_tache_given_to_groupe FOREIGN KEY (tache_id)
    REFERENCES tache(id),
  CONSTRAINT fk_groupe_given_tache FOREIGN KEY (groupe_id)
    REFERENCES groupe(id)
);

CREATE TABLE submission (
  id int PRIMARY KEY AUTO_INCREMENT,
  description longtext,
  date_soumission date,
  date_validation date,
  note decimal(4,3) DEFAULT NULL,
  cible_id int,
  CONSTRAINT fk_cible FOREIGN KEY (cible_id)
    REFERENCES cible_tache_groupe(id) ON DELETE CASCADE,
  CONSTRAINT chk_date CHECK (date_soumission <= date_validation)
);

CREATE TABLE attachement (
  id int PRIMARY KEY AUTO_INCREMENT,
  cheminFichier varchar(255) NOT NULL,
  type varchar(50) NOT NULL,
  size int NOT NULL,
  dateSoumission datetime NOT NULL,
  submission_id int,
  CONSTRAINT fk_submission FOREIGN KEY (submission_id)
    REFERENCES submission(id)
);

CREATE TABLE meta (
  id int PRIMARY KEY AUTO_INCREMENT,
  libelle varchar(50),
  statut varchar(51) 
);

CREATE TABLE administrateur (
  id int PRIMARY KEY,
  nom varchar(100) NOT NULL,
  prenom varchar(100) NOT NULL,
  CONSTRAINT fk_user_administrateur FOREIGN KEY (id)
    REFERENCES user(id)
);
INSERT INTO meta(libelle, statut) VALUES("LIBELLE_PLACEHOLDER","active");

INSERT INTO `role`(id, libelle) VALUES
(1, "professeur"),
(2, "etudiant"),
(3, "responsable"),
(4, "administrateur");


INSERT INTO user(role_id, login, password) VALUES
(1, "professeur", "professeur"),
(1, "mohamme mani", "mohamme mani"),
(1, "java mouhib", "java mouhib"),
(1, "echchadli", "echchadli"),
(1, "agriculture", "agriculture"),
(1, "belkassem", "belkassem"),

(2, "etudiant", "etudiant"),
(2, "youness", "youness"),
(2, "marzouki", "marzouki"),
(2, "el hakimi", "el hakimi"),
(2, "yousra maizi", "yousra maizi"),
(2, "abdessamii", "abdessamii"),

(3, "responsable", "responsable"),
(3, "ounnessa", "ounnessa"),
(3, "xi wahed", "xi wahed"),
(3, "xi wahed akhor", "xi wahed akhor"),

(4, "administrateur", "administrateur"),
(4, "hicham li fel idara", "hicham li fel idara"),
(4, "lbent li flidara", "lbent li flidara"),
(4, "lvibe codder de syllab", "lvibe codder de syllab"),
(4, "abdel9ader l3esas", "abdel9ader l3esas");


INSERT INTO professeur(id, nom, prenom) VALUES
(1, "professeur", "professeur"),
(2, "mohamme mani", "mohamme mani"),
(3, "java mouhib", "java mouhib"),
(4, "echchadli", "echchadli"),
(5, "agriculture", "agriculture"),
(6, "belkassem", "belkassem");

INSERT INTO etudiant(id, nom, prenom) VALUES
(7, "etudiant", "dans g1"),
(8, "youness", "youness"),
(9, "marzouki", "marzouki"),
(10, "el hakimi", "el hakimi"),
(11, "yousra maizi", "yousra maizi"),
(12, "abdessamii", "abdessamii");

INSERT INTO responsable(id) VALUES
(13),
(14),
(15),
(16);

INSERT INTO administrateur(id, nom, prenom) VALUES
(17, "administrateur", "administrateur"),
(18, "hicham li fel idara", "hicham li fel idara"),
(19, "lbent li flidara", "lbent li flidara"),
(20, "lvibe codder de syllab", "lvibe codder de syllab"),
(21, "abdel9ader l3esas", "abdel9ader l3esas");
