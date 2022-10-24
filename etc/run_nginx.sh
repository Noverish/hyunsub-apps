#!/bin/bash

ROOT_PATH=`pwd`
CERT_PATH=$ROOT_PATH/cert
AUTH_CONF_PATH=$ROOT_PATH/../hyunsub-auth/etc/auth-local.conf
VIDEO_CONF_PATH=$ROOT_PATH/../hyunsub-video/etc/video-local.conf
PHOTO_CONF_PATH=$ROOT_PATH/../hyunsub-photo/etc/photo-local.conf
ENCODE_CONF_PATH=$ROOT_PATH/../hyunsub-encode/etc/encode-local.conf
APPAREL_CONF_PATH=$ROOT_PATH/../hyunsub-apparel/etc/apparel-local.conf
DIVISION_CONF_PATH=$ROOT_PATH/../hyunsub-division/etc/division-local.conf
NGINX_IMG=nginx:1.23.1

docker run -d --rm -p 80:80 -p 443:443 \
  --name hyunsub_nginx \
  --platform linux/amd64 \
	-v $CERT_PATH:/etc/nginx/cert \
	-v $AUTH_CONF_PATH:/etc/nginx/conf.d/auth-local.conf \
	-v $VIDEO_CONF_PATH:/etc/nginx/conf.d/video-local.conf \
	-v $PHOTO_CONF_PATH:/etc/nginx/conf.d/photo-local.conf \
	-v $ENCODE_CONF_PATH:/etc/nginx/conf.d/encode-local.conf \
	-v $APPAREL_CONF_PATH:/etc/nginx/conf.d/apparel-local.conf \
	-v $DIVISION_CONF_PATH:/etc/nginx/conf.d/division-local.conf \
	$NGINX_IMG
