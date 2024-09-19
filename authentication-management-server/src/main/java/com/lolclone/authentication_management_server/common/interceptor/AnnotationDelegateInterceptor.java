package com.lolclone.authentication_management_server.common.interceptor;

import com.lolclone.common_module.commonexception.UnexpectedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.annotation.Annotation;

public class AnnotationDelegateInterceptor implements HandlerInterceptor {

    private final Class<? extends Annotation> annotation;
    private final HandlerInterceptor interceptor;

    protected AnnotationDelegateInterceptor(
            Class<? extends Annotation> annotation,
            HandlerInterceptor interceptor
    ) {
        if (annotation == null) {
            throw new UnexpectedException("annotation은 null이 될 수 없습니다.");
        }
        if (interceptor == null) {
            throw new UnexpectedException("interceptor는 null이 될 수 없습니다.");
        }
        this.annotation = annotation;
        this.interceptor = interceptor;
    }

    public static AnnotationsDelegateInterceptorBuilder builder() {
        return new AnnotationsDelegateInterceptorBuilder();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (handlerMethod.hasMethodAnnotation(annotation)) {
            return interceptor.preHandle(request, response, handler);
        }
        return true;
    }

    public static class AnnotationsDelegateInterceptorBuilder {

        private Class<? extends Annotation> annotation;
        private HandlerInterceptor interceptor;

        public AnnotationsDelegateInterceptorBuilder annotation(Class<? extends Annotation> annotation) {
            this.annotation = annotation;
            return this;
        }

        public AnnotationsDelegateInterceptorBuilder interceptor(HandlerInterceptor interceptor) {
            this.interceptor = interceptor;
            return this;
        }

        public AnnotationDelegateInterceptor build() {
            return new AnnotationDelegateInterceptor(annotation, interceptor);
        }
    }
}
