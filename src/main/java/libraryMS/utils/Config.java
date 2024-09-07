package libraryMS.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import libraryMS.utils.annotation.ParameterName;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class Config extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {

    public class ArgumentResolver implements HandlerMethodArgumentResolver {

        public boolean supportsParameter(MethodParameter methodParameter) {
            return true;
        }

        public Object resolveArgument(MethodParameter parameter,
                                      ModelAndViewContainer mavContainer,
                                      NativeWebRequest webRequest,
                                      WebDataBinderFactory binderFactory) throws Exception {
            ObjectMapper mapper = new ObjectMapper();

            Object paramValue = null;

            String requestValue = "";

            if (parameter.hasParameterAnnotation(ParameterName.class)) {
                requestValue = webRequest.getParameter(
                        parameter.getParameterAnnotation(ParameterName.class).value());
                if (parameter.getParameterAnnotation(ParameterName.class).required() && requestValue == null) {
                    throw new RuntimeException("Parameter: " + parameter.getParameterAnnotation(ParameterName.class).value() +
                            " might be required");
                }


                if (requestValue != null && !"null".equalsIgnoreCase(requestValue)) {

                    requestValue = StringUtils.newStringUtf8(requestValue.getBytes());

                    if (parameter.getParameterType().getName().equals("java.util.List")) {
                        TypeFactory t = TypeFactory.defaultInstance();
                        paramValue = mapper.readValue(requestValue,
                                t.constructCollectionType(ArrayList.class, ((Class) ((ParameterizedType) parameter.getGenericParameterType()).getActualTypeArguments()[0])));

                    }
                    else if(parameter.getGenericParameterType() instanceof ParameterizedType){
                        TypeFactory t = TypeFactory.defaultInstance();
                        paramValue = mapper.readValue(requestValue,
                                t.constructParametricType(parameter.getParameterType(), ((Class) ((ParameterizedType) parameter.getGenericParameterType()).getActualTypeArguments()[0])));
                    }
                    else {
                        if ("Date".equals(parameter.getParameterType().getSimpleName())) {
                            String dateFormat = "yyyy-MM-dd";
                            if (parameter.hasParameterAnnotation(DateTimeFormat.class)) {
                                dateFormat = parameter.getParameterAnnotation(DateTimeFormat.class).pattern();
                            }
                            paramValue = new SimpleDateFormat(dateFormat).parse(requestValue);
                        } else {
                            paramValue = mapper.readValue(requestValue, parameter.getParameterType());
                        }
                    }
                }
            } else {
// paramValue = webRequest.getParameter(parameter.getParameterName());

                paramValue = webRequest.getParameter(parameter.getParameterName());

            }

            return paramValue;
        }
    }
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(this.argumentResolver());
    }

    @Bean
    public ArgumentResolver argumentResolver() {
        return (new ArgumentResolver());
    }
}
