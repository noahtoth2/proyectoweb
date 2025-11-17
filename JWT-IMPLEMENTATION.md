# Implementación de JWT Authentication

## ✅ Backend Completado

### 1. Dependencias Agregadas (pom.xml)
- Spring Security
- Lombok (para reducir código boilerplate)
- JJWT 0.12.3 (API, Implementation, Jackson)

### 2. Componentes Creados

#### ApplicationConfig.java
Configuración de beans de seguridad:
- `UserDetailsService`: Carga usuarios desde la base de datos
- `AuthenticationProvider`: Proveedor de autenticación DAO
- `PasswordEncoder`: BCrypt para encriptar contraseñas
- `AuthenticationManager`: Maneja la autenticación

#### JwtService.java
Servicio para manejar tokens JWT:
- `generateToken()`: Genera token JWT con información del usuario
- `extractUsername()`: Extrae el nombre de usuario del token
- `isTokenValid()`: Valida token (firma + expiración)
- Configuración: 24 horas de expiración, HS256 algorithm

#### JwtAuthenticationFilter.java
Filtro que intercepta todas las peticiones HTTP:
- Extrae el token JWT del header "Authorization: Bearer <token>"
- Valida el token
- Establece la autenticación en el SecurityContext
- Se ejecuta antes de cada request

#### SecurityConfig.java
Configuración de Spring Security:
- Endpoints públicos: `/api/auth/**`, `/h2-console/**`, `/swagger-ui/**`
- Endpoints protegidos: Todos los demás requieren JWT válido
- Sesiones: STATELESS (sin estado, solo JWT)
- CSRF: Deshabilitado (no necesario con JWT)

### 3. Modelos Actualizados

#### User.java
- Implementa `UserDetails` interface
- Métodos agregados: `getAuthorities()`, `isAccountNonExpired()`, etc.
- Compatible con Spring Security

#### UserService.java
- `register()`: Ahora encripta password con BCrypt y retorna JWT token
- `login()`: Usa AuthenticationManager y retorna JWT token
- AuthResponse incluye el token JWT

### 4. Configuración (application.properties)
```properties
jwt.secret.key=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000
```

## 🔄 Próximos Pasos - Frontend

### 1. Actualizar AuthService (auth.service.ts)

Modificar el servicio de autenticación para manejar JWT:

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  register(username: string, email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, { username, email, password })
      .pipe(
        tap((response: any) => {
          if (response.token) {
            localStorage.setItem('jwt_token', response.token);
            localStorage.setItem('currentUser', JSON.stringify(response.user));
          }
        })
      );
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { username, password })
      .pipe(
        tap((response: any) => {
          if (response.token) {
            localStorage.setItem('jwt_token', response.token);
            localStorage.setItem('currentUser', JSON.stringify(response.user));
          }
        })
      );
  }

  logout(): void {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('currentUser');
    localStorage.removeItem('jugadorActual');
    localStorage.removeItem('partidaActual');
  }

  getToken(): string | null {
    return localStorage.getItem('jwt_token');
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    return !!token && !this.isTokenExpired(token);
  }

  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp * 1000 < Date.now();
    } catch (e) {
      return true;
    }
  }
}
```

### 2. Crear HTTP Interceptor (auth.interceptor.ts)

Crear interceptor para agregar JWT a todas las peticiones:

```typescript
import { HttpInterceptorFn } from '@angular/core';
import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const token = localStorage.getItem('jwt_token');

  // No agregar token a peticiones de autenticación
  if (req.url.includes('/api/auth/login') || req.url.includes('/api/auth/register')) {
    return next(req);
  }

  // Agregar token si existe
  if (token) {
    const clonedRequest = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
    return next(clonedRequest);
  }

  // Si no hay token y la petición no es pública, redirigir al login
  if (!req.url.includes('/api/auth/')) {
    router.navigate(['/login']);
  }

  return next(req);
};
```

### 3. Registrar Interceptor (app.config.ts)

Agregar el interceptor a la configuración:

```typescript
import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { authInterceptor } from './interceptors/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor]))
  ]
};
```

### 4. Actualizar Componente de Login/Register

Usar el AuthService actualizado:

```typescript
// En auth.component.ts o login.component.ts
import { AuthService } from '../services/auth.service';

export class AuthComponent {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onLogin() {
    this.authService.login(this.username, this.password).subscribe({
      next: (response) => {
        console.log('Login exitoso:', response);
        this.router.navigate(['/lobby']); // O donde corresponda
      },
      error: (error) => {
        console.error('Error en login:', error);
        // Mostrar mensaje de error al usuario
      }
    });
  }

  onRegister() {
    this.authService.register(this.username, this.email, this.password).subscribe({
      next: (response) => {
        console.log('Registro exitoso:', response);
        this.router.navigate(['/lobby']); // O donde corresponda
      },
      error: (error) => {
        console.error('Error en registro:', error);
        // Mostrar mensaje de error al usuario
      }
    });
  }
}
```

## 🔒 Seguridad Implementada

### Backend
✅ Contraseñas encriptadas con BCrypt
✅ Tokens JWT firmados criptográficamente
✅ Validación de tokens en cada request
✅ Sesiones stateless (sin estado en servidor)
✅ Endpoints públicos vs protegidos
✅ Expiración de tokens (24 horas)

### Frontend (Pendiente)
⏳ Almacenar token JWT en localStorage
⏳ Interceptor HTTP para agregar token a requests
⏳ Validación de expiración de token
⏳ Redirección automática si token inválido/expirado
⏳ Logout limpia token y datos de usuario

## 🧪 Testing

### Probar en Postman/curl

#### 1. Register
```bash
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123"
}

# Response:
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "user": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    ...
  }
}
```

#### 2. Login
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}

# Response: (igual que register)
```

#### 3. Usar token en request protegido
```bash
GET http://localhost:8080/api/barco/disponibles
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

# Sin token -> 401 Unauthorized
# Con token válido -> 200 OK con datos
```

## ⚠️ Notas Importantes

1. **Migración de Datos**: Las contraseñas existentes en BD están en texto plano. Al implementar BCrypt, los usuarios existentes NO podrán hacer login. Opciones:
   - Recrear usuarios con registro nuevo
   - Script de migración para encriptar contraseñas existentes
   - Forzar reset de password

2. **Secret Key**: La clave JWT está en el código por ahora. En producción:
   - Usar variable de entorno
   - No commitear en git
   - Rotar periódicamente

3. **Expiración**: Tokens expiran en 24 horas. Considerar:
   - Implementar refresh tokens
   - Renovación automática antes de expirar
   - Manejo de sesión expirada en frontend

4. **CORS**: Si frontend está en diferente dominio/puerto, configurar CORS en backend:
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

## 📝 Checklist de Implementación

### Backend ✅
- [x] Agregar dependencias JWT y Security
- [x] Crear JwtService
- [x] Crear JwtAuthenticationFilter
- [x] Crear SecurityConfig
- [x] Crear ApplicationConfig
- [x] Actualizar User para UserDetails
- [x] Actualizar UserService con JWT
- [x] Configurar application.properties
- [x] Compilar sin errores

### Frontend ⏳
- [ ] Crear/Actualizar AuthService
- [ ] Crear AuthInterceptor
- [ ] Registrar interceptor en app.config
- [ ] Actualizar componentes login/register
- [ ] Manejar errores 401 (token inválido)
- [ ] Implementar logout
- [ ] Probar flujo completo

### Testing ⏳
- [ ] Registrar nuevo usuario
- [ ] Login con usuario
- [ ] Acceder endpoint protegido con token
- [ ] Verificar rechazo sin token
- [ ] Verificar expiración de token
- [ ] Probar logout
