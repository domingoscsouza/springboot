package curso.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	
	@Override // Configura as solicitações de acesso por HTTP.
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
		.disable() // Desabilita as configurações padrão de memória.
		.authorizeRequests() // Permite restringir acessos.
		.antMatchers(HttpMethod.GET, "/").permitAll() // Qualquer usuário acessa a página inicial.
		//.antMatchers("/materialize/**").permitAll()
		.antMatchers(HttpMethod.GET, "/cadastropessoa").hasAnyRole("ADMIN") 
		.anyRequest().authenticated()
		.and().formLogin().permitAll() // Permite qualquer usuário.
		.loginPage("/login")
		.defaultSuccessUrl("/cadastropessoa")
		.failureUrl("/login?error=true")
		.and().logout().logoutSuccessUrl("/login") // Mapeia URL de logout e invalida usuário autenticado.
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}
	
	@Override // Cria a autenticação do usuário com o banco de dados ou em memória.
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(implementacaoUserDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
		
		/*
		auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())
		.withUser("admin")
		.password("admin")
		.roles("ADMIN");
		*/
	}
	
	@Override // Ignora URLs específicas.
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/materialize/**");
	}
	
//	@Override
//
//	public void configure(WebSecurity web) throws Exception {
//
//	          web.ignoring().antMatchers("/materialize/**")
//
//	         .antMatchers(HttpMethod.GET,"/resources/**","/static/**", "/**", "/materialize/**", "**/materialize/**");
//
//	}


}
