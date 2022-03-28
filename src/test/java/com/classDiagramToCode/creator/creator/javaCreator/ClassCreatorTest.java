package com.classDiagramToCode.creator.creator.javaCreator;

import com.classDiagramToCode.creator.javaCreator.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class ClassCreatorTest {

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
    void 正常にクラスを出力できる(){
        List<FieldCreator> fieldCreatorList = new ArrayList<>();
        FieldCreator fieldCreator = FieldCreator.builder()
                .accessModifier(AccessModifier.PRIVATE)
                .typeName("int")
                .memberName("x")
                .implementedTypeName("0")
                .build();
        FieldCreator fieldCreator2 = FieldCreator.builder()
                .accessModifier(AccessModifier.PRIVATE)
                .typeName("int")
                .memberName("y")
                .implementedTypeName("0")
                .build();
        fieldCreatorList.add(fieldCreator);
        fieldCreatorList.add(fieldCreator2);

        List<MethodCreator> methodCreatorList = new ArrayList<>();
        MethodCreator methodCreator = MethodCreator.builder()
                .accessModifier(AccessModifier.PUBLIC)
                .methodName("method")
                .returnTypeName("int")
                .build();
        methodCreatorList.add(methodCreator);


        ClassCreator classCreator = ClassCreator.builder()
                .accessModifier(AccessModifier.PRIVATE)
                .classType(ClassType.CLASS)
                .className("Test")
                .extendsClassName("Object")
                .implementsInterfaceNameList(Arrays.asList("animal","plant"))
                .fieldCreatorList(fieldCreatorList)
                .methodCreatorList(methodCreatorList)
                .build();

        System.out.println(classCreator.create());
    }

    @Test
    void アクセス修飾子が指定されていない場合エラーを吐く(){
        try {
            ClassCreator classCreator = ClassCreator.builder()
                    .classType(ClassType.ABSTRACT_CLASS)
                    .className("Test")
                    .build();
            assert false;
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Test
    void クラス名が指定されていない場合エラーを吐く(){
        try {
            ClassCreator classCreator = ClassCreator.builder()
                    .accessModifier(AccessModifier.PRIVATE)
                    .classType(ClassType.ABSTRACT_CLASS)
                    .build();
            assert false;
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

}