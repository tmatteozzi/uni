# Calculadora Cliente/Servidor — Sockets TCP/IP en Python

> Implementación de una calculadora distribuida usando el modelo cliente/servidor sobre TCP/IP.

---

## Estructura del proyecto

```
📂 calculadora-sockets/
├── server.py   # Servidor: recibe expresiones, calcula y responde
├── client.py   # Cliente: envía expresiones y muestra resultados
└── README.md   # Este archivo
```

---

## Requisitos

- Python **3.6** o superior
- Sin dependencias externas (solo módulos de la biblioteca estándar: `socket`, `re`, `sys`)

---

## Cómo ejecutar

### 1. Iniciar el servidor

Abrí una terminal y ejecutá:

```bash
python server.py
```

Deberías ver:

```
[SERVIDOR] Escuchando en 127.0.0.1:65432 ...
[SERVIDOR] Esperando conexiones (Ctrl+C para detener)
```

El servidor queda bloqueado esperando conexiones. Para detenerlo usá `Ctrl+C`.

---

### 2. Ejecutar el cliente

En **otra terminal** (con el servidor corriendo), ejecutá:

```bash
# Modo completo: casos de prueba + modo interactivo
python client.py

# Modo solo pruebas automáticas
python client.py --test
```

---

## Flujo de comunicación

```
  CLIENTE                            SERVIDOR
    │                                   │
    │  1. Conectar (TCP handshake)      │
    │ ────────────────────────────────> │
    │                                   │
    │  2. Enviar expresión (UTF-8)      │
    │  "1*(2+3^4)-5*(6+7)/(8+9/10)"     │
    │ ────────────────────────────────> │
    │                                   │  3. Validar expresión
    │                                   │  4. Reemplazar ^ por **
    │                                   │  5. Evaluar con eval()
    │                                   │  6. Formatear resultado
    │                                   │
    │  7. Recibir resultado (UTF-8)     │
    │  "75.6966292134"                  │
    │ <──────────────────────────────── │
    │                                   │
    │  8. Cerrar conexión               │
    │ ────────────────────────────────> │
```

Cada expresión abre una nueva conexión TCP. El servidor atiende a un cliente a la vez (modelo secuencial).

---

## Descripción del servidor (`server.py`)

### Configuración

| Parámetro | Valor        | Descripción                    |
| --------- | ------------ | ------------------------------ |
| `HOST`    | `127.0.0.1`  | Solo acepta conexiones locales |
| `PORT`    | `65432`      | Puerto TCP de escucha          |
| Buffer    | `1024 bytes` | Tamaño máximo por mensaje      |

### Funciones principales

#### `start_server()`

- Crea un socket TCP (`AF_INET`, `SOCK_STREAM`)
- Activa `SO_REUSEADDR` para reutilizar el puerto tras reinicios
- Llama a `bind()`, `listen()` y entra en un loop de `accept()`

#### `handle_client(conn, addr)`

- Recibe los bytes del cliente y los decodifica como UTF-8
- Llama a `safe_eval()` con la expresión recibida
- Formatea el resultado (sin decimales si es entero, hasta 10 cifras significativas si es float)
- Maneja errores como división por cero o expresiones inválidas
- Envía la respuesta de vuelta al cliente

#### `safe_eval(expression)`

- Reemplaza `^` por `**` (notación Python para potencia)
- Valida con regex que la expresión solo contenga: dígitos, espacios y operadores `+ - * / ( ) .`
- Evalúa con `eval(expr, {"__builtins__": {}}, {})` — sin acceso a built-ins de Python, evitando ejecución de código arbitrario

---

## Descripción del cliente (`client.py`)

### Funciones principales

#### `send_expression(expression)`

- Abre una conexión TCP al servidor
- Codifica y envía la expresión en UTF-8
- Espera y retorna la respuesta del servidor
- Cierra la conexión automáticamente (context manager `with`)

#### `run_test_cases()`

- Ejecuta automáticamente los 4 casos de prueba del TP
- Imprime expresión y resultado en formato tabular

#### `interactive_mode()`

- Permite al usuario ingresar expresiones manualmente
- Continúa hasta que el usuario escribe `salir`, `exit` o `quit`

---

## Operadores soportados

| Operador       | Símbolo | Ejemplo   | Resultado |
| -------------- | ------- | --------- | --------- |
| Suma           | `+`     | `3+4`     | `7`       |
| Resta          | `-`     | `10-3`    | `7`       |
| Multiplicación | `*`     | `3*4`     | `12`      |
| División       | `/`     | `10/4`    | `2.5`     |
| Potencia       | `^`     | `2^8`     | `256`     |
| Paréntesis     | `( )`   | `2*(3+4)` | `14`      |

### Precedencia de operadores (de mayor a menor)

1. `( )` — Paréntesis
2. `^` — Potencia
3. `*` `/` — Multiplicación y División
4. `+` `-` — Suma y Resta

---

## Casos de prueba y resultados

| #   | Expresión                      | Resultado          |
| --- | ------------------------------ | ------------------ |
| 1   | `1+2+3+4+5+6+7+8+9+10`         | `55`               |
| 2   | `1*2+3^4-5*6+7/8+9/10`         | `54.775`           |
| 3   | `1*(2+3^4)-5*(6+7)/(8+9/10)`   | `75.6966292134...` |
| 4   | `1*(2+3^4)-5*((6+7)/(8+9/10))` | `75.6966292134...` |

> Los casos 3 y 4 tienen el mismo resultado ya que la agrupación con paréntesis adicionales no altera el orden de evaluación en esas expresiones particulares.

---

## Manejo de errores

El servidor captura y retorna mensajes de error descriptivos en los siguientes casos:

| Situación                             | Respuesta del servidor                                |
| ------------------------------------- | ----------------------------------------------------- |
| División por cero                     | `ERROR: División por cero`                            |
| Caracteres no permitidos (ej: letras) | `ERROR: Expresión contiene caracteres no permitidos.` |
| Expresión malformada                  | `ERROR: <detalle del error>`                          |

---

## Detalles del protocolo

- **Capa de transporte:** TCP (orientado a conexión, garantiza entrega ordenada)
- **Codificación:** UTF-8
- **Modelo de servidor:** iterativo (un cliente a la vez)
- **Formato de mensaje:** texto plano (sin cabeceras ni delimitadores adicionales)
- **Puerto:** 65432 (rango dinámico/privado, por encima del 49151)
