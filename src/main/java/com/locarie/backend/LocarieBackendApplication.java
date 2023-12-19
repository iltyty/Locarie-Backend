package com.locarie.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @Log4j2
@SpringBootApplication
public class LocarieBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocarieBackendApplication.class, args);
    }
    //
    //    @Bean
    //    public OncePerRequestFilter contentCachingRequestFilter() {
    //        // 配置一个Filter
    //        return new OncePerRequestFilter() {
    //            @Override
    //            protected void doFilterInternal(final HttpServletRequest request, final
    // HttpServletResponse response, final FilterChain filterChain) throws ServletException,
    // IOException {
    //                // 包装HttpServletRequest，把输入流缓存下来
    //                ContentCachingRequestWrapper wrappedRequest = new
    // ContentCachingRequestWrapper(request);
    //                Collection<Part> parts = wrappedRequest.getParts();
    //                // 包装HttpServletResponse，把输出流缓存下来
    //                ContentCachingResponseWrapper wrappedResponse = new
    // ContentCachingResponseWrapper(response);
    //                filterChain.doFilter(wrappedRequest, wrappedResponse);
    //                log.info("http request:{}", new
    // String(wrappedRequest.getContentAsByteArray()));
    //                log.info("http response:{}", new
    // String(wrappedResponse.getContentAsByteArray()));
    //                // 注意这一行代码一定要调用，不然无法返回响应体
    //                wrappedResponse.copyBodyToResponse();
    //            }
    //        };
    //    }
}
