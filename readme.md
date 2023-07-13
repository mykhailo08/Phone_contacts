Додаю коротку інструкцію для користування та тестування данного тествого проекту
1. Необхідно створити БД за допомогою наступних скриптів:
```
use phonecontactsdb;

create table users (
id int auto_increment primary key,
username varchar(30) not null unique,
password varchar(255) not null
);

create table contacts (
id int auto_increment primary key,
name varchar(40) not null unique,
user_id int not null,
foreign key (user_id) references users(id)
);

create table phone_numbers (
id int auto_increment primary key,
number char(13) not null unique,
contact_id int not null,
foreign key (contact_id) references contacts(id)
);

create table email_addresses (
id int auto_increment primary key,
email varchar(40) not null unique,
contact_id int not null,
foreign key (contact_id) references contacts(id)
);
```
2. Також додаю текілька прикладів тестування з коротким поясненням, як я бачив реалізацію вимог тестового завдання.

   - Для тестування данної вимоги - "Register in the app, login and password should be provided during registration"
   >відправлямоє на енподінт http://localhost:8080/auth/register запит з тілом:
   ```
   { 
   "login": "your-login",
   "password": "your-password"
   }
   ```
   >Очікуваний результат: Створився юзер в базі данних
    - Для тестування данної вимоги - "Login to the app"
    >відправлямоє на енподінт http://localhost:8080/auth/login запит з тілом:
   ```
   { 
   "login": "your-login",
   "password": "your-password"
   }
   ```
   >Очікуваний результат: Повернувася JWT Token який використовується, для аутентифікації наступиних запитів
    - Для тестування данної вимоги - "Add new contact"
   >відправлямоє на енподінт http://localhost:8080/contacts/create-contact запит з тілом:
   ```
   {
    "name": "name",
    "emails": ["aaaaaa@xxx.com", "bbbb@yyy.com"],
    "phones": ["+380123456789"]
   }
   ```
   >а також в Headers треба додати наступну пару key-value
   ```
   Authorization : Bearer TOKEN(приклад Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0NjYiLCJleHAiOjE2ODg4NTc5NDgsImlhdCI6MTY4ODc3MTU0OH0.tSYhA1TAdYJaMeMC-Wpw6aZKPF8pArpkaFgCR5GCgaBaX76axIgLSO6F86MBDEsxZSqerQgl6jFqz5r_HdITGQ)
   ```
   >Очікуваний результат: Контакт був успішно створений
   - Для тестування данної вимоги - "Edit existing contact"
   >відправлямоє на енподінт http://localhost:8080/contacts/edit-contact запит з тілом:
   ```
   {
    "name": "name",
    "emails": ["qqqqq@xxx.com", "wwwww@yyy.com"],
    "phones": ["+380123456789","+380123456781"]
   }
   ```
   >а також в Headers треба додати наступну пару key-value
   ```
   Authorization : Bearer TOKEN(приклад Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0NjYiLCJleHAiOjE2ODg4NTc5NDgsImlhdCI6MTY4ODc3MTU0OH0.tSYhA1TAdYJaMeMC-Wpw6aZKPF8pArpkaFgCR5GCgaBaX76axIgLSO6F86MBDEsxZSqerQgl6jFqz5r_HdITGQ)
   ```
   >Очікуваний результат: Контакт був успішно змінений.  
    - Для тестування данної вимоги - "Delete existing contact"
   >відправлямоє на енподінт http://localhost:8080/contacts/delete/name
   >а також в Headers треба додати наступну пару key-value
   ```
   Authorization : Bearer TOKEN(приклад Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0NjYiLCJleHAiOjE2ODg4NTc5NDgsImlhdCI6MTY4ODc3MTU0OH0.tSYhA1TAdYJaMeMC-Wpw6aZKPF8pArpkaFgCR5GCgaBaX76axIgLSO6F86MBDEsxZSqerQgl6jFqz5r_HdITGQ)
   ```
   >Очікуваний результат: Контакт був успішно видалений.  
    - Для тестування данної вимоги - "Delete existing contact" Але вже з видаленням всіх контактів для юзера, так як, не достатньо інформації про те що треба видаляти всіх, або одного.
   >відправлямоє на енподінт http://localhost:8080/contacts/delete-all
   >а також в Headers треба додати наступну пару key-value
   ```
   Authorization : Bearer TOKEN(приклад Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0NjYiLCJleHAiOjE2ODg4NTc5NDgsImlhdCI6MTY4ODc3MTU0OH0.tSYhA1TAdYJaMeMC-Wpw6aZKPF8pArpkaFgCR5GCgaBaX76axIgLSO6F86MBDEsxZSqerQgl6jFqz5r_HdITGQ)
   ```
   >Очікуваний результат: Всі контакти у юзера успішно видалені.  
   - Для тестування данної вимоги - "Get list of existing contacts".
   >відправлямоє на енподінт http://localhost:8080/contacts
   >а також в Headers треба додати наступну пару key-value
   ```
   Authorization : Bearer TOKEN(приклад Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0NjYiLCJleHAiOjE2ODg4NTc5NDgsImlhdCI6MTY4ODc3MTU0OH0.tSYhA1TAdYJaMeMC-Wpw6aZKPF8pArpkaFgCR5GCgaBaX76axIgLSO6F86MBDEsxZSqerQgl6jFqz5r_HdITGQ)
   ```
   >Очікуваний результат: Всі контакти у юзера успішно повернуті. 
   
