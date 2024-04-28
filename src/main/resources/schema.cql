DROP TABLE IF EXISTS chat;
DROP TABLE IF EXISTS user_chat;

CREATE TABLE IF NOT EXISTS chat (
  chatid uuid PRIMARY KEY,
  userid uuid,
  createdtime timestamp
);

CREATE TABLE IF NOT EXISTS user_chat (
  id uuid,
  userid uuid,
  chatid uuid,
  role text,
  createdtime timestamp,
  textembedding list<double>,
  PRIMARY KEY (id)
);