// Инициализация административного пользователя
db.createUser({
  user: "admin",
  pwd: "admin123",
  roles: [{ role: "userAdminAnyDatabase", db: "admin" }]
});

// Создание пользователя с определенной ролью для конкретной базы данных
db.createUser({
  user: "rhogoron",
  pwd: "rhogoron",
  roles: [{ role: "readWrite", db: "myDatabase" }]
});
