## grocer shopping RESTful API

API for planning grocery shopping

### db schema

The database schema from *dbdiagramm.io*

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

