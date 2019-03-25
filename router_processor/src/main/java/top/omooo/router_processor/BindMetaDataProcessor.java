package top.omooo.router_processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import top.omooo.router_annotations.annotations.Router;

/**
 * Created by Omooo
 * Date:2019/3/20
 * Desc:处理绑定 MetaData 元数据的注解处理器
 */
@AutoService(Processor.class)
public class BindMetaDataProcessor extends AbstractProcessor {

    private HashMap<String, Object> mRouterMap;
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mRouterMap = new HashMap<>();
        mFiler = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Router.class);
        createFile(elements);
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(Router.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void createFile(Set<? extends Element> elements) {
        FieldSpec fieldSpec = FieldSpec
                .builder(HashMap.class, "sHashMap", Modifier.PUBLIC, Modifier.STATIC)
                .build();
        MethodSpec.Builder methodBuilder = MethodSpec
                .methodBuilder("init")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addException(ClassNotFoundException.class)
                .returns(void.class);
        methodBuilder.addStatement("sHashMap = new HashMap()");
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            Router metaDataAnn = typeElement.getAnnotation(Router.class);
            methodBuilder.addStatement("sHashMap.put($S,$T.forName($S))", metaDataAnn.value(), Class.class, typeElement.getQualifiedName().toString());
        }

        TypeSpec type = TypeSpec
                .classBuilder("RouterFactory")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(methodBuilder.build())
                .addField(fieldSpec)
                .build();
        JavaFile javaFile = JavaFile
                .builder("top.omooo.easyrouter", type)
                .build();
        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
