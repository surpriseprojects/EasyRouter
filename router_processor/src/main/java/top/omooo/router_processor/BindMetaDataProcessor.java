package top.omooo.router_processor;

import com.google.auto.service.AutoService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import top.omooo.router_annotations.annotations.BindMetaDataAnn;

/**
 * Created by Omooo
 * Date:2019/3/20
 * Desc:处理绑定 MetaData 元数据的注解处理器
 */
@AutoService(Processor.class)
public class BindMetaDataProcessor extends AbstractProcessor {

    private HashMap<String, Object> mRouterMap;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mRouterMap = new HashMap<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindMetaDataAnn.class);
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            BindMetaDataAnn metaDataAnn = typeElement.getAnnotation(BindMetaDataAnn.class);
            try {
                mRouterMap.put(metaDataAnn.value(), Class.forName(typeElement.getQualifiedName().toString()));
            } catch (ClassNotFoundException e) {
                //ignore
            }
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(BindMetaDataAnn.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
