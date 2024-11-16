package realtech;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

public class UnitTest {
    
    @Test
    public void test() {
        String content = "<p><br></p><div style=\"text-align: center;\" align=\"center\"><img src=\"http://www.xn--369an22at7ae6e24kqwke6h.kr/data/editor/2311/ba08cb9e893881f7834d9072c8c4bc00_1699705037_7559.png\" title=\"ba08cb9e893881f7834d9072c8c4bc00_1699705037_7559.png\"><br style=\"clear:both;\">&nbsp;</div><div style=\"text-align: center;\" align=\"center\"><img src=\"http://www.xn--369an22at7ae6e24kqwke6h.kr/data/editor/2311/ba08cb9e893881f7834d9072c8c4bc00_1699704728_8687.png\" title=\"ba08cb9e893881f7834d9072c8c4bc00_1699704728_8687.png\"></div><div style=\"text-align: center;\" align=\"center\"><img src=\"http://www.xn--369an22at7ae6e24kqwke6h.kr/data/editor/2311/ba08cb9e893881f7834d9072c8c4bc00_1699704740_1375.png\" title=\"ba08cb9e893881f7834d9072c8c4bc00_1699704740_1375.png\"></div><div style=\"text-align: center;\" align=\"center\"><br></div><div style=\"text-align: center;\" align=\"center\"><span style=\"font-size: 14pt;\">77인치엘지 올레드티비를 구매하신후 벽걸이티비설치 의뢰를 맞기셧으나..</span></div><div style=\"text-align: center;\" align=\"center\"><span style=\"font-size: 14pt;\">기사분께서 아트벽뒤 무보강 상태라 설치 불가능하다하시어 저희 업체에 의뢰를 맞기셧습니다!!</span></div><div style=\"text-align: center;\" align=\"center\"><span style=\"font-size: 14pt;\">먼저 설치전 벽형태를 확인하고 안전체크 한후 벽뒤 보강 및 안전점검후 시공하겠습니다.</span></div><div style=\"text-align: center;\" align=\"center\"><br></div><div style=\"text-align: center;\" align=\"center\"><img src=\"http://www.xn--369an22at7ae6e24kqwke6h.kr/data/editor/2311/ba08cb9e893881f7834d9072c8c4bc00_1699704748_8373.png\" title=\"ba08cb9e893881f7834d9072c8c4bc00_1699704748_8373.png\"></div><div style=\"text-align: center;\" align=\"center\"><br></div><div style=\"text-align: center;\" align=\"center\"><span style=\"font-size: 14pt;\">아트쪽 외부확인후 아트타공 확인결과 아트뒤편 15CM 가량 공간 바로 뒤 시멘트 확인!!</span></div><div style=\"text-align: center;\" align=\"center\"><span style=\"font-size: 14pt;\">시멘트쪽까지 타공후!! 20CM 열처리 칼블럭앙카!!&nbsp;</span></div><div style=\"text-align: center;\" align=\"center\"><br></div><div style=\"text-align: center;\" align=\"center\"><img src=\"http://www.xn--369an22at7ae6e24kqwke6h.kr/data/editor/2311/ba08cb9e893881f7834d9072c8c4bc00_1699704789_1202.png\" title=\"ba08cb9e893881f7834d9072c8c4bc00_1699704789_1202.png\"></div><div style=\"text-align: center;\" align=\"center\"><br></div><div style=\"text-align: center;\" align=\"center\"><span style=\"font-size: 14pt;\">타공후 20CM 칼블럭을 삽입한후 벽걸이용브라켓 설치를 하였습니다!!</span></div><div style=\"text-align: center;\" align=\"center\"><br></div><div style=\"text-align: center;\" align=\"center\"><img src=\"http://www.xn--369an22at7ae6e24kqwke6h.kr/data/editor/2311/ba08cb9e893881f7834d9072c8c4bc00_1699704795_4352.png\" title=\"ba08cb9e893881f7834d9072c8c4bc00_1699704795_4352.png\"></div><div style=\"text-align: center;\" align=\"center\"><br></div><div style=\"text-align: center;\" align=\"center\"><span style=\"font-size: 14pt;\">벽걸이 브라켓 설치후 안전테스트결과! 대략 하중 200KG이상 끝덕 없습니다!!</span></div><div style=\"text-align: center;\" align=\"center\"><span style=\"font-size: 14pt;\">이제 선정리와 셋탑매립 ,선매립을 해야겠죠??</span></div><div style=\"text-align: center;\" align=\"center\"><br></div><div style=\"text-align: center;\" align=\"center\"><img src=\"http://www.xn--369an22at7ae6e24kqwke6h.kr/data/editor/2311/ba08cb9e893881f7834d9072c8c4bc00_1699704800_0037.png\" title=\"ba08cb9e893881f7834d9072c8c4bc00_1699704800_0037.png\"></div><div style=\"text-align: center;\" align=\"center\"><span style=\"font-size: 14pt;\"><br></span></div><div style=\"text-align: center;\" align=\"center\"><span style=\"font-size: 14pt;\">배선 단자함이 하부에 있으니 선을 위로 매립 하였습니다.</span></div><div style=\"text-align: center;\" align=\"center\"><br></div><div style=\"text-align: center;\" align=\"center\"><img src=\"http://www.xn--369an22at7ae6e24kqwke6h.kr/data/editor/2311/ba08cb9e893881f7834d9072c8c4bc00_1699704807_462.png\" title=\"ba08cb9e893881f7834d9072c8c4bc00_1699704807_462.png\"></div><div style=\"text-align: center;\" align=\"center\"><br></div><div style=\"text-align: center;\" align=\"center\"><span style=\"font-size: 14pt;\">안전하게 설치 완료 된모습입니다.</span></div><div style=\"text-align: center;\" align=\"center\"><span style=\"font-size: 14pt;\">정말 화질이 생각했던것보다 훨씬 뛰어납니다.</span></div><div style=\"text-align: center;\" align=\"center\"><span style=\"font-size: 14pt;\">저도 가지고 싶네요 ^^</span></div><div style=\"text-align: center;\" align=\"center\"><br></div><div style=\"text-align: center;\" align=\"center\"><img src=\"http://www.xn--369an22at7ae6e24kqwke6h.kr/data/editor/2311/ba08cb9e893881f7834d9072c8c4bc00_1699704811_4934.png\" title=\"ba08cb9e893881f7834d9072c8c4bc00_1699704811_4934.png\"></div><div style=\"text-align: center;\" align=\"center\"><br></div><div style=\"text-align: center;\" align=\"center\"><span style=\"font-size: 14pt;\">저희 리얼테크는 첫째도 안전!! 둘쨰도 안전!! 셋째도 안전!!</span></div><div style=\"text-align: center;\" align=\"center\"><span style=\"font-size: 14pt;\">안전을 최우선을 하는 TV설치전문 리얼테크!!</span></div><div style=\"text-align: center;\" align=\"center\"><span style=\"font-size: 14pt;\">10년 무사고 10년 무A/S&nbsp;</span></div><div style=\"text-align: center;\" align=\"center\"><span style=\"font-size: 14pt;\">오래된 경력과 기술로 고객분께 안전하고 깔끔하게 설치해드리겠습니다.</span></div><div style=\"text-align: center;\" align=\"center\"><span style=\"font-size: 14pt;\">감사합니다~!!</span></div><div style=\"text-align: center;\" align=\"center\"><br></div><div style=\"text-align: center;\" align=\"center\"><img src=\"http://www.xn--369an22at7ae6e24kqwke6h.kr/data/editor/2311/ba08cb9e893881f7834d9072c8c4bc00_1699704821_4787.png\" title=\"ba08cb9e893881f7834d9072c8c4bc00_1699704821_4787.png\"><a href=\"tel:01083776410\"></a></div><div style=\"text-align: center;\" align=\"center\"><br></div><div style=\"text-align: center;\" align=\"center\"><a href=\"tel:01083776410\"><img src=\"http://www.xn--369an22at7ae6e24kqwke6h.kr/data/editor/2311/ba08cb9e893881f7834d9072c8c4bc00_1699704829_9748.png\" title=\"ba08cb9e893881f7834d9072c8c4bc00_1699704829_9748.png\"></a></div><div style=\"text-align: center;\"></div>";
        
        
        Document document = Jsoup.parse(content);

        // img 태그를 모두 찾기
        Elements imgTags = document.select("img");
        
        // img 태그의 src 속성을 변경
        for (int i=0; i<imgTags.size(); i++) {
            Element imgTag = imgTags.get(i);
            String originalSrc = imgTag.attr("src"); // 기존 src 값
            String newSrc = originalSrc.replace("http://www.xn--369an22at7ae6e24kqwke6h.kr", "https://realtech-board.s3.ap-northeast-2.amazonaws.com"); // 새 src 값
            imgTag.attr("src", newSrc); // src 속성 업데이트
        }

        // 변경된 DOM을 다시 HTML 문자열로 출력
        String updatedHtml = document.html();

        // 결과 출력
        System.out.println(updatedHtml);
    }
    
    
}
