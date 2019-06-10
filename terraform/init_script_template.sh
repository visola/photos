#!/bin/bash
sudo apt-get update

sudo apt-get install -y openjdk-8-jre

sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password password ${db_password}'
sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password_again password ${db_password}'
sudo apt-get -y install mysql-server
mysql -u root '-p${db_password}' -e 'CREATE DATABASE life_booster'

sudo apt-get install -y nginx
sudo rm /etc/nginx/sites-enabled/default
sudo tee -a /etc/nginx/sites-enabled/photos.conf > /dev/null <<EOF
server {
    listen 80 default_server;
    listen [::]:80 default_server;
    client_max_body_size 0;

    location / {
        proxy_pass http://localhost:8080/;
    }
}
EOF

sudo add-apt-repository -y ppa:certbot/certbot
sudo apt-get update
sudo apt-get install -y certbot python-certbot-nginx
sudo certbot register --register-unsafely-without-email --agree-tos
sudo certbot -n --nginx -d "${subdomain}"
