# Shoppingmall Project

<p>
<img src="https://img.shields.io/badge/license-mit-green">
<img src="https://img.shields.io/github/issues/hongjin4790/SYE-project">
<img src="https://img.shields.io/badge/tag-v1.0.0-blue">
<img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring-3DDC84?style=flat-square&logo=Spring&logoColor=white"/>
<br>
</p>

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#소개">소개</a></li>
    <li><a href="#CRUD">CRUD</a></li>
    <li><a href="#인증과-인가">인증과 인가</a></li>
    <li><a href="#AOP">AOP</a></li>
    <li><a href="#Swagger">Swagger</a></li>
  </ol>
</details>

## 소개
Shopping mall 프로젝트를 협업하며 구현한내용을 단계별로 작성합니다.

<br><br>

## CRUD
안세준은 적으세요

<br><br>

## 인증과 인가

#### 인증(Authentication)
    사용자의 신원을 검증하는 행위 ex) 로그인

<br>

#### 인가(Authorization)
    사용자에게 특정 리소스나 기능에 액세스할 수 있는 권한을 부여하는 행위 ex) USER, MANAGER, ADMIN..

<br>

#### 사용이유
    Http 프로토콜은 기본적으로 Stateless한 특징을 지니고있어 서버 디자인이 간편합니다.
    하지만 Stateless 하기에 사용자에게 매번 인증에 관한 절차를 묻게됩니다.
    이를 위해 Cookie나 Session을 사용해서 사용자의 불편함을 덜어줍니다.

<br>

#### Spring Security
    Spring Security란 Spring기반의 어플리케이션의 인증과 인가를 담당하는 프레임워크입니다.
    Spring Security는 Filter 기반으로 동작하기 때문에 Spring MVC와 분리되어 관리할 수 있고, Session-Cookie기반으로 동작합니다.
    또한 모든 요청에 대해 AuthenticationFilter 가 인증 및 권한 부여과정을 거친 후 Dispatcher Servlet으로 요청을 넘깁니다.
    다음은 Spring Security의 구조입니다.

<br>

![SpringSecurity](https://user-images.githubusercontent.com/29851990/204126595-38e0be3d-5bf4-48ed-8391-be3102567eaf.png)

<br>

동작방식을 다음과 같습니다.

<br>

    1. 사용자가 로그인을 시도합니다. (Http Request)
    2. Request는 AuthenticationFilter에 도달하게 되고, 이는 UsernamePasswordAuthenticationFilter에 (username, password)를 보내게 됩니다. 
    3. UsernamePasswordAuthenticationFilter에 도착하면 해당 클래스의 attempAuthentication가 동작하고 그 메서드에서 사용자 자격 증명을 기반으로한 UsernamePasswordAuthenticationToken을 생성합니다.
    4. 이어서 UsernamePasswordAuthenticationToken을 가지고 AuthenticationManager에게 인증을 진행하도록 위임합니다. (실제로는 AuthenticationManger 인터페이스를 구현한 ProviderManager)
    5. AuthenticationProvider의 목록으로 인증을 시도합니다.
    6. UserDetailService는 username기반의 UserDetails를 검색합니다.
    7. UserDetails를 이용해서 User 객체에 대한 정보를 검색합니다.
    8. User 객체의 정보를 UserDetails가 UserDetailsService에 전달함니다.
    9. 사용자의 인증이 성공하면 전체 인증정보를 리턴하고, 아니라면 AuthenticationException을 던집니다.
    10. AuthenicationManager가 인증객체를 Authentication Filter에 반환합니다.
    11. SecurityContext에 인증객체를 설정하고 마무리가 됩니다.

<br>

#### Security Session의 단점
    1. 대용량 트래픽에 대비한 scale-out이 어렵다. (세션 불일치문제)
    2. Session기반이기 떄문에 서버에 과부화가 올 수 있다.


<br>

#### JWT Token
    Stateless한 Http 프로토콜상에서 사용자의 인증과 권한에대한 정보를 암호화하여 Token형식으로 만들어 사용자에게 주는 방식입니다.
    JWT를 이용하면 따로 서버의 메모리에 저장 공간을 확보할 필요가 없습니다.
    또 서버가 토큰을 한번 클라이언트에게 보내주면 클라이언트는 토큰을 보관하고 있다가 요청을 보낼때마다 헤더에 토큰을 실어보내면 됩니다.
    마지막으로 쿠키를 사용할 수 없는 모바일 어플리케이션에는 JWT를 사용한 인증방식이 최적입니다.
    따라서 이번 프로젝트는 JWT 기반으로 구현하려합니다.

<br>

#### 의존성 부여
다음과 같이 build.gradle에 의존성을 부여해줍니다. (JWT가 최신이 아니라면 MVM Repository에서 찾아주시면 됩니다!)

    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation group: 'com.auth0', name: 'java-jwt', version: '3.19.2'

#### SecurityConfig

authorizeRequests : 권한과 그에따른 url을 정해줍니다.<br>
formLogin().disable() : 구현하고자 하는 서버의 디자인은 Rest Api 형태의 디자인이기 때문에 loginForm을 거치지않습니다.<br>
httpBasic : (Anthorization : {ID, PW}) 를 담아 보내는 방식입니다. 따라서 암호화가 안되있기 때문에 보안상 너무 위험합니다.<br>
MyCustomDsl : AbstractHttpConfigurer 에 거쳐야할 Filter들을 담습니다.<br>

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {
        @Autowired
        private UserRepository userRepository;

        @Autowired
        private CorsConfig corsConfig;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 사용하지 않아 Stateless로 만들겠다.
                    .and()
                    .formLogin().disable()
                    .httpBasic().disable()
                    .apply(new MyCustomDsl()) // FilterChain으로 사용해야하므로 addFilter대신 apply사용 또한 authenticationManager를 사용해야하기 때문에
                    // 아래와같이 Custom으로 클래스를 만들어 사용
                    // beraer 방식 : 위의 Basic 방식과 다른 방식으로 Anthorization에 토큰을 담으므로 노출이 되도 위험부담이 적은 방식입니다.
                    .and()
                    .authorizeRequests()
                    .antMatchers("/user/**")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_REGISTER') or hasRole('ROLE_ADMIN')")
                    .antMatchers("/register/**")
                    .access("hasRole('ROLE_REGISTER') or hasRole('ROLE_ADMIN')")
                    .antMatchers("/admin/**")
                    .access("hasRole('ROLE_ADMIN')")
                    .anyRequest().permitAll();

            return http.build();
        }

        public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
            @Override
            public void configure(HttpSecurity http) throws Exception {
                AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
                http
                        .addFilter(corsConfig.corsFilter()) // cors 요청처리 // @CrossOrigin(인증 x), 시큐리티 필터에 등록 인증(o)
                        .addFilter(new JwtAuthenticationFilter(authenticationManager))
                        .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
            }
        }
    }

<br>

#### Cors

SOP(동일 출처 정핵)으로 인해 다른 출처의 리소스접근이 막힌 것을 풀어주는 "다른 출처간에 리소스를 공유할 수 있도록 해주는 정책"입니다.<br>
  
    @Configuration
    public class CorsConfig {

        @Bean
        public CorsFilter corsFilter(){
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(true); // 내 서버가 응답을 할 때, json을 자바스크립트에서 처리할 수 있게 할지를 설정
            config.addAllowedOrigin("*"); // 모든 ip에 응답을 허용하겠다.
            config.addAllowedHeader("*"); // 모든 header에 응답을 허용하겠다.
            config.addAllowedMethod("*"); // 모든 methode(get, post, put...)에 응답을 허용하겠다.

            source.registerCorsConfiguration("/api/**", config);
            return new CorsFilter(source);
        }
    }

<br>

#### JwtAuthenticationFilter.attemptAuthentication
    사용자가 로그인을 하게되면 SecurityFilterChain에 등록시킨 JwtAuthenticationFilter.attemptAuthentication(request, response)이 동작합니다.
    request로 온 username, password를 받아서 UsernamePasswordAuthenticationToken을 받고 정상인지 로그인을 시도합니다.
    authenticationManager로 로그인 시도하면 PrincipalDetailsService가 호출합니다.
    토큰을 이용해 PrincipalDetailsService의 loadUserByUsername() 함수를 실행시켜 DB에 있는 username과 password와 일치하는지 확인합니다.
    PrincipalDetails를 세션에 담고 (권한관리를 하기위함) authentication 객체가 session 영역에 저장됩니다.

<br>

#### JwtAuthenticationFilter.successfulAuthentication
    JWT 토큰을 만들어서 request를 보낸 사용자에게 jwt 토큰을 response 해줍니다.
    해당 프로젝트에서는 HMAC512 방식을 사용합니다.

<br>

#### JwtAuthorizationFilter.doFilterInternal
    인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 거칩니다.
    사용자가 보낸 JWT토큰을 확인해 정상적인 JWT Token이라면 Filter를 더 타게합니다.


<br><br>

## AOP
    Aspect Oriented Programming의 약자로 특정 로직을 기준으로 핵심적인 관점, 부가적인 관점으로 나누어서 그 관점을 기준으로 모듈화하는 것입니다.


#### Logging
    com.example.shoppingmall..*.*(..)를 통해 모든 메서드에 로그를 찍게 만들었습니다.
    해당 로그가 동작하는 기점은 @Before로 메서드 시작전에 동작합니다.


#### RunningTime
    RunningTime 어노테이션 파일을 하나 만들었습니다.
    com.example.shoppingmall..*.*(..) && enableRunningTime()를 통해 @RunningTime이 설정되있는 메서드라면 StopWatch를 통해 동작 시간을 측정했습니다.
    해당 작업은 @Around를 사용해 실행 전 후에 동작하게 설정하였습니다.

<br><br>

## Swagger
    Api들이 가지고있는 스펙을 명세, 관리할 수 있는 프로젝트입니다.
    Api 사용방법을 사용자에게 상세히 알려주어 협업에도 좋은 영향을 발휘합니다.

http://localhost:8080/swagger-ui/index.html 에 접근하여 확인할 수 있습니다.
