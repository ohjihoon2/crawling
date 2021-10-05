package com.crawling.example.common;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Crawling {
    public static void main(String args[]){
        try {
            // 1. 수집 대상 URL
            String URL = "https://heodolf.tistory.com/18";

            // 2. Connection 생성
            Connection conn = Jsoup.connect(URL);

            // 3. HTML 파싱.
            Document html = conn.get(); // conn.post();

            // 4. HTML 출력
            System.out.println(" Start -- ");
            System.out.println( html.toString() );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
