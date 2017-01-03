--
-- Structure de la table 'stock_recommendation'
--

CREATE TABLE IF NOT EXISTS stock_recommendation (
  username varchar(255) NOT NULL,
  id_product int(11) NOT NULL,
  score FLOAT NOT NULL,
  PRIMARY KEY (username , id_product ),
  INDEX( username )
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

