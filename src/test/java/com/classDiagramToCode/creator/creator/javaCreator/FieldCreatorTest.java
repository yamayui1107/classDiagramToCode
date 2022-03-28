package com.classDiagramToCode.creator.creator.javaCreator;

import com.classDiagramToCode.creator.javaCreator.AccessModifier;
import com.classDiagramToCode.creator.javaCreator.FieldCreator;
import org.junit.jupiter.api.*;


class FieldCreatorTest {

    @BeforeEach
    void beforeEach(TestInfo info){
        System.out.println("--------------------------------------------------");
        System.out.println("テスト開始：" + info.getDisplayName());
    }
    @AfterEach
    void afterEach(TestInfo info){
        System.out.println("テスト終了：" + info.getDisplayName());
    }

    @BeforeAll
    static void setup(){
    }

    @Test
    void フィールドを正常に表示することができる() {
        FieldCreator fieldParser = FieldCreator.builder()
                .accessModifier(AccessModifier.PUBLIC)
                .typeName("Integer")
                .memberName("testField")
                .implementedTypeName("new Integer()")
                .build();
        System.out.println(fieldParser.create());
    }

    @Test
    void フィールドを正常に表示することができる2() {
        FieldCreator fieldParser = FieldCreator.builder()
                .accessModifier(AccessModifier.PUBLIC)
                .typeName("Integer")
                .memberName("testField")
                .build();
        System.out.println(fieldParser.create());
    }

    @Test
    void 変数名が不明な場合エラーを吐く() {
        try {
            FieldCreator fieldParser = FieldCreator.builder()
                    .accessModifier(AccessModifier.PUBLIC)
                    .typeName("Integer")
                    .build();
            System.out.println(fieldParser.create());
            assert false;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            assert true;
        }
    }

    @Test
    void アクセス修飾子が不明な場合エラーを吐く() {
        try {
            FieldCreator fieldParser = FieldCreator.builder()
                    .typeName("Integer")
                    .memberName("testError")
                    .build();
            System.out.println(fieldParser.create());
            assert false;
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            assert true;
        }
    }
}