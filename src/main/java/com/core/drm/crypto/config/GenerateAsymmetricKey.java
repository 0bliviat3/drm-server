package com.core.drm.crypto.config;

/*
최초 비대칭키 생성시 사용할 main
생성된 비대칭키는 별도 관리 필요
 */
public class GenerateAsymmetricKey {

    public static void main(String[] args) {
        try {
            new RSAKey().generate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
