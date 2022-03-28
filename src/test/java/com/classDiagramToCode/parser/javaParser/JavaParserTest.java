package com.classDiagramToCode.parser.javaParser;

import com.classDiagramToCode.creator.javaCreator.PackageCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.io.File;
import java.util.List;

class JavaParserTest {

    @BeforeEach
    void beforeEach(TestInfo info){
        System.out.println("--------------------------------------------------");
        System.out.println("テスト開始：" + info.getDisplayName());
    }
    @AfterEach
    void afterEach(TestInfo info){
        System.out.println("テスト終了：" + info.getDisplayName());
    }

    @Test
    void 正常に生成対象ファイルを取得できる() {
        //ローカル環境のファイルを指定して実行してください
        File inputFile = new File("");
        File outputFolder = new File("");
        JavaParser javaParser = new JavaParser(inputFile);


        List<PackageCreator> packageCreatorList = javaParser.parse();
        for(PackageCreator packageCreator : packageCreatorList){
            System.out.println(packageCreator.getClassFileName());
            System.out.println(packageCreator.create());
            System.out.println("------------------------------------------------------");
        }

    }
}