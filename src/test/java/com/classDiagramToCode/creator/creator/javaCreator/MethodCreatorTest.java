package com.classDiagramToCode.creator.creator.javaCreator;

import com.classDiagramToCode.creator.javaCreator.AccessModifier;
import com.classDiagramToCode.creator.javaCreator.MethodCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.util.Arrays;
import java.util.List;

class MethodCreatorTest {

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
    void 正常にメソッドを出力できる() {
        List<String> argumentList = Arrays.asList("int test","String test2");
        MethodCreator methodCreator = MethodCreator.builder()
                .accessModifier(AccessModifier.PROTECTED)
                .methodName("testMethod")
                .returnTypeName("char")
                .argumentList(argumentList)
                .build();
        System.out.println(methodCreator.create());
    }

    @Test
    void 正常にメソッドを出力できる_返り値なし() {
        List<String> argumentList = Arrays.asList("int test","String test2");
        MethodCreator methodCreator = MethodCreator.builder()
                .accessModifier(AccessModifier.DEFAULT)
                .methodName("testMethod2")
                .argumentList(argumentList)
                .build();
        System.out.println(methodCreator.create());
    }

    @Test
    void 正常にメソッドを出力できる_引数なし() {
        MethodCreator methodCreator = MethodCreator.builder()
                .accessModifier(AccessModifier.DEFAULT)
                .methodName("testMethod3")
                .build();
        System.out.println(methodCreator.create());
    }

    @Test
    void アクセスタイプが指定されていない場合エラーを吐く() {
        try {
            MethodCreator methodCreator = MethodCreator.builder()
                    .methodName("testError")
                    .returnTypeName("int")
                    .build();
            System.out.println(methodCreator.create());
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Test
    void メソッド名が不明な場合エラーを吐く() {
        try {
            MethodCreator methodCreator = MethodCreator.builder()
                    .methodName("")
                    .accessModifier(AccessModifier.DEFAULT)
                    .returnTypeName("String")
                    .build();
            System.out.println(methodCreator.create());
            assert false;
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }
}