package com.crawling.example.controller;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class MainController {

    @RequestMapping("/")
    public String Main(){
        System.out.println("[ 메인 페이지 ]");

        return "index";
    }


    @RequestMapping("/test")
    public String CrawlingTest(){
        System.out.println("[ 크롤링 페이지 ]");

        try {
            // 1. 수집 대상 URL
            String URL = "https://heodolf.tistory.com/18";

            // 2. Connection 생성
            Connection conn = Jsoup.connect(URL);

            // 3. HTML 파싱.
            Document html = conn.get(); // conn.post();

            // 4. HTML 출력
//            System.out.println(" Start -- ");
//            System.out.println( html.toString() );


            // 4. 요소 탐색
            // 4-1. Attribute 탐색
            System.out.println("[Attribute 탐색]");
            Elements fileblocks = html.getElementsByClass("fileblock");
            for( Element fileblock : fileblocks ) {

                Elements files = fileblock.getElementsByTag("a");
                for( Element elm : files ) {
                    String text = elm.text();
                    String href = elm.attr("href");

                    System.out.println( text+" > "+href );
                }
            }

            // 4-2. CSS Selector 탐색
            System.out.println("\n[CSS Selector 탐색]");
            Elements files = html.select(".fileblock a");
            for( Element elm : files ) {
                String text = elm.text();
                String href = elm.attr("href");

                System.out.println( text+" > "+href );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "crawling";
    }

    @RequestMapping("/crawling/{search}")
    public String Crawling(@PathVariable("search") String search){
        System.out.println("[ 크롤링 페이지 ]");

        try {
            // 1. 수집 대상 URL
            String URL1 = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query="+search;

            // 2. Connection 생성
            Connection conn1 = Jsoup.connect(URL1);

            // 3. HTML 파싱.
            Document html1 = conn1.get(); // conn.post();


            // 1. 수집 대상 URL
            String URL2 = "https://www.google.com/search?q="+search;

            // 2. Connection 생성
            Connection conn2 = Jsoup.connect(URL2);

            // 3. HTML 파싱.
            Document html2 = conn2.get(); // conn.post();

            // 4. HTML 출력
//            System.out.println(" Start -- ");
//            System.out.println( html.toString() );


            // 4. 요소 탐색
            // 4-1. Attribute 탐색

            /*System.out.println("[Attribute 탐색]");
            Elements fileblocks = html.getElementsByClass("fileblock");
            for( Element fileblock : fileblocks ) {

                Elements files = fileblock.getElementsByTag("a");
                for( Element elm : files ) {
                    String text = elm.text();
                    String href = elm.attr("href");

                    System.out.println( text+" > "+href );
                }
            }*/

            // 4-2. CSS Selector 탐색
            System.out.println("\n[CSS Selector 탐색]");
            Elements files = html1.select(".lst_related_srch._list_box .item .keyword");
            System.out.println("===== Naver 연관검색어 START =====");
            for( Element elm : files ) {
                String text = elm.text();
                String href = elm.attr("href");

                System.out.println( text+" > "+href );
            }

            System.out.println("===== Naver 연관검색어 END  =====");

            Elements googleSearches = html2.select(".EIaa9b > div > div");
            System.out.println("===== Google 연관검색어 START =====");
            for( Element elm : googleSearches ) {
                String text = elm.text();
                String href = elm.attr("href");

                System.out.println( text+" > "+href );
            }

            System.out.println("=====Google 연관검색어 END  =====");


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "crawling";
    }


}
