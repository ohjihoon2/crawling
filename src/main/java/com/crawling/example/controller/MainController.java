package com.crawling.example.controller;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @RequestMapping("/")
    public String Main(){
        System.out.println("[ 메인 페이지 ]");

        return "index";
    }

    @RequestMapping("/sele")
    public String SeleniumTest(){
        System.out.println("[ 셀레니움 크롤링 페이지 ]");
        WebDriver driver;
        System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

//        다른 디렉토리에 geckodriver.exe 가 존재 할 경우
//        System.setProperty("webdriver.gecko.driver", "c:\geckodriver.exe");

        // 실행 시 크롬 창 안열리는 옵션
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");

        driver = new ChromeDriver(chromeOptions);

        driver.get("https://ko.wikipedia.org/wiki/%EB%84%A4%EC%9D%B4%EB%B2%84");

        List<WebElement> posts = driver.findElements(By.className("mw-body-content"));

        for(WebElement e : posts) {
            System.out.println(e.getText());
            System.out.println("1");
        }

        driver.quit(); // 웹브라우져 닫음
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
    public String Crawling(@PathVariable("search") String search, Model model){
        System.out.println("[ "+ search +"- 크롤링 페이지 ]");
        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();

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
                Map<String,Object> map = new HashMap<String,Object>();
                String text = elm.text();
                String href = elm.attr("href");

                map.put("site","naver");
                map.put("text",text);
                map.put("href",href);

                list1.add(map);

                System.out.println( text+" > "+href );
            }

            System.out.println("===== Naver 연관검색어 END  =====");

            Elements googleSearches = html2.select(".EIaa9b > div > div");
            System.out.println("===== Google 연관검색어 START =====");


            for( Element elm : googleSearches ) {
                String text = elm.text();
                String href = elm.attr("href");

                Map<String,Object> map = new HashMap<>();
                map.put("site","google");
                map.put("text",text);
                map.put("href",href);

                list2.add(map);

                System.out.println( text+" > "+href );

            }

            System.out.println("=====Google 연관검색어 END  =====");


        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("list1 = " + list1);
        model.addAttribute("search", search);
        model.addAttribute("list1",list1);
        model.addAttribute("list2",list2);

        return "crawling";
    }


}
