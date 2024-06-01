## Grocery shopping RESTful API

This application is designed to expose a set of RESTful endpoints to manage grocery shopping, It serves as the backend
component for other web or mobile applications.

### Database schema

The database schema from **dbdiagramm.io**

Table app_user {
id integer [primary key]
firstname varchar
latname varchar
email varchar
app_username varchar
app_password hash
role enum
is_verified boolean
}

Table verification_token {
id integer [primary key]
token varchar
expiry_date date
user_id int [ref: - app_user.id]
}

Table supermarket {
id integer [primary key]
name enum
}

Table grocery_item {
id integer [primary key]
name varchar
category enum
}

Table shopping_list {
id integer [primary key]
receipt_picture_url varchar
is_done bool
user_id int [ref: > app_user.id]
supermarket_id int [ref: > supermarket.id]
}

Table shopping_list_item {
id integer [primary key]
quantity int
price float
shopping_list_id int [ref: > shopping_list.id]
grocery_item_id int [ref: > grocery_item.id]
}

### Setup

Before starting the project run the following command in the terminal:

```
./gradlew clean build openApiGenerate
```

Or just run:

```
./gradlew openApiGenerate
```

### Swagger UI

http://localhost:9000/api/swagger-ui/index.html