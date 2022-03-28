package com.classDiagramToCode.creator.creator.javaCreator;

import com.classDiagramToCode.creator.javaCreator.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class PackageCreatorTest {
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
    void 正常にパッケージを出力できる(){
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

        PackageCreator packageCreator = PackageCreator.builder()
                .packageName("testPackage")
                .classCreator(classCreator)
                .build();

        System.out.println(packageCreator.create());
    }
}