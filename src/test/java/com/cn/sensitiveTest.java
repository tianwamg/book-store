package com.cn;

import com.cn.common.Page;
import com.cn.domain.Sensitive;
import com.cn.service.ISensitiveService;
import com.github.houbb.sensitive.word.api.IWordDeny;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.github.houbb.sensitive.word.core.SensitiveWords;
import com.github.houbb.sensitive.word.support.deny.WordDenySystem;
import com.github.houbb.sensitive.word.support.deny.WordDenys;
import com.github.houbb.sensitive.word.support.resultcondition.WordResultConditionAlwaysTrue;
import com.github.houbb.sensitive.word.support.resultcondition.WordResultConditions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class sensitiveTest {

    @Autowired
    ISensitiveService sensitiveService;

    String text = "Renaissance Art CharlesVictoria,MAO TSE-TUNG FOUR ESSAYS ON PHILOSOPHY," +
            "THE BIBLE IN SPAIN,Infinite Warfare Phillip Marcus and Thom Denic,Time-tested secrets for Capturing the," +
            "Last Tango in Toulouse,HUMAN REIATION SHIPS,Catalogue of rivers for southeast Asia and the Pacific,WAR AND CONTEMPORARY ART EVENING SALE," +
            "Der wandernde See,Pediatric Rhinosinusitis,Early Netherlandish Painting from Rogier Van Der Weyden,Trump: How to Get Rich,ETAIS DEVENUE UNE VRAIE PETITE PERVERSE," +
            "19.Dezember,Erst grau dann wei dann blau von Margriet de Moor,TEKLA AV DEX I WONDERLAND,FORBES 福布斯 2004,Flex by Marcia Williams,Hotter Than Wildfire Lisa Marie Rice,СЕТИ ЯНУШ ЛЕОН ВИ,Body Moves,Atala Rene,understanding poetry god s world,Gender Queer," +
            "A Radical Approach to Permane,嫌な奴とつき合いなさぃ,航空ファン,占い,マイヨール展,ルーベンスとその时代展,パブリッシング,ワールドガイド中国,The Rise of ISIS,EMOTIONAL AWARENESS,Joby Warrick,THE WASHINGTON POST,understanding poetry god s world,BEYOND THE HORIZON,KIN DNESS CLARITY AND INSIGHT,Salvador Dali,BEYOND THE HORIZON,作业,彩雲,军帽,日记本,纪念章,勋章,笔墨,砚台,宣纸,年历,剪贴,香炉,药片,花瓶,爵杯,说明书,合格证,保修证,平尺,手表,肩章,领章,胸章,照片,唱片,信札,信封,书签,日历,邮票,粮票,油票,车票,煤票,食票,乐欣,玉匣子,车牌,毕业留念,介绍信,上访信,一幅,证件,收据,准考证,登记证,文件夹,纪念卡,奖章,字帖,袖标,酒标,烟标," +
            "药标,烟盒,论文,贺卡,明信片,天皇,神社,光碟,光盘,碟片,片装,碟装,号码薄,电话薄,电话簿,结业证,逮捕证,通知书,退休证,秦光中,王凤仪,明目功,健目功,康乐功,暂住证,登记证,准考证,旧证,传票,女王力,检查单,报告单,游戏卡,芈月传,书法一副,性爱,情色艺术,催情,爱经,爱的艺术,学而思,爱学习,高思,高斯,猿辅导,爱尖子,跟谁学,学霸笔记,毛泽东,毛沢东,毛选,习近平,周恩来,邓小平,高培淇,周总理,中央,中共,共产党,全党,党的,党史,党建,党文件,决议,学习辅导,工农兵,革委,党宣言,最高指示,领导干部,半日读,水浒传,中国革命,统一战线,宣传部,决议,哈金,慧人,达赖,喇嘛,岳玉峰,苏斯博士,陈一阳,加藤嘉一,苏阿德·戴尔维希,李舫舫,宫白羽原,蒋经国,厉声,孙宏年,张永攀,王慈官,邵伟华,张志春,李长明,真人,元开,白云山,张义,刘剑青,李劼人," +
            "金波,金文杰,聂云台,王潮音,印光,雅克萨,天闻角川,角川历彦,黄文雄,雀帝,汤素兰,周广宇,白事会,高峰秀子,邓敏华,印顺,维摩,塔木德,恩格斯,陈桂明,赵国宗,赖昌星,华理克,张爱萍,纪果庵,索罗斯,义水智泉,孙皓晖,伊万内奇,朱昱,项扬惠,三上义一,吴某凡,普列汉诺夫,袁天罡,希拉里,陈红,黄光国,程俊堂,舟恒划,郑德良,田代哲也,西元龙新,肯雅塔,李登辉,达赖,喇嘛,活瓷,乾唐轩,路德维希费尔巴哈,王文学,马步芳,马效忠,川岛芳子,方励之,张显扬,刘宾雁,王若望,严家其,陈一谘,陈一咨,万润南,苏绍智,胡绩伟,刘晓波,苏晓康,李淑娴,吾尔开希,高自联,钦天监,赵紫阳,柴静,胡锦涛,石原慎太郎,再回来,帕布帕德,周安士,王萃甫,孔明,黎智英,赵薇,肖伟中,张秋生,段立欣,郭先举,梁安滨";


    @Test
    public void sensitive(){
        String t = "尼扎姆莫尔克";
        boolean isFalse = SensitiveWordHelper.contains("周广宇");
        System.out.println(isFalse);
        SensitiveWordBs words = getSensitvieDBs(8571568672l);
        isFalse = words.contains("尼扎姆莫尔克");
        isFalse = words.contains("人民调解工作法律实务丛书");
        System.out.println(isFalse);
    }


    public SensitiveWordBs getSensitvieDBs(Long userId){
        return SensitiveWordBs.newInstance()
                .wordDeny(new IWordDeny() {
                    @Override
                    public List<String> deny() {
                        Page page = new Page();
                        int current =1 ;
                        page.setPageSize(50);
                        page.setCurrent(current);
                        Sensitive sensitive = new Sensitive();
                        sensitive.setUserId(userId);
                        sensitive.setStatus(1);
                        List<Sensitive> list = sensitiveService.getPageList(page,sensitive).getRecords();
                        List<String> wordsList = new ArrayList<>();
                        while (list != null && list.size() >0){
                            wordsList.addAll(list.parallelStream().map(s -> s.getWord()).collect(Collectors.toList()));
                            current ++;
                            page.setCurrent(current);
                            list = sensitiveService.getPageList(page,sensitive).getRecords();
                        }
                        return wordsList;
                    }
                })
                .ignoreChineseStyle(true)
                .ignoreEnglishStyle(true)
                .init();
    }



    @Test
    public void f() throws IOException {
        File file = new File("/Users/pandawong/Desktop/se.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = reader.readLine()) != null){
            String[] tmp = line.split(",");
            Arrays.stream(tmp).filter(str -> !str.trim().isEmpty()).parallel().forEach( n ->{
                Sensitive sensitive = new Sensitive();
                sensitive.setUserId(8571568672l);
                sensitive.setWord(n.trim());
                sensitive.setStatus(1);
                sensitiveService.save(sensitive);
            });
        }
        reader.close();

    }
}
